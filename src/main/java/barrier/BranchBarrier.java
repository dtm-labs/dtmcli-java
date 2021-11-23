/*
 * MIT License
 *
 * Copyright (c) 2021 yedf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package barrier;

import com.alibaba.fastjson.JSONObject;
import common.constant.ParamFieldConstant;
import common.utils.StreamUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;

@Data
@NoArgsConstructor
@Slf4j
public class BranchBarrier {
    
    /**
     * 事务类型
     */
    private String transType;
    
    /**
     * 全局事务id
     */
    private String gid;
    
    /**
     * 分支id
     */
    private String branchId;
    
    /**
     * 操作
     */
    private String op;
    
    /**
     * 屏障id
     */
    private int barrierId;
    
    
    public BranchBarrier(InputStream inputStream) throws Exception {
        byte[] bytes = StreamUtil.copyToByteArray(inputStream);
        BarrierParam barrierParam = JSONObject.parseObject(bytes, BarrierParam.class);
        if (Objects.isNull(barrierParam)) {
            throw new Exception("read InputStream null");
        }
        if (barrierParam.getTrans_type().length > 0) {
            this.transType = barrierParam.getTrans_type()[0];
        }
        if (barrierParam.getGid().length > 0) {
            this.gid = barrierParam.getGid()[0];
        }
        if (barrierParam.getBranch_id().length > 0) {
            this.branchId = barrierParam.getBranch_id()[0];
        }
        if (barrierParam.getOp().length > 0) {
            this.op = barrierParam.getOp()[0];
        }
    }
    
    /**
     * connection 由使用方自行管理，创建、回收。
     *
     * @param connection
     * @param consumer
     * @return
     * @throws SQLException
     */
    public void call(Connection connection, Consumer<BranchBarrier> consumer) throws SQLException {
        this.barrierId++;
        connection.setAutoCommit(false);
        try {
            boolean result = insertBarrier(connection);
            if (result) {
                consumer.accept(this);
                connection.commit();
            }
        } catch (Exception exception) {
            log.warn("barrier call error", exception);
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }
    
    private boolean insertBarrier(Connection connection) throws SQLException {
        if (Objects.isNull(connection)) {
            return false;
        }
        PreparedStatement preparedStatement = null;
        try {
            String sql = "insert ignore into barrier(trans_type, gid, branch_id, op, barrier_id, reason) values(?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, transType);
            preparedStatement.setString(2, gid);
            preparedStatement.setString(3, branchId);
            preparedStatement.setString(4, op);
            preparedStatement.setString(5, String.format("%02d", barrierId));
            preparedStatement.setString(6, op);
            
            if (preparedStatement.executeUpdate() == 0) {
                return false;
            }
            if (op.equals(ParamFieldConstant.CANCEL)) {
                int opIndex = 4;
                preparedStatement.setString(opIndex, ParamFieldConstant.TRY);
                if (preparedStatement.executeUpdate() > 0) {
                    return false;
                }
            }
        } finally {
            if (Objects.nonNull(preparedStatement)) {
                preparedStatement.close();
            }
        }
        return true;
    }
}

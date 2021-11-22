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

import common.constant.ParamFieldConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Supplier;

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
    
    
    public BranchBarrier(BarrierParam barrierParam) {
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
    
    public void call(Connection connection, Supplier<Boolean> supplier) throws SQLException {
        this.barrierId++;
        connection.setAutoCommit(false);
        try {
            boolean result = getResult(connection);
            if (result) {
                if (supplier.get()) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
            }
            connection.setAutoCommit(true);
        } catch (SQLException sqlException) {
            log.warn("barrier insert error", sqlException);
            connection.rollback();
        } finally {
            connection.close();
        }
    }
    
    private boolean getResult(Connection connection) throws SQLException {
        if (Objects.isNull(connection)) {
            return false;
        }
        
        PreparedStatement preparedStatement = null;
        boolean result;
        try {
            String sql = "insert ignore into barrier(trans_type, gid, branch_id, op, barrier_id, reason) values(?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, transType);
            preparedStatement.setString(2, gid);
            preparedStatement.setString(3, branchId);
            preparedStatement.setString(5, String.format("%02d", barrierId));
            preparedStatement.setString(6, op);
            
            result = false;
            if (op.equals(ParamFieldConstant.TRY) || op.equals(ParamFieldConstant.CONFIRM)) {
                preparedStatement.setString(4, op);
                result = preparedStatement.executeUpdate() > 0;
            } else if (op.equals(ParamFieldConstant.CANCEL)) {
                // 先插入try
                preparedStatement.setString(4, ParamFieldConstant.TRY);
                boolean tryResult = preparedStatement.executeUpdate() > 0;
                // 插入cancel
                if (tryResult) {
                    preparedStatement.setString(4, op);
                    result = preparedStatement.executeUpdate() > 0;
                }
            }
        } finally {
            if (Objects.nonNull(preparedStatement)) {
                preparedStatement.close();
            }
        }
        return result;
    }
}

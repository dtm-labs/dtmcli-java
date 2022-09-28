/*
 * MIT License
 *
 * Copyright (c) 2022 dtm-labs
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

package pub.dtm.client.barrier;

import pub.dtm.client.barrier.itfc.BarrierDBOperator;
import pub.dtm.client.barrier.itfc.ConnectionManager;
import pub.dtm.client.constant.ParamFieldConstants;
import pub.dtm.client.enums.TransTypeEnum;
import pub.dtm.client.exception.FailureException;
import pub.dtm.client.interfaces.dtm.DtmConsumer;
import lombok.Data;
import lombok.NoArgsConstructor;
import pub.dtm.client.model.dtm.TransBase;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.dtm.client.utils.JsonUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

/**
 * Branch barrier service
 *
 * @author horseLk
 */
@Data
@NoArgsConstructor
public class BranchBarrier extends TransBase {
    private static final Logger log = LoggerFactory.getLogger(BranchBarrier.class);

    /**
     * branch id
     */
    private String branchId;

    /**
     * operator
     */
    private String op;

    /**
     * barrier id
     */
    private int barrierId;

    private ConnectionManager connectionManager;

    public BranchBarrier(Map<String, String[]> paramsMap) throws Exception {
        this(paramsMap, null);
    }

    public BranchBarrier(Map<String, String[]> paramsMap, ConnectionManager connectionManager) throws Exception {
        if (paramsMap == null || paramsMap.isEmpty()) {
            throw new FailureException("build BranchBarrier error, paramsMap can not be empty.");
        }
        BarrierParam barrierParam = JsonUtils.parseJson(JsonUtils.toJson(paramsMap), BarrierParam.class);
        if (ArrayUtils.isNotEmpty(barrierParam.getTrans_type())) {
            this.setTransTypeEnum(TransTypeEnum.parseString(barrierParam.getTrans_type()[0]));
        }
        if (ArrayUtils.isNotEmpty(barrierParam.getGid())) {
            this.setGid(barrierParam.getGid()[0]);
        }
        if (ArrayUtils.isNotEmpty(barrierParam.getBranch_id())) {
            this.branchId = barrierParam.getBranch_id()[0];
        }
        if (ArrayUtils.isNotEmpty(barrierParam.getOp())) {
            this.op = barrierParam.getOp()[0];
        }
        this.connectionManager = connectionManager;
    }

    /**
     * Busi can call method call() to open branch barrier
     *
     * @param connection data source connection
     * @param consumer consumer
     * @throws Exception exception
     */
    public void call(Connection connection, DtmConsumer<BranchBarrier> consumer) throws Exception {
        ++this.barrierId;
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
            throw exception;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Busi can call method call() to open branch barrier
     *
     * @param DBOperator db operator
     * @param consumer busi consumer
     * @throws Exception exception
     */
    public void call(BarrierDBOperator DBOperator, DtmConsumer<BranchBarrier> consumer) throws Exception {
        ++this.barrierId;
        try {
            boolean insertRes = DBOperator.insertBarrier(this.getTransTypeEnum().getValue(), this.getGid(), branchId, this.op,
                    this.barrierId);
            if (insertRes) {
                consumer.accept(this);
                DBOperator.commit();
            }
        } catch (Exception exception) {
            log.warn("barrier call error", exception);
            DBOperator.rollback();
            throw new Exception(exception);
        }
    }

//    public void call(DtmConsumer<BranchBarrier> consumer) throws Exception {
//        if (connectionManager == null) {
//            throw new IllegalStateException(
//                    "Connection cannot be automatically created because ConnectionManager is not specified"
//            );
//        }
//        connectionManager.<Void>execute(con -> {
//            call(con, consumer);
//            return null;
//        });
//    }

    private boolean insertBarrier(Connection connection) throws SQLException {
        log.info("insert barrier {}", this);
        if (Objects.isNull(connection)) {
            return false;
        }
        PreparedStatement preparedStatement = null;
        try {
            String sql = "insert ignore into barrier(trans_type, gid, branch_id, op, barrier_id, reason) values(?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.getTransTypeEnum().getValue());
            preparedStatement.setString(2, this.getGid());
            preparedStatement.setString(3, branchId);
            preparedStatement.setString(4, op);
            preparedStatement.setString(5, String.format("%02d", barrierId));
            preparedStatement.setString(6, op);

            if (preparedStatement.executeUpdate() == 0) {
                return false;
            }
            if (ParamFieldConstants.CANCEL.equals(op)) {
                int opIndex = 4;
                preparedStatement.setString(opIndex, ParamFieldConstants.TRY);
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

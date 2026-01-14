package pub.dtm.client.barrier.itfc.impl;

import pub.dtm.client.barrier.itfc.BarrierDBOperator;
import pub.dtm.client.barrier.itfc.BarrierInsertResult;
import pub.dtm.client.constant.ParamFieldConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;

/**
 * BarrierDBOperator for MySQL
 *
 * @author horseLk
 * @date 2022-09-28 22:05
 */
public class BarrierMysqlOperator implements BarrierDBOperator {
    private Object connection;

    public BarrierMysqlOperator(Object connection) {
        this.connection = connection;
    }

    @Override
    public boolean insertBarrier(String transType, String gid, String branchId, String op, int barrierId) throws Exception {
        if (Objects.isNull(connection)) {
            return false;
        }
        Connection conn = (Connection)this.connection;
        conn.setAutoCommit(false);
        PreparedStatement preparedStatement = null;
        try {
            String sql = "insert ignore into barrier(trans_type, gid, branch_id, op, barrier_id, reason) values(?,?,?,?,?,?)";
            preparedStatement = conn.prepareStatement(sql);
            // Set timeout to avoid waiting too long
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setString(1, transType);
            preparedStatement.setString(2, gid);
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

    @Override
    public BarrierInsertResult insertBarrier(String transType, String gid, String branchId, String op, int barrierId, String reason) throws Exception {
        if (Objects.isNull(connection)) {
            throw new IllegalArgumentException("connection can not be null");
        }

        if (op == null || op.trim().isEmpty()) {
            return new BarrierInsertResult(0, null);
        }

        Connection conn = (Connection)this.connection;
        PreparedStatement preparedStatement = null;
        int totalAffected = 0;
        try {
            boolean originalAutoCommit = conn.getAutoCommit();
            if (originalAutoCommit) {
                conn.setAutoCommit(false);
            }

            String sql = "insert ignore into barrier(trans_type, gid, branch_id, op, barrier_id, reason) values(?,?,?,?,?,?)";
            preparedStatement = conn.prepareStatement(sql);
            // Set timeout to avoid waiting too long
            preparedStatement.setQueryTimeout(30);
            preparedStatement.setString(1, transType);
            preparedStatement.setString(2, gid);
            preparedStatement.setString(3, branchId);
            preparedStatement.setString(4, op);
            preparedStatement.setString(5, String.format("%02d", barrierId));
            preparedStatement.setString(6, reason);

            totalAffected = preparedStatement.executeUpdate();

            return new BarrierInsertResult(totalAffected, null);
        } catch (Exception ex) {
            return new BarrierInsertResult(0, ex);
        } finally {
            if (Objects.nonNull(preparedStatement)) {
                preparedStatement.close();
            }
        }
    }

    @Override
    public void commit() throws Exception {
        if (Objects.isNull(connection)) {
            return;
        }
        Connection conn = (Connection)this.connection;
        conn.commit();
        conn.setAutoCommit(true);
    }

    @Override
    public void rollback() throws Exception {
        if (Objects.isNull(connection)) {
            return;
        }
        Connection conn = (Connection)this.connection;
        conn.rollback();
        conn.setAutoCommit(true);
    }
}

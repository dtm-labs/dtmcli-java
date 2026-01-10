package pub.dtm.client.barrier.itfc.impl;

import pub.dtm.client.barrier.itfc.BarrierDBOperator;
import pub.dtm.client.constant.ParamFieldConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;

/**
 * BarrierDBOperator for SqlServer
 *
 * @author yxou
 * @date 2026-01-07
 */
public class BarrierSqlServerOperator implements BarrierDBOperator {
    private Object connection;

    public BarrierSqlServerOperator(Object connection) {
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
            String sql = "insert into dbo.barrier(trans_type, gid, branch_id, op, barrier_id, reason) values(?,?,?,?,?,?)";
            preparedStatement = conn.prepareStatement(sql);
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
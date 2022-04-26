package barrier;

import barrier.model.BranchBarrier;
import constant.ParamFieldConstants;
import interfaces.dtm.DtmConsumer;
import interfaces.dtm.IDtmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class BranchBarrierService implements IDtmService {
    private static final Logger log = LoggerFactory.getLogger(BranchBarrierService.class);

    public void call(BranchBarrier barrier, Connection connection, DtmConsumer<BranchBarrierService, BranchBarrier> consumer) throws Exception {
        barrier.addBarrierId();
        connection.setAutoCommit(false);
        try {
            boolean result = insertBarrier(connection, barrier);
            if (result) {
                consumer.accept(this, barrier);
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

    private boolean insertBarrier(Connection connection, BranchBarrier barrier) throws SQLException {
        log.info("insert barrier {}", barrier);
        if (Objects.isNull(connection)) {
            return false;
        }
        PreparedStatement preparedStatement = null;
        try {
            String sql = "insert ignore into barrier(trans_type, gid, branch_id, op, barrier_id, reason) values(?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, barrier.getTransTypeEnum().toString());
            preparedStatement.setString(2, barrier.getGid());
            preparedStatement.setString(3, barrier.getBranchId());
            preparedStatement.setString(4, barrier.getOp());
            preparedStatement.setString(5, String.format("%02d", barrier.getBarrierId()));
            preparedStatement.setString(6, barrier.getOp());

            if (preparedStatement.executeUpdate() == 0) {
                return false;
            }
            if (ParamFieldConstants.CANCEL.equals(barrier.getOp())) {
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

package pub.dtm.client.barrier;

import com.google.gson.Gson;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
public class BranchBarrier extends TransBase {
    private static final Logger log = LoggerFactory.getLogger(BranchBarrier.class);

    private String branchId;

    private String op;

    private int barrierId;

    public BranchBarrier(Map<String, String[]> paramsMap) throws Exception {
        if (paramsMap == null || paramsMap.isEmpty()) {
            throw new FailureException("build BranchBarrier error, paramsMap can not be empty.");
        }
        Gson gson = new Gson();
        BarrierParam barrierParam = gson.fromJson(gson.toJson(paramsMap), BarrierParam.class);
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
    }

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

    private boolean insertBarrier(Connection connection) throws SQLException {
        log.info("insert barrier {}", this);
        if (Objects.isNull(connection)) {
            return false;
        }
        PreparedStatement preparedStatement = null;
        try {
            String sql = "insert ignore into barrier(trans_type, gid, branch_id, op, barrier_id, reason) values(?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.getTransTypeEnum().toString());
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

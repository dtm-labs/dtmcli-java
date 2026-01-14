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
import pub.dtm.client.barrier.itfc.BarrierInsertResult;
import pub.dtm.client.barrier.itfc.ConnectionManager;
import pub.dtm.client.barrier.itfc.impl.BarrierMysqlOperator;
import pub.dtm.client.constant.BarrierConstants;
import pub.dtm.client.constant.ParamFieldConstants;
import pub.dtm.client.enums.TransTypeEnum;
import pub.dtm.client.exception.DtmDuplicatedException;
import pub.dtm.client.exception.DtmException;
import pub.dtm.client.exception.DtmOngingException;
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
        if (Objects.isNull(connection))
            throw new IllegalArgumentException("connection can not be null");

        BarrierDBOperator operator = new BarrierMysqlOperator(connection);

        call(operator, consumer);
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
            String originOp = BarrierConstants.OP_DICT.get(this.op);
            if (originOp == null) {
                originOp = "";
            }
            BarrierInsertResult originInsertRes = DBOperator.insertBarrier(this.getTransTypeEnum().getValue(), this.getGid(), branchId, originOp, this.barrierId, this.op);
            if (originInsertRes.getException() != null)
            {
                throw new DtmOngingException(originInsertRes.getException().getMessage());
            }

            BarrierInsertResult currentInsertRes = DBOperator.insertBarrier(this.getTransTypeEnum().getValue(), this.getGid(), branchId, this.op, this.barrierId, this.op);
            if (currentInsertRes.getException() != null)
            {
                throw new DtmOngingException(currentInsertRes.getException().getMessage());
            }

            if (isMsgRejected(currentInsertRes.getException(), this.op, currentInsertRes.getAffectedRows())){
                throw new DtmDuplicatedException();
            }

            boolean isNullCompensation = isNullCompensation(this.op, originInsertRes.getAffectedRows());
            boolean isDuplicateOrPend = isDuplicateOrPend(currentInsertRes.getAffectedRows());

            if (isNullCompensation || isDuplicateOrPend)
            {
                log.warn(String.format("Will not exec busiCall, isNullCompensation=%s, isDuplicateOrPend=%s",isNullCompensation, isDuplicateOrPend));
                return;
            }
            consumer.accept(this);
            DBOperator.commit();
        } catch (DtmException e) {
            log.warn(String.format("dtm known %s, gid=%s, trans_type=%s",e.getMessage(), this.getGid(), this.getTransTypeEnum().getValue()));
            DBOperator.rollback();
            throw e;
        }
        catch (Exception e) {
            log.warn(String.format("Call error %s, gid=%s, trans_type=%s",e.getMessage(), this.getGid(), this.getTransTypeEnum().getValue()));
            DBOperator.rollback();
            throw e;
        }
    }


    private boolean isMsgRejected(Exception err, String op, int currentAffected) {
        return (err == null || err.getMessage() == null || err.getMessage().trim().isEmpty())
                && ParamFieldConstants.MESSAGE.equals(op)
                && currentAffected == 0;
    }

    private boolean isNullCompensation(String op, int originAffected) {
        return (ParamFieldConstants.CANCEL.equals(op) || ParamFieldConstants.COMPENSATE.equals(op)) && originAffected > 0;
    }

    private boolean isDuplicateOrPend(int currentAffected) {
        return currentAffected == 0;
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
}

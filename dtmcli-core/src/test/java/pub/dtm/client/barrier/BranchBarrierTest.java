package pub.dtm.client.barrier;

import org.junit.jupiter.api.Test;
import pub.dtm.client.barrier.itfc.impl.BarrierMysqlOperator;
import pub.dtm.client.barrier.itfc.impl.BarrierSqlServerOperator;
import pub.dtm.client.busi.BusiUtil;
import pub.dtm.client.busi.DbType;
import pub.dtm.client.busi.TransReq;
import pub.dtm.client.enums.TransTypeEnum;

import java.sql.Connection;

class BranchBarrierTest {

    @Test
    void mysql_call1() throws Exception {
        BranchBarrier branchBarrier = new BranchBarrier();
        branchBarrier.setGid("gid-unit-test1-" + System.currentTimeMillis());
        branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
        branchBarrier.setBranchId("branch-1");
        branchBarrier.setOp("try");

        Connection connection = BusiUtil.getConnection();
        branchBarrier.call(connection, (barrier) -> {
            BusiUtil.adjustBalance(connection, new TransReq(1, 30));
        });
    }

    @Test
    void mysql_call2() throws Exception {
        BranchBarrier branchBarrier = new BranchBarrier();
        branchBarrier.setGid("gid-unit-test2-" + System.currentTimeMillis());
        branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
        branchBarrier.setBranchId("branch-1");
        branchBarrier.setOp("try");

        Connection connection = BusiUtil.getConnection();
        branchBarrier.call(new BarrierMysqlOperator(connection), (barrier) -> {
            BusiUtil.adjustTrading(connection, new TransReq(1, 30));
        });
    }

    @Test
    void sqlserver_call1() throws Exception {
        BranchBarrier branchBarrier = new BranchBarrier();
        branchBarrier.setGid("gid-unit-test1-" + System.currentTimeMillis());
        branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
        branchBarrier.setBranchId("branch-1");
        branchBarrier.setOp("try");

        Connection connection = BusiUtil.getSqlServerConnection();
        branchBarrier.call(new BarrierSqlServerOperator(connection), (barrier) -> {
            BusiUtil.adjustBalance(connection, new TransReq(1, 30), DbType.SQLSERVER);
        });
    }

    @Test
    void sqlserver_call2() throws Exception {
        BranchBarrier branchBarrier = new BranchBarrier();
        branchBarrier.setGid("gid-unit-test2-" + System.currentTimeMillis());
        branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
        branchBarrier.setBranchId("branch-1");
        branchBarrier.setOp("try");

        Connection connection = BusiUtil.getSqlServerConnection();
        branchBarrier.call(new BarrierSqlServerOperator(connection), (barrier) -> {
            BusiUtil.adjustTrading(connection, new TransReq(1, 30), DbType.SQLSERVER);
        });
    }
}
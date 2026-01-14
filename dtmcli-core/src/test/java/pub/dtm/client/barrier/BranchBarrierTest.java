package pub.dtm.client.barrier;

import org.junit.jupiter.api.Test;
import pub.dtm.client.barrier.itfc.impl.BarrierMysqlOperator;
import pub.dtm.client.barrier.itfc.impl.BarrierSqlServerOperator;
import pub.dtm.client.busi.BusiUtil;
import pub.dtm.client.busi.DbType;
import pub.dtm.client.busi.TransReq;
import pub.dtm.client.enums.TransTypeEnum;
import pub.dtm.client.exception.DtmOngingException;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

// http 状态码返回
// (1) OK: 状态码 200 状态码 200
//（2）FAILURE: 状态码 409 StatusConflict，别来了
//（3）ONGOING: 状态码 425 StatusTooEarly，过会再来
class BranchBarrierTest {
    @Test
    void mysql_call_with_connection() throws Exception {
        BranchBarrier branchBarrier = new BranchBarrier();
        branchBarrier.setGid("gid-unit-test1-" + UUID.randomUUID());
        branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
        branchBarrier.setBranchId("branch-1");
        branchBarrier.setOp("try");

        Connection connection = BusiUtil.getConnection();
        branchBarrier.call(connection, (barrier) -> {
            BusiUtil.adjustBalance(connection, new TransReq(1, 30));
        });
    }

    @Test
    void mysql_call_with_connection_null() throws Exception {
        BranchBarrier branchBarrier = new BranchBarrier();
        branchBarrier.setGid("gid-unit-test1-" + UUID.randomUUID());
        branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
        branchBarrier.setBranchId("branch-1");
        branchBarrier.setOp("try");


        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            Connection connection = null;
            branchBarrier.call(connection, (barrier) -> {
                BusiUtil.adjustBalance(connection, new TransReq(1, 30));
            });
        });
        assert ex.getMessage().equals("connection can not be null");
    }

    @Test
    void mysql_call_with_BarrierMysqlOperator_OK() throws Exception {
        BranchBarrier branchBarrier = new BranchBarrier();
        branchBarrier.setGid("gid-unit-test2-" + UUID.randomUUID());
        branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
        branchBarrier.setBranchId("branch-1");
        branchBarrier.setOp("try");

        Connection connection = BusiUtil.getConnection();

        branchBarrier.call(new BarrierMysqlOperator(connection), (barrier) -> {
            BusiUtil.adjustTrading(connection, new TransReq(1, 30));
        });
    }

    @Test
    void mysql_call_with_BarrierMysqlOperator_FAILURE() throws Exception {
        BranchBarrier branchBarrier = new BranchBarrier();
        branchBarrier.setGid("gid-unit-test2-" + UUID.randomUUID());
        branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
        branchBarrier.setBranchId("branch-1");
        branchBarrier.setOp("try");

        Connection connection = BusiUtil.getConnection();
        SQLException thrown = assertThrows(SQLException.class, () -> {
            branchBarrier.call(new BarrierMysqlOperator(connection), (barrier) -> {
                TransReq trans = new TransReq(1, 30);
                trans.ex = new SQLException("my SQLException");
                BusiUtil.adjustTrading(connection, trans);
            });
        });
        assert "my SQLException".equals(thrown.getMessage()) : "Expected message 'my SQLException', but got: " + thrown.getMessage();
    }

    @Test
    void mysql_call_with_BarrierMysqlOperator_ONGOING() throws Exception {

        String gid = "gid-unit-test2-" + UUID.randomUUID();
        // 先进入的，业务体35秒后报异常。异步调用让后调用的进入
        Thread asyncThread = new Thread(() -> {
            try {
                BranchBarrier branchBarrier = new BranchBarrier();
                branchBarrier.setGid(gid);
                branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
                branchBarrier.setBranchId("branch-1");
                branchBarrier.setOp("try");

                Connection connection = BusiUtil.getConnection();
                branchBarrier.call(new BarrierMysqlOperator(connection), (barrier) -> {
                    TransReq trans = new TransReq(1, 20);
                    trans.sleepDuration = Duration.ofSeconds(35);
                    trans.ex = new SQLException("my SQLException");
                    BusiUtil.adjustTrading(connection, trans);
                });
            } catch (Exception e) {
                try {
                    throw new RuntimeException(e);
                } catch (Exception ex) {
                    // pass
                }
            }
        });
        asyncThread.start();

        // 等待2秒再次调用, 应当被屏障挡住，因为调用1还未完成
        Thread.sleep(Duration.ofSeconds(2).toMillis());
        {
            BranchBarrier branchBarrier = new BranchBarrier();
            branchBarrier.setGid(gid);
            branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
            branchBarrier.setBranchId("branch-1");
            branchBarrier.setOp("try");

            Connection connection = BusiUtil.getConnection();
            DtmOngingException thrown = assertThrows(DtmOngingException.class, () -> {
                branchBarrier.call(new BarrierMysqlOperator(connection), (barrier) -> {
                    // pass， 因为
                    throw new Exception("should pass");
                });
            });
            assert "Statement cancelled due to timeout or client request".equals(thrown.getMessage()) ;

        }

        // 40秒后再次调用会成功, 调用1抛异常回滚
        Thread.sleep(Duration.ofSeconds(40).toMillis());
        {
            BranchBarrier branchBarrier = new BranchBarrier();
            branchBarrier.setGid(gid);
            branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
            branchBarrier.setBranchId("branch-1");
            branchBarrier.setOp("try");

            Connection connection = BusiUtil.getConnection();
            branchBarrier.call(new BarrierMysqlOperator(connection), (barrier) -> {
                BusiUtil.adjustTrading(connection, new TransReq(1, 30));
            });
        }
    }

    @Test
    void sqlserver_call_with_connection() throws Exception {
        BranchBarrier branchBarrier = new BranchBarrier();
        branchBarrier.setGid("gid-unit-test1-" + UUID.randomUUID());
        branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
        branchBarrier.setBranchId("branch-1");
        branchBarrier.setOp("try");

        Connection connection = BusiUtil.getSqlServerConnection();
        branchBarrier.call(new BarrierSqlServerOperator(connection), (barrier) -> {
            BusiUtil.adjustBalance(connection, new TransReq(1, 30), DbType.SQLSERVER);
        });
    }

    @Test
    void sqlserver_call_with_connection_null() throws Exception {
        BranchBarrier branchBarrier = new BranchBarrier();
        branchBarrier.setGid("gid-unit-test1-" + UUID.randomUUID());
        branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
        branchBarrier.setBranchId("branch-1");
        branchBarrier.setOp("try");


        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            Connection connection = null;
            branchBarrier.call(new BarrierSqlServerOperator(connection), (barrier) -> {
                BusiUtil.adjustBalance(connection, new TransReq(1, 30), DbType.SQLSERVER);
            });
        });
        assert ex.getMessage().equals("connection can not be null");
    }

    @Test
    void sqlserver_call_with_BarrierSqlServerOperator_OK() throws Exception {
        BranchBarrier branchBarrier = new BranchBarrier();
        branchBarrier.setGid("gid-unit-test2-" + UUID.randomUUID());
        branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
        branchBarrier.setBranchId("branch-1");
        branchBarrier.setOp("try");

        Connection connection = BusiUtil.getSqlServerConnection();

        branchBarrier.call(new BarrierSqlServerOperator(connection), (barrier) -> {
            BusiUtil.adjustTrading(connection, new TransReq(1, 30), DbType.SQLSERVER);
        });
    }

    @Test
    void sqlserver_call_with_BarrierSqlServerOperator_FAILURE() throws Exception {
        BranchBarrier branchBarrier = new BranchBarrier();
        branchBarrier.setGid("gid-unit-test2-" + UUID.randomUUID());
        branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
        branchBarrier.setBranchId("branch-1");
        branchBarrier.setOp("try");

        Connection connection = BusiUtil.getSqlServerConnection();
        SQLException thrown = assertThrows(SQLException.class, () -> {
            branchBarrier.call(new BarrierSqlServerOperator(connection), (barrier) -> {
                TransReq trans = new TransReq(1, 30);
                trans.ex = new SQLException("my SQLException");
                BusiUtil.adjustTrading(connection, trans, DbType.SQLSERVER);
            });
        });
        assert "my SQLException".equals(thrown.getMessage()) : "Expected message 'my SQLException', but got: " + thrown.getMessage();
    }

    @Test
    void sqlserver_call_with_BarrierSqlServerOperator_ONGOING() throws Exception {

        String gid = "gid-unit-test2-" + UUID.randomUUID();
        // 先进入的，业务体35秒后报异常。异步调用让后调用的进入
        Thread asyncThread = new Thread(() -> {
            try {
                BranchBarrier branchBarrier = new BranchBarrier();
                branchBarrier.setGid(gid);
                branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
                branchBarrier.setBranchId("branch-1");
                branchBarrier.setOp("try");

                Connection connection = BusiUtil.getSqlServerConnection();
                branchBarrier.call(new BarrierSqlServerOperator(connection), (barrier) -> {
                    TransReq trans = new TransReq(1, 20);
                    trans.sleepDuration = Duration.ofSeconds(35);
                    trans.ex = new SQLException("my SQLException");
                    BusiUtil.adjustTrading(connection, trans, DbType.SQLSERVER);
                });
            } catch (Exception e) {
                try {
                    throw new RuntimeException(e);
                } catch (Exception ex) {
                    // pass
                }
            }
        });
        asyncThread.start();

        // 等待2秒再次调用, 应当被屏障挡住，因为调用1还未完成
        Thread.sleep(Duration.ofSeconds(2).toMillis());
        {
            BranchBarrier branchBarrier = new BranchBarrier();
            branchBarrier.setGid(gid);
            branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
            branchBarrier.setBranchId("branch-1");
            branchBarrier.setOp("try");

            Connection connection = BusiUtil.getSqlServerConnection();
            DtmOngingException thrown = assertThrows(DtmOngingException.class, () -> {
                branchBarrier.call(new BarrierSqlServerOperator(connection), (barrier) -> {
                    // pass， 因为
                    throw new Exception("should pass");
                });
            });
            assert "The query has timed out.".equals(thrown.getMessage()) ;

        }

        // 40秒后再次调用会成功, 调用1抛异常回滚
        Thread.sleep(Duration.ofSeconds(40).toMillis());
        {
            BranchBarrier branchBarrier = new BranchBarrier();
            branchBarrier.setGid(gid);
            branchBarrier.setTransTypeEnum(TransTypeEnum.TCC);
            branchBarrier.setBranchId("branch-1");
            branchBarrier.setOp("try");

            Connection connection = BusiUtil.getSqlServerConnection();
            branchBarrier.call(new BarrierSqlServerOperator(connection), (barrier) -> {
                BusiUtil.adjustTrading(connection, new TransReq(1, 30), DbType.SQLSERVER);
            });
        }
    }
}
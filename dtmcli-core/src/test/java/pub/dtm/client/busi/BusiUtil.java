package pub.dtm.client.busi;

import pub.dtm.client.exception.FailureException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BusiUtil {

    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/dtm_busi?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "";
        return java.sql.DriverManager.getConnection(url, user, password);
    }

    /**
     * 更新交易金额
     *
     * @param connection
     * @param transReq
     * @throws SQLException
     */
    public static void adjustTrading(Connection connection, TransReq transReq) throws Exception {
        if (transReq.sleepDuration != null)
            Thread.sleep(transReq.sleepDuration.toMillis());

        if (transReq.ex != null)
            throw transReq.ex;

        adjustTrading(connection, transReq, DbType.MYSQL);
    }

    /**
     * 更新交易金额
     *
     * @param connection
     * @param transReq
     * @param dbType
     * @throws SQLException
     */
    public static void adjustTrading(Connection connection, TransReq transReq, DbType dbType) throws Exception {

        String sql = "update %s set trading_balance=trading_balance+?"
                + " where user_id=? and trading_balance + ? + balance >= 0";
        sql = String.format(sql, getTableName(dbType));

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, transReq.amount);
            preparedStatement.setInt(2, transReq.userId);
            preparedStatement.setInt(3, transReq.amount);
            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("交易金额更新成功");
            } else {
                throw new FailureException("交易失败");
            }
        } finally {
            if (null != preparedStatement) {
                preparedStatement.close();
            }
        }

    }

    /**
     * 更新余额
     */
    public static void adjustBalance(Connection connection, TransReq transReq) throws Exception {
        if (transReq.ex != null)
            throw transReq.ex;

        if (transReq.sleepDuration != null)
            Thread.sleep(transReq.sleepDuration.toMillis());

        adjustBalance(connection, transReq, DbType.MYSQL);
    }

    /**
     * 更新余额
     *
     * @param connection
     * @param transReq
     * @param dbType
     * @throws SQLException
     */
    public static void adjustBalance(Connection connection, TransReq transReq, DbType dbType) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            String sql = "update %s set trading_balance=trading_balance-?,balance=balance+? where user_id=?";
            sql = String.format(sql, getTableName(dbType));

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, transReq.amount);
            preparedStatement.setInt(2, transReq.amount);
            preparedStatement.setInt(3, transReq.userId);
            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("余额更新成功");
            }
        } finally {
            if (null != preparedStatement) {
                preparedStatement.close();
            }
        }
    }

    /**
     * 获取MsSql连接
     *
     * @return
     * @throws Exception
     */
    public static Connection getSqlServerConnection() throws Exception {
        String url = "jdbc:sqlserver://localhost:1433;DatabaseName=dtm_busi;encrypt=true;trustServerCertificate=true;";
        String user = "sa";
        String password = "";
        return java.sql.DriverManager.getConnection(url, user, password);
    }

    /**
     * 根据数据库类型获取表名
     *
     * @param dbType
     * @return
     */
    private static String getTableName(DbType dbType) {
        String tableName;
        switch (dbType){
            case MYSQL:
                tableName="dtm_busi.user_account";
                break;
            case SQLSERVER:
                tableName="dtm_busi.dbo.user_account";
                break;
            default:
                tableName="dtm_busi.user_account";
                break;
        }
        return tableName;
    }
}

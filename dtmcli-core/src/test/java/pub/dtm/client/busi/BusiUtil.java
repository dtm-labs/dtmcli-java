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
        String sql = "update dtm_busi.user_account set trading_balance=trading_balance+?"
                + " where user_id=? and trading_balance + ? + balance >= 0";
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
    public static void adjustBalance(Connection connection, TransReq transReq) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            String sql = "update dtm_busi.user_account set trading_balance=trading_balance-?,balance=balance+? where user_id=?";
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
}

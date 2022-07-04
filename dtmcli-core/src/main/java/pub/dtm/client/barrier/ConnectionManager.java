package pub.dtm.client.barrier;

import javax.sql.DataSource;
import java.sql.Connection;

/*
 * 1. If user want to use simple connection, uses this
 *
 * ```
 * ConnectionManager.simpleConnectionManager(dataSource)
 * ```
 *
 * 2. If user want to reuse the connection/transaction of spring, use this
 *
 * ```
 * new ConnectionManager() {
 *     @Override
 *     public <R> R execute(ConnectionCallback<R> block) throws Exception {
 *         Connection con = DataSourceUtils.getConnection(dataSource);
 *         try {
 *             return block.apply(con);
 *         } finally {
 *             DataSourceUtils.releaseConnection(con, dataSource);
 *         }
 *     }
 * }
 * ```
 *
 * Please view
 * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/datasource/DataSourceUtils.html
 * to know more
 */
public interface ConnectionManager {

    <R> R execute(ConnectionCallback<R> block) throws Exception;

    static ConnectionManager simpleConnectionManager(DataSource dataSource) {
        return new ConnectionManager() {
            @Override
            public <R> R execute(ConnectionCallback<R> block) throws Exception {
                try (Connection con = dataSource.getConnection()) {
                    return block.execute(con);
                }
            }
        };
    }
}

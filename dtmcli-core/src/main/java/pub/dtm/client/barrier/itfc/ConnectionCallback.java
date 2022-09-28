package pub.dtm.client.barrier.itfc;

import java.sql.Connection;

@FunctionalInterface
public interface ConnectionCallback<R> {

    R execute(Connection con) throws Exception;
}

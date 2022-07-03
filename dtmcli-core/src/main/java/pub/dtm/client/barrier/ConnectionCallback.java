package pub.dtm.client.barrier;

import java.sql.Connection;

@FunctionalInterface
public interface ConnectionCallback<R> {

    R execute(Connection con) throws Exception;
}

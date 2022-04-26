package pub.dtm.client.interfaces.dtm;

import pub.dtm.client.model.dtm.TransBase;

@FunctionalInterface
public interface DtmConsumer<T extends TransBase> {
    void accept(T t) throws Exception;
}

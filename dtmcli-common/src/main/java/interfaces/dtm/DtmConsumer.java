package interfaces.dtm;

import model.dtm.TransBase;

@FunctionalInterface
public interface DtmConsumer<S extends IDtmService, T extends TransBase> {
    void accept(S s, T t) throws Exception;
}

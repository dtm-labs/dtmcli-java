package client;

import interfaces.dtm.DtmConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcc.TccService;
import tcc.model.Tcc;

@Component
public class DtmClient {
    @Autowired
    private TccService tccService;

    public DtmClient(TccService tccService) {
        this.tccService = tccService;
    }

    public String tccGlobalTransaction(DtmConsumer<TccService, Tcc> function) throws Exception {
        return tccService.tccGlobalTransaction(null, function);
    }

    public String tccGlobalTransaction(String gid, DtmConsumer<TccService, Tcc> function) throws Exception {
        return tccService.tccGlobalTransaction(gid, function);
    }
}

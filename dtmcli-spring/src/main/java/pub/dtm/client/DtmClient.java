package pub.dtm.client;

import pub.dtm.client.interfaces.dtm.DtmConsumer;
import pub.dtm.client.interfaces.feign.IDtmFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pub.dtm.client.tcc.Tcc;

@Component
public class DtmClient {
    @Autowired
    private IDtmFeignClient feignClient;

    public DtmClient(IDtmFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    public String tccGlobalTransaction(DtmConsumer<Tcc> function) throws Exception {
        Tcc tcc = new Tcc(null, feignClient);
        return tcc.tccGlobalTransaction(function);
    }

    public String tccGlobalTransaction(String gid, DtmConsumer<Tcc> function) throws Exception {
        Tcc tcc = new Tcc(gid, feignClient);
        return tcc.tccGlobalTransaction(function);
    }
}

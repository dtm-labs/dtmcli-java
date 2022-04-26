package client;

import com.alibaba.nacos.api.naming.pojo.Instance;
import constant.Constants;
import feign.DtmFeignClient;
import feign.Feign;
import feign.URIParser;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import interfaces.dtm.DtmConsumer;
import interfaces.feign.IDtmFeignClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import properties.DtmProperties;
import tcc.TccService;
import tcc.model.Tcc;
import utils.NacosUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_CLUSTER_NAME;
import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;
import static com.alibaba.nacos.api.naming.CommonParams.CLUSTER_NAME;
import static com.alibaba.nacos.api.naming.CommonParams.GROUP_NAME;

public class DtmClient {
    private static final Logger log = LoggerFactory.getLogger(DtmClient.class);


    private TccService tccService;

    public DtmClient() {
        // init URIParser
        new URIParser();

        String endpoint = null;
        try {
            // redirect connect to dtm
            endpoint = DtmProperties.get("dtm.server.endpoint");
            // connect to dtm by nacos
            if (StringUtils.isEmpty(endpoint)) {
                Instance instance = NacosUtils.selectOneHealthyInstance(DtmProperties.get(Constants.MICRO_SERVICE_NAME_KEY),
                        DtmProperties.getOrDefault(GROUP_NAME, DEFAULT_GROUP), genClusters(DtmProperties.get(CLUSTER_NAME)));
                endpoint = instance.toInetAddr();
            }
        } catch (Exception e) {
            log.error("initial dtm client for java error.", e);
            System.exit(-1);
        }
        if (StringUtils.isEmpty(endpoint)) {
            log.error("can not resolve dtm server message from config file, you can use nacos or redirect configure to config it.");
            System.exit(-1);
        }
        IDtmFeignClient feignClient = Feign
                                    .builder()
                                    .decoder(new GsonDecoder())
                                    .encoder(new GsonEncoder())
                                    .target(DtmFeignClient.class, Constants.HTTP_PREFIX + endpoint);
        if (feignClient == null) {
            log.error("initial dtm client for java error, feign client can't be null.");
            System.exit(-1);
        }

        this.tccService = new TccService(feignClient);
    }

    private List<String> genClusters(String clusterStr) {
        if (StringUtils.isEmpty(clusterStr)) {
            List<String> clusters = new ArrayList<>();
            clusters.add(DEFAULT_CLUSTER_NAME);
            return clusters;
        }
        String[] split = StringUtils.split(clusterStr, ",");
        return Arrays.asList(split);
    }

    public String tccGlobalTransaction(DtmConsumer<TccService, Tcc> function) throws Exception {
        return tccService.tccGlobalTransaction(null, function);
    }

    public String tccGlobalTransaction(String gid, DtmConsumer<TccService, Tcc> function) throws Exception {
        return tccService.tccGlobalTransaction(gid, function);
    }
}

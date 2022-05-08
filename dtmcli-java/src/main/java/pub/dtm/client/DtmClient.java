/*
 * MIT License
 *
 * Copyright (c) 2022 yedf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package pub.dtm.client;

import com.alibaba.nacos.api.naming.pojo.Instance;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import pub.dtm.client.constant.Constants;
import pub.dtm.client.feign.DtmFeignClient;
import feign.Feign;
import pub.dtm.client.feign.URIParser;
import pub.dtm.client.interfaces.dtm.DtmConsumer;
import pub.dtm.client.interfaces.feign.IDtmFeignClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.dtm.client.log.DtmFeignLogger;
import pub.dtm.client.properties.DtmProperties;
import pub.dtm.client.saga.Saga;
import pub.dtm.client.tcc.Tcc;
import pub.dtm.client.utils.NacosUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_CLUSTER_NAME;
import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;
import static com.alibaba.nacos.api.naming.CommonParams.CLUSTER_NAME;
import static com.alibaba.nacos.api.naming.CommonParams.GROUP_NAME;

public class DtmClient {
    private static final Logger log = LoggerFactory.getLogger(DtmClient.class);

    private IDtmFeignClient feignClient;

    public DtmClient() {
        // init URIParser
        new URIParser();

        String endpoint = null;
        try {
            // redirect connect to dtm
            endpoint = DtmProperties.get("dtm.ipport");
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
                                    .decoder(new JacksonDecoder())
                                    .encoder(new JacksonEncoder())
// if you need read detail log of feign, please cancel the note.
//                                    .logLevel(feign.Logger.Level.FULL)
//                                    .logger(new DtmFeignLogger())
                                    .target(DtmFeignClient.class, Constants.HTTP_PREFIX + endpoint);
        if (feignClient == null) {
            log.error("initial dtm client for java error, feign client can't be null.");
            System.exit(-1);
        }

        this.feignClient = feignClient;
    }

    public DtmClient(String endpoint) {
        // init URIParser
        new URIParser();

        if (StringUtils.isEmpty(endpoint)) {
            log.error("dtm server endpoint can not be empty.");
            System.exit(-1);
        }
        IDtmFeignClient feignClient = Feign
                .builder()
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
// if you need read detail log of feign, please cancel the note.
//                .logLevel(feign.Logger.Level.FULL)
//                .logger(new DtmFeignLogger())
                .target(DtmFeignClient.class, Constants.HTTP_PREFIX + endpoint);

        if (feignClient == null) {
            log.error("initial dtm client for java error, feign client can't be null.");
            System.exit(-1);
        }

        this.feignClient = feignClient;
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

    /**
     * start a tcc transaction without gid, client send a request to dtm svr for obtain a new gid.
     * @param function consumer
     * @return gid
     * @throws Exception exception
     */
    public String tccGlobalTransaction(DtmConsumer<Tcc> function) throws Exception {
        Tcc tcc = new Tcc(null, feignClient);
        return tcc.tccGlobalTransaction(function);
    }

    /**
     * start a tcc transaction with a custom gid.
     * @param gid gid
     * @param function consumer
     * @return gid
     * @throws Exception exception
     */
    public String tccGlobalTransaction(String gid, DtmConsumer<Tcc> function) throws Exception {
        Tcc tcc = new Tcc(gid, feignClient);
        return tcc.tccGlobalTransaction(function);
    }

    /**
     * start a saga transaction with custom gid
     * @param gid gid
     * @return Saga
     */
    public Saga newSaga(String gid) {
        return new Saga(gid, feignClient);
    }

    /**
     * start a saga transaction without gid, client send a request to dtm svr for obtain a new gid.
     * @return Saga
     */
    public Saga newSaga() {
        return new Saga(null, feignClient);
    }
}

package pub.dtm.client.feign;

import com.alibaba.nacos.api.naming.pojo.Instance;
import pub.dtm.client.constant.Constants;
import pub.dtm.client.interfaces.feign.IURIParser;
import pub.dtm.client.model.feign.ServiceMessage;
import pub.dtm.client.properties.DtmProperties;
import pub.dtm.client.utils.FeignUtils;
import pub.dtm.client.utils.NacosUtils;

public class URIParser implements IURIParser {
    static {
        FeignUtils.setUriParser(new URIParser());
    }

    @Override
    public String generatorURI(ServiceMessage serviceMessage, boolean httpType) throws Exception {
        Instance instance = NacosUtils.selectOneHealthyInstance(serviceMessage.getServiceName(),
                serviceMessage.getGroupName(), serviceMessage.getCluster());
        if (httpType) {
            return Constants.HTTP_PREFIX + instance.toInetAddr();
        }
        return DtmProperties.get("dtm.service.registryType") + "://" + serviceMessage.toString();
    }
}

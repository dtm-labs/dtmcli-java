package feign;

import com.alibaba.nacos.api.naming.pojo.Instance;
import constant.Constants;
import interfaces.feign.IURIParser;
import model.feign.ServiceMessage;
import properties.DtmProperties;
import utils.FeignUtils;
import utils.NacosUtils;

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

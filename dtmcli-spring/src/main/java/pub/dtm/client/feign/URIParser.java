package pub.dtm.client.feign;

import pub.dtm.client.constant.Constants;
import pub.dtm.client.interfaces.feign.IURIParser;
import pub.dtm.client.model.feign.ServiceMessage;
import pub.dtm.client.utils.FeignUtils;

public class URIParser implements IURIParser {
    static {
        FeignUtils.setUriParser(new URIParser());
    }

    private static String registryType;

    public static void setRegistryType(String registryType) {
        URIParser.registryType = registryType;
    }

    @Override
    public String generatorURI(ServiceMessage serviceMessage, boolean httpType) throws Exception {
        if (httpType) {
            return Constants.HTTP_PREFIX + serviceMessage.getServiceName();
        }
        return registryType + "://" + serviceMessage.toString();
    }
}

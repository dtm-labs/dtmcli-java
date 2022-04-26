package feign;

import constant.Constants;
import interfaces.feign.IURIParser;
import model.feign.ServiceMessage;
import utils.FeignUtils;

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

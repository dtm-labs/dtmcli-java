package pub.dtm.client.interfaces.feign;

import pub.dtm.client.model.feign.ServiceMessage;

public interface IURIParser {
    String generatorURI(ServiceMessage serviceMessage, boolean httpType) throws Exception;
}

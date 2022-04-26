package interfaces.feign;

import model.feign.ServiceMessage;

public interface IURIParser {
    String generatorURI(ServiceMessage serviceMessage, boolean httpType) throws Exception;
}

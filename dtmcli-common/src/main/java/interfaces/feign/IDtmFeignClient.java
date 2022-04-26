package interfaces.feign;

import feign.Response;
import model.param.OperatorParam;
import model.response.ResponseBase;

import java.net.URI;
import java.util.Map;

public interface IDtmFeignClient {
    ResponseBase newGid();

    ResponseBase ping();

    ResponseBase prepare(OperatorParam body);

    ResponseBase submit(OperatorParam body);

    ResponseBase abort(OperatorParam body);

    ResponseBase registerBranch(OperatorParam body);

    Response busiGet(URI host, String path, Map<String, Object> queryMap);

    Response busiPost(URI host, String path, Map<String, Object> queryMap, Object body);
}

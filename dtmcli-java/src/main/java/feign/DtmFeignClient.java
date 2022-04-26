package feign;

import constant.Constants;
import interfaces.feign.IDtmFeignClient;
import model.param.OperatorParam;
import model.response.ResponseBase;

import java.net.URI;
import java.util.Map;

@Headers("Content-Type: application/json")
public interface DtmFeignClient extends IDtmFeignClient {
    @Override
    @RequestLine(Constants.GET_METHOD + Constants.NEW_GID_URL)
    ResponseBase newGid();

    @Override
    @RequestLine(Constants.GET_METHOD + Constants.PING_URL)
    ResponseBase ping();

    @Override
    @RequestLine(Constants.POST_METHOD + Constants.PREPARE_URL)
    ResponseBase prepare(OperatorParam body);

    @Override
    @RequestLine(Constants.POST_METHOD + Constants.SUBMIT_URL)
    ResponseBase submit(OperatorParam body);

    @Override
    @RequestLine(Constants.POST_METHOD + Constants.ABORT_URL)
    ResponseBase abort(OperatorParam body);

    @Override
    @RequestLine(Constants.POST_METHOD + Constants.REGISTER_BRANCH_URL)
    ResponseBase registerBranch(OperatorParam body);

    @Override
    @RequestLine(Constants.GET_METHOD + "{path}")
    Response busiGet(URI host, @Param("path") String path, @QueryMap Map<String, Object> queryMap);

    @Override
    @RequestLine(Constants.POST_METHOD + "{path}")
    Response busiPost(URI host, @Param("path") String path, @QueryMap Map<String, Object> queryMap, Object body);
}

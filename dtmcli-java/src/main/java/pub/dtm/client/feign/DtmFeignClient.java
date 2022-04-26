package pub.dtm.client.feign;

import feign.*;
import pub.dtm.client.constant.Constants;
import pub.dtm.client.interfaces.feign.IDtmFeignClient;
import pub.dtm.client.model.param.OperatorParam;
import pub.dtm.client.model.responses.DtmResponse;

import java.net.URI;
import java.util.Map;

@Headers("Content-Type: application/json")
public interface DtmFeignClient extends IDtmFeignClient {
    @Override
    @RequestLine(Constants.GET_METHOD + Constants.NEW_GID_URL)
    DtmResponse newGid();

    @Override
    @RequestLine(Constants.GET_METHOD + Constants.PING_URL)
    DtmResponse ping();

    @Override
    @RequestLine(Constants.POST_METHOD + Constants.PREPARE_URL)
    DtmResponse prepare(OperatorParam body);

    @Override
    @RequestLine(Constants.POST_METHOD + Constants.SUBMIT_URL)
    DtmResponse submit(OperatorParam body);

    @Override
    @RequestLine(Constants.POST_METHOD + Constants.ABORT_URL)
    DtmResponse abort(OperatorParam body);

    @Override
    @RequestLine(Constants.POST_METHOD + Constants.REGISTER_BRANCH_URL)
    DtmResponse registerBranch(OperatorParam body);

    @Override
    @RequestLine(Constants.GET_METHOD + "{path}")
    Response busiGet(URI host, @Param("path") String path, @QueryMap Map<String, Object> queryMap);

    @Override
    @RequestLine(Constants.POST_METHOD + "{path}")
    Response busiPost(URI host, @Param("path") String path, @QueryMap Map<String, Object> queryMap, Object body);
}

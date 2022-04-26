package pub.dtm.client.interfaces.feign;

import feign.Response;
import pub.dtm.client.model.param.OperatorParam;
import pub.dtm.client.model.responses.DtmResponse;

import java.net.URI;
import java.util.Map;

public interface IDtmFeignClient {
    DtmResponse newGid();

    DtmResponse ping();

    DtmResponse prepare(OperatorParam body);

    DtmResponse submit(OperatorParam body);

    DtmResponse abort(OperatorParam body);

    DtmResponse registerBranch(OperatorParam body);

    Response busiGet(URI host, String path, Map<String, Object> queryMap);

    Response busiPost(URI host, String path, Map<String, Object> queryMap, Object body);
}

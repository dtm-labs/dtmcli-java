package pub.dtm.client.interfaces.feign;

import feign.Response;
import pub.dtm.client.model.param.OperatorParam;
import pub.dtm.client.model.responses.DtmResponse;

import java.net.URI;
import java.util.Map;

/**
 * A feign client interface for dtm svr, we designed different implements for different client.
 *
 * @author horseLk
 */
public interface IDtmFeignClient {
    /**
     * get a new gid
     */
    DtmResponse newGid();

    /**
     * test connection
     */
    DtmResponse ping();

    /**
     * prepare
     * @param body prepare body
     */
    DtmResponse prepare(OperatorParam body);

    /**
     * submit
     * @param body submit bosy
     */
    DtmResponse submit(OperatorParam body);

    /**
     * abort
     * @param body abort body
     */
    DtmResponse abort(OperatorParam body);

    /**
     * registerBranch
     * @param body registerBranch body
     */
    DtmResponse registerBranch(OperatorParam body);

    /**
     * use feign send busi get request
     * @param host busi host
     * @param path busi path
     * @param queryMap querymao
     */
    Response busiGet(URI host, String path, Map<String, Object> queryMap);

    /**
     * use feign send busi post request
     * @param host busi host
     * @param path busi path
     * @param queryMap query map
     * @param body request body
     */
    Response busiPost(URI host, String path, Map<String, Object> queryMap, Object body);
}

/*
 * MIT License
 *
 * Copyright (c) 2022 yedf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package pub.dtm.client.interfaces.communication;

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
public interface IDtmCommunicationClient {
    /**
     * get stubType
     * @return type
     */
    String stubType();

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

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

package pub.dtm.client.feign;

import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import feign.Response;
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

/*
 * MIT License
 *
 * Copyright (c) 2022 dtm-labs
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

package pub.dtm.client.stub;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pub.dtm.client.constant.Constants;
import pub.dtm.client.interfaces.stub.IDtmServerStub;
import pub.dtm.client.model.param.OperatorParam;
import pub.dtm.client.model.responses.DtmResponse;

import java.net.URI;
import java.util.Map;

/**
 * IdtmServerStub implements for feign-spring
 *
 * @author horseLk
 */
@FeignClient(value = "${dtm.service.name}")
public interface DtmFeignClient extends IDtmServerStub {
    @Override
    default String stubType() {
        return "feign-spring";
    }

    @Override
    @GetMapping(Constants.NEW_GID_URL)
    DtmResponse newGid();

    @Override
    @GetMapping(Constants.PING_URL)
    DtmResponse ping();

    @Override
    @PostMapping(Constants.PREPARE_URL)
    DtmResponse prepare(@RequestBody OperatorParam body);

    @Override
    @PostMapping(Constants.SUBMIT_URL)
    DtmResponse submit(@RequestBody OperatorParam body);

    @Override
    @PostMapping(Constants.ABORT_URL)
    DtmResponse abort(@RequestBody OperatorParam body);

    @Override
    @PostMapping(Constants.REGISTER_BRANCH_URL)
    DtmResponse registerBranch(@RequestBody OperatorParam body);

    @Override
    @GetMapping(value = "{path}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Response busiGet(URI host, @PathVariable("path") String path, @SpringQueryMap Map<String, Object> queryMap);

    @Override
    @PostMapping(value = "{path}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Response busiPost(URI host, @PathVariable("path") String path, @SpringQueryMap Map<String, Object> queryMap, @RequestBody Object body);
}

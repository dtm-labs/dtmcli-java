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

package pub.dtm.client.utils;

import pub.dtm.client.constant.Constants;
import pub.dtm.client.exception.FailureException;
import feign.Response;
import pub.dtm.client.interfaces.feign.IURIParser;
import pub.dtm.client.model.feign.ServiceMessage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.dtm.client.model.responses.DtmResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Feign Utils
 *
 * @author horseLk
 */
public class FeignUtils {
    /**
     * log
     */
    private static final Logger log = LoggerFactory.getLogger(FeignUtils.class);

    /**
     * URIParser
     */
    private static IURIParser uriParser;

    public static void setUriParser(IURIParser uriParser) {
        FeignUtils.uriParser = uriParser;
    }

    /**
     * Get gid from DtmResponse
     * @param base dtmResponse
     * @return gid
     * @throws Exception exception
     */
    public static String parseGid(DtmResponse base) throws Exception {
        if (base == null || !Constants.SUCCESS_RESULT.equals(base.getDtmResult())) {
            throw new Exception("get new gid from dtm server fail.");
        }
        return base.getGid();
    }

    /**
     * generate uri
     * @param serviceMessage service message
     * @param httpType true means http, false means micro service
     * @return uri
     * @throws Exception exception
     */
    public static String generatorURI(ServiceMessage serviceMessage, boolean httpType) throws Exception {
        return uriParser.generatorURI(serviceMessage, httpType);
    }

    /**
     * check response
     * @param response feign response
     * @throws FailureException exception
     */
    public static void checkResult(Response response) throws FailureException {
        if (response.status() >= Constants.RESP_ERR_CODE){
            if (response.reason() != null) {
                throw new FailureException(response.reason());
            }
            try {
                log.error("response code is {}, but unknown reason, response body is {}", response.status(),
                        IOUtils.toString(response.body().asReader(StandardCharsets.UTF_8)));
            } finally {
                throw new FailureException("response code is " + response.status());
            }
        }
        String result = "";
        try {
            InputStream inputStream = response.body().asInputStream();
            if (inputStream != null) {
                result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new FailureException("response is null");
        }
        if (StringUtils.isBlank(result)) {
            throw new FailureException("response is null");
        }
        if (result.contains(Constants.FAILURE_RESULT)){
            throw new FailureException("Service returned failed");
        }
    }
}

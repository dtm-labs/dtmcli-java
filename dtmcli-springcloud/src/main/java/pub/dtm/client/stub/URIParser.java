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

import pub.dtm.client.constant.Constants;
import pub.dtm.client.interfaces.stub.IURIParser;
import pub.dtm.client.model.feign.ServiceMessage;
import pub.dtm.client.utils.FeignUtils;

/**
 * Parse url to dtm server for spring cloud client.
 *
 * @author horse
 */
public class URIParser implements IURIParser {
    static {
        FeignUtils.setUriParser(new URIParser());
    }

    private static String registryType;

    public static void setRegistryType(String registryType) {
        URIParser.registryType = registryType;
    }

    @Override
    public String generatorURI(ServiceMessage serviceMessage, boolean httpType) throws Exception {
        if (httpType) {
            return Constants.HTTP_PREFIX + serviceMessage.getServiceName();
        }
        return registryType + "://" + serviceMessage.toString();
    }
}

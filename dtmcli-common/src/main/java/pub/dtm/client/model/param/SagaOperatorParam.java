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

package pub.dtm.client.model.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import pub.dtm.client.constant.ParamFieldConstants;
import pub.dtm.client.enums.TransTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * Saga Operator Param
 *
 * @author horseLk
 */
@Data
@NoArgsConstructor
public class SagaOperatorParam extends OperatorParam {
    @JsonProperty(ParamFieldConstants.STEPS)
    private List<Map<String, String>> steps;

    @JsonProperty(ParamFieldConstants.PAYLOADS)
    private List<String> payloads;

    @JsonProperty(ParamFieldConstants.CUSTOM_DATA)
    private String customData;

    @JsonProperty(ParamFieldConstants.WAIT_RESULT)
    private boolean waitResult;

    @JsonProperty(ParamFieldConstants.TIMEOUT_TO_FAIL)
    private long timeoutToFail;

    @JsonProperty(ParamFieldConstants.RETRY_INTERVAL)
    private long retryInterval;

    @JsonProperty(ParamFieldConstants.PASSTHROGH_HEADERS)
    private List<String> passthroughHeaders;

    @JsonProperty(ParamFieldConstants.BRANCH_HEADERS)
    private Map<String, String> branchHeaders;

    public SagaOperatorParam(String gid, TransTypeEnum transType, List<Map<String, String>> steps,
                             List<String> payloads, String customData, boolean waitResult, long timeoutToFail,
                             long retryInterval, List<String> passthroughHeaders, Map<String, String> branchHeaders) {
        super(gid, transType);
        setSubType(transType.getValue());
        this.steps = steps;
        this.payloads = payloads;
        this.customData = customData;
        this.waitResult = waitResult;
        this.timeoutToFail = timeoutToFail;
        this.retryInterval = retryInterval;
        this.passthroughHeaders = passthroughHeaders;
        this.branchHeaders = branchHeaders;
    }
}

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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pub.dtm.client.constant.ParamFieldConstants;
import pub.dtm.client.enums.TransTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request Param for dtm svr
 *
 * @author horseLk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "subType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TccOperatorParam.class, name = "tcc"),
        @JsonSubTypes.Type(value = SagaOperatorParam.class, name = "saga")
})
public class OperatorParam {
    /**
     * gid
     */
    @JsonProperty(ParamFieldConstants.GID)
    private String gid;

    /**
     * trans type string value
     */
    @JsonProperty(ParamFieldConstants.TRANS_TYPE)
    private String transType;

    /**
     * this attribute just for mark different sub class for encode/decode.
     */
    @JsonProperty("subType")
    private String subType;

    public OperatorParam(String gid, TransTypeEnum transType) {
        this.gid = gid;
        this.transType = transType.getValue();
    }
}

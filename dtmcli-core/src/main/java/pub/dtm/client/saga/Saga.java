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

package pub.dtm.client.saga;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.dtm.client.constant.ParamFieldConstants;
import pub.dtm.client.enums.TransTypeEnum;
import pub.dtm.client.exception.FailureException;
import pub.dtm.client.interfaces.stub.IDtmServerStub;
import pub.dtm.client.model.dtm.TransBase;
import pub.dtm.client.model.feign.ServiceMessage;
import pub.dtm.client.model.param.SagaOperatorParam;
import pub.dtm.client.utils.FeignUtils;
import pub.dtm.client.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Saga trans type service
 *
 * @author horseLk
 */
@Data
@NoArgsConstructor
public class Saga extends TransBase {
    private static final Logger log = LoggerFactory.getLogger(Saga.class);

    private static final String ORDERS = "orders";

    private static final String CONCURRENT = "concurrent";

    private IDtmServerStub dtmServerStub;

    private boolean concurrent;

    private Map<String, List<Integer>> orders;

    private long timeoutToFail;

    private long retryInterval;

    private Map<String, String> branchHeaders = new HashMap<>();

    private List<String> passthroughHeaders = new ArrayList<>();

    private String customData;

    private List<Map<String, String>> steps = new ArrayList<>();

    private List<String> payloads = new ArrayList<>();

    public Saga(String gid, IDtmServerStub dtmServerStub) {
        super(gid, TransTypeEnum.SAGA, false);
        this.concurrent = false;
        this.orders = new HashMap<>();
        this.dtmServerStub = dtmServerStub;
    }

    public Saga add(ServiceMessage action, ServiceMessage compensate, Object postData) {
        Map<String, String> step = new HashMap<>();
        try {
            step.put(ParamFieldConstants.ACTION, FeignUtils.generatorURI(action, false));
            step.put(ParamFieldConstants.COMPENSATE, FeignUtils.generatorURI(compensate, false));
            this.payloads.add(JsonUtils.toJson(postData));
        } catch (Exception e) {
            log.error("saga add branch error.");
        }
        this.steps.add(step);

        return this;
    }

    public Saga add(String action, String compensate, Object postData) {
        Map<String, String> step = new HashMap<>();
        step.put(ParamFieldConstants.ACTION, action);
        step.put(ParamFieldConstants.COMPENSATE, compensate);
        this.steps.add(step);
        try {
            this.payloads.add(JsonUtils.toJson(postData));
        } catch (Exception e) {
            log.error("encode json error.");
        }
        return this;
    }

    public String submit() throws Exception {
        if (StringUtils.isEmpty(this.getGid())) {
            this.setGid(FeignUtils.parseGid(dtmServerStub.newGid()));
        }
        addConcurrentContext();
        SagaOperatorParam operatorParam = new SagaOperatorParam(this.getGid(), TransTypeEnum.SAGA, this.getSteps(),
                this.getPayloads(), this.getCustomData(), this.isWaitResult(), this.getTimeoutToFail(),
                this.getRetryInterval(), this.getPassthroughHeaders(), this.getBranchHeaders());

        try {
            dtmServerStub.submit(operatorParam);
        } catch (Exception e) {
            log.error("saga transaction submit failed, transaction gid is {}", this.getGid());
            throw new FailureException(e);
        }
        return this.getGid();
    }

    public Saga addBranchOrder(Integer branch, List<Integer> preBranches) {
        orders.put(branch.toString(), preBranches);
        return this;
    }

    public Saga enableConcurrent() {
        concurrent = true;
        return this;
    }

    public Saga enableWaitResult() {
        this.setWaitResult(true);
        return this;
    }

    public Saga setTimeoutToFail(long timeoutToFail) {
        this.setTimeoutToFail(timeoutToFail);
        return this;
    }

    public Saga setRetryInterval(long retryInterval) {
        this.setRetryInterval(retryInterval);
        return this;
    }

    public Saga setBranchHeaders(Map<String, String> headers) {
        this.setBranchHeaders(headers);
        return this;
    }

    public Saga setPassthroughHeaders(ArrayList<String> passthroughHeaders) {
        this.setPassthroughHeaders(passthroughHeaders);
        return this;
    }

    private void addConcurrentContext() {
        if (concurrent) {
            HashMap<String, Object> data = new HashMap<>();
            data.put(ORDERS, orders);
            data.put(CONCURRENT, true);
            try {
                this.setCustomData(JsonUtils.toJson(data));
            } catch (Exception e) {
                log.error("encode json error.");
            }
        }
    }
}

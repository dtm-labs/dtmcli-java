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

package pub.dtm.client.grpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.google.protobuf.GeneratedMessageV3;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.dtm.client.enums.TransTypeEnum;
import pub.dtm.client.exception.FailureException;
import pub.dtm.client.grpc.Dtmgimp.DtmRequest;
import pub.dtm.client.grpc.Dtmgimp.DtmTransOptions;
import pub.dtm.client.model.dtm.TransBase;
import pub.dtm.client.utils.JsonUtils;


@Data
public class SagaGrpc extends TransBase {
    private static final Logger log = LoggerFactory.getLogger(SagaGrpc.class);

    private static final String ORDERS = "orders";

    private static final String CONCURRENT = "concurrent";

    private boolean concurrent;

    private long retryLimit;

    private long requestTimeout;

    private long retryInterval;

    private final DtmGrpc.DtmBlockingStub dtmBlockingStub;

    private final Dtmgimp.DtmRequest.Builder dtmRequest = DtmRequest.newBuilder();

    private List<Map<String, String>> steps = new ArrayList<>();

    private List<ByteString> binPayloads = new ArrayList<>();

    private Map<String, String> branchHeaders = new HashMap<>();

    private Map<String, List<Integer>> orders = new HashMap<>();

    public SagaGrpc(String gid, DtmGrpc.DtmBlockingStub dtmServerStub) {
        super(gid, TransTypeEnum.SAGA, false);
        this.dtmBlockingStub = dtmServerStub;
    }

    public <ReqT extends GeneratedMessageV3> SagaGrpc add(String action, String compensate, ReqT protoMsg) {
        Map<String, String> step = new HashMap<>();
        step.put("action", action);
        step.put("compensate", compensate);
        this.steps.add(step);

        try {
            this.binPayloads.add(protoMsg.toByteString());
        } catch (Exception var6) {
            log.error("proto msg encode byte string error.");
        }

        return this;
    }

    public String submit() throws FailureException {
        if (StringUtils.isEmpty(this.getGid())) {
            Empty empty = Empty.newBuilder().build();
            Dtmgimp.DtmGidReply gidReply = this.dtmBlockingStub.newGid(empty);
            this.setGid(gidReply.getGid());
        }

        this.addConcurrentContext();
        Dtmgimp.DtmRequest.Builder dtmRequest = this.dtmRequest;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String steps = objectMapper.writeValueAsString(this.getSteps());
            dtmRequest
                    .setTransType(TransTypeEnum.SAGA.toString().toLowerCase())
                    .setGid(this.getGid())
                    .addAllBinPayloads(this.getBinPayloads())
                    .setTransOptions(this.getRequestOptions())
                    .setSteps(steps);
            Empty rsp = this.dtmBlockingStub.submit(dtmRequest.build());
            log.info("saga transaction submit success,  transaction gid is {} req: {}, resp: {}", this.getGid(), dtmRequest, rsp);
        } catch (Exception e) {
            log.error("saga transaction submit failed, transaction gid is {}, error {}", this.getGid(), e.getMessage());
            throw new FailureException(e);
        }

        return this.getGid();
    }

    Dtmgimp.DtmTransOptions getRequestOptions() {
        Dtmgimp.DtmTransOptions.Builder options = DtmTransOptions.newBuilder()
                .setWaitResult(this.isWaitResult())
                .setRetryLimit(this.retryLimit)
                .setRequestTimeout(this.requestTimeout)
                .setRetryInterval(this.retryInterval)
                .putAllBranchHeaders(this.branchHeaders);
        return options.build();
    }

    public SagaGrpc setRequestTimeout(long requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    public SagaGrpc setRetryInterval(long retryInterval) {
        this.retryInterval = retryInterval;
        return this;
    }

    public SagaGrpc setRetryLimit(long retryLimit) {
        this.retryLimit = retryLimit;
        return this;
    }

    public SagaGrpc addBranchOrder(Integer branch, List<Integer> preBranches) {
        this.orders.put(branch.toString(), preBranches);
        return this;
    }

    public SagaGrpc enableConcurrent() {
        this.concurrent = true;
        return this;
    }

    public SagaGrpc setBranchHeaders(Map<String, String> branchHeaders) {
        this.branchHeaders = branchHeaders;
        return this;
    }

    private void addConcurrentContext() {
        if (this.concurrent) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("orders", this.orders);
            data.put("concurrent", true);

            try {
                this.dtmRequest.setCustomedData(JsonUtils.toJson(data));
            } catch (Exception var3) {
                log.error("saga grpc add context, encode json error.");
            }
        }

    }


}

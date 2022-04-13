/*
 * MIT License
 *
 * Copyright (c) 2021 yedf
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

package saga;

import com.alibaba.fastjson.JSONObject;
import common.constant.Constant;
import common.constant.ParamFieldConstant;
import common.model.DtmServerInfo;
import common.utils.HttpUtil;
import common.enums.TransTypeEnum;
import common.model.TransBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Saga {

    private static final String ORDERS = "orders";

    private static final String CONCURRENT = "concurrent";

    private TransBase transBase;

    private DtmServerInfo dtmServerInfo;

    private boolean concurrent;

    private Map<String, ArrayList<Integer>> orders;

    public Saga(String ipPort, String gid) {
        this.dtmServerInfo = new DtmServerInfo(ipPort);
        this.transBase = new TransBase(TransTypeEnum.SAGA, gid, false);
        this.concurrent = false;
        this.orders = new HashMap<>();
    }

    public Saga add(String action, String compensate, Object postData) {
        HashMap<String, String> step = new HashMap<>();
        step.put(ParamFieldConstant.ACTION, action);
        step.put(ParamFieldConstant.COMPENSATE, compensate);
        transBase.getSteps().add(step);
        transBase.getPayloads().add(JSONObject.toJSONString(postData));
        return this;
    }

    public Saga addBranchOrder(Integer branch, ArrayList<Integer> preBranches) {
        orders.put(branch.toString(), preBranches);
        return this;
    }

    public Saga enableConcurrent() {
        concurrent = true;
        return this;
    }

    public void submit() throws Exception {
        addConcurrentContext();
        HashMap<String, Object> mapParam = new HashMap<>(Constant.DEFAULT_INITIAL_CAPACITY);
        mapParam.put(ParamFieldConstant.GID, transBase.getGid());
        mapParam.put(ParamFieldConstant.TRANS_TYPE, TransTypeEnum.SAGA.getValue());
        mapParam.put(ParamFieldConstant.STEPS, transBase.getSteps());
        mapParam.put(ParamFieldConstant.PAYLOADS, transBase.getPayloads());
        mapParam.put(ParamFieldConstant.CUSTOM_DATA, transBase.getCustomData());
        mapParam.put(ParamFieldConstant.WAIT_RESULT, transBase.isWaitResult());
        mapParam.put(ParamFieldConstant.TIMEOUT_TO_FAIL, transBase.getTimeoutToFail());
        mapParam.put(ParamFieldConstant.RETRY_INTERVAL, transBase.getRetryInterval());
        mapParam.put(ParamFieldConstant.PASSTHROGH_HEADERS, transBase.getPassthroughHeaders());
        mapParam.put(ParamFieldConstant.BRANCH_HEADERS, transBase.getBranchHeaders());

        HttpUtil.post(dtmServerInfo.submit(), JSONObject.toJSONString(mapParam));
    }

    public Saga enableWaitResult() {
        transBase.setWaitResult(true);
        return this;
    }

    public Saga setTimeoutToFail(long timeoutToFail) {
        transBase.setTimeoutToFail(timeoutToFail);
        return this;
    }

    public Saga setRetryInterval(long retryInterval) {
        transBase.setRetryInterval(retryInterval);
        return this;
    }

    public Saga setBranchHeaders(Map<String, String> headers) {
        transBase.setBranchHeaders(headers);
        return this;
    }

    public Saga setPassthroughHeaders(ArrayList<String> passthroughHeaders) {
        transBase.setPassthroughHeaders(passthroughHeaders);
        return this;
    }

    private void addConcurrentContext() {
        if (concurrent) {
            HashMap<String, Object> data = new HashMap<>();
            data.put(ORDERS, orders);
            data.put(CONCURRENT, true);
            transBase.setCustomData(JSONObject.toJSONString(data));
        }
    }
}

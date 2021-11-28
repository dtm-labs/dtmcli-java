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

package tcc;

import com.alibaba.fastjson.JSONObject;
import common.constant.Constant;
import common.model.DtmServerInfo;
import common.utils.HttpUtil;
import common.utils.BranchIdGeneratorUtil;
import common.constant.ParamFieldConstant;
import common.enums.TransTypeEnum;
import common.model.TransBase;
import exception.FailureException;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * @author lixiaoshuang
 */
public class Tcc {
    
    private static final String DEFAULT_STATUS = "prepared";
    
    private static final String OP = "try";
    
    private static final String FAIL_RESULT = "FAILURE";
    
    Logger log = LoggerFactory.getLogger(Tcc.class);
    
    
    /**
     * 事务信息
     */
    private TransBase transBase;
    
    /**
     * server 信息
     */
    private DtmServerInfo dtmServerInfo;
    
    /**
     * id 生成器
     */
    private BranchIdGeneratorUtil branchIdGeneratorUtil;
    
    public Tcc(String ipPort, String gid) {
        this.dtmServerInfo = new DtmServerInfo(ipPort);
        this.branchIdGeneratorUtil = new BranchIdGeneratorUtil("");
        this.transBase = new TransBase(TransTypeEnum.TCC, gid, false);
    }
    
    public void tccGlobalTransaction(Consumer<Tcc> consumer) throws Exception {
        HashMap<String, Object> paramMap = new HashMap<>(Constant.DEFAULT_INITIAL_CAPACITY);
        paramMap.put(ParamFieldConstant.GID, transBase.getGid());
        paramMap.put(ParamFieldConstant.TRANS_TYPE, TransTypeEnum.TCC.getValue());
        
        try {
            Response post = HttpUtil.post(dtmServerInfo.prepare(), JSONObject.toJSONString(paramMap));
            this.checkResult(post.body().string());
        } catch (FailureException failureException) {
            log.info("tccGlobalTransaction fail message:{}" + failureException.getLocalizedMessage());
            throw failureException;
        }
        
        try {
            consumer.accept(this);
            HttpUtil.post(dtmServerInfo.submit(), JSONObject.toJSONString(paramMap));
        } catch (Exception e) {
            HttpUtil.post(dtmServerInfo.abort(), JSONObject.toJSONString(paramMap));
            throw e;
        }
    }
    
    public Response callBranch(Object body, String tryUrl, String confirmUrl, String cancelUrl) throws Exception {
        String branchId = branchIdGeneratorUtil.genBranchId();
        HashMap<String, Object> registerParam = new HashMap<>(Constant.DEFAULT_INITIAL_CAPACITY);
        registerParam.put(ParamFieldConstant.GID, transBase.getGid());
        registerParam.put(ParamFieldConstant.BRANCH_ID, branchId);
        registerParam.put(ParamFieldConstant.TRANS_TYPE, TransTypeEnum.TCC.getValue());
        registerParam.put(ParamFieldConstant.STATUS, DEFAULT_STATUS);
        registerParam.put(ParamFieldConstant.DATA, JSONObject.toJSONString(body));
        registerParam.put(ParamFieldConstant.TRY, tryUrl);
        registerParam.put(ParamFieldConstant.CONFIRM, confirmUrl);
        registerParam.put(ParamFieldConstant.CANCEL, cancelUrl);
        
        try {
            Response registerResponse = HttpUtil
                    .post(dtmServerInfo.registerTccBranch(), JSONObject.toJSONString(registerParam));
            this.checkResult(registerResponse.body().string());
        } catch (FailureException e) {
            throw e;
        }
        return HttpUtil.post(splicingTryUrl(tryUrl, transBase.getGid(), TransTypeEnum.TCC.getValue(), branchId, OP),
                JSONObject.toJSONString(body));
    }
    
    /**
     * 和go 保持同样的发送方式 参数拼在url后边
     */
    private String splicingTryUrl(String tryUrl, String gid, String transType, String branchId, String op) {
        return tryUrl + "?gid=" + gid + "&trans_type=" + transType + "&branch_id=" + branchId + "&op=" + op;
    }
    
    
    public void checkResult(String response) {
        if (StringUtils.isBlank(response)) {
            throw new FailureException("response is null");
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        Object code = jsonObject.get(ParamFieldConstant.CODE);
        if (null != code && (int) code >= 0) {
            log.error("server error,message:{}", jsonObject.get(ParamFieldConstant.MESSAGE));
            throw new FailureException("server error code >0");
        }
        Object dtmResult = jsonObject.get(ParamFieldConstant.DTM_RESULT);
        if (null == dtmResult || dtmResult.toString().equals(FAIL_RESULT)) {
            log.error("server error,dtmResult:{}", dtmResult);
            throw new FailureException("Service returned failed");
        }
    }
}
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

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import common.constant.Constant;
import common.model.DtmServerInfo;
import common.utils.IdGeneratorUtil;
import common.constant.ParamFieldConstant;
import common.enums.TransTypeEnum;
import common.model.TransBase;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

public class Tcc {
    
    private static final String DEFAULT_STATUS = "prepared";
    
    private static final String BRANCH_TYPE = "try";
    
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
    private IdGeneratorUtil idGeneratorUtil;
    
    public Tcc(TransBase transBase, DtmServerInfo dtmServerInfo) {
        this.transBase = transBase;
        this.dtmServerInfo = dtmServerInfo;
        this.idGeneratorUtil = new IdGeneratorUtil("");
    }
    
    public String tccGlobalTransaction(Function<Tcc, Boolean> function) {
        HashMap<String, Object> paramMap = new HashMap<>(Constant.DEFAULT_INITIAL_CAPACITY);
        paramMap.put(ParamFieldConstant.GID, transBase.getGid());
        paramMap.put(ParamFieldConstant.TRANS_TYPE, TransTypeEnum.TCC.getValue());
        HttpResponse response = HttpRequest.post(dtmServerInfo.prepare()).body(JSONUtil.toJsonStr(paramMap)).execute();
        if (this.checkResult(response)) {
            if (function.apply(this)) {
                HttpRequest.post(dtmServerInfo.submit()).body(JSONUtil.toJsonStr(paramMap)).execute();
            } else {
                HttpRequest.post(dtmServerInfo.abort()).body(JSONUtil.toJsonStr(paramMap)).execute();
            }
        }
        return transBase.getGid();
    }
    
    public boolean callBranch(Object body, String tryUrl, String confirmUrl, String cancelUrl) throws Exception {
        String branchId = idGeneratorUtil.genBranchId();
        HashMap<String, Object> registerParam = new HashMap<>(Constant.DEFAULT_INITIAL_CAPACITY);
        registerParam.put(ParamFieldConstant.GID, transBase.getGid());
        registerParam.put(ParamFieldConstant.BRANCH_ID, branchId);
        registerParam.put(ParamFieldConstant.TRANS_TYPE, TransTypeEnum.TCC.getValue());
        registerParam.put(ParamFieldConstant.STATUS, DEFAULT_STATUS);
        registerParam.put(ParamFieldConstant.DATA, body);
        registerParam.put(ParamFieldConstant.TRY, tryUrl);
        registerParam.put(ParamFieldConstant.CONFIRM, confirmUrl);
        registerParam.put(ParamFieldConstant.CANCEL, cancelUrl);
        
        HttpResponse registerResponse = HttpRequest.post(dtmServerInfo.registerTccBranch())
                .body(JSONUtil.toJsonStr(registerParam)).execute();
        
        if (this.checkResult(registerResponse)) {
            HashMap<String, Object> tryParam = new HashMap<>(Constant.DEFAULT_INITIAL_CAPACITY);
            tryParam.put(ParamFieldConstant.GID, transBase.getGid());
            tryParam.put(ParamFieldConstant.TRANS_TYPE, TransTypeEnum.TCC.getValue());
            tryParam.put(ParamFieldConstant.BRANCH_ID, branchId);
            tryParam.put(ParamFieldConstant.BRANCH_TYPE, BRANCH_TYPE);
            
            HttpResponse tryResponse = HttpRequest.post(tryUrl).body(JSONUtil.toJsonStr(body)).form(tryParam).execute();
            
            return this.checkResult(tryResponse);
        }
        return false;
    }
    
    public boolean checkResult(HttpResponse response) {
        if (Objects.isNull(response)) {
            return false;
        }
        if (!response.isOk()) {
            return false;
        }
        return !response.body().contains("FAILURE");
    }
}
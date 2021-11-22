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
import common.utils.IdGeneratorUtil;
import common.constant.ParamFieldConstant;
import common.enums.TransTypeEnum;
import common.model.TransBase;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Function;

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
    private IdGeneratorUtil idGeneratorUtil;
    
    public Tcc(String address) throws Exception {
        this.idGeneratorUtil = new IdGeneratorUtil("");
        String gid = idGeneratorUtil.genGid(dtmServerInfo.newGid());
        this.transBase = new TransBase(TransTypeEnum.TCC, gid, false);
        this.dtmServerInfo = new DtmServerInfo(address);
    }
    
    public String tccGlobalTransaction(Function<Tcc, Boolean> function) throws IOException {
        HashMap<String, Object> paramMap = new HashMap<>(Constant.DEFAULT_INITIAL_CAPACITY);
        paramMap.put(ParamFieldConstant.GID, transBase.getGid());
        paramMap.put(ParamFieldConstant.TRANS_TYPE, TransTypeEnum.TCC.getValue());
        String response = HttpUtil.post(dtmServerInfo.prepare(), JSONObject.toJSONString(paramMap));
        if (this.checkResult(response)) {
            if (function.apply(this)) {
                HttpUtil.post(dtmServerInfo.submit(), JSONObject.toJSONString(paramMap));
            } else {
                HttpUtil.post(dtmServerInfo.abort(), JSONObject.toJSONString(paramMap));
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
        
        String registerResponse = HttpUtil
                .post(dtmServerInfo.registerTccBranch(), JSONObject.toJSONString(registerParam));
        
        if (this.checkResult(registerResponse)) {
            HashMap<String, Object> tryParam = new HashMap<>(Constant.DEFAULT_INITIAL_CAPACITY);
            tryParam.put(ParamFieldConstant.GID, transBase.getGid());
            tryParam.put(ParamFieldConstant.TRANS_TYPE, TransTypeEnum.TCC.getValue());
            tryParam.put(ParamFieldConstant.BRANCH_ID, branchId);
            tryParam.put(ParamFieldConstant.OP, OP);
            
            String tryResponse = HttpUtil.post(tryUrl, JSONObject.toJSONString(tryParam));
            
            return this.checkResult(tryResponse);
        }
        return false;
    }
    
    public boolean checkResult(String response) {
        if (StringUtils.isBlank(response)) {
            return false;
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        Object code = jsonObject.get(ParamFieldConstant.CODE);
        if (null != code && (int) code >= 0) {
            log.error("server error,message:{}", jsonObject.get(ParamFieldConstant.MESSAGE));
            return false;
        }
        Object dtmResult = jsonObject.get(ParamFieldConstant.DTM_RESULT);
        if (null == dtmResult || dtmResult.toString().equals(FAIL_RESULT)) {
            log.error("server error,dtmResult:{}", dtmResult);
            return false;
        }
        return true;
    }
}
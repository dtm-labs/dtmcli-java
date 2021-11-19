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

package dtmcli;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import common.TransType;

import java.util.HashMap;
import java.util.function.Function;

public class Tcc extends TransBase {
    
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    
    public Tcc(String dtm, boolean waitResult) throws Exception {
        super(TransType.TCC, dtm, waitResult);
    }
    
    public String tccGlobalTransaction(Function<Tcc, Boolean> function) {
        HashMap<String, Object> paramMap = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        paramMap.put("gid", this.getGid());
        paramMap.put("trans_type", TransType.TCC.getValue());
        HttpResponse response = HttpRequest.post(this.getDtm() + "/prepare").body(JSONUtil.toJsonStr(paramMap))
                .execute();
        if (checkResult(response)) {
            if (function.apply(this)) {
                HttpRequest.post(this.getDtm() + "/submit").body(JSONUtil.toJsonStr(paramMap)).execute();
            } else {
                HttpRequest.post(this.getDtm() + "/abort").body(JSONUtil.toJsonStr(paramMap)).execute();
            }
        }
        return this.getGid();
    }
    
    public boolean callBranch(Object body, String tryUrl, String confirmUrl, String cancelUrl) throws Exception {
        String branchId = this.getIdGenerator().newBranchId();
        HashMap<String, Object> registerParam = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        registerParam.put("gid", this.getGid());
        registerParam.put("branch_id", branchId);
        registerParam.put("trans_type", TransType.TCC.getValue());
        registerParam.put("status", "prepared");
        registerParam.put("data", body);
        registerParam.put("try", tryUrl);
        registerParam.put("confirm", confirmUrl);
        registerParam.put("cancel", cancelUrl);
        
        HttpResponse registerResponse = HttpRequest.post(this.getDtm() + "/registerTccBranch")
                .body(JSONUtil.toJsonStr(registerParam)).execute();
        
        if (checkResult(registerResponse)) {
            HashMap<String, Object> tryParam = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
            tryParam.put("gid", this.getGid());
            tryParam.put("trans_type", TransType.TCC.getValue());
            tryParam.put("branch_id", branchId);
            tryParam.put("branch_type", "try");
            
            HttpResponse tryResponse = HttpRequest.post(tryUrl).body(JSONUtil.toJsonStr(body)).form(tryParam).execute();
            
            return checkResult(tryResponse);
        }
        return false;
    }
}
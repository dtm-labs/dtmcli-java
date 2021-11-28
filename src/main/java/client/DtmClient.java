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

package client;


import com.alibaba.fastjson.JSONObject;
import common.model.DtmServerInfo;
import common.utils.HttpUtil;
import okhttp3.Response;
import tcc.Tcc;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author lixiaoshuang
 */
public class DtmClient {
    
    private String ipPort;
    
    public DtmClient(String ipPort) {
        this.ipPort = ipPort;
    }
    
    /**
     * 生成全局事务id
     *
     * @return
     * @throws Exception
     */
    public String genGid() throws Exception {
        DtmServerInfo dtmServerInfo = new DtmServerInfo(ipPort);
        JSONObject jsonObject = null;
        try {
            Response response = HttpUtil.get(dtmServerInfo.newGid());
            if (Objects.nonNull(response.body())) {
                String result = response.body().string();
                jsonObject = JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            throw new Exception("Can’t get gid, please check the dtm server.");
        }
        if (Objects.isNull(jsonObject)) {
            throw new Exception("Can’t get gid, please check the dtm server.");
        }
        Object code = jsonObject.get("code");
        if (null != code && (int) code > 0) {
            Object message = jsonObject.get("message");
            throw new Exception(message.toString());
        }
        return jsonObject.get("gid").toString();
    }
    
    /**
     * tcc事务
     *
     * @param gid
     * @param function
     * @return
     * @throws Exception
     */
    public void tccGlobalTransaction(String gid, Function<Tcc, Boolean> function) throws Exception {
        Tcc tcc = new Tcc(ipPort, gid);
        tcc.tccGlobalTransaction(function);
    }
    
    
}

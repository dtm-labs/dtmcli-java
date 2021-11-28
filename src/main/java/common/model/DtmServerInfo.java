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

package common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lixiaoshuang
 */
@Data
@NoArgsConstructor
public class DtmServerInfo {
    
    /**
     * ip+port
     */
    private String ipPort;
    
    
    public static final String PREFIX = "http://";
    
    public static final String BASE = "/api/dtmsvr";
    
    public static final String NEW_GID = BASE + "/newGid";
    
    public static final String PREPARE = BASE + "/prepare";
    
    public static final String SUBMIT = BASE + "/submit";
    
    public static final String ABORT = BASE + "/abort";
    
    public static final String REGISTER_TCC_BRANCH = BASE + "/registerTccBranch";
    
    public DtmServerInfo(String ipPort) {
        this.ipPort = ipPort;
    }
    
    /**
     * 生成gid url
     *
     * @return
     */
    public String newGid() {
        return PREFIX + ipPort + NEW_GID;
    }
    
    /**
     * 生成gid url
     *
     * @return
     */
    public String prepare() {
        return PREFIX + ipPort + PREPARE;
    }
    
    /**
     * submit 阶段 url
     *
     * @return
     */
    public String submit() {
        return PREFIX + ipPort + SUBMIT;
    }
    
    /**
     * abort 阶段 url
     *
     * @return
     */
    public String abort() {
        return PREFIX + ipPort + ABORT;
    }
    
    /**
     * 注册tcc 事务分支
     *
     * @return
     */
    public String registerTccBranch() {
        return PREFIX + ipPort + REGISTER_TCC_BRANCH;
    }
    
}

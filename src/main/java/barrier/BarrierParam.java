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

package barrier;

import lombok.Data;

/**
 * dtm 服务端传过来的字段格式为下划线分隔，为了减少sdk对json包依赖也采用下划线格式
 *
 * @author lixiaoshuang
 */
@Data
public class BarrierParam {
    
    /**
     * 事务类型
     */
    private String trans_type[];
    
    /**
     * 全局事务id
     */
    private String gid[];
    
    /**
     * 分支id
     */
    private String branch_id[];
    
    /**
     * 操作
     */
    private String op[];
    
}

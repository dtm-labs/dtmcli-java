/*
 * MIT License
 *
 * Copyright (c) 2022 yedf
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

package pub.dtm.client.base;

/**
 * BranchIdGenerator for generate branch id
 *
 * @author horseLk
 */
public class BranchIdGenerator {
    private static final int MAX_BRANCH_ID = 99;

    private static final int LENGTH = 20;

    /**
     * branch id prefix
     */
    private final String branchId;

    /**
     * last branch id
     */
    private int subBranchId;

    public BranchIdGenerator(String branchId) {
        this.branchId = branchId;
    }

    /**
     * generate branch id
     *
     * @return branch id
     * @throws Exception exception
     */
    public String genBranchId() throws Exception {
        if (this.subBranchId >= MAX_BRANCH_ID) {
            throw new Exception("branch id is larger than 99");
        }
        if (this.branchId.length() >= LENGTH) {
            throw new Exception("total branch id is longer than 20");
        }
        this.subBranchId++;
        return this.branchId + String.format("%02d", this.subBranchId);
    }
}

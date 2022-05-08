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

package pub.dtm.client.constant;

/**
 * Constants
 *
 * @author horseLk
 */
public class Constants {
    public static final String MICRO_SERVICE_NAME_KEY = "dtm.service.name";

    public static final String GET_METHOD = "GET ";

    public static final String POST_METHOD = "POST ";

    public static final String HTTP_PREFIX = "http://";

    public static final String HTTPS_PREFIX = "https://";

    public static final String PING_URL = "/api/ping";

    private static final String BASE_URL = "/api/dtmsvr";

    public static final String NEW_GID_URL = BASE_URL + "/newGid";

    public static final String PREPARE_URL = BASE_URL + "/prepare";

    public static final String SUBMIT_URL = BASE_URL + "/submit";

    public static final String ABORT_URL = BASE_URL + "/abort";

    public static final String REGISTER_BRANCH_URL = BASE_URL + "/registerBranch";

    public static final String DEFAULT_STATUS = "prepared";

    public static final String EMPTY_STRING = "";

    public static final String SUCCESS_RESULT = "SUCCESS";

    public static final String FAILURE_RESULT = "FAILURE";

    public static final int RESP_ERR_CODE = 400;
}

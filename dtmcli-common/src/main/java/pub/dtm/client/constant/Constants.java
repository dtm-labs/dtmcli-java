package pub.dtm.client.constant;

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

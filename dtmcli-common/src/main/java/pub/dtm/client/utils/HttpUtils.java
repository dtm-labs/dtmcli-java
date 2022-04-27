package pub.dtm.client.utils;

import pub.dtm.client.constant.Constants;
import pub.dtm.client.exception.FailureException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtils {
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS).build();

    public static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    public static Response get(String url) throws IOException {
        Request request = new Request.Builder().url(url).get().build();
        return CLIENT.newCall(request).execute();
    }

    public static Response post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE, json);
        Request request = new Request.Builder().url(url).post(body).build();
        return CLIENT.newCall(request).execute();
    }

    public static String splicingUrl(String url, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        StringBuilder builder = new StringBuilder(url).append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }


    public static String splicingUrl(String ip, int port, String path) {
        return Constants.HTTP_PREFIX + ip + ":" + String.valueOf(port) + path;
    }

    public static void checkResult(Response response) throws Exception {
        if (response.code() >= Constants.RESP_ERR_CODE){
            throw new FailureException(response.message());
        }
        ResponseBody body = response.body();
        String result;
        if (body == null || StringUtils.isBlank(result = body.string())) {
            throw new FailureException("response is null");
        }
        if (result.contains(Constants.FAILURE_RESULT)){
            throw new FailureException("Service returned failed");
        }
    }
}

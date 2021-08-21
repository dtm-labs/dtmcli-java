package dtmcli;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import common.TransType;

import java.util.HashMap;
import java.util.function.Function;

public class Tcc extends TransBase {

    public Tcc(String dtm, boolean waitResult) throws Exception {
        super(TransType.TCC, dtm, waitResult);
    }

    public String tccGlobalTransaction(Function<Tcc, Boolean> function) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("gid", this.getGid());
        paramMap.put("trans_type", TransType.TCC.getValue());
        HttpResponse response = HttpRequest.post(this.getDtm() + "/prepare")
                .body(JSONUtil.toJsonStr(paramMap)).execute();
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
        HashMap<String, Object> paramMap1 = new HashMap<>();
        paramMap1.put("gid", this.getGid());
        paramMap1.put("branch_id", branchId);
        paramMap1.put("trans_type", TransType.TCC.getValue());
        paramMap1.put("status", "prepared");
        paramMap1.put("data", body);
        paramMap1.put("try", tryUrl);
        paramMap1.put("confirm", confirmUrl);
        paramMap1.put("cancel", cancelUrl);

        HttpResponse response = HttpRequest.post(this.getDtm() + "/registerTccBranch")
                .body(JSONUtil.toJsonStr(paramMap1))
                .execute();

        if (checkResult(response)) {
            HashMap<String, Object> paramMap2 = new HashMap<>();
            paramMap2.put("gid", this.getGid());
            paramMap2.put("trans_type", TransType.TCC.getValue());
            paramMap2.put("branch_id", branchId);
            paramMap2.put("branch_type", "try");

            HttpResponse response2 = HttpRequest.post(tryUrl)
                    .body(JSONUtil.toJsonStr(body))
                    .form(paramMap2)
                    .execute();

            return checkResult(response2);
        }
        return false;
    }
}
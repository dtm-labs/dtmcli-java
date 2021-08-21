package dtmcli;

import cn.hutool.http.HttpResponse;
import common.IdGenerator;
import common.TransType;

public class TransBase {
    private String gid;
    private TransType transType;
    private IdGenerator idGenerator;
    private String dtm;
    private boolean waitResult;

    public TransBase(TransType transType, String dtm, boolean waitResult) throws Exception {
        this.gid = IdGenerator.genGid(dtm);
        this.transType = transType;
        this.dtm = dtm;
        this.waitResult = waitResult;
        this.idGenerator = new IdGenerator("");
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public TransType getTransType() {
        return transType;
    }

    public void setTransType(TransType transType) {
        this.transType = transType;
    }

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public String getDtm() {
        return dtm;
    }

    public void setDtm(String dtm) {
        this.dtm = dtm;
    }

    public boolean isWaitResult() {
        return waitResult;
    }

    public void setWaitResult(boolean waitResult) {
        this.waitResult = waitResult;
    }

    public static boolean checkResult(HttpResponse response) {
        if (!response.isOk()) {
            return false;
        }
        if (response.body().contains("FAILURE")) {
            return false;
        }
        return true;
    }
}
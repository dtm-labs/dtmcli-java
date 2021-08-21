package common;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class IdGenerator {
    private String parentId;
    private int branchId;

    public IdGenerator(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }


    public static String genGid(String dtm) throws Exception {
        String content = HttpUtil.get(dtm + "/newGid");
        try {
            JSONObject jsonObject = JSONUtil.parseObj(content);
            return jsonObject.get("gid").toString();
        } catch (Exception e) {
            throw new Exception("Can’t get gid, please check the dtm server.");
        }
    }

    public String newBranchId() throws Exception {
        if (this.branchId >= 99) {
            throw new Exception("branch id is larger than 99");
        }
        if ((this.parentId + "").length() >= 20) {
            throw new Exception("total branch id is longer than 20");
        }
        this.branchId++;
        return this.parentId + String.format("%02d", this.branchId);
    }
}
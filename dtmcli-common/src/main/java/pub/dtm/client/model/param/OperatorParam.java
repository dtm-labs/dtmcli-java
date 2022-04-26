package pub.dtm.client.model.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import pub.dtm.client.constant.ParamFieldConstants;
import pub.dtm.client.enums.TransTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatorParam {
    @SerializedName(ParamFieldConstants.GID)
    @JsonProperty(ParamFieldConstants.GID)
    private String gid;

    @SerializedName(ParamFieldConstants.TRANS_TYPE)
    @JsonProperty(ParamFieldConstants.TRANS_TYPE)
    private String transType;

    @SerializedName(ParamFieldConstants.BRANCH_ID)
    @JsonProperty(ParamFieldConstants.BRANCH_ID)
    private String branchId;

    @SerializedName(ParamFieldConstants.STATUS)
    @JsonProperty(ParamFieldConstants.STATUS)
    private String status;

    @SerializedName(ParamFieldConstants.DATA)
    @JsonProperty(ParamFieldConstants.DATA)
    private String data;

    @SerializedName(ParamFieldConstants.CONFIRM)
    @JsonProperty(ParamFieldConstants.CONFIRM)
    private String confirm;

    @SerializedName(ParamFieldConstants.CANCEL)
    @JsonProperty(ParamFieldConstants.CANCEL)
    private String cancel;

    public OperatorParam(String gid, TransTypeEnum transType, String branchId,
                         String status, String data, String confirmUrl, String cancelUrl) {
        this.gid = gid;
        this.transType = transType.toString();
        this.branchId = branchId;
        this.status = status;
        this.data = data;
        this.confirm = confirmUrl;
        this.cancel = cancelUrl;
    }

    public OperatorParam(String gid, TransTypeEnum transType) {
        this.gid = gid;
        this.transType = transType.toString();
    }
}

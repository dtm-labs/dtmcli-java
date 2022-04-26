package pub.dtm.client.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtmResponse {
    @SerializedName("dtm_result")
    @JsonProperty("dtm_result")
    private String dtmResult;

    @SerializedName("gid")
    @JsonProperty("gid")
    private String gid;

    public static DtmResponse buildDtmResponse(String result) {
        return  new DtmResponse(result, null);
    }
}

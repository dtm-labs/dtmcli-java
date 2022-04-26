package model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBase {
    @SerializedName("dtm_result")
    @JsonProperty("dtm_result")
    private String dtmResult;

    @SerializedName("gid")
    @JsonProperty("gid")
    private String gid;
}

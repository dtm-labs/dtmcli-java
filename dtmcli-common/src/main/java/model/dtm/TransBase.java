package model.dtm;

import enums.TransTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransBase {
    /**
     * global transaction id
     */
    private String gid;

    /**
     * 事务类型
     */
    private TransTypeEnum transTypeEnum;

    private boolean waitResult;
}

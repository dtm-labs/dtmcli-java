package barrier.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarrierParam {
    /**
     * 事务类型
     */
    private String trans_type[];

    /**
     * 全局事务id
     */
    private String gid[];

    /**
     * 分支id
     */
    private String branch_id[];

    /**
     * 操作
     */
    private String op[];
}

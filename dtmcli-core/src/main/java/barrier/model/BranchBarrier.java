package barrier.model;

import com.google.gson.Gson;
import enums.TransTypeEnum;
import exception.DtmException;
import lombok.Data;
import model.dtm.TransBase;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;

@Data
public class BranchBarrier extends TransBase {
    private String branchId;

    private String op;

    private int barrierId;

    public void addBarrierId() {
        this.barrierId += 1;
    }

    public static BranchBarrier buildBranchBarrier(Map<String, String[]> paramsMap) throws Exception {
        if (paramsMap == null || paramsMap.isEmpty()) {
            throw new DtmException("build BranchBarrier error, paramsMap can not be empty.");
        }
        Gson gson = new Gson();
        BarrierParam barrierParam = gson.fromJson(gson.toJson(paramsMap), BarrierParam.class);
        BranchBarrier branchBarrier = new BranchBarrier();
        if (ArrayUtils.isNotEmpty(barrierParam.getTrans_type())) {
            branchBarrier.setTransTypeEnum(TransTypeEnum.parseString(barrierParam.getTrans_type()[0]));
        }
        if (ArrayUtils.isNotEmpty(barrierParam.getGid())) {
            branchBarrier.setGid(barrierParam.getGid()[0]);
        }
        if (ArrayUtils.isNotEmpty(barrierParam.getBranch_id())) {
            branchBarrier.branchId = barrierParam.getBranch_id()[0];
        }
        if (ArrayUtils.isNotEmpty(barrierParam.getOp())) {
            branchBarrier.op = barrierParam.getOp()[0];
        }
        return branchBarrier;
    }

}

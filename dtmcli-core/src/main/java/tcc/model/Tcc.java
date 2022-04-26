package tcc.model;

import base.BranchIdGenerator;
import constant.Constants;
import enums.TransTypeEnum;
import model.dtm.TransBase;

public class Tcc extends TransBase {
    private final BranchIdGenerator branchIdGenerator;

    public Tcc(String gid) {
        super(gid, TransTypeEnum.TCC, false);
        this.branchIdGenerator = new BranchIdGenerator(Constants.EMPTY_STRING);
    }

    public BranchIdGenerator getBranchIdGenerator() {
        return branchIdGenerator;
    }
}

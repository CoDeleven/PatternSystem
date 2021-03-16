package com.codeleven.patternsystem.vo;

import com.codeleven.patternsystem.common.PatternUpdateOperation;

import java.util.List;

public class ShoesPatternUpdateVO {
    private int shoesPatternId;
    private List<PatternUpdateOperation> patternUpdateOperationList;

    public int getShoesPatternId() {
        return shoesPatternId;
    }

    public void setShoesPatternId(int shoesPatternId) {
        this.shoesPatternId = shoesPatternId;
    }

    public List<PatternUpdateOperation> getPatternUpdateOperationList() {
        return patternUpdateOperationList;
    }

    public void setPatternUpdateOperationList(List<PatternUpdateOperation> patternUpdateOperationList) {
        this.patternUpdateOperationList = patternUpdateOperationList;
    }

}

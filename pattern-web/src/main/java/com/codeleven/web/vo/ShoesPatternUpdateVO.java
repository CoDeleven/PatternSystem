package com.codeleven.web.vo;

import com.codeleven.common.entity.PatternTransformCommand;

import java.util.List;

public class ShoesPatternUpdateVO {
    private int shoesPatternId;
    private List<PatternTransformCommand> patternUpdateOperationList;

    public int getShoesPatternId() {
        return shoesPatternId;
    }

    public void setShoesPatternId(int shoesPatternId) {
        this.shoesPatternId = shoesPatternId;
    }

    public List<PatternTransformCommand> getPatternUpdateOperationList() {
        return patternUpdateOperationList;
    }

    public void setPatternUpdateOperationList(List<PatternTransformCommand> patternUpdateOperationList) {
        this.patternUpdateOperationList = patternUpdateOperationList;
    }
}

package com.codeleven.patternsystem.vo;

import com.codeleven.patternsystem.common.PatternUpdateOperation;

import java.util.List;

public class ShoesPatternUpdateVO {
    private int shoesPatternId;
    private int childPatternNo; // 从1开始，0表示为空
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

    public int getChildPatternNo() {
        return childPatternNo;
    }

    public void setChildPatternNo(int childPatternNo) {
        this.childPatternNo = childPatternNo;
    }
}

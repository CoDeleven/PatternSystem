package com.codeleven.common.entity;

/**
 * 花样更新操作
 */
public class PatternUpdateOperation {
    private String operationCode;
    private Integer num;
    private int childPatternNo; // 从1开始，0表示为空

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public int getChildPatternNo() {
        return childPatternNo;
    }

    public void setChildPatternNo(int childPatternNo) {
        this.childPatternNo = childPatternNo;
    }
}

package com.codeleven.common.entity;


import com.codeleven.common.constants.TransformOperation;

public class PatternTransformCommand {
    private TransformOperation operation;
    private int[] param;
    private long childPatternNo;

    public PatternTransformCommand() {
    }

    public void setOperation(TransformOperation operation) {
        this.operation = operation;
    }

    public void setParam(int[] param) {
        this.param = param;
    }

    public void setChildPatternNo(long childPatternNo) {
        this.childPatternNo = childPatternNo;
    }

    public PatternTransformCommand(TransformOperation operation, long childPatternNo, int... param) {
        this.operation = operation;
        this.param = param;
        this.childPatternNo = childPatternNo;
    }

    public TransformOperation getOperation() {
        return operation;
    }

    public int[] getParam() {
        return param;
    }

    public long getChildPatternNo() {
        return childPatternNo;
    }
}

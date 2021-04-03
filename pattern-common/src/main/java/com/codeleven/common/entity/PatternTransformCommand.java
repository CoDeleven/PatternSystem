package com.codeleven.common.entity;


import com.codeleven.common.constants.TransformOperation;

public class PatternTransformCommand {
    private final TransformOperation operation;
    private final int[] param;
    private final long childPatternNo;

    public PatternTransformCommand(TransformOperation operation, int childPatternNo, int... param) {
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

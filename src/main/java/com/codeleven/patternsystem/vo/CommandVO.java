package com.codeleven.patternsystem.vo;

import com.codeleven.patternsystem.parser.transform.TransformReceiver;

public class CommandVO {
    private final TransformReceiver.Operation operation;
    private final int[] param;
    private final long childPatternNo;

    public CommandVO(TransformReceiver.Operation operation, int childPatternNo, int... param) {
        this.operation = operation;
        this.param = param;
        this.childPatternNo = childPatternNo;
    }

    public TransformReceiver.Operation getOperation() {
        return operation;
    }

    public int[] getParam() {
        return param;
    }

    public long getChildPatternNo() {
        return childPatternNo;
    }
}

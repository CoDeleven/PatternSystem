package com.codeleven.core.transform.command;

import com.codeleven.common.constants.TransformOperation;
import com.codeleven.core.transform.TransformReceiver;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ChangeFirstSewingCommand implements ITransformCommand{
    @JsonIgnore
    private final TransformReceiver receiver;
    // 指令
    private final TransformOperation operation = TransformOperation.CHANGE_FIRST_SEWING;
    // 指令的参数
    private final int pointIndex;

    public ChangeFirstSewingCommand(TransformReceiver receiver, int pointIndex) {
        this.receiver = receiver;
        this.pointIndex = pointIndex;
    }

    @Override
    public void execute() {
        receiver.receive(operation, this.pointIndex);
    }
}

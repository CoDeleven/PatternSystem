package com.codeleven.patternsystem.parser.transform;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ChangeFirstSewingCommand implements ITransformCommand{
    @JsonIgnore
    private final TransformReceiver receiver;
    // 指令
    private final TransformReceiver.Operation operation = TransformReceiver.Operation.CHANGE_FIRST_SEWING;
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

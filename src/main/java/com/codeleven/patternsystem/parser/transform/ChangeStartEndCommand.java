package com.codeleven.patternsystem.parser.transform;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ChangeStartEndCommand implements ITransformCommand{

    @JsonIgnore
    private final TransformReceiver receiver;
    // 指令
    private final TransformReceiver.Operation operation = TransformReceiver.Operation.CHANGE_START_END;

    public ChangeStartEndCommand(TransformReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.receive(operation);
    }
}

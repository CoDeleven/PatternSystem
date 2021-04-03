package com.codeleven.core.transform.command;

import com.codeleven.common.constants.TransformOperation;
import com.codeleven.core.transform.TransformReceiver;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ChangeStartEndCommand implements ITransformCommand{

    @JsonIgnore
    private final TransformReceiver receiver;
    // 指令
    private final TransformOperation operation = TransformOperation.CHANGE_START_END;

    public ChangeStartEndCommand(TransformReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.receive(operation);
    }
}

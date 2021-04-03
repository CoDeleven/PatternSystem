package com.codeleven.core.transform.command;

import com.codeleven.common.constants.TransformOperation;
import com.codeleven.core.transform.TransformReceiver;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 平移指令
 */
public class TranslateXCommand implements ITransformCommand{
    @JsonIgnore
    private final TransformReceiver receiver;
    // 指令
    private final TransformOperation operation = TransformOperation.MOVE_X;
    // 指令的参数
    private final int num;

    public TranslateXCommand(TransformReceiver receiver, int num) {
        this.receiver = receiver;
        this.num = num;
    }

    @Override
    public void execute() {
        receiver.receive(operation, num);
    }
}

package com.codeleven.patternsystem.parser.transform;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 平移指令
 */
public class TranslateYCommand implements ITransformCommand{
    @JsonIgnore
    private final TransformReceiver receiver;
    // 指令
    private final TransformReceiver.Operation operation = TransformReceiver.Operation.MOVE_Y;
    // 指令的参数
    private final int num;

    public TranslateYCommand(TransformReceiver receiver, int num) {
        this.receiver = receiver;
        this.num = num;
    }

    @Override
    public void execute() {
        receiver.receive(operation, num);
    }
}

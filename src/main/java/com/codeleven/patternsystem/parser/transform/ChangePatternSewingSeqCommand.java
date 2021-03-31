package com.codeleven.patternsystem.parser.transform;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 更改子花样顺序的命令
 */
public class ChangePatternSewingSeqCommand implements ITransformCommand{
    @JsonIgnore
    private final TransformReceiver receiver;
    // 指令
    private final TransformReceiver.Operation operation = TransformReceiver.Operation.CHANGE_PATTERN_SEWING_SEQ;
    // 指令的参数
    private final int targetId;
    private final int insertId;

    public ChangePatternSewingSeqCommand(TransformReceiver receiver, int targetId, int insertId) {
        this.receiver = receiver;
        this.targetId = targetId;
        this.insertId = insertId;
    }

    @Override
    public void execute() {
        receiver.receive(operation, this.targetId, this.insertId);
    }
}

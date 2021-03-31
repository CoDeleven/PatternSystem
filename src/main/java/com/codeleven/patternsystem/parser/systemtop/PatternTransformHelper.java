package com.codeleven.patternsystem.parser.systemtop;

import com.codeleven.patternsystem.entity.ChildPattern;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.parser.transform.*;
import com.codeleven.patternsystem.vo.CommandVO;

public class PatternTransformHelper {
    private final UniPattern pattern;

    public PatternTransformHelper(UniPattern pattern) {
        this.pattern = pattern;
    }

    private static ITransformCommand getCommand(TransformReceiver receiver, TransformReceiver.Operation operation, int[] data) {
        switch (operation) {
            case MOVE_X:
                return new TranslateXCommand(receiver, data[0]);
            case MOVE_Y:
                return new TranslateYCommand(receiver, data[0]);
            case CHANGE_PATTERN_SEWING_SEQ:
                return new ChangePatternSewingSeqCommand(receiver, data[0], data[1]);
            case CHANGE_FIRST_SEWING:
                return new ChangeFirstSewingCommand(receiver, data[0]);
            case CHANGE_START_END:
                return new ChangeStartEndCommand(receiver);
            default:
                return null;
        }
    }

    /**
     * 根据CommandVO生成Command
     *
     * @param commandVO 前端部分传入的Command
     * @return
     */
    public ITransformCommand genCommand(CommandVO commandVO) {
        // 获取 commandVO 内的 childPatternNo
        int childPatternNo = commandVO.getChildPatternNo();
        // 如果 childPatternNo 不为 0
        if (childPatternNo != 0) {
            // 获取 childPatternNo 对应的 子花样的 针迹列表
            ChildPattern childPattern = pattern.getChildPatterns().get(childPatternNo - 1);
            // 构建Receiver
            TransformReceiver receiver = TransformReceiver.getInstance(childPattern.getFrameList(), true);
            // 获取Command
            return getCommand(receiver, commandVO.getOperation(), commandVO.getParam());
        } else {
            TransformReceiver receiver = TransformReceiver.getInstance(pattern.getFrames(), false);
            return getCommand(receiver, commandVO.getOperation(), commandVO.getParam());
        }
    }
}

package com.codeleven.core.transform;

import com.codeleven.common.constants.TransformOperation;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.core.transform.command.*;
import com.codeleven.common.entity.PatternTransformCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatternTransformHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PatternTransformHelper.class);

    private final UniPattern pattern;

    public PatternTransformHelper(UniPattern pattern) {
        this.pattern = pattern;
    }

    private static ITransformCommand getCommand(TransformReceiver receiver, TransformOperation operation, int[] data) {
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
     * @param patternTransformCommand 前端部分传入的Command
     * @return
     */
    public ITransformCommand genCommand(PatternTransformCommand patternTransformCommand) {
        // 获取 commandVO 内的 childPatternId
        long childPatternId = patternTransformCommand.getChildPatternNo();
        // 如果 childPatternId 不为 0
        if (childPatternId != 0) {
            // 获取 childPatternId 对应的 子花样的 针迹列表
            UniChildPattern childPattern = pattern.getChildList().get(childPatternId);
            if (childPattern == null) {
                LOGGER.error("ID为：{} 的子花样不存在", childPatternId);
                throw new RuntimeException("ID为" + childPatternId + "的子花样不存在");
            }
            // 构建Receiver
            TransformReceiver receiver = TransformReceiver.getInstance(pattern, childPatternId);
            // 获取Command
            return getCommand(receiver, patternTransformCommand.getOperation(), patternTransformCommand.getParam());
        } else {
            TransformReceiver receiver = TransformReceiver.getInstance(pattern);
            return getCommand(receiver, patternTransformCommand.getOperation(), patternTransformCommand.getParam());
        }
    }
}

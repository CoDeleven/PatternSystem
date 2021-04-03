package com.codeleven.patternsystem.parser.systemtop;

import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.patternsystem.parser.transform.*;
import com.codeleven.patternsystem.vo.CommandVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PatternTransformHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PatternTransformHelper.class);

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
        long childPatternNo = commandVO.getChildPatternNo();
        // 如果 childPatternNo 不为 0
        if (childPatternNo != 0) {
            // 获取 childPatternNo 对应的 子花样的 针迹列表
            UniChildPattern childPattern = pattern.getChildList().get(childPatternNo);
            if(childPattern == null){
                LOGGER.error("ID为：{} 的子花样不存在", childPatternNo);
                throw new RuntimeException("ID为" + childPatternNo + "的子花样不存在");
            }
            // 构建Receiver
            TransformReceiver receiver = TransformReceiver.getInstance(childPattern.getPatternData(), true);
            // 获取Command
            return getCommand(receiver, commandVO.getOperation(), commandVO.getParam());
        } else {
            Map<Long, UniChildPattern> childList = pattern.getChildList();
            List<UniFrame> totalFrames = childList.values().stream()
                    .flatMap((Function<UniChildPattern, Stream<UniFrame>>) uniChildPattern -> uniChildPattern.getPatternData().stream()).collect(Collectors.toList());

            TransformReceiver receiver = TransformReceiver.getInstance(totalFrames, false);
            return getCommand(receiver, commandVO.getOperation(), commandVO.getParam());
        }
    }
}

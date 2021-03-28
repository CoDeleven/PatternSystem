package com.codeleven.patternsystem.parser.systemtop;

import com.codeleven.patternsystem.entity.ChildPattern;
import com.codeleven.patternsystem.entity.UniFrame;
import com.codeleven.patternsystem.parser.IParserStrategy;
import com.codeleven.patternsystem.parser.systemtop.FrameHelper;
import com.codeleven.patternsystem.parser.systemtop.SystemTopFileStruct;
import com.codeleven.patternsystem.utils.PatternPointUtil;

import java.util.ArrayList;
import java.util.List;

import static com.codeleven.patternsystem.parser.systemtop.SystemTopControlCode.SKIP;

/**
 * 上亿花样 解析策略
 */
public class SystemTopParserStrategy implements IParserStrategy {
    @Override
    public byte[] getFrameEndCode() {
        return SystemTopFileStruct.FRAME_END_CODE;
    }

    @Override
    public int getFrameEndCodeLen() {
        return this.getFrameEndCode().length;
    }

    @Override
    public byte[] getFileStartCode() {
        return SystemTopFileStruct.FILE_START_CODE;
    }

    @Override
    public int getFileStartCodeLen() {
        return this.getFileStartCode().length;
    }

    @Override
    public List<UniFrame> readFrames(byte[] totalBytes, int readOffset) {
        byte[] readBytes = new byte[4];
        // 用于构造List<UniFrame>
        FrameHelper helper = new FrameHelper();
        int frameOffset = 0;
        do {
            int beginOffset = SystemTopFileStruct.FIRST_FRAME_OFFSET.offset + frameOffset;
            readBytes[0] = totalBytes[beginOffset];
            readBytes[1] = totalBytes[beginOffset + 1];
            readBytes[2] = totalBytes[beginOffset + 2];
            readBytes[3] = totalBytes[beginOffset + 3];
            // 中间有个 00 不知道什么作用
            helper.addFrame(readBytes[0], PatternPointUtil.computeByteToInt(readBytes[2]), PatternPointUtil.computeByteToInt(readBytes[3]));
            frameOffset += SystemTopFileStruct.FIRST_FRAME_OFFSET.size;
        } while (!isEndOfFile(readBytes));
        return helper.build();
    }

    @Override
    public List<ChildPattern> splitPattern(List<UniFrame> frames) {
        List<ChildPattern> patterns = new ArrayList<>();
        List<UniFrame> childPatternFrameList = new ArrayList<>();
        int beginThisPatternIndex = 0;
        ChildPattern prevChildPattern = null;
        for (int i = 0; i < frames.size(); i++) {
            UniFrame item = frames.get(i);
            if(i == 0 && item.getControlCode() == SKIP.getCode()){
                childPatternFrameList.add(item);
                continue;
            }
            // 遇到空送，这个子花样结束
            if(item.getControlCode() == SKIP.getCode()){
                ChildPattern childPattern = new ChildPattern();
                // 设置帧数
                childPattern.setFrameCount(childPatternFrameList.size());
                // 设置帧列表
                childPattern.setFrameList(childPatternFrameList);

                if(prevChildPattern != null){
                    // 设置当前的子花样.prev 为前一个子花样
                    childPattern.setPrevChildPattern(prevChildPattern);
                    // 上一个子花样的next指向 当前的子花样
                    prevChildPattern.setNextChildPattern(childPattern);
                }
                // 设置当前子花样的编号
                childPattern.setPatternNo(patterns.size() + 1);
                // 设置当前子花样的开始针下标
                childPattern.setBeginFrameIndex(beginThisPatternIndex);

                // 保存记录——上一个子花样
                prevChildPattern = childPattern;
                // 添加这个子花样到花样集合中
                patterns.add(childPattern);
                // 记录新的子花样的起始下标
                beginThisPatternIndex = i;
                // 创新构建一个子花样链表
                childPatternFrameList = new ArrayList<>();
            }
            childPatternFrameList.add(item);
        }

        return patterns;
    }

    @Override
    public int getAvailableBytesWithBytes(byte[] totalBytes) {
        byte[] readBytes = new byte[2];
        readBytes[0] = totalBytes[SystemTopFileStruct.FRAME_BYTES_OFFSET.offset];
        readBytes[1] = totalBytes[SystemTopFileStruct.FRAME_BYTES_OFFSET.offset + 1];
        return PatternPointUtil.getUnsignedIntFrom2Byte(readBytes);
    }

    @Override
    public int getFrameStartOffset(byte[] totalBytes) {
        return SystemTopFileStruct.FIRST_FRAME_OFFSET.offset;
    }

}

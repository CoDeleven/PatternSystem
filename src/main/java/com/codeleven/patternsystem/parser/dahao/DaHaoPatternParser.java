package com.codeleven.patternsystem.parser.dahao;

import com.codeleven.patternsystem.entity.ChildPattern;
import com.codeleven.patternsystem.entity.UniFrame;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.parser.IPatternParser;
import com.codeleven.patternsystem.parser.systemtop.FrameHelper;
import com.codeleven.patternsystem.parser.systemtop.SystemTopControlCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeleven.patternsystem.parser.systemtop.SystemTopControlCode.SKIP;
import static com.codeleven.patternsystem.parser.systemtop.SystemTopFileStruct.FRAME_END_CODE;
import static com.codeleven.patternsystem.parser.systemtop.SystemTopFileStruct.FIRST_FRAME_OFFSET;

public class DaHaoPatternParser implements IPatternParser {
    private final byte[] patternData;

    public DaHaoPatternParser(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[0x1000];
        int len = -1;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        this.patternData = byteArrayOutputStream.toByteArray();
    }

    public UniPattern readAll(){
        UniPattern pattern = new UniPattern();

        int machineOffset = this.getMachineFrameOffset();
        List<UniFrame> frames = this.readFrames(machineOffset);

        int[] dimension = this.getDimension(frames);
        List<ChildPattern> patterns = this.splitPatternForChild(frames);

        pattern.setFrames(frames);
        pattern.setMinX(dimension[0]);
        pattern.setMaxY(dimension[1]);
        pattern.setMaxX(dimension[2]);
        pattern.setMinY(dimension[3]);

        pattern.setFrameNumber(frames.size());
        pattern.setChildPatterns(patterns);

        return pattern;
    }

    public List<ChildPattern> splitPatternForChild(List<UniFrame> frames){
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
            if(item.getControlCode() == SKIP.getCode() || SystemTopControlCode.isEndControlCode(item.getControlCode())){
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

    /**
     * 左 上 右 下
     * @return
     */
    private int[] getDimension(List<UniFrame> frames){
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (UniFrame frame : frames) {
            if(frame.getY() > maxY) {
                maxY = frame.getY();
            } else if(frame.getY() < minY){
                minY = frame.getY();
            }

            if(frame.getX() > maxX) {
                maxX = frame.getX();
            } else if(frame.getX() < minX){
                minX = frame.getX();
            }
        }

        return new int[] {minX, maxY, maxX, minY};
    }

    private List<UniFrame> readFrames(int offset) {
        byte[] readBytes = new byte[4];
        // 用于构造List<UniFrame>
        FrameHelper helper = new FrameHelper();
        do {
            readBytes[0] = this.patternData[offset];
            readBytes[1] = this.patternData[offset + 1];
            readBytes[2] = this.patternData[offset + 2];
            readBytes[3] = this.patternData[offset + 3];
            // 中间有个 00 不知道什么作用
            helper.addFrame(readBytes[0], computeNumber(readBytes[2]), computeNumber(readBytes[3]));
            offset += FIRST_FRAME_OFFSET.size;
        } while (!isEndOfFile(readBytes));
        return helper.build();
    }

    private int computeNumber(byte data) {
        if ((data & 0x80) == 0x80) {
            return -1 * (data & 0x7F);
        }
        return data;
    }


    private int getMachineFrameOffset(){
        return getUnsignedShortByBytes(patternData[0x10], patternData[0x11]);
    }

    @Override
    public boolean isTargetPattern() throws IOException {
        return true;
    }

    @Override
    public boolean isEndOfFile(byte[] byteArray) {
        return Arrays.equals(FRAME_END_CODE, byteArray);
    }

    private int getUnsignedShortByBytes(byte byte1, byte byte2) {
        return ((byte2 & 0xFF) << 8) | (byte1 & 0xFF);
    }
}

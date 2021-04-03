package com.codeleven.parser.shangyi;

import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.parser.utils.ChildFrameHelper;
import com.codeleven.parser.utils.FrameHelper;
import com.codeleven.parser.IParserStrategy;
import com.codeleven.parser.utils.PatternPointUtil;

import java.util.ArrayList;
import java.util.List;

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
    public List<UniChildPattern> splitPattern(List<UniFrame> frames) {
        ChildFrameHelper childFrameHelper = new ChildFrameHelper();
        for (int i = 0; i < frames.size(); i++) {
            UniFrame item = frames.get(i);
            childFrameHelper.addFrame(item);
        }
        return childFrameHelper.build();
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

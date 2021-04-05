package com.codeleven.core.parser;

import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.core.utils.ChildFrameHelper;
import com.codeleven.core.utils.FrameHelper;
import com.codeleven.core.utils.PatternPointUtil;

import java.util.List;

/**
 * 因为上亿 和 大豪的花样文件  关于机器码这一块 基本就是一样的，所以定义一个公共父类
 * 实现代码复用
 */
public abstract class BaseParserStrategy implements IParserStrategy, IPatternFileParserStrategy{

    @Override
    public List<UniFrame> readFrames(byte[] totalBytes) {
        // 第一帧的偏移
        final int firstFrameOffset = this.getFrameStartOffset(totalBytes);
        // 用于存放一个针的数组
        byte[] readBytes = new byte[4];
        // 用于构造List<UniFrame>
        FrameHelper helper = new FrameHelper();
        int frameOffset = 0;
        do {
            int beginOffset = firstFrameOffset + frameOffset;
            readBytes[0] = totalBytes[beginOffset];
            readBytes[1] = totalBytes[beginOffset + 1];
            readBytes[2] = totalBytes[beginOffset + 2];
            readBytes[3] = totalBytes[beginOffset + 3];
            // 中间有个 00 不知道什么作用
            helper.addFrame(readBytes[0], PatternPointUtil.computeByteToInt(readBytes[2]), PatternPointUtil.computeByteToInt(readBytes[3]));
            frameOffset += this.getOneFrameSize();
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
}

package com.codeleven.patternsystem.systemtop;

import cn.hutool.core.io.IoUtil;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.parser.IParserStrategy;
import com.codeleven.parser.IPatternFileParserStrategy;
import com.codeleven.parser.shangyi.SystemTopParserStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

/**
 * 上亿花样 结构读取测试
 */
public class SystemTopFileStructReadTest {
    /**
     * 测试帧数据的读取
     */
    @Test
    public void testFrameRead() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        byte[] totalBytes = IoUtil.readBytes(is);
        SystemTopParserStrategy strategy = new SystemTopParserStrategy();
        // 起始帧的位置
        int frameStartOffset = strategy.getFrameStartOffset(totalBytes);
        // 获取所有的Frame
        List<UniFrame> uniFrames = strategy.readFrames(totalBytes);

        assert uniFrames != null;
        assert uniFrames.size() > 0;
        // 判断最后一帧是 0x1F
        assert uniFrames.get(uniFrames.size() - 1).getControlCode() == 0x1F;
    }

    @Test
    public void testAvailableBytes() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        byte[] totalBytes = IoUtil.readBytes(is);
        SystemTopParserStrategy strategy = new SystemTopParserStrategy();
        int bytesWithFrameUsed = strategy.getAvailableBytesWithBytes(totalBytes);

        Assertions.assertEquals(0x059C, bytesWithFrameUsed);
    }

    @Test
    public void testChildPattern() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        byte[] totalBytes = IoUtil.readBytes(is);
        SystemTopParserStrategy strategy = new SystemTopParserStrategy();
        // 起始帧的位置
        int frameStartOffset = strategy.getFrameStartOffset(totalBytes);
        // 获取所有的Frame
        List<UniFrame> uniFrames = strategy.readFrames(totalBytes);

        List<UniChildPattern> childPatterns = strategy.splitPattern(uniFrames);
        int childPatternSize = childPatterns.stream().mapToInt((i) -> i.getPatternData().size()).sum();
        // 拆分子花样的时候，最后的跳针 和 结尾标识符被忽略
        Assertions.assertEquals(uniFrames.size(), childPatternSize + 2);
    }
}

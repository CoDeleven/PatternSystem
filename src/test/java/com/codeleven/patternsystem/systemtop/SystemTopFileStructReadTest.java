package com.codeleven.patternsystem.systemtop;

import cn.hutool.core.io.IoUtil;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.patternsystem.entity.ChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.parser.IParserStrategy;
import com.codeleven.parser.shangyi.SystemTopParserStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

/**
 * 上亿花样 结构读取测试
 */
public class SystemTopFileStructReadTest {
//    @Test
//    public void readMinXTest() {
//        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
//        UniParser parser = new UniParser();
//        UniPattern uniPattern = parser.doParse(is);
//
//        int dimension = uniPattern.getMinX();
//        short data = (short) 0xFB31;
//        assert dimension == data;
//    }

//    @Test
//    public void readMaxXTest() {
//        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
//        UniParser parser = new UniParser();
//        UniPattern uniPattern = parser.doParse(is);
//
//        int dimension = uniPattern.getMaxX();
//        short data = (short) 0x04D4;
//        assert dimension == data;
//    }

//    @Test
//    public void readMaxYTest() throws IOException {
//        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
//        UniParser parser = new UniParser();
//        UniPattern uniPattern = parser.doParse(is);
//
//        int dimension = uniPattern.getMaxY();
//        short data = (short) 0x039E;
//        assert dimension == data;
//    }

//    @Test
//    public void readMinYTest() throws IOException {
//        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
//        UniParser parser = new UniParser();
//        UniPattern uniPattern = parser.doParse(is);
//        int dimension = uniPattern.getMinY();
//
//        short data = (short) 0x0;
//        assert dimension == data;
//    }

//    @Test
//    public void readPatternTotalFile() throws IOException {
//        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
//        UniParser parser = new UniParser();
//        UniPattern uniPattern = parser.doParse(is);
//
//        assert uniPattern.getFrames() != null;
//        assert uniPattern.getFrames().size() > 0;
//    }

    /**
     * 测试帧数据的读取
     */
    @Test
    public void testFrameRead() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        byte[] totalBytes = IoUtil.readBytes(is);
        IParserStrategy strategy = new SystemTopParserStrategy();
        // 起始帧的位置
        int frameStartOffset = strategy.getFrameStartOffset(totalBytes);
        // 获取所有的Frame
        List<UniFrame> uniFrames = strategy.readFrames(totalBytes, frameStartOffset);

        assert uniFrames != null;
        assert uniFrames.size() > 0;
        // 判断最后一帧是 0x1F
        assert uniFrames.get(uniFrames.size() - 1).getControlCode() == 0x1F;
    }

    @Test
    public void testAvailableBytes() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        byte[] totalBytes = IoUtil.readBytes(is);
        IParserStrategy strategy = new SystemTopParserStrategy();
        int bytesWithFrameUsed = strategy.getAvailableBytesWithBytes(totalBytes);

        Assertions.assertEquals(0x059C, bytesWithFrameUsed);
    }

    @Test
    public void testChildPattern() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        byte[] totalBytes = IoUtil.readBytes(is);
        IParserStrategy strategy = new SystemTopParserStrategy();
        // 起始帧的位置
        int frameStartOffset = strategy.getFrameStartOffset(totalBytes);
        // 获取所有的Frame
        List<UniFrame> uniFrames = strategy.readFrames(totalBytes, frameStartOffset);

        List<UniChildPattern> childPatterns = strategy.splitPattern(uniFrames);
        int childPatternSize = childPatterns.stream().mapToInt((i) -> i.getPatternData().size()).sum();
        // 拆分子花样的时候，最后的跳针 和 结尾标识符被忽略
        Assertions.assertEquals(uniFrames.size(), childPatternSize + 2);
    }
}

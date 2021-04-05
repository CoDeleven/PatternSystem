package com.codeleven.core.parser.shangyi;

import cn.hutool.core.io.IoUtil;
import com.codeleven.common.constants.SystemTopFileStruct;
import com.codeleven.core.parser.shangyi.SystemTopParserStrategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;

public class SystemTopParserStrategyTest {
    private byte[] totalBytes;
    private InputStream is;
    private SystemTopParserStrategy strategy;

    @Before
    public void setUp() throws Exception {
        is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        assert is != null;
        totalBytes = IoUtil.readBytes(is);
        strategy = new SystemTopParserStrategy();
    }

    @Test
    public void getFrameEndCode() {
        Assert.assertTrue(Arrays.equals(strategy.getFrameEndCode(), SystemTopFileStruct.FRAME_END_CODE));
    }

    @Test
    public void getFrameEndCodeLen() {
        Assert.assertEquals(strategy.getFrameEndCodeLen(), SystemTopFileStruct.FRAME_END_CODE.length);
    }

    @Test
    public void getFileStartCode() {
        Assert.assertTrue(Arrays.equals(strategy.getFileStartCode(), SystemTopFileStruct.FILE_START_CODE));
    }

    @Test
    public void getFileStartCodeLen() {
        Assert.assertEquals(strategy.getFileStartCodeLen(), SystemTopFileStruct.FILE_START_CODE.length);
    }

    @Test
    public void getOneFrameSize() {
        Assert.assertEquals(SystemTopFileStruct.FIRST_FRAME_OFFSET.size, strategy.getOneFrameSize());
    }

    @Test
    public void getAvailableBytesWithBytes() {
        int bytesWithFrameUsed = strategy.getAvailableBytesWithBytes(totalBytes);
        Assert.assertEquals(0x059C, bytesWithFrameUsed);
    }

    @Test
    public void getFrameStartOffset() {
        Assert.assertEquals(SystemTopFileStruct.FIRST_FRAME_OFFSET.offset, strategy.getFrameStartOffset(totalBytes));
    }

    @Test
    public void isSupport() {
        Assert.assertTrue(strategy.isSupport(totalBytes));
    }
}
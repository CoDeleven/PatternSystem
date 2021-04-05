package com.codeleven.core.parser;

import cn.hutool.core.io.IoUtil;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.core.parser.shangyi.SystemTopParserStrategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class BaseParserStrategyTest {
    private byte[] totalBytes;
    private InputStream is;
    private SystemTopParserStrategy strategy;

    @Before
    public void setUp() throws Exception {
        is = this.getClass().getClassLoader().getResourceAsStream("systemtop/data1.NPT");
        assert is != null;
        totalBytes = IoUtil.readBytes(is);
        strategy = new SystemTopParserStrategy();
    }

    @Test
    public void readFrames() {
        List<UniFrame> uniFrames = strategy.readFrames(totalBytes);
        Assert.assertEquals(167, uniFrames.size());
    }

    @Test
    public void splitPattern() {
        // -2 是因为最后的SKIP、0x1F不算入； -1 是因为存在次元点，次元点可能夹在SKIP中间，我们去最大的SKIP
        int removeFinal2Frames = 167 - 2 - 1;
        List<UniFrame> uniFrames = strategy.readFrames(totalBytes);
        List<UniChildPattern> uniChildPatterns = strategy.splitPattern(uniFrames);
        int sum = 0;
        for (int i = 0; i < uniChildPatterns.size(); i++) {
            sum += uniChildPatterns.get(i).getPatternData().size();
        }
        Assert.assertEquals(removeFinal2Frames, sum);
    }
}

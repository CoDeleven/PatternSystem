package com.codeleven.core.output;

import cn.hutool.core.io.FileUtil;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.core.parser.UniParser;
import com.codeleven.core.utils.PatternUtil;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class NPTOutputHelperTest {
    @Test
    public void testOutput() {
        byte[] bytes = FileUtil.readBytes(new File("C:\\Users\\Administrator\\Desktop\\999.NPT"));
        UniParser parser = new UniParser();
        UniPattern uniPattern = parser.doParse(bytes);
        List<UniFrame> uniFrames = PatternUtil.mergeChildPattern(uniPattern);
        for (UniFrame uniFrame : uniFrames) {
            System.out.println(uniFrame);
        }
    }
}

package com.codeleven.core.output;

import cn.hutool.core.io.FileUtil;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.core.parser.UniParser;
import com.codeleven.core.utils.PatternUtil;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageOutputHelperTest {
    @Test
    public void testOutput() throws IOException {
        File inputFile = new File("C:\\Users\\Administrator\\Desktop\\花样\\2021.4.7\\img105.dxf");
        String filename = FileUtil.getPrefix(inputFile);
        File outputFile = new File("C:\\Users\\Administrator\\Desktop\\花样\\2021.4.7\\" + filename + "-" + System.currentTimeMillis() + ".jpg");
        byte[] bytes = FileUtil.readBytes(inputFile);

        UniParser parser = new UniParser();
        UniPattern uniPattern = parser.doParse(bytes);

        ByteArrayOutputStream imageOutput = PrettyFramesOutputStrategy.getImageOutput(PatternUtil.mergeChildPattern(uniPattern));
        FileUtil.writeBytes(imageOutput.toByteArray(), outputFile);
    }
}

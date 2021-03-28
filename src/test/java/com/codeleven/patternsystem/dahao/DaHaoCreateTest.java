package com.codeleven.patternsystem.dahao;

import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.output.NSPOutputHelper;
import com.codeleven.patternsystem.output.PrettyFramesOutputStrategy;
import com.codeleven.patternsystem.parser.dahao.DaHaoPatternParser;
import com.codeleven.patternsystem.parser.UniParser;
import org.junit.jupiter.api.Test;

import java.io.*;

public class DaHaoCreateTest {
    @Test
    public void testDaHaoCreate() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/test.NPT");
        UniParser parser = new UniParser();
        UniPattern uniPattern = parser.doParse(is);

        ByteArrayOutputStream output = NSPOutputHelper.output(uniPattern);
        FileOutputStream fos = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\DESIGN1.NSP");
        fos.write(output.toByteArray());
    }

    @Test
    public void testDaHaoImgCreate() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("dahao/test.NSP");
        DaHaoPatternParser parser = new DaHaoPatternParser(is);
        UniPattern uniPattern = parser.readAll();

        ByteArrayOutputStream imageOutput = PrettyFramesOutputStrategy.getImageOutput(uniPattern.getFrames());
        FileOutputStream fos = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\test.png");
        fos.write(imageOutput.toByteArray());
    }
}

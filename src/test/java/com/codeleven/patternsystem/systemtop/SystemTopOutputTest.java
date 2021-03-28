package com.codeleven.patternsystem.systemtop;

import com.codeleven.patternsystem.entity.UniFrame;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.output.NPTOutputHelper;
import com.codeleven.patternsystem.output.PrettyFramesOutputStrategy;
import com.codeleven.patternsystem.parser.UniParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

public class SystemTopOutputTest {
    @Test
    public void testFramePrettyOutput() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        UniParser parser = new UniParser();
        UniPattern uniPattern = parser.doParse(is);
        String textOutput = PrettyFramesOutputStrategy.getTextOutput(uniPattern.getFrames(), true);
        System.out.println(textOutput);
    }

    @Test
    public void testFramePrettyOutputImage() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        UniParser parser = new UniParser();
        UniPattern pattern = parser.doParse(is);
        List<UniFrame> uniFrames = pattern.getFrames();
        ByteArrayOutputStream imageOutput = PrettyFramesOutputStrategy.getImageOutput(uniFrames);
        FileOutputStream fos = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\test.PNG");
        fos.write(imageOutput.toByteArray());
    }

    @Test
    public void testPatternOutputNPT() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        UniParser parser = new UniParser();
        UniPattern uniPattern = parser.doParse(is);

        ByteArrayOutputStream output = NPTOutputHelper.output(uniPattern);
        FileOutputStream fos = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\test2.NPT");
        fos.write(output.toByteArray());

        FileInputStream fis = new FileInputStream("C:\\Users\\Administrator\\Desktop\\test2.NPT");
        UniParser parser2 = new UniParser();
        UniPattern uniPattern2 = parser2.doParse(fis);

        assert uniPattern2.getFrames().size() == uniPattern.getFrames().size();

        for (int i = 0; i < uniPattern.getFrames().size(); i++) {
            assert uniPattern.getFrames().get(i).getY() == uniPattern2.getFrames().get(i).getY();
            Assertions.assertEquals(uniPattern.getFrames().get(i).getX(), uniPattern2.getFrames().get(i).getX());
            assert uniPattern.getFrames().get(i).getControlCode() == uniPattern2.getFrames().get(i).getControlCode();
        }
    }
//    @Test
//    public void testChildPattern() throws IOException {
//        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/test.NPT");
//        UniParser parser = new UniParser();
//        UniPattern pattern = parser.doParse(is);
//        Assertions.assertEquals(9, pattern.getChildPatterns().size());
//    }
}

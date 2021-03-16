package com.codeleven.patternsystem.systemtop;


import com.codeleven.patternsystem.common.PatternUpdateOperation;
import com.codeleven.patternsystem.entity.UniFrame;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.output.NPTOutputHelper;
import com.codeleven.patternsystem.output.PrettyFramesOutputStrategy;
import com.codeleven.patternsystem.parser.systemtop.PatternTransformHelper;
import com.codeleven.patternsystem.parser.systemtop.SystemTopPatternParser;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SystemTomFrameReadTest {
    @Test
    public void testFrameRead() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser(is);
        List<UniFrame> uniFrames = parser.readFrames();
        for (UniFrame uniFrame : uniFrames) {
            System.out.println(uniFrame);
        }
    }

    @Test
    public void testFramePrettyOutput() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/test.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser(is);
        List<UniFrame> uniFrames = parser.readFrames();
        String textOutput = PrettyFramesOutputStrategy.getTextOutput(uniFrames, true);
        System.out.println(textOutput);
    }

    @Test
    public void testFramePrettyOutputImage() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/test.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser(is);
        List<UniFrame> uniFrames = parser.readFrames();
        PrettyFramesOutputStrategy.getImageOutput(uniFrames);
    }

    @Test
    public void testPatternOutputNPT() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser(is);
        UniPattern uniPattern = parser.readAll();
        NPTOutputHelper.output(uniPattern);

        FileInputStream fis = new FileInputStream("C:\\Users\\Administrator\\Desktop\\test2.NPT");
        SystemTopPatternParser parser2 = new SystemTopPatternParser(fis);
        UniPattern uniPattern2 = parser2.readAll();

        assert uniPattern2.getFrames().size() == uniPattern.getFrames().size();

        for (int i = 0; i < uniPattern.getFrames().size(); i++) {
            assert uniPattern.getFrames().get(i).getY() == uniPattern2.getFrames().get(i).getY();
            assert uniPattern.getFrames().get(i).getX() == uniPattern2.getFrames().get(i).getX();
            assert uniPattern.getFrames().get(i).getControlCode() == uniPattern2.getFrames().get(i).getControlCode();
        }
    }

    @Test
    public void testTransform() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser(is);
        UniPattern uniPattern = parser.readAll();
        UniPattern uniPattern2 = parser.readAll();
        List<PatternUpdateOperation> operations = new ArrayList<>();
        PatternUpdateOperation operation = new PatternUpdateOperation();
        operation.setOperationCode("move-x");
        operation.setNum(10);
        operations.add(operation);
        PatternTransformHelper helper = new PatternTransformHelper(uniPattern, operations);
        helper.doTransform();

        assert uniPattern2.getFrames().size() == uniPattern.getFrames().size();

        for (int i = 0; i < uniPattern.getFrames().size(); i++) {
            assert uniPattern.getFrames().get(i).getY() == uniPattern2.getFrames().get(i).getY();
            assert uniPattern.getFrames().get(i).getX() == (uniPattern2.getFrames().get(i).getX() + 10);
            assert uniPattern.getFrames().get(i).getControlCode() == uniPattern2.getFrames().get(i).getControlCode();
        }
    }

    @Test
    public void testTransformAndOutput() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser(is);
        UniPattern uniPattern = parser.readAll();
        List<PatternUpdateOperation> operations = new ArrayList<>();
        PatternUpdateOperation operation = new PatternUpdateOperation();
        operation.setOperationCode("move-x");
        operation.setNum(10);
        operations.add(operation);
        PatternTransformHelper helper = new PatternTransformHelper(uniPattern, operations);
        helper.doTransform();
        NPTOutputHelper.output(uniPattern);
    }
}

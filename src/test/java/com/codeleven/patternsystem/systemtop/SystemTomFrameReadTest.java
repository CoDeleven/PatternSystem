package com.codeleven.patternsystem.systemtop;


import com.codeleven.patternsystem.common.PatternUpdateOperation;
import com.codeleven.patternsystem.entity.ChildPattern;
import com.codeleven.patternsystem.entity.UniFrame;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.output.NPTOutputHelper;
import com.codeleven.patternsystem.output.PrettyFramesOutputStrategy;
import com.codeleven.patternsystem.parser.systemtop.PatternTransformHelper;
import com.codeleven.patternsystem.parser.systemtop.SystemTopPatternParser;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
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
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
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
        ByteArrayOutputStream output = NPTOutputHelper.output(uniPattern);
        FileOutputStream fos = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\test2.NPT");
        fos.write(output.toByteArray());

        FileInputStream fis = new FileInputStream("C:\\Users\\Administrator\\Desktop\\test2.NPT");
        SystemTopPatternParser parser2 = new SystemTopPatternParser(fis);
        UniPattern uniPattern2 = parser2.readAll();

        assert uniPattern2.getFrames().size() == uniPattern.getFrames().size();

        for (int i = 0; i < uniPattern.getFrames().size(); i++) {
            assert uniPattern.getFrames().get(i).getY() == uniPattern2.getFrames().get(i).getY();
            Assertions.assertEquals(uniPattern.getFrames().get(i).getX(), uniPattern2.getFrames().get(i).getX());
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
    public void testChildPatternTransform() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/test.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser(is);
        UniPattern uniPattern = parser.readAll();
        UniPattern uniPattern2 = parser.readAll();
        List<PatternUpdateOperation> operations = new ArrayList<>();
        PatternUpdateOperation operation = new PatternUpdateOperation();
        operation.setOperationCode("move-y");
        operation.setNum(-100);
        operations.add(operation);
        PatternTransformHelper helper = new PatternTransformHelper(uniPattern, operations);
        helper.setTargetPatternNo(2);
        helper.doTransform();

        ChildPattern childPattern = uniPattern.getChildPatterns().get(1);
        int beginIndex = childPattern.getBeginFrameIndex();
        int count = childPattern.getFrameCount();

        assert uniPattern2.getFrames().size() == uniPattern.getFrames().size();
        for (int i = 0; i < uniPattern.getFrames().size(); i++) {
            if(i < beginIndex || i >= (beginIndex + count)){
                assert uniPattern.getFrames().get(i).getY() == (uniPattern2.getFrames().get(i).getY());
            } else {
                assert uniPattern.getFrames().get(i).getY() == (uniPattern2.getFrames().get(i).getY() - 100);
            }
            assert uniPattern.getFrames().get(i).getX() == uniPattern2.getFrames().get(i).getX();
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

    @Test
    public void testChildPattern() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/test.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser(is);
        UniPattern uniPattern = parser.readAll();
        Assertions.assertEquals(9, uniPattern.getChildPatterns().size());
    }

    @Test
    public void testChildPatternJavaCode4AndroidTest() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/test.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser(is);
        UniPattern uniPattern = parser.readAll();
        List<ChildPattern> childPatterns = uniPattern.getChildPatterns();

        for (UniFrame uniFrame : childPatterns.get(5).getFrameList()) {
            System.out.printf("result.add(new UniFrame(%d, %d, %d));\n", uniFrame.getX(), uniFrame.getY(), uniFrame.getControlCode());
        }

    }


    public void goTransform() throws IOException {
        SystemTopPatternParser parser = new SystemTopPatternParser(new FileInputStream("C:\\Users\\Administrator\\Desktop\\043-1.NPT"));
        UniPattern uniPattern = parser.readAll();

        List<PatternUpdateOperation> operations = new ArrayList<>();
        PatternUpdateOperation operation = new PatternUpdateOperation();
        operation.setOperationCode("move-y");
        operation.setNum(310);
        operations.add(operation);
        PatternTransformHelper helper = new PatternTransformHelper(uniPattern, operations);
        helper.doTransform();
        ByteArrayOutputStream output = NPTOutputHelper.output(uniPattern);
        FileOutputStream fos = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\043-1.NPT");
        fos.write(output.toByteArray());
    }


    public void testExchange() throws IOException {
        SystemTopPatternParser parser = new SystemTopPatternParser(new FileInputStream("C:\\Users\\Administrator\\Desktop\\002.NPT"));
        UniPattern uniPattern = parser.readAll();

        List<PatternUpdateOperation> operations = new ArrayList<>();
        PatternUpdateOperation operation = new PatternUpdateOperation();
        operation.setOperationCode("exchange-s-e");
        operations.add(operation);
        PatternTransformHelper helper = new PatternTransformHelper(uniPattern, operations);
        helper.doTransform();
        ByteArrayOutputStream output = NPTOutputHelper.output(uniPattern);
        FileOutputStream fos = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\002-2.NPT");
        fos.write(output.toByteArray());
    }
}

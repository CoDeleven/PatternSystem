package com.codeleven.patternsystem.systemtop;


import com.codeleven.patternsystem.common.PatternUpdateOperation;
import com.codeleven.patternsystem.entity.ChildPattern;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.output.NPTOutputHelper;
import com.codeleven.patternsystem.parser.systemtop.PatternTransformHelper;
import com.codeleven.patternsystem.parser.UniParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SystemTomFrameReadTest {

    @Test
    public void testTransform() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        UniParser parser = new UniParser();
        UniPattern uniPattern = parser.doParse(is);
        UniPattern uniPattern2 = parser.doParse(is);
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
        UniParser parser = new UniParser();
        UniPattern uniPattern = parser.doParse(is);
        UniPattern uniPattern2 = parser.doParse(is);
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
            if (i < beginIndex || i >= (beginIndex + count)) {
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
        UniParser parser = new UniParser();
        UniPattern uniPattern = parser.doParse(is);
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

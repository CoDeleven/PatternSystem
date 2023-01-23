package com.codeleven.core;

import cn.hutool.core.io.FileUtil;
import com.codeleven.common.constants.TransformOperation;
import com.codeleven.common.entity.PatternTransformCommand;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.core.output.*;
import com.codeleven.core.parser.UniParser;
import com.codeleven.core.transform.PatternTransformHelper;
import com.codeleven.core.transform.command.ITransformCommand;
import com.codeleven.core.utils.PatternUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.Lock;

public class GoPattern {
    private String globalFilename = "41";
    private int beginIndex = 131;
    private long childPatternNo = 0;
    @Test
    public void testOutputNPT(){
        File inputFile = new File("C:\\Users\\Administrator\\Desktop\\" + globalFilename +".dxf");
        String filename = FileUtil.getPrefix(inputFile);
        File outputFile = new File("C:\\Users\\Administrator\\Desktop\\" + filename + ".NPT");
//        File outputFile2 = new File("C:\\Users\\Administrator\\Desktop\\" + filename + "-skip.NPT");
        byte[] bytes = FileUtil.readBytes(inputFile);

        UniParser parser = new UniParser();
        UniPattern uniPattern = parser.doParse(bytes);

        // 需要对Pattern进行排序
//        PatternUtil.changeWeightByLRTB(uniPattern);
        PatternTransformHelper patternTransformHelper = new PatternTransformHelper(uniPattern);
        childPatternNo = PatternUtil.getChildPatternBySortedIndex(uniPattern, 0).getId();
        ITransformCommand transformCommand = patternTransformHelper.genCommand(new PatternTransformCommand(TransformOperation.CHANGE_FIRST_SEWING, childPatternNo, beginIndex));
        transformCommand.execute();

        UniOutput uniOutput = new UniOutput();
        byte[] outputBytes = uniOutput.output(uniPattern, new NPTOutputStrategy());

        UniPattern pattern = PatternUtil.generateSkipFrameWithSingleSewing(uniPattern);
//        byte[] outputBytes2 = uniOutput.output(pattern, new NPTOutputStrategy());
        FileUtil.writeBytes(outputBytes, outputFile);
//        FileUtil.writeBytes(outputBytes2, outputFile2);
    }

    @Test
    public void testOutputNSP() throws IOException {
        File inputFile = new File("C:\\Users\\Administrator\\Desktop\\" + globalFilename +".dxf");
        String filename = FileUtil.getPrefix(inputFile);
        File outputFile = new File("C:\\Users\\Administrator\\Desktop\\" + filename + ".NSP");
//        File outputFile2 = new File("C:\\Users\\Administrator\\Desktop\\" + filename + "-skip.NSP");

        byte[] bytes = FileUtil.readBytes(inputFile);

        UniParser parser = new UniParser();

        UniPattern uniPattern = parser.doParse(bytes);
        PatternTransformHelper patternTransformHelper = new PatternTransformHelper(uniPattern);
        childPatternNo = PatternUtil.getChildPatternBySortedIndex(uniPattern, 0).getId();
        ITransformCommand transformCommand = patternTransformHelper.genCommand(new PatternTransformCommand(TransformOperation.CHANGE_FIRST_SEWING, childPatternNo, beginIndex));
        transformCommand.execute();

        UniOutput uniOutput = new UniOutput();
        // 需要对Pattern进行排序
//        PatternUtil.changeWeightByLRTB(uniPattern);

        byte[] outputBytes = uniOutput.output(uniPattern, new NSPOutputStrategy());
        UniPattern pattern2 = PatternUtil.generateSkipFrameWithSingleSewing(uniPattern);
//        byte[] outputBytes2 = uniOutput.output(pattern2, new NSPOutputStrategy());
        FileUtil.writeBytes(outputBytes, outputFile);
//        FileUtil.writeBytes(outputBytes2, outputFile2);
    }

}

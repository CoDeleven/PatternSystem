package com.codeleven.core;

import cn.hutool.core.io.FileUtil;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.core.data.OutputData;
import com.codeleven.core.output.NPTOutputHStrategy;
import com.codeleven.core.output.UniOutput;
import com.codeleven.core.parser.UniParser;
import org.junit.Test;

import java.io.File;

public class GoPattern {
    @Test
    public void testOutputNPT(){
        File inputFile = new File("C:\\Users\\Administrator\\Desktop\\花样\\2021.4.7\\img105.dxf");
        String filename = FileUtil.getPrefix(inputFile);
        File outputFile = new File("C:\\Users\\Administrator\\Desktop\\花样\\2021.4.7\\" + filename + ".NPT");
        byte[] bytes = FileUtil.readBytes(inputFile);

        UniParser parser = new UniParser();
        UniPattern uniPattern = parser.doParse(bytes);

        UniOutput uniOutput = new UniOutput();
        byte[] outputBytes = uniOutput.output(uniPattern, new NPTOutputHStrategy());
        FileUtil.writeBytes(outputBytes, outputFile);
    }
}

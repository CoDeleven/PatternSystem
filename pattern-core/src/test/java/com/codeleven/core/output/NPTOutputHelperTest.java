package com.codeleven.core.output;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.codeleven.core.data.OutputData;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

public class NPTOutputHelperTest {
    @Test
    public void testOutput() {
        UniOutput uniOutput = new UniOutput();
        byte[] output = uniOutput.output(OutputData.outputData202104070115(), new NPTOutputStrategy());

        InputStream correctFileStream = NPTOutputHelperTest.class.getClassLoader().getResourceAsStream("systemtop/output/outputData202104070115.NPT");
        byte[] correctFileBytes = IoUtil.readBytes(correctFileStream);
        assert correctFileBytes.length == output.length;
    }

    @Test
    public void testOutput2(){
        File outputFile = new File("C:\\Users\\Administrator\\Desktop\\999.NPT");

        UniOutput uniOutput = new UniOutput();
        byte[] output = uniOutput.output(OutputData.outputData202104070137(), new NPTOutputStrategy());
        FileUtil.writeBytes(output, outputFile);
        System.out.println((byte) 0xB2);
//        InputStream correctFileStream = NPTOutputHelperTest.class.getClassLoader().getResourceAsStream("systemtop/output/outputData202104070137.NPT");
//        byte[] correctFileBytes = IoUtil.readBytes(correctFileStream);
//        assert correctFileBytes.length == output.length;
    }
}

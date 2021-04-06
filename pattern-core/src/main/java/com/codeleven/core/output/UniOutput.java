package com.codeleven.core.output;

import com.codeleven.common.entity.UniPattern;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UniOutput {
    public byte[] output(UniPattern uniPattern, IOutputHStrategy strategy){
        try {
            ByteArrayOutputStream headerStream = strategy.genFileHeader(uniPattern);
            ByteArrayOutputStream contentStream = strategy.genContent(uniPattern);
            strategy.afterContentGenerated(headerStream, contentStream, uniPattern);
            ByteArrayOutputStream fileEndStream = strategy.genEndFrameList(uniPattern);

            ByteArrayOutputStream result = new ByteArrayOutputStream();
            // 写入数据
            result.write(headerStream.toByteArray());
            result.write(contentStream.toByteArray());
            result.write(fileEndStream.toByteArray());

            return result.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

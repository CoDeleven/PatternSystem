package com.codeleven.patternsystem.systemtop;


import com.codeleven.patternsystem.dto.UniFrame;
import com.codeleven.patternsystem.output.PrettyFramesOutputStrategy;
import com.codeleven.patternsystem.parser.systemtop.SystemTopPatternParser;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

public class SystemTomFrameReadTest {
    @Test
    public void testFrameRead() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser();
        List<UniFrame> uniFrames = parser.readFrames(is);
        for (UniFrame uniFrame : uniFrames) {
            System.out.println(uniFrame);
        }
    }

    @Test
    public void testFramePrettyOutput() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser();
        List<UniFrame> uniFrames = parser.readFrames(is);
        String textOutput = PrettyFramesOutputStrategy.getTextOutput(uniFrames, true);
        System.out.println(textOutput);
    }

    @Test
    public void testFramePrettyOutputImage() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser();
        List<UniFrame> uniFrames = parser.readFrames(is);
        PrettyFramesOutputStrategy.getImageOutput(uniFrames);

    }
}

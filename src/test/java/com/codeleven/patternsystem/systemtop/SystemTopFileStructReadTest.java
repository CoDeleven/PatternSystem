package com.codeleven.patternsystem.systemtop;

import com.codeleven.patternsystem.dto.UniFrame;
import com.codeleven.patternsystem.output.PrettyFramesOutputStrategy;
import com.codeleven.patternsystem.parser.systemtop.SystemTopPatternParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SystemTopFileStructReadTest {
    @Test
    public void readMinXTest() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser();
        int dimension = parser.readDimension(is, SystemTopPatternParser.DIMENSION_MIN_X);
        short data = (short)0xFB31;
        assert dimension == data;
    }

    @Test
    public void readMaxXTest() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser();
        int dimension = parser.readDimension(is, SystemTopPatternParser.DIMENSION_MAX_X);
        short data = (short)0x04D4;
        assert dimension == data;
    }

    @Test
    public void readMaxYTest() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser();
        int dimension = parser.readDimension(is, SystemTopPatternParser.DIMENSION_MAX_Y);
        short data = (short)0x039E;
        assert dimension == data;
    }

    @Test
    public void readMinYTest() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/002.NPT");
        SystemTopPatternParser parser = new SystemTopPatternParser();
        int dimension = parser.readDimension(is, SystemTopPatternParser.DIMENSION_MIN_Y);
        short data = (short)0x0;
        assert dimension == data;
    }
}

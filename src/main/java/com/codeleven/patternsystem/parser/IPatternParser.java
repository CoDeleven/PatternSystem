package com.codeleven.patternsystem.parser;

import java.io.IOException;
import java.io.InputStream;

public interface IPatternParser {
    boolean isTargetPattern(InputStream is) throws IOException;

    boolean isEndOfFile(byte[] byteArray);
}

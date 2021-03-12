package com.codeleven.patternsystem.parser;

import java.io.IOException;

public interface IPatternParser {
    boolean isTargetPattern() throws IOException;

    boolean isEndOfFile(byte[] byteArray);
}

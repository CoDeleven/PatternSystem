package com.codeleven.core.output;

import com.codeleven.common.entity.UniPattern;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface IOutputStrategy {
    ByteArrayOutputStream genFileHeader(UniPattern pattern) throws IOException;

    ByteArrayOutputStream genContent(UniPattern pattern) throws IOException;

    void afterContentGenerated(ByteArrayOutputStream headerStream, ByteArrayOutputStream contentStream, UniPattern pattern) throws IOException;

    ByteArrayOutputStream genEndFrameList(UniPattern pattern) throws IOException;
}

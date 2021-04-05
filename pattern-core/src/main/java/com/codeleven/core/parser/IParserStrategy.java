package com.codeleven.core.parser;

import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public interface IParserStrategy {

    List<UniFrame> readFrames(byte[] totalBytes);

    /**
     * 将一整个花样进行切割，中间遇到 剪线都认为是一个新的花样
     *
     * @param frames 所有的针
     * @return 返回子花样列表
     */
    List<UniChildPattern> splitPattern(List<UniFrame> frames);

    default boolean isSupport(byte[] totalFileBytes) {
        return this.isSupport(new ByteArrayInputStream(totalFileBytes));

    }
    boolean isSupport(InputStream is);
}

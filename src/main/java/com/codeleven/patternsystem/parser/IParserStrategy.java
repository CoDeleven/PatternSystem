package com.codeleven.patternsystem.parser;

import com.codeleven.patternsystem.entity.ChildPattern;
import com.codeleven.patternsystem.entity.UniFrame;

import java.util.Arrays;
import java.util.List;

public interface IParserStrategy {
    /**
     * 获取文件头开始的标识，用来决定用什么解析器去解析
     * @return 字节码
     */
    byte[] getFileStartCode();

    /**
     * 获取文件头开始标识的位数
     * @return 文件头开始标识的长度
     */
    int getFileStartCodeLen();

    /**
     * 获取 针迹 结束代码
     * @return 针迹 结束代码
     */
    byte[] getFrameEndCode();

    /**
     * 获取 针迹 结束代码的长度
     * @return 针迹 结束代码
     */
    int getFrameEndCodeLen();

    /**
     * 判断 byteArray 是否符合当前解析器的结束标识
     * @param byteArray byte[]
     * @return true 文件结束； false 文件尚未结束
     */
    default boolean isEndOfFile(byte[] byteArray) {
        return Arrays.equals(getFrameEndCode(), byteArray);
    }

    List<UniFrame> readFrames(byte[] totalBytes, int readOffset);

    /**
     * 将一整个花样进行切割，中间遇到 剪线都认为是一个新的花样
     * @param frames 所有的针
     * @return 返回子花样列表
     */
    List<ChildPattern> splitPattern(List<UniFrame> frames);

    /**
     * 获取有效的针迹字节数量
     * @param totalBytes
     * @return
     */
    int getAvailableBytesWithBytes(byte[] totalBytes);

    /**
     * 获取针迹开始的偏移位置
     * @return 偏移位置
     */
    int getFrameStartOffset(byte[] totalBytes);


}

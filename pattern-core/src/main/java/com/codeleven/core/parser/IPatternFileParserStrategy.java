package com.codeleven.core.parser;

import java.util.Arrays;

public interface IPatternFileParserStrategy {
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

    /**
     * 获取有效的针迹字节数量
     * 大豪和上亿都通过各自的偏移获取
     *
     * @param totalBytes 整个文件的字节
     * @return
     */
    int getAvailableBytesWithBytes(byte[] totalBytes);

    /**
     * 获取针迹开始的偏移位置
     * 对于大豪来说，因为有一个构造线层，所以需要根据实际文件处理
     * 对于上亿来说，没有构造线层，针迹开始偏移都是固定的
     *
     * @return 偏移位置
     */
    int getFrameStartOffset(byte[] totalBytes);

    /**
     * 获取单个针占用的字节长度
     *
     * @return 单个针占用的字节长度
     */
    int getOneFrameSize();
}

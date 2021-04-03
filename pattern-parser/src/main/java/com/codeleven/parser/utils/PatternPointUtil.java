package com.codeleven.parser.utils;

public class PatternPointUtil {

    /**
     * 计算 byte 对应的 有符号Int
     * @param data
     * @return
     */
    public static int computeByteToInt(byte data) {
        if ((data & 0x80) == 0x80) {
            return -1 * (data & 0x7F);
        }
        return data;
    }

    public static int getUnsignedIntFrom2Byte(byte[] readBytes) {
        return ((readBytes[1] & 0xFF) << 8) | (readBytes[0] & 0xFF);
    }
}

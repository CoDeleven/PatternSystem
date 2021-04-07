package com.codeleven.core.utils;

import com.codeleven.common.constants.SystemTopControlCode;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPoint;

import java.util.List;

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

    /**
     * 在上亿系统中，坐标轴的 值，第一位是0表示正数，为1表示负数
     *
     * @param val
     * @param isNegative
     * @return
     */
    public static byte computeInt2NPTOneByte(int val, boolean isNegative){
        return (byte) (isNegative ? (0x80 | val) : val);
    }

    public static int getLastSewingFrameIndex(UniChildPattern childPattern){
        List<UniFrame> patternData = childPattern.getPatternData();
        return getLastSewingFrameIndex(patternData);
    }

    public static int getLastSewingFrameIndex(List<UniFrame> frameList){
        for (int i = frameList.size() - 1; i >= 0; i--) {
            if(SystemTopControlCode.isSewingControlCode(frameList.get(i).getControlCode())){
                return i;
            }
        }
        throw new RuntimeException("获取最后一个车缝点失败");
    }

    public static UniPoint convertFrame2Point(UniFrame frame){
        return new UniPoint(frame.getX(), frame.getY());
    }

    public static UniFrame convertPoint2Frame(UniPoint point){
        return new UniFrame(point.getX(), point.getY());
    }
}

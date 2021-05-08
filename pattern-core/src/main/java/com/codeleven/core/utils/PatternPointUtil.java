package com.codeleven.core.utils;

import com.codeleven.common.constants.SystemTopControlCode;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.common.entity.UniPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.codeleven.common.constants.SystemTopControlCode.*;
import static com.codeleven.common.constants.SystemTopControlCode.END;

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

    public static UniFrame getFirstFrame(UniPattern pattern){
        if(pattern == null){
            throw new RuntimeException("必须传入UniPattern，才能重新设置原点");
        }
        List<UniChildPattern> patterns = PatternUtil.sortChildPatternByWeight(pattern.getChildList().values());
        List<UniFrame> patternData = patterns.get(0).getPatternData();
        return patternData.get(0);
    }

    public static byte[] convertUniFrame(UniFrame lastFrame, UniFrame uniFrame) throws IOException {
        final int threshold = 127;
        int subX = uniFrame.getX() - lastFrame.getX();
        int subY = uniFrame.getY() - lastFrame.getY();

        int absoluteX = Math.abs(subX);
        int absoluteY = Math.abs(subY);
        boolean negativeY = subY < 0;
        boolean negativeX = subX < 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 是否需要拆分
        boolean needSplit = absoluteX > threshold || absoluteY > threshold;
        if (needSplit) {
            int max = Math.max(absoluteX, absoluteY);   // 最大的数
            int splitNum = (max / threshold) + 2;       // 需要拆分的个数；实际上拆分出 splitNum+1 个字节
            if (splitNum <= 1) {
                throw new RuntimeException("异常...");
            }

            int avgXByte = absoluteX / splitNum;    // X: 每一帧移动多少内容
            int surplusX = absoluteX % splitNum;     // X: 需要补齐的偏移量

            int avgYByte = absoluteY / splitNum;    // Y: 每一帧移动多少内容
            int surplusY = absoluteY % splitNum;    // Y: 需要补齐的偏移量

            // 如果是车缝控制码，需要 splitNum 个移动码 + 1个 0x7 0x0 0x0 0x0 针
            if (SystemTopControlCode.isSewingControlCode(uniFrame.getControlCode())) {
                int tempSurplusX = surplusX;
                int tempSurplusY = surplusY;
                for (int i = 0; i < splitNum; i++) {
                    int tempX = avgXByte;
                    int tempY = avgYByte;
                    if (tempSurplusX > 0) {
                        tempX += 1;
                        tempSurplusX--;
                    }
                    if (tempSurplusY > 0) {
                        tempY += 1;
                        tempSurplusY--;
                    }
                    byteArrayOutputStream.write(new byte[]{(byte) CONTINUE_SKIP.getCode(), 0x0, computeInt2NPTOneByte(tempX, negativeX), computeInt2NPTOneByte(tempY, negativeY)});
                }
                byteArrayOutputStream.write(new byte[]{(byte) DROP_NEEDLE.getCode(), 0, 0, 0});
            } else if (SystemTopControlCode.isSkipControlCode(uniFrame.getControlCode())) {
                int tempSurplusX = surplusX;
                int tempSurplusY = surplusY;
                for (int i = 0; i < splitNum - 1; i++) {
                    int tempX = avgXByte;
                    int tempY = avgYByte;
                    if (tempSurplusX > 0) {
                        tempX += 1;
                        tempSurplusX--;
                    }
                    if (tempSurplusY > 0) {
                        tempY += 1;
                        tempSurplusY--;
                    }
                    byteArrayOutputStream.write(new byte[]{(byte) CONTINUE_SKIP.getCode(), 0x0, computeInt2NPTOneByte(tempX, negativeX), computeInt2NPTOneByte(tempY, negativeY)});
                }
                byteArrayOutputStream.write(new byte[]{(byte) SKIP.getCode(), 0x0, computeInt2NPTOneByte(avgXByte, negativeX), computeInt2NPTOneByte(avgYByte, negativeY)});
            } else if (SystemTopControlCode.isEndControlCode(uniFrame.getControlCode())) {
                byteArrayOutputStream.write(new byte[]{(byte) END.getCode(), 0, 0, 0});
            } else {
                System.out.println("需要拆分但控制码不正确:" + uniFrame.getControlCode());
            }
        } else {
            byteArrayOutputStream.write(new byte[]{(byte) uniFrame.getControlCode(), 0x0, computeInt2NPTOneByte(absoluteX, negativeX), computeInt2NPTOneByte(absoluteY, negativeY)});
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static double getLength(UniFrame frame1, UniFrame frame2){
        int delt1 = frame1.getX() - frame2.getX();
        int delt2 = frame1.getY() - frame2.getY();
        double len = Math.sqrt(delt1 * delt1 + delt2 * delt2);
        return len;
    }
}

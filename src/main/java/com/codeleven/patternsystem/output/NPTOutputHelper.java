package com.codeleven.patternsystem.output;

import com.codeleven.patternsystem.entity.UniFrame;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.parser.systemtop.SystemTopControlCode;
import com.codeleven.patternsystem.parser.systemtop.SystemTopStruct;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.codeleven.patternsystem.parser.systemtop.SystemTopControlCode.*;

public class NPTOutputHelper {
    public static void output(UniPattern pattern) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(SystemTopStruct.FILE_START_CODE);
        byteArrayOutputStream.write("NEW".getBytes());
        byteArrayOutputStream.write(new byte[9]);

        UniFrame lastFrame = UniFrame.ZERO_FRAME;
        // 针数组
        ByteArrayOutputStream frameByteArray = new ByteArrayOutputStream();
        for (UniFrame frame : pattern.getFrames()) {
            byte[] bytes = convertUniFrame(lastFrame, frame);
            frameByteArray.write(bytes);
            lastFrame = frame;
        }

        // 写入针的总字节数
        byteArrayOutputStream.write(convertInt2NPTStructByte(frameByteArray.size() * 4, 4));
        byteArrayOutputStream.write(convertInt2NPTStructByte(frameByteArray.size(), 2));
        byteArrayOutputStream.write(convertInt2NPTStructByte(frameByteArray.size() - 10, 2));
        byteArrayOutputStream.write(convertInt2NPTStructByte(pattern.getMinX(), 2));
        byteArrayOutputStream.write(convertInt2NPTStructByte(pattern.getMaxX(), 2));
        byteArrayOutputStream.write(convertInt2NPTStructByte(pattern.getMinY(), 2));
        byteArrayOutputStream.write(convertInt2NPTStructByte(pattern.getMaxY(), 2));
        byteArrayOutputStream.write(new byte[12]);

        byteArrayOutputStream.write(frameByteArray.toByteArray());

        FileOutputStream fos = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\test2.NPT");
        fos.write(byteArrayOutputStream.toByteArray());
    }

    private static byte[] convertUniFrame(UniFrame lastFrame, UniFrame uniFrame) throws IOException {
        int subX = uniFrame.getX() - lastFrame.getX();
        int subY = uniFrame.getY() - lastFrame.getY();

        int absoluteX = Math.abs(subX);
        int absoluteY = Math.abs(subY);
        boolean negativeY = subY < 0;
        boolean negativeX = subX < 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 是否需要拆分
        boolean needSplit = absoluteX > 127 || absoluteY > 127;
        if (needSplit) {
            int max = Math.max(absoluteX, absoluteY);   // 最大的数
            int splitNum = (max / 127) + 1;       // 需要拆分的个数；实际上拆分出 splitNum+1 个字节
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
                    byteArrayOutputStream.write(new byte[]{(byte) CONTINUE_SKIP.getCode(), 0x0, convertInt2NTPByte(tempX, negativeX), convertInt2NTPByte(tempY, negativeY)});
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
                    byteArrayOutputStream.write(new byte[]{(byte) CONTINUE_SKIP.getCode(), 0x0, convertInt2NTPByte(tempX, negativeX), convertInt2NTPByte(tempY, negativeY)});
                }
                byteArrayOutputStream.write(new byte[]{(byte) SKIP.getCode(), 0x0, convertInt2NTPByte(avgXByte, negativeX), convertInt2NTPByte(avgYByte, negativeY)});
            } else {
                System.out.println("需要拆分但控制码不正确:" + uniFrame.getControlCode());
            }
        } else {
            byteArrayOutputStream.write(new byte[]{(byte) uniFrame.getControlCode(), 0x0, convertInt2NTPByte(absoluteX, negativeX), convertInt2NTPByte(absoluteY, negativeY)});
        }
        return byteArrayOutputStream.toByteArray();
    }

    private static byte convertInt2NTPByte(int val, boolean isNegative) {
        return (byte) (isNegative ? (val | 0x80) : val);
    }

    private static byte[] convertInt2NPTStructByte(int val, int availableByteCount) {
        byte[] bytes = new byte[availableByteCount];
        for (int i = 0; i < availableByteCount; i++) {
            bytes[i] = (byte) ((val >> (i * 8)) & 0xFF);
        }
        return bytes;
    }
}

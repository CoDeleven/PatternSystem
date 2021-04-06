package com.codeleven.core.output;

import com.codeleven.common.constants.SystemTopControlCode;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.core.utils.PatternLockUtil;
import com.codeleven.core.utils.PatternUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.codeleven.common.constants.SystemTopControlCode.*;


public class NSPOutputHelper {
    public static ByteArrayOutputStream output(UniPattern pattern) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(new byte[]{0x6E, 0x73, 0x70, 0x00, (byte) 0x80, 0x00, 0x00, 0x00});

        UniFrame lastFrame = UniFrame.ZERO_FRAME;
        // 针数组
        ByteArrayOutputStream frameByteArray = new ByteArrayOutputStream();
        Collection<UniChildPattern> childPatterns = PatternUtil.sortChildPatternByWeight(pattern.getChildList().values());

        boolean setSecondPoint = false;
        for (UniChildPattern childPattern : childPatterns) {
            List<UniFrame> patternData = childPattern.getPatternData();
            if(PatternUtil.isFengBiShape(childPattern)){
                PatternLockUtil.lockJoin(childPattern);
            } else {
                PatternLockUtil.lockStart(patternData, 3, 2);
                PatternLockUtil.lockEnd(patternData, 3, 2);
            }

            for (UniFrame frame : patternData) {
                byte[] bytes = convertUniFrame(lastFrame, frame);
                frameByteArray.write(bytes);
                lastFrame = frame;

                if(!setSecondPoint){
                    UniFrame temp = frame.copyFrame();
                    temp.setControlCode(SECOND_ORIGIN_POINT.code);
                    bytes = convertUniFrame(lastFrame, temp);
                    frameByteArray.write(bytes);
                    lastFrame = frame;
                    setSecondPoint = true;
                }
            }
        }
        UniFrame skip2SecondOriginal = new UniFrame(pattern.getSecondOrigin().getX(), pattern.getSecondOrigin().getY(), SKIP.code);
        byte[] bytes = convertUniFrame(lastFrame, skip2SecondOriginal);
        lastFrame = skip2SecondOriginal;
        frameByteArray.write(bytes);
        frameByteArray.write(new byte[]{0x1F, 0, 0, 0});

        byte[] frameBytes = convertInt2NPTStructByte(frameByteArray.size(), 2);
        byteArrayOutputStream.write(new byte[]{frameBytes[0], frameBytes[1], 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});
        byteArrayOutputStream.write(new byte[]{(byte) 0x90, 0x0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});
        byteArrayOutputStream.write(new byte[]{(byte) 0x00, 0x00, 0x19, 0x00, 0x00, 0x00, (byte) 0xBE, 0x02});
        byteArrayOutputStream.write(new byte[]{
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        });
        // 模拟构造线层，4 * 16 = 64个字节
        byte[] constructionLine = mockConstructionLine(frameByteArray.size() / 4);
        byteArrayOutputStream.write(constructionLine);
        // 字节数组，有效针迹
        byteArrayOutputStream.write(frameByteArray.toByteArray());

        return byteArrayOutputStream;
    }

    private static byte[] mockConstructionLine(int totalFrameCount){
        byte[] frameCountBytes = convertInt2NPTStructByte(totalFrameCount - 3, 2);

        byte[] bytes = new byte[]{(byte) 0x88, 0x00, 0x00, 0x00, 0x46, 0x00, 0x01, 0x00, 0x7F, 0, (byte) 0x7F, 0x00, 0x00, 0x00, 0x00, 0x00,
                (byte) 0xD8, 0x00, 0x60, 0x00, 0x1E, 0x00, frameCountBytes[0], frameCountBytes[1], (byte) 0x96, 0x01, (byte) 0xE4, (byte) 0xFD, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x02, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x1F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };
        return bytes;
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
            } else if(SystemTopControlCode.isEndControlCode(uniFrame.getControlCode())){
                byteArrayOutputStream.write(new byte[]{(byte) END.getCode(), 0, 0, 0});
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

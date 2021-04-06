package com.codeleven.core.output;

import com.codeleven.common.constants.LockMethod;
import com.codeleven.common.constants.SystemTopControlCode;
import com.codeleven.common.constants.SystemTopFileStruct;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.common.entity.UniPoint;
import com.codeleven.core.utils.PatternLockUtil;
import com.codeleven.core.utils.PatternPointUtil;
import com.codeleven.core.utils.PatternUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.codeleven.common.constants.SystemTopControlCode.*;
import static com.codeleven.core.utils.PatternPointUtil.computeInt2OneByte;

public class NPTOutputHStrategy implements IOutputHStrategy {

    // 用于记录最大值，最小值。在处理了Content时候自动填充这些属性
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;

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
                    byteArrayOutputStream.write(new byte[]{(byte) CONTINUE_SKIP.getCode(), 0x0, computeInt2OneByte(tempX, negativeX), computeInt2OneByte(tempY, negativeY)});
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
                    byteArrayOutputStream.write(new byte[]{(byte) CONTINUE_SKIP.getCode(), 0x0, computeInt2OneByte(tempX, negativeX), computeInt2OneByte(tempY, negativeY)});
                }
                byteArrayOutputStream.write(new byte[]{(byte) SKIP.getCode(), 0x0, computeInt2OneByte(avgXByte, negativeX), computeInt2OneByte(avgYByte, negativeY)});
            } else if (SystemTopControlCode.isEndControlCode(uniFrame.getControlCode())) {
                byteArrayOutputStream.write(new byte[]{(byte) END.getCode(), 0, 0, 0});
            } else {
                System.out.println("需要拆分但控制码不正确:" + uniFrame.getControlCode());
            }
        } else {
            byteArrayOutputStream.write(new byte[]{(byte) uniFrame.getControlCode(), 0x0, computeInt2OneByte(absoluteX, negativeX), computeInt2OneByte(absoluteY, negativeY)});
        }
        return byteArrayOutputStream.toByteArray();
    }

    private static byte[] convertInt2NPTStructByte(int val, int availableByteCount) {
        byte[] bytes = new byte[availableByteCount];
        for (int i = 0; i < availableByteCount; i++) {
            bytes[i] = (byte) ((val >> (i * 8)) & 0xFF);
        }
        return bytes;
    }

    @Override
    public ByteArrayOutputStream genFileHeader(UniPattern pattern) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        // 写入文件头，四个字节
        result.write(SystemTopFileStruct.FILE_START_CODE);
        // 写入文件名称
        result.write("NEW".getBytes());
        // 空余的9个字节。截止该行语句执行完后，已写入16个字节
        result.write(new byte[9]);

        // 这里还有一些字节需要写入，但是缺少信息，放在 genContent()之后
        return result;
    }

    @Override
    public ByteArrayOutputStream genContent(UniPattern pattern) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        // 按权重排序
        Collection<UniChildPattern> childPatterns = PatternUtil.sortChildPatternByWeight(pattern.getChildList().values());
        // 最普通的车缝数据，跳缝+剪线，通过DXF进来的通常只有这几种数据
        List<UniFrame> allFrames = PatternUtil.mergeChildPattern(new ArrayList<>(childPatterns));

        extendContent(pattern, allFrames);

        UniFrame lastFrame = UniFrame.ZERO_FRAME;
        for (UniFrame frame : allFrames) {
            byte[] bytes = convertUniFrame(lastFrame, frame);
            result.write(bytes);
            lastFrame = frame;
            int uniFrameX = frame.getX();
            int uniFrameY = frame.getY();

            if (uniFrameX > maxX) {
                maxX = uniFrameX;
            } else if (uniFrameX < minX) {
                minX = uniFrameX;
            }
            if (uniFrameY > maxY) {
                maxY = uniFrameY;
            } else if (uniFrameY < minY) {
                minY = uniFrameY;
            }
        }

        return result;
    }

    @Override
    public ByteArrayOutputStream genEndFrameList(UniPattern pattern) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        UniPoint secondOrigin = pattern.getSecondOrigin();
        if(secondOrigin == null){
            throw new RuntimeException("生成文件结尾时，缺少次元点...");
        }
        UniFrame skip2SecondOriginal = PatternPointUtil.convertPoint2Frame(secondOrigin);
        skip2SecondOriginal.setControlCode(SECOND_ORIGIN_POINT.code);

        List<UniFrame> uniFrames = PatternUtil.mergeChildPattern(pattern);
        UniFrame lastFrame = uniFrames.get(uniFrames.size() - 1);
        byte[] bytes = convertUniFrame(lastFrame, skip2SecondOriginal);
        result.write(bytes);
        result.write(new byte[]{0x1F, 0, 0, 0});
        return result;
    }

    @Override
    public void afterContentGenerated(ByteArrayOutputStream headerStream, ByteArrayOutputStream contentStream, UniPattern pattern) throws IOException {
        // 因为结尾还有2个帧需要写入，所以这里需要+8
        headerStream.write(convertInt2NPTStructByte(contentStream.size() + 8, 4));
        headerStream.write(convertInt2NPTStructByte(contentStream.size() / 4, 2));
        headerStream.write(convertInt2NPTStructByte(contentStream.size() - 10, 2));
        headerStream.write(convertInt2NPTStructByte(minX, 2));
        headerStream.write(convertInt2NPTStructByte(maxX, 2));
        headerStream.write(convertInt2NPTStructByte(minY, 2));
        headerStream.write(convertInt2NPTStructByte(maxY, 2));
        headerStream.write(new byte[12]);
    }

    /**
     * 扩展车缝内容
     *
     * @param content 普通车缝数据
     */
    private void extendContent(UniPattern uniPattern, List<UniFrame> content) {
        this.handleLock(uniPattern, content);
        // 处理次元点
        this.handleSecondPoint(uniPattern, content);
    }

    private void handleLock(UniPattern uniPattern, List<UniFrame> content) {
        // 获取所有的子花样
        List<UniChildPattern> childList = new ArrayList<>(uniPattern.getChildList().values());
        // 遍历子花样
        for (UniChildPattern uniChildPattern : childList) {
            // 获取子花样的锁针方式
            LockMethod lockMethod = uniChildPattern.getLockMethod();
            // 倒缝锁针
            if (lockMethod == LockMethod.LOCK_BACK) {
                // 从起点开始，往前3个， 回来，过去，算一次
                PatternUtil.repeatStartSewing(uniChildPattern.getPatternData(), 3, 2);
            } else if (lockMethod == LockMethod.LOCK_JOIN) {
                PatternLockUtil.lockJoin(uniChildPattern);
            }
        }
    }

    private void handleSecondPoint(UniPattern uniPattern, List<UniFrame> content) {
        // 目前没有手动设置次元点
        UniFrame first = content.get(0);
        UniFrame temp = first.copyFrame();

        if (first.getControlCode() == SKIP.code) {
            temp.setControlCode(SECOND_ORIGIN_POINT.code);
            content.add(1, temp);
        } else if (first.getControlCode() == HIGH_SEWING.code) {
            temp.setControlCode(SECOND_ORIGIN_POINT.code);
            content.add(0, temp);
        }
        if(uniPattern.getSecondOrigin() == null){
            uniPattern.setSecondOrigin(PatternPointUtil.convertFrame2Point(temp));
        }
    }
}

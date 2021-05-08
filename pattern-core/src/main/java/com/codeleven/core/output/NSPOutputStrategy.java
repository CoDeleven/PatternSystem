package com.codeleven.core.output;

import com.codeleven.common.constants.LockMethod;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.common.entity.UniPoint;
import com.codeleven.core.utils.PatternHeaderUtil;
import com.codeleven.core.utils.PatternLockUtil;
import com.codeleven.core.utils.PatternPointUtil;
import com.codeleven.core.utils.PatternUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.codeleven.common.constants.SystemTopControlCode.*;
import static com.codeleven.common.constants.SystemTopControlCode.SECOND_ORIGIN_POINT;

public class NSPOutputStrategy implements IOutputHStrategy{
    // 用于记录最大值，最小值。在处理了Content时候自动填充这些属性
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;

    @Override
    public ByteArrayOutputStream genFileHeader(UniPattern pattern) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        // 写入文件头，四个字节
        result.write(DaHaoFileStruct.FILE_START_CODE);
        // 写入我也不知道是啥玩意的东西，因为都固定是它，所以不动
        result.write(new byte[]{(byte) 0x80, 0x00, 0x00, 0x00});
        // 这里还有一些字节需要写入，但是缺少信息，放在 genContent()之后
        return result;
    }

    @Override
    public ByteArrayOutputStream genContent(UniPattern pattern) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        // 按权重排序
        Collection<UniChildPattern> childPatterns = PatternUtil.sortChildPatternByWeight(pattern.getChildList().values());
        // 最普通的车缝数据，跳缝+剪线，通过DXF进来的通常只有这几种数据

        extendContent(pattern);
        List<UniFrame> allFrames = PatternUtil.mergeChildPattern(new ArrayList<>(childPatterns));
        // 上亿的特殊处理
        handleZeroSewing(allFrames);

        UniFrame lastFrame = UniFrame.ZERO_FRAME;
        for (UniFrame frame : allFrames) {
            byte[] bytes = PatternPointUtil.convertUniFrame(lastFrame, frame);
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

    private static byte[] convertInt2NPTStructByte(int val, int availableByteCount) {
        byte[] bytes = new byte[availableByteCount];
        for (int i = 0; i < availableByteCount; i++) {
            bytes[i] = (byte) ((val >> (i * 8)) & 0xFF);
        }
        return bytes;
    }

    @Override
    public void afterContentGenerated(ByteArrayOutputStream headerStream, ByteArrayOutputStream contentStream, UniPattern pattern) throws IOException {
        byte[] availableBytesForFrames = convertInt2NPTStructByte(contentStream.size() + endFrameUsedBytes, 2);
        headerStream.write(new byte[]{availableBytesForFrames[0], availableBytesForFrames[1]});
        headerStream.write(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00});
        headerStream.write(new byte[]{(byte) 0x90, 0x0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});
        headerStream.write(new byte[]{(byte) 0x00, 0x00, 0x19, 0x00, 0x00, 0x00, (byte) 0xBE, 0x02});
        headerStream.write(new byte[]{
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        });
        // 模拟构造线层，4 * 16 = 64个字节
        byte[] constructionLine = mockConstructionLine((contentStream.size() + endFrameUsedBytes) / 4);
        headerStream.write(constructionLine);
    }

    private int endFrameUsedBytes = -1;

    private static byte[] mockConstructionLine(int totalFrameCount){
        byte[] frameCountBytes = convertInt2NPTStructByte(totalFrameCount - 3, 2);

        byte[] bytes = new byte[]{(byte) 0x88, 0x00, 0x00, 0x00, 0x46, 0x00, 0x01, 0x00, 0x7F, 0, (byte) 0x7F, 0x00, 0x00, 0x00, 0x00, 0x00,
                (byte) 0xD8, 0x00, 0x60, 0x00, 0x1E, 0x00, frameCountBytes[0], frameCountBytes[1], (byte) 0x96, 0x01, (byte) 0xE4, (byte) 0xFD, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x02, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x1F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };
        return bytes;
    }

    @Override
    public ByteArrayOutputStream genEndFrameList(UniPattern pattern) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        UniPoint secondOrigin = pattern.getSecondOrigin();
        if(secondOrigin == null){
            throw new RuntimeException("生成文件结尾时，缺少次元点...");
        }
        UniFrame skip2SecondOriginal = PatternPointUtil.convertPoint2Frame(secondOrigin);
        skip2SecondOriginal.setControlCode(SKIP.code);

        List<UniFrame> uniFrames = PatternUtil.mergeChildPattern(pattern);
        UniFrame lastFrame = uniFrames.get(uniFrames.size() - 1);
        byte[] bytes = PatternPointUtil.convertUniFrame(lastFrame, skip2SecondOriginal);
        // 需要记录最后写入的帧 占用的字节数
        endFrameUsedBytes = bytes.length;
        result.write(bytes);
        result.write(new byte[]{0x1F, 0, 0, 0});
        // 增加最后 0x1F 0 0 0 占用的字节
        endFrameUsedBytes += 4;
        return result;
    }

    /**
     * 扩展车缝内容
     *
     */
    private void extendContent(UniPattern uniPattern) {
        this.handleLock(uniPattern);
        // 处理次元点
        this.handleSecondPoint(uniPattern);
    }

    private void handleLock(UniPattern uniPattern) {
        // 获取所有的子花样
        Collection<UniChildPattern> childList = uniPattern.getChildList().values();
        // 遍历子花样
        for (UniChildPattern uniChildPattern : childList) {
            // 获取子花样的锁针方式
            LockMethod lockMethod = uniChildPattern.getLockMethod();
            // 倒缝锁针
            if (lockMethod == LockMethod.LOCK_BACK) {
                // 从起点开始，往前3个， 回来，过去，算一次
                PatternLockUtil.lockStart(uniChildPattern.getPatternData(), 3, 2);
                PatternLockUtil.lockEnd(uniChildPattern.getPatternData(), 3, 2);
            } else if (lockMethod == LockMethod.LOCK_JOIN) {
                PatternLockUtil.lockJoin(uniChildPattern);
            }
        }
    }

    private void handleSecondPoint(UniPattern uniPattern) {
        UniChildPattern uniChildPattern = PatternUtil.getChildPatternBySortedIndex(uniPattern, 0);
        List<UniFrame> content = uniChildPattern.getPatternData();
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

    /**
     * 上亿似乎不能出现 6,0,0  61,0,0这样的情况
     * @return
     */
    private void handleZeroSewing(List<UniFrame> allFrames){
        for (int i = 0; i < allFrames.size() - 1; i++) {
            UniFrame uniFrame = allFrames.get(i);
            if(uniFrame.getControlCode() == SECOND_ORIGIN_POINT.code &&
                    uniFrame.getX() == 0 && uniFrame.getY() == 0) {
                UniFrame next = allFrames.get(i + 1);
//                if(SystemTopControlCode.isSewingControlCode(next.getControlCode()) &&
//                    next.getX() == 0 && next.getY() == 0)   {
//
//                }
                allFrames.remove(0);
            }
        }
    }
}
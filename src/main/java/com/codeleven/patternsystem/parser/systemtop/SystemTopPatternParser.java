package com.codeleven.patternsystem.parser.systemtop;

import com.codeleven.patternsystem.dto.UniFrame;
import com.codeleven.patternsystem.parser.IPatternParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static com.codeleven.patternsystem.parser.systemtop.SystemTopStruct.*;

/**
 * 上亿花样机 NPT格式解析器
 */
public class SystemTopPatternParser implements IPatternParser {
    public final static byte DIMENSION_MIN_X = 1;
    public final static byte DIMENSION_MAX_X = 2;
    public final static byte DIMENSION_MIN_Y = 3;
    public final static byte DIMENSION_MAX_Y = 4;

    // 标识文件结束的四个字节
    private final static byte[] FILE_END_CODE = new byte[]{(byte) 0x1F, 0, 0, 0};

    public List<UniFrame> readFrames(InputStream is) throws IOException {
        // 校验文件流是否正确
        boolean isShangYiPatternFile = this.isTargetPattern(is);
        if (!isShangYiPatternFile) {
            throw new RuntimeException("花样文件标志头不正确...");
        }

        /* 跳过前面无关的字节，直接定位到第一帧，因为前面已经读过文件标识符，所以要减去文件标识符的个数 */
        is.skip(FIRST_FRAME_OFFSET.getOffset() - FIRST_START_CODE_OFFSET.getOffset());
        byte[] readBytes = new byte[4];
        // 用于构造List<UniFrame>
        FrameHelper helper = new FrameHelper();
        do {
            int read = is.read(readBytes);
            if (read == -1) {
                // TODO 抛出异常，因为应该在EOF处结束的，但是没结束
                break;
            }
            // 中间有个 00 不知道什么作用
            helper.addFrame(readBytes[0], computeNumber(readBytes[2]), computeNumber(readBytes[3]));
        } while (!isEndOfFile(readBytes));
        return helper.build();
    }

    public int readTotalFrameCount(InputStream is) throws IOException {
        byte[] readBytes = new byte[2];
        is.skip(TOTAL_FRAME_COUNT_OFFSET.getOffset());
        int readLen = is.read(readBytes);
        if (readLen == -1) {
            // TODO 抛出异常
            throw new RuntimeException("读取总帧数失败...");
        }
        // 小端模式，低字节排在低地址；所以高地址先左移1个字节，再拼上低字节
        int totalFrameCount = readBytes[1] << 8 | readBytes[0];
        // 上亿系统的特色，数据文件记录的总针数和显示的针数差1。
        return totalFrameCount;
    }

    public int readDimension(InputStream is, int dimensionType) throws IOException {
        byte[] readBytes = new byte[2];
        int readLen = -1;
        switch (dimensionType) {
            case DIMENSION_MIN_X:
                is.skip(MIN_X_OFFSET.getOffset());
                readLen = is.read(readBytes);
                break;
            case DIMENSION_MIN_Y:
                is.skip(MIN_Y_OFFSET.getOffset());
                readLen = is.read(readBytes);
                break;
            case DIMENSION_MAX_X:
                is.skip(MAX_X_OFFSET.getOffset());
                readLen = is.read(readBytes);
                break;
            case DIMENSION_MAX_Y:
                is.skip(MAX_Y_OFFSET.getOffset());
                readLen = is.read(readBytes);
                break;
        }
        if (readLen == -1) {
            // TODO 抛出异常
            throw new RuntimeException("读取 DimensionType:" + dimensionType + "失败...");
        }
        short readBytes0 = readBytes[0];
        // 因为是对单个byte进行处理，容易转成int造成补码问题，所以这里需要将高位都设置为0
        if(readBytes0 < 0) {
            readBytes0 &= 0xFF;
        }
        // 注意一定要设置为short，利用short的溢出规则
        short result = (short) ((readBytes[1] << 8) + readBytes0);
        return result;
    }

    @Override
    public boolean isTargetPattern(InputStream is) throws IOException {
        byte[] temp = new byte[4];
        int readLen = is.read(temp);
        if (readLen == 4) {
            return Arrays.equals(temp, FILE_START_CODE);
        }
        return false;
    }

    @Override
    public boolean isEndOfFile(byte[] byteArray) {
        return Arrays.equals(FILE_END_CODE, byteArray);
    }

    private int computeNumber(byte data) {
        if ((data & 0x80) == 0x80) {
            return -1 * (data & 0x7F);
        }
        return data;
    }
}

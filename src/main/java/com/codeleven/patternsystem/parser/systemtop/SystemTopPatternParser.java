package com.codeleven.patternsystem.parser.systemtop;

import com.codeleven.patternsystem.entity.UniFrame;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.parser.IPatternParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.codeleven.patternsystem.parser.systemtop.SystemTopControlCode.*;
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
    private final byte[] patternData;

    public SystemTopPatternParser(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[0x1000];
        int len = -1;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        this.patternData = byteArrayOutputStream.toByteArray();
    }

    public UniPattern readAll() {
        UniPattern pattern = new UniPattern();
        // 校验文件流是否正确
        boolean isShangYiPatternFile = this.isTargetPattern();
        if (!isShangYiPatternFile) {
            throw new RuntimeException("花样文件标志头不正确...");
        }
        // 设置总帧数
        int totalFrameCount = this.readTotalFrameCount();
        pattern.setFrameNumber(totalFrameCount);

        // 设置维度
        int minX = this.readDimension(DIMENSION_MIN_X);
        pattern.setMinX(minX);
        int maxX = this.readDimension(DIMENSION_MAX_X);
        pattern.setMaxX(maxX);
        int minY = this.readDimension(DIMENSION_MIN_Y);
        pattern.setMinY(minY);
        int maxY = this.readDimension(DIMENSION_MAX_Y);
        pattern.setMaxY(maxY);

        List<UniFrame> frames = this.readFrames();
        pattern.setFrames(frames);
        // 上亿系统的BUG，如果最小的Y大于0，那么minY就会等于0。猜测是 运行时添加了一个(0,0)到集合里（导出的时候会移除）。
        // （在[0,0]增加一个节点保存后再打开发现[0,0]的点被移除了，所以觉得会添加一个(0,0)到集合里且导出的时候会移除）
        if(minY == 0){
            // 对检查的帧进行筛选
            Optional<UniFrame> min = frames.stream().filter(uniFrame -> {
                if(uniFrame.getControlCode() == HIGH_SEWING.getCode() ||
                uniFrame.getControlCode()== MID_HIGH_SEWING.getCode() ||
                uniFrame.getControlCode() == MID_LOW_SEWING.getCode() ||
                uniFrame.getControlCode() == LOW_SEWING.getCode()){
                    return true;
                }
                return false;
            }).min(Comparator.comparingInt(UniFrame::getY));    // 查找最小的Y值
            min.ifPresent(uniFrame -> pattern.setMinY(uniFrame.getY()));    // 若存在最小的Y值则更新Pattern数据
        }
        return pattern;
    }

    public List<UniFrame> readFrames() {
        byte[] readBytes = new byte[4];
        // 用于构造List<UniFrame>
        FrameHelper helper = new FrameHelper();
        int frameOffset = 0;
        do {
            int beginOffset = FIRST_FRAME_OFFSET.getOffset() + frameOffset;
            readBytes[0] = this.patternData[beginOffset];
            readBytes[1] = this.patternData[beginOffset + 1];
            readBytes[2] = this.patternData[beginOffset + 2];
            readBytes[3] = this.patternData[beginOffset + 3];
            // 中间有个 00 不知道什么作用
            helper.addFrame(readBytes[0], computeNumber(readBytes[2]), computeNumber(readBytes[3]));
            frameOffset += FIRST_FRAME_OFFSET.getSize();
        } while (!isEndOfFile(readBytes));
        return helper.build();
    }

    public int readTotalFrameCount() {
        if (this.checkPatternStructDataAvailable(SEWING_FRAME_COUNT_OFFSET)) {
            byte[] readBytes = new byte[2];
            readBytes[0] = this.patternData[SEWING_FRAME_COUNT_OFFSET.getOffset()];
            readBytes[1] = this.patternData[SEWING_FRAME_COUNT_OFFSET.getOffset() + 1];
            int totalFrameCount = readBytes[1] << 8 | readBytes[0];
            // 上亿系统的特色，数据文件记录的总针数和显示的针数差1。
            return totalFrameCount;
        }
        // TODO 抛出异常
        throw new RuntimeException("读取总帧数失败...");
    }

    /**
     * 读取维度信息
     *
     * @param dimensionType
     * @return
     * @throws IOException
     */
    public int readDimension(int dimensionType) {
        SystemTopStruct structEnum;
        switch (dimensionType) {
            case DIMENSION_MIN_X:
                structEnum = MIN_X_OFFSET;
                break;
            case DIMENSION_MIN_Y:
                structEnum = MIN_Y_OFFSET;
                break;
            case DIMENSION_MAX_X:
                structEnum = MAX_X_OFFSET;
                break;
            case DIMENSION_MAX_Y:
                structEnum = MAX_Y_OFFSET;
                break;
            default:
                throw new RuntimeException("未知的类型");
        }
        if (this.checkPatternStructDataAvailable(structEnum)) {
            // 保存数据
            byte[] readBytes = new byte[2];
            readBytes[0] = this.patternData[structEnum.getOffset()];
            readBytes[1] = this.patternData[structEnum.getOffset() + 1];
            short readBytes0 = readBytes[0];
            // 因为是对单个byte进行处理，容易转成int造成补码问题，所以这里需要将高位都设置为0
            if (readBytes0 < 0) {
                readBytes0 &= 0xFF;
            }
            // 注意一定要设置为short，利用short的溢出规则
            short result = (short) ((readBytes[1] << 8) + readBytes0);
            return result;
        }
        throw new RuntimeException("读取花样维度信息错误...");
    }

    @Override
    public boolean isTargetPattern() {
        byte[] temp = new byte[4];
        System.arraycopy(this.patternData, 0, temp, 0, 4);
        return Arrays.equals(temp, FILE_START_CODE);
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

    /**
     * 检查花样文件数据里 文件结构偏移 是否都存在
     *
     * @param structEnum
     * @return
     */
    private boolean checkPatternStructDataAvailable(SystemTopStruct structEnum) {
        return this.patternData.length > structEnum.getOffset() + structEnum.getSize();
    }
}

package com.codeleven.parser.dahao;

import com.codeleven.common.constants.DaHaoFileStruct;
import com.codeleven.parser.BaseParserStrategy;
import com.codeleven.parser.utils.PatternPointUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 大豪花样解析策略
 */
public class DaHaoPatternParser extends BaseParserStrategy {
    @Override
    public int getOneFrameSize() {
        return DaHaoFileStruct.FIRST_FRAME_OFFSET.size;
    }

    @Override
    public byte[] getFileStartCode() {
        return DaHaoFileStruct.FILE_START_CODE;
    }

    @Override
    public int getFileStartCodeLen() {
        return DaHaoFileStruct.FILE_START_CODE.length;
    }

    @Override
    public byte[] getFrameEndCode() {
        return DaHaoFileStruct.FRAME_END_CODE;
    }

    @Override
    public int getFrameEndCodeLen() {
        return DaHaoFileStruct.FRAME_END_CODE.length;
    }

    @Override
    public int getAvailableBytesWithBytes(byte[] totalBytes) {
        byte[] readBytes = new byte[2];
        readBytes[0] = totalBytes[DaHaoFileStruct.FRAME_BYTES_OFFSET.offset];
        readBytes[1] = totalBytes[DaHaoFileStruct.FRAME_BYTES_OFFSET.offset + 1];
        return PatternPointUtil.getUnsignedIntFrom2Byte(readBytes);
    }

    /**
     * 获取针迹开始的偏移位置
     * 对于大豪来说，因为有一个构造线层，所以需要根据实际文件处理
     * 对于上亿来说，没有构造线层，针迹开始偏移都是固定的
     *
     * @return 偏移位置
     */
    @Override
    public int getFrameStartOffset(byte[] totalBytes) {
        byte firstByte = totalBytes[DaHaoFileStruct.FIRST_FRAME_OFFSET.offset];
        byte secondByte = totalBytes[DaHaoFileStruct.FIRST_FRAME_OFFSET.offset + 1];
        return PatternPointUtil.getUnsignedIntFrom2Byte(new byte[]{firstByte, secondByte});
    }

    @Override
    public boolean isSupport(InputStream is) {
        byte[] temp = new byte[4];
        try {
            int read = is.read(temp);
            if(read != DaHaoFileStruct.FILE_START_CODE.length) {
                return false;
            }
            return Arrays.equals(temp, DaHaoFileStruct.FILE_START_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

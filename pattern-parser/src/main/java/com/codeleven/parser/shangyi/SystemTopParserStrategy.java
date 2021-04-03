package com.codeleven.parser.shangyi;

import com.codeleven.parser.BaseParserStrategy;
import com.codeleven.parser.dahao.DaHaoFileStruct;
import com.codeleven.parser.utils.PatternPointUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 上亿花样 解析策略
 */
public class SystemTopParserStrategy extends BaseParserStrategy {
    @Override
    public byte[] getFrameEndCode() {
        return SystemTopFileStruct.FRAME_END_CODE;
    }

    @Override
    public int getFrameEndCodeLen() {
        return this.getFrameEndCode().length;
    }

    @Override
    public byte[] getFileStartCode() {
        return SystemTopFileStruct.FILE_START_CODE;
    }

    @Override
    public int getFileStartCodeLen() {
        return this.getFileStartCode().length;
    }

    @Override
    public int getOneFrameSize() {
        return SystemTopFileStruct.FIRST_FRAME_OFFSET.size;
    }

    @Override
    public int getAvailableBytesWithBytes(byte[] totalBytes) {
        byte[] readBytes = new byte[2];
        readBytes[0] = totalBytes[SystemTopFileStruct.FRAME_BYTES_OFFSET.offset];
        readBytes[1] = totalBytes[SystemTopFileStruct.FRAME_BYTES_OFFSET.offset + 1];
        return PatternPointUtil.getUnsignedIntFrom2Byte(readBytes);
    }

    @Override
    public int getFrameStartOffset(byte[] totalBytes) {
        return SystemTopFileStruct.FIRST_FRAME_OFFSET.offset;
    }

    @Override
    public boolean isSupport(InputStream is) {
        byte[] temp = new byte[4];
        try {
            int read = is.read(temp);
            if(read != SystemTopFileStruct.FILE_START_CODE.length) {
                return false;
            }
            return Arrays.equals(temp, SystemTopFileStruct.FILE_START_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

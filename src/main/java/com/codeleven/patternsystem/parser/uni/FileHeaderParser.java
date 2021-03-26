package com.codeleven.patternsystem.parser.uni;

import com.codeleven.patternsystem.parser.PatternSystemVendor;

/**
 * 文件头部解释器
 */
public class FileHeaderParser {

    private int machineFrameOffset;
    private int availableMachineFrameCount;




    public enum UniPatternFileHeader {
        FIRST_START_CODE_OFFSET(0x0, 4, "标识文件开始的四个字节");

        private int offset;
        private int size;
        private String description;

        UniPatternFileHeader(int offset, int size, String description) {
            this.offset = offset;
            this.size = size;
            this.description = description;
        }

        public int getOffset() {
            return offset;
        }

        public int getSize() {
            return size;
        }

        public String getDescription() {
            return description;
        }
    }
}

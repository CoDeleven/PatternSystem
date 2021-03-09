package com.codeleven.patternsystem.parser.systemtop;

/**
 * 上亿花样机文件结构
 */
public enum SystemTopStruct {
    FIRST_START_CODE_OFFSET(0x0, 4, "标识文件开始的四个字节"),
    // 第一帧的起始地址，后续每4个字节代表一帧
    FIRST_FRAME_OFFSET(0x2C, 4, "第一帧的偏移量，十进制44，十六进制0x2C。一帧占用4个字节"),
    TOTAL_FRAME_COUNT_OFFSET(0x16, 2, "总针数的偏移量记录未知，十进制22，十六进制0x16。占用2个字节"),
    MIN_X_OFFSET(0x18, 2, "最左边的点的X（最小的X）。占用2个字节"),
    MAX_X_OFFSET(0x1A, 2, "最右边的点的X（最大的X）。占用2个字节"),
    MAX_Y_OFFSET(0x1E, 2, "最上面的点的Y（最大的Y）。占用2个字节"),
    MIN_Y_OFFSET(0x1C, 2, "最下面的点的Y（最小的Y）。占用2个字节");
    // 标识文件开始的四个字节
    public final static byte[] FILE_START_CODE = new byte[]{(byte) 0xAA, 0x55, 0x08, 0x00};

    private int offset;
    private int size;
    private String description;

    SystemTopStruct(int offset, int size, String description) {
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

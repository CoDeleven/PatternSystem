package com.codeleven.parser.dahao;

public enum DaHaoFileStruct {
    FIRST_START_CODE_OFFSET(0x0, 4, "标识文件开始的四个字节"),
    CONSTRUCTOR_LINE_BYTES_OFFSET(0x4, 2, "记录构造线层占用的字节数"),
    FRAME_BYTES_OFFSET(0x8, 2, "针迹部分占用的总字节数（即0x08起的 帧有几个，若有10个，则此处为10 * 4 =40(0x28) ）"),
    // 第一帧的起始地址，后续每4个字节代表一帧
    FIRST_FRAME_OFFSET(0x10, 4, "第一帧的偏移量，十进制44，十六进制0x2C。一帧占用4个字节"),
    FIRST_CONSTRUCTOR_LINE_OFFSET(0x50, 0, "第一个构造线层的数据偏移，这里的长度取决于： CONSTRUCTOR_LINE_BYTES_OFFSET 的值 - 0x10");

    // 标识文件开始的四个字节
    public final static byte[] FILE_START_CODE = new byte[]{(byte) 0x6E, 0x73, 0x70, 0x00};

    // 针迹数据，最后是以 这个 结尾的
    public final static byte[] FRAME_END_CODE = new byte[]{(byte) 0x1F, 0, 0, 0};

    public int offset;
    public int size;
    public String description;

    DaHaoFileStruct(int offset, int size, String description) {
        this.offset = offset;
        this.size = size;
        this.description = description;
    }
}

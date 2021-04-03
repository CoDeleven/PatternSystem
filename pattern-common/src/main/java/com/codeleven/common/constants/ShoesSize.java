package com.codeleven.common.constants;

/**
 * 目前花样都是针对鞋子的
 * 所以是针对鞋子尺码的一个枚举类。
 * 另外鞋子可能存在41.5这样的尺码，这里采用415来表示这种尺码；所以350=35码，360=36码
 */
public enum ShoesSize implements BaseEnum<ShoesSize, Integer> {
    SHOES_SIZE_35(350, "35码"), SHOES_SIZE_36(360, "36码"), SHOES_SIZE_37(370, "37码"),
    SHOES_SIZE_38(380, "38码"), SHOES_SIZE_39(390, "39码"), SHOES_SIZE_40(400, "40码"),
    SHOES_SIZE_41(410, "41码"), UNKNOWN(999, "未知的鞋码");

    private int size;
    private String sizeDesc;

    ShoesSize(int size, String sizeDesc) {
        this.size = size;
        this.sizeDesc = sizeDesc;
    }



    public int getSize() {
        return size;
    }

    public String getSizeDesc() {
        return sizeDesc;
    }

    public static ShoesSize getShoesSize(int size){
        for (ShoesSize sizeEnum : ShoesSize.values()) {
            if(sizeEnum.getSize() == size){
                return sizeEnum;
            }
        }
        return UNKNOWN;
    }

    @Override
    public Integer getValue() {
        return this.size;
    }

    @Override
    public String getDisplayName() {
        return this.sizeDesc;
    }
}

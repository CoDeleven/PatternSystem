package com.codeleven.patternsystem.parser.systemtop;

/**
 * 上亿控制码
 */
public enum SystemTopControlCode {
    SKIP(0x1B, "空送"), HIGH_SEWING(0x61, "高速车缝"), MID_HIGH_SEWING(0x41, "中高速车缝"),
    MID_LOW_SEWING(0x21, "中低速车缝"), LOW_SEWING(0x01, "低速车缝"), CUT(0x02, "剪线"),
    CONTINUE_SKIP(0x03, "持续移动"),
    UNKNOWN_CODE(0, "未知码");

    private int code;
    private String codeName;

    SystemTopControlCode(int code, String codeName) {
        this.code = code;
        this.codeName = codeName;
    }

    public int getCode() {
        return code;
    }

    public String getCodeName() {
        return codeName;
    }

    public static SystemTopControlCode getEnumByCode(int code){
        for (SystemTopControlCode enumItem : SystemTopControlCode.values()) {
            if(enumItem.code == code){
                return enumItem;
            }
        }
        return UNKNOWN_CODE;
    }
}

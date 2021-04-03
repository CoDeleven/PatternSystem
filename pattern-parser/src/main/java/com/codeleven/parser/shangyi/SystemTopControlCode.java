package com.codeleven.parser.shangyi;

/**
 * 上亿控制码
 */
public enum SystemTopControlCode {
    SKIP(0x1B, "空送"), HIGH_SEWING(0x61, "高速车缝"), MID_HIGH_SEWING(0x41, "中高速车缝"),
    MID_LOW_SEWING(0x21, "中低速车缝"), LOW_SEWING(0x01, "低速车缝"), CUT(0x02, "剪线"),
    CONTINUE_SKIP(0x03, "持续移动"), DROP_NEEDLE(0x07, "下针"), END(0x1F, "结束标识符"),
    PRESSER_FOOT_UPLIFT(0x04, "上暂停压脚抬起"),
    SECOND_ORIGIN_POINT(0x06, "设置次原点"), UNKNOWN_CODE(0, "未知码");

    public int code;
    public String codeName;

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

    /**
     * 判断控制码是否属于车缝
     * @param code int 控制码
     * @return boolean 是否为车缝控制码
     */
    public static boolean isSewingControlCode(int code){
        return code == HIGH_SEWING.code || code == MID_HIGH_SEWING.code || code == MID_LOW_SEWING.code || code == LOW_SEWING.code || code == DROP_NEEDLE.code;
    }

    public static boolean isSkipControlCode(int code){
        return code == SKIP.code;
    }

    public static boolean isEndControlCode(int code){
        return code == END.code;
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

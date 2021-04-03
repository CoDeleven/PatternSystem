package com.codeleven.common.constants;

public enum PatternSystemVendor implements BaseEnum<PatternSystemVendor, Integer> {
    SYSTEM_TOP("上亿花样机", 0x11), DAHAO("大豪花样机", 0x22), UNKNOWN("未知", 0x999);

    private String patternName;
    private int code;



    PatternSystemVendor(String patternName, int code) {
        this.patternName = patternName;
        this.code = code;
    }

    public static PatternSystemVendor getVendor(int vendorCode){
        for (PatternSystemVendor value : PatternSystemVendor.values()) {
            if(value.code == vendorCode){
                return value;
            }
        }
        return UNKNOWN;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }

    @Override
    public String getDisplayName() {
        return this.patternName;
    }
}

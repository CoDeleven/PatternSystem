package com.codeleven.patternsystem.parser;

public enum PatternSystemVendor {
    SYSTEM_TOP("上亿花样机", 0x11), DAHAO("大豪花样机", 0x22), UNKNOWN("未知", 0x999);

    private String patternName;
    private int code;

    PatternSystemVendor(String patternName, int code) {
        this.patternName = patternName;
        this.code = code;
    }
}

package com.codeleven.patternsystem.vo;

import lombok.Data;

@Data
public class PatternCreateVO {
    private String name;
    private int size;
    private String patternDataPath;
    private String coverPath;
}

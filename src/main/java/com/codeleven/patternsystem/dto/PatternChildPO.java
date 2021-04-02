package com.codeleven.patternsystem.dto;

import lombok.Data;

@Data
public class PatternChildPO {
    private long patternId;         // 归属的patternId
    private long id;                // 自己的ID
    private int weight;             // 权重，越大顺序越在前面
    private String patternDataJson; // 花样数据，JSON，仅限当前子花样
}

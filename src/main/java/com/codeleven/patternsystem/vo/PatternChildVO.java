package com.codeleven.patternsystem.vo;

import com.codeleven.common.entity.UniFrame;
import lombok.Data;

import java.util.List;

@Data
public class PatternChildVO {
    private long id;                    // 自己的ID
    private int weight;                 // 权重，越大顺序越在前面
    private List<UniFrame> patternData; // 花样数据，JSON，仅限当前子花样
}

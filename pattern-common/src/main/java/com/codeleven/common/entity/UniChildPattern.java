package com.codeleven.common.entity;

import com.codeleven.common.constants.LockMethod;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;
import lombok.Data;

import java.util.List;

@Data
public class UniChildPattern {
    private UniPattern pattern;         // 归属的patternId
    private long id;                // 自己的ID
    private int weight;             // 权重，越大顺序越在前面
    private List<UniFrame> patternData; // 花样数据，JSON，仅限当前子花样
    private LockMethod lockMethod;      // 锁针方式
}

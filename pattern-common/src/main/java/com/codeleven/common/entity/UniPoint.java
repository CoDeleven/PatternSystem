package com.codeleven.common.entity;

import lombok.Data;

@Data
public class UniPoint {
    public final int x;
    public final int y;

    public UniPoint(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
}
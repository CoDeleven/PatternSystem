package com.codeleven.core.data;

import com.codeleven.common.constants.SystemTopControlCode;
import com.codeleven.common.entity.UniFrame;

import java.util.ArrayList;
import java.util.List;

public class LogicTestData {
    public static List<UniFrame> initData() {
        List<UniFrame> frames1 = new ArrayList<>();
        frames1.add(new UniFrame(1, 1, SystemTopControlCode.SKIP.code));
        frames1.add(new UniFrame(2, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(5, 5, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(6, 6, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(7, 7, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(8, 8, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 9, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 10, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(11, 11, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(12, 12, SystemTopControlCode.SKIP.code));
        return frames1;
    }

    public static List<UniFrame> afterLockEndForLen4Count4(){
        List<UniFrame> frames1 = new ArrayList<>();
        frames1.add(new UniFrame(1, 1, SystemTopControlCode.SKIP.code));
        frames1.add(new UniFrame(2, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(5, 5, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(6, 6, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(7, 7, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(8, 8, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 9, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 10, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(11, 11, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 10, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 9, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(8, 8, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 9, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 10, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(11, 11, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 10, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 9, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(8, 8, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 9, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 10, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(11, 11, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(12, 12, SystemTopControlCode.SKIP.code));
        return frames1;
    }

    public static List<UniFrame> afterLockStartForLen4Count4() {
        List<UniFrame> frames1 = new ArrayList<>();
        frames1.add(new UniFrame(1, 1, SystemTopControlCode.SKIP.code));
        frames1.add(new UniFrame(2, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(5, 5, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(2, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(5, 5, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(2, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(5, 5, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(6, 6, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(7, 7, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(8, 8, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 9, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 10, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(11, 11, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(12, 12, SystemTopControlCode.SKIP.code));
        return frames1;
    }

    public static List<UniFrame> initData4Join() {
        List<UniFrame> frames1 = new ArrayList<>();
        frames1.add(new UniFrame(1, 1, SystemTopControlCode.SKIP.code));
        frames1.add(new UniFrame(2, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(5, 5, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(6, 6, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(7, 7, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(8, 8, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 9, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 10, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(11, 11, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 5, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(8, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(7, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(6, 1, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(5, -1, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, -2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(2, 7, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(1, 1, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(1, 1, SystemTopControlCode.CUT.code));
        return frames1;
    }

    public static List<UniFrame> afterLockJoin(){
        List<UniFrame> frames1 = new ArrayList<>();
        frames1.add(new UniFrame(1, 1, SystemTopControlCode.SKIP.code));
        frames1.add(new UniFrame(2, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(5, 5, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(6, 6, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(7, 7, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(8, 8, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 9, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 10, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(11, 11, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 5, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(8, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(7, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(6, 1, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(5, -1, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, -2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(2, 7, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(1, 1, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(2, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.CUT.code));
        return frames1;
    }

    public static List<UniFrame> initData4Join2() {
        List<UniFrame> frames1 = new ArrayList<>();
        frames1.add(new UniFrame(1, 1, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(2, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(5, 5, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(6, 6, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(7, 7, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(8, 8, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 9, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 10, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(11, 11, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 5, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(8, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(7, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(6, 1, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(5, -1, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, -2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(2, 7, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(1, 1, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(1, 1, SystemTopControlCode.CUT.code));
        return frames1;
    }

    public static List<UniFrame> afterLockJoin2(){
        List<UniFrame> frames1 = new ArrayList<>();
        frames1.add(new UniFrame(1, 1, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(2, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(5, 5, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(6, 6, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(7, 7, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(8, 8, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 9, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 10, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(11, 11, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(10, 5, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(9, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(8, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(7, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(6, 1, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(5, -1, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, -2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(2, 7, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(1, 1, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(2, 2, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(3, 3, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(4, 4, SystemTopControlCode.CUT.code));
        return frames1;
    }
}

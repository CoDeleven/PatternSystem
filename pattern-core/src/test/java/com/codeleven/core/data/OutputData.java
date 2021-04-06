package com.codeleven.core.data;

import cn.hutool.core.util.RandomUtil;
import com.codeleven.common.constants.LockMethod;
import com.codeleven.common.constants.SystemTopControlCode;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.common.entity.UniPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputData {
    public static UniPattern outputData202104070115(){
        UniPattern pattern = new UniPattern();

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
        frames1.add(new UniFrame(12, 12, SystemTopControlCode.CUT.code));

        Map<Long, UniChildPattern> childList = new HashMap<>();
        UniChildPattern child1 = new UniChildPattern();
        child1.setPatternData(frames1);
        child1.setLockMethod(LockMethod.LOCK_BACK);
        child1.setWeight(Byte.MAX_VALUE - 1);
        long id = RandomUtil.randomLong();
        child1.setId(id);
        childList.put(id, child1);

        pattern.setSecondOrigin(new UniPoint(1, 1));

        pattern.setChildList(childList);
        return pattern;
    }

    public static UniPattern outputData202104070137(){
        UniPattern pattern = new UniPattern();

        List<UniFrame> frames1 = new ArrayList<>();
        frames1.add(new UniFrame(10, 10, SystemTopControlCode.SKIP.code));
        frames1.add(new UniFrame(20, 20, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(30, 30, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(40, 40, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(50, 50, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(60, 60, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(70, 70, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(80, 80, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(90, 90, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(100, 100, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(110, 110, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(120, 120, SystemTopControlCode.HIGH_SEWING.code));
        frames1.add(new UniFrame(120, 120, SystemTopControlCode.CUT.code));

        Map<Long, UniChildPattern> childList = new HashMap<>();
        UniChildPattern child1 = new UniChildPattern();
        child1.setPatternData(frames1);
        child1.setLockMethod(LockMethod.LOCK_BACK);
        child1.setWeight(Byte.MAX_VALUE - 1);
        long id = RandomUtil.randomLong();
        child1.setId(id);
        childList.put(id, child1);

        pattern.setSecondOrigin(new UniPoint(1, 1));

        pattern.setChildList(childList);
        return pattern;
    }
}

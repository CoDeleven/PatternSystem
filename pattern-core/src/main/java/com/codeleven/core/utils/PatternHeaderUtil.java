package com.codeleven.core.utils;

import com.codeleven.common.constants.SystemTopControlCode;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;

import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class PatternHeaderUtil {
    public static int getSewingFrameCount(UniPattern uniPattern){
        Map<Long, UniChildPattern> childList = uniPattern.getChildList();
        Integer collect = childList.values().stream().collect(Collectors.summingInt(new ToIntFunction<UniChildPattern>() {
            @Override
            public int applyAsInt(UniChildPattern childPattern) {
                int sum = 0;
                for (UniFrame patternDatum : childPattern.getPatternData()) {
                    if (SystemTopControlCode.isSewingControlCode(patternDatum.getControlCode())) {
                        ++sum;
                    }
                }
                return sum;
            }
        }));
        return collect;
    }
}

package com.codeleven.core.utils;

import cn.hutool.core.collection.ListUtil;
import com.codeleven.common.constants.SystemTopControlCode;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;

import java.util.ArrayList;
import java.util.List;

public class PatternLockUtil {

    // 如果是封闭的图形，尾部节点需要往后继续打一些
    public static void lockJoin(UniChildPattern childPattern) {
        List<UniFrame> patternData = childPattern.getPatternData();
        lockJoin(patternData);
    }

    // 如果是封闭的图形，尾部节点需要往后继续打一些
    public static void lockJoin(List<UniFrame> patternData) {
        List<UniFrame> other = PatternUtil.copyList(PatternUtil.getLastControlCodeFrames(patternData));
        int otherSize = other.size();
        for (int i = 1; i <= otherSize; i++) {
            patternData.remove(patternData.size() - 1);
        }


        // 获取第一个车缝点
        int firstSewingIndex = PatternUtil.getFirstSewingIndex(patternData);
        // 第一个车缝点会被使用，所以应该从第一个点往后算
        if (firstSewingIndex == 0) {
            firstSewingIndex++;
        }
        List<UniFrame> joinList = patternData.subList(firstSewingIndex, firstSewingIndex + 3);
        patternData.addAll(joinList);

        UniFrame newLastFrame = patternData.get(patternData.size() - 1);

        for (UniFrame uniFrame : other) {
            UniFrame foo = newLastFrame.copyFrame();
            foo.setControlCode(uniFrame.getControlCode());
            patternData.add(foo);
        }
    }

    public static void repeatSewing(List<UniFrame> total, int start, int to, int count) {
        // [start, to]
        List<UniFrame> temp = PatternUtil.copyList(total.subList(start, to + 1));
        //
        List<UniFrame> negative = PatternUtil.copyList(ListUtil.reverseNew(temp));
        // 不包含最后一个（反转后不包含第一个）
        negative.remove(0);
        List<UniFrame> positive = PatternUtil.copyList(temp);
        // 不包含第一个
        positive.remove(0);

        List<UniFrame> tempList = new ArrayList<>();
        assert count % 2 == 0;
        for (int i = 0; i < count / 2; i++) {
            tempList.addAll(PatternUtil.copyList(negative));
            tempList.addAll(PatternUtil.copyList(positive));
        }

        total.addAll(to + 1, tempList);
    }

    public static void lockStart(List<UniFrame> total, int len, int count) {
        int beginIndex = -1;
        for (int i = 0; i < total.size(); i++) {
            UniFrame foo = total.get(i);
            if (foo.getControlCode() == SystemTopControlCode.HIGH_SEWING.code) {
                beginIndex = i;
                break;
            }
        }
        repeatSewing(total, beginIndex, beginIndex + len - 1, count);
    }

    public static void lockEnd(List<UniFrame> total, int len, int count) {
        int beginIndex = -1;
        for (int i = total.size() - 1; i >= 0; i--) {
            UniFrame foo = total.get(i);
            if (foo.getControlCode() == SystemTopControlCode.HIGH_SEWING.code) {
                beginIndex = i;
                break;
            }
        }

        repeatSewing(total, beginIndex - len + 1, beginIndex, count);
    }
}

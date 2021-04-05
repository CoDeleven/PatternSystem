package com.codeleven.core.utils;

import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.constants.SystemTopControlCode;

import java.util.ArrayList;
import java.util.List;

public class ChildFrameHelper {
    private List<UniFrame> childPatternFrameList;
    private List<UniChildPattern> uniChildPatterns = new ArrayList<>();
    private byte weightCount = Byte.MAX_VALUE;
    // 参见 原因1
    private boolean needMergeSkip = false;

    public ChildFrameHelper addFrame(UniFrame frame) {
        // 如果是跳缝，则新建一个List
        if(frame.getControlCode() == SystemTopControlCode.SKIP.code){
            if(needMergeSkip){
                // 把起始的SKIP移除，用新的SKIP。第一个是SKIP
                childPatternFrameList.remove(0);
                // 第二个通常会是次元点
                UniFrame tempSecondOriginal = childPatternFrameList.remove(0);
                childPatternFrameList.add(frame);
                childPatternFrameList.add(tempSecondOriginal);
                needMergeSkip = false;
            } else {
                // 如果遇到 SKIP 则新建一个List
                childPatternFrameList = new ArrayList<>();
                childPatternFrameList.add(frame);
            }
        } else if(frame.getControlCode() == SystemTopControlCode.CUT.code) {
            // 如果遇到 CUT
            childPatternFrameList.add(frame);
            uniChildPatterns.add(genChildPattern(childPatternFrameList));
            childPatternFrameList = null;
        } else if(frame.getControlCode() == SystemTopControlCode.PRESSER_FOOT_UPLIFT.code){
            // 如果遇到上暂停压脚抬起，这个追加到前面的ChildPattern。。因为这个值往往是在剪线后面，而我们用剪线判断是否为单独一个花样
            getLastChildPattern().getPatternData().add(frame);
        } else if(frame.getControlCode() == SystemTopControlCode.END.code) {
            // 遇到结束符，就没了，结束符之前往往会有一个移动到次元点的SKIP
        } else {
            childPatternFrameList.add(frame);
        }
        if(needMergeSkip) {
            needMergeSkip = false;
        }
        // 原因1：对于手动防止次元点，可能和起缝点有偏移，所以常常会出现 跳缝、次元点、跳缝，如果发现这种情况，最好对它进行合并
        // 如果当前这个点是次元点，设置一个标识，如果下一个Frame是SKIP，则合并SKIP
        if(frame.getControlCode() == SystemTopControlCode.SECOND_ORIGIN_POINT.code) {
            needMergeSkip = true;
        }
        return this;
    }

    public List<UniChildPattern> build(){
        // 生成
        return uniChildPatterns;
    }

    private UniChildPattern genChildPattern(List<UniFrame> frames){
        UniChildPattern pattern = new UniChildPattern();
        pattern.setPatternData(frames);
        pattern.setWeight(--weightCount);
        return pattern;
    }

    private UniChildPattern getLastChildPattern(){
        return uniChildPatterns.get(uniChildPatterns.size() - 1);
    }
}

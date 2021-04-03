package com.codeleven.parser.utils;

import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.constants.SystemTopControlCode;

import java.util.ArrayList;
import java.util.List;

public class ChildFrameHelper {
    private List<UniFrame> childPatternFrameList;
    private List<UniChildPattern> uniChildPatterns = new ArrayList<>();
    private byte weightCount = Byte.MAX_VALUE;

    public ChildFrameHelper addFrame(UniFrame frame) {
        // 如果是跳缝，则新建一个List
        if(frame.getControlCode() == SystemTopControlCode.SKIP.code){
            // 如果遇到 SKIP 则新建一个List
            childPatternFrameList = new ArrayList<>();
            childPatternFrameList.add(frame);
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

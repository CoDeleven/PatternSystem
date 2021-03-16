package com.codeleven.patternsystem.entity;

import java.util.List;

/**
 * 一个花样可以由多个子花样构成（是以 空送 进行划分）
 */
public class ChildPattern {
    private int patternNo;                  // 子花样的序号
    private ChildPattern prevChildPattern;  // 上一个子花样
    private ChildPattern nextChildPattern;  // 下一个子花样
    private int beginFrameIndex;    // 包含当前这个index
    private int frameCount;         // 帧数 数量
    private List<UniFrame> frameList;

    public int getBeginFrameIndex() {
        return beginFrameIndex;
    }

    public void setBeginFrameIndex(int beginFrameIndex) {
        this.beginFrameIndex = beginFrameIndex;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public List<UniFrame> getFrameList() {
        return frameList;
    }

    public void setFrameList(List<UniFrame> frameList) {
        this.frameList = frameList;
    }

    public int getPatternNo() {
        return patternNo;
    }

    public void setPatternNo(int patternNo) {
        this.patternNo = patternNo;
    }

    public ChildPattern getPrevChildPattern() {
        return prevChildPattern;
    }

    public void setPrevChildPattern(ChildPattern prevChildPattern) {
        this.prevChildPattern = prevChildPattern;
    }

    public ChildPattern getNextChildPattern() {
        return nextChildPattern;
    }

    public void setNextChildPattern(ChildPattern nextChildPattern) {
        this.nextChildPattern = nextChildPattern;
    }
}

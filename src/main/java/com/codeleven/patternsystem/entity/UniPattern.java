package com.codeleven.patternsystem.entity;

import java.util.List;

public class UniPattern {
    private String patternName;             // 花样名称
    private int frameNumber;                // 针数
    private int minX;                       // 最小的X值
    private int minY;                       // 最小的Y值
    private int maxX;                       // 最大的X值
    private int maxY;                       // 最大的Y值
    private List<UniFrame> frames;          // 针迹

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public List<UniFrame> getFrames() {
        return frames;
    }

    public void setFrames(List<UniFrame> frames) {
        this.frames = frames;
    }

    public int getHeight(){
        return this.maxY - this.minY;
    }

    public int getWidth(){
        return this.maxX - this.minX;
    }
}

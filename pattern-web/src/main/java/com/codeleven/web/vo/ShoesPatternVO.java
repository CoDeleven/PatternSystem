package com.codeleven.web.vo;

public class ShoesPatternVO {
    private String name;
    private int vendor;
    private int size;
    private String patternDataPath;
    private String coverPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVendor() {
        return vendor;
    }

    public void setVendor(int vendor) {
        this.vendor = vendor;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPatternDataPath() {
        return patternDataPath;
    }

    public void setPatternDataPath(String patternDataPath) {
        this.patternDataPath = patternDataPath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }
}

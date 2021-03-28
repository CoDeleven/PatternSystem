package com.codeleven.patternsystem.vo;

import java.util.List;

/**
 * Section 列表
 */
public class SectionListVO {
    private String sectionHeader;
    private List<PatternVO> sectionList;

    public String getSectionHeader() {
        return sectionHeader;
    }

    public void setSectionHeader(String sectionHeader) {
        this.sectionHeader = sectionHeader;
    }

    public List<PatternVO> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<PatternVO> sectionList) {
        this.sectionList = sectionList;
    }
}

package com.codeleven.patternsystem.vo;

import com.codeleven.patternsystem.entity.UniFrame;

import java.util.List;

/**
 * 正常来说的列表 Entry VO
 * 一般不单独使用，配合列表类使用
 */
public class PatternDetailVO extends PatternVO{
    private List<UniFrame> uniFrameList;
    private List<List<UniFrame>> childFrameList;

    public PatternDetailVO() {
    }

    public PatternDetailVO(PatternVO patternVO) {
        this.setName(patternVO.getName());
        this.setPatternUrl(patternVO.getPatternUrl());
        this.setCoverUrl(patternVO.getCoverUrl());
        this.setSize(patternVO.getSize());
        this.setCreateDate(patternVO.getCreateDate());
    }

    public List<UniFrame> getUniFrameList() {
        return uniFrameList;
    }

    public void setUniFrameList(List<UniFrame> uniFrameList) {
        this.uniFrameList = uniFrameList;
    }

    public List<List<UniFrame>> getChildFrameList() {
        return childFrameList;
    }

    public void setChildFrameList(List<List<UniFrame>> childFrameList) {
        this.childFrameList = childFrameList;
    }
}

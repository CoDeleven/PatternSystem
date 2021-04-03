package com.codeleven.patternsystem.vo;

import lombok.Data;

import java.util.List;

/**
 * 正常来说的列表 Entry VO
 * 一般不单独使用，配合列表类使用
 */
@Data
public class PatternDetailVO extends PatternVO{
    private List<PatternChildVO> childFrameList;
}

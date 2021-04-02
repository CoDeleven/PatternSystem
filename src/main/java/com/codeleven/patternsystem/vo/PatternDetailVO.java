package com.codeleven.patternsystem.vo;

import com.codeleven.patternsystem.entity.UniChildPattern;
import com.codeleven.patternsystem.entity.UniFrame;
import lombok.Data;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.List;

/**
 * 正常来说的列表 Entry VO
 * 一般不单独使用，配合列表类使用
 */
@Data
public class PatternDetailVO extends PatternVO{
    private List<PatternChildVO> childFrameList;
}

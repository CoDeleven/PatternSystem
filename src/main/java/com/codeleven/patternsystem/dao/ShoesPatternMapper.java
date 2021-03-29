package com.codeleven.patternsystem.dao;

import com.codeleven.patternsystem.dto.PatternDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ShoesPatternMapper {
    /**
     * 查询 鞋子花样 列表
     * @param pageParam 分页参数
     * @return List<ShoesPatternDto> 花样列表
     */
    List<PatternDto> queryForPage(Map<String, Object> pageParam);

    /**
     * 创建鞋子花样记录
     * @param patternDto 记录实体
     * @return 创建成功条数
     */
    int create(PatternDto patternDto);

    /**
     * 更新鞋子花样记录
     * @param patternDto 记录实体
     * @return 更新成功条数
     */
    int update(PatternDto patternDto);

    PatternDto findPatternById(@Param("id") int id);
}

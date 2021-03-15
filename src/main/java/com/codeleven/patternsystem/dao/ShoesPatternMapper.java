package com.codeleven.patternsystem.dao;

import com.codeleven.patternsystem.dto.ShoesPatternDto;
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
    List<ShoesPatternDto> queryForPage(Map<String, Object> pageParam);

    /**
     * 创建鞋子花样记录
     * @param patternDto 记录实体
     * @return 创建成功条数
     */
    int create(ShoesPatternDto patternDto);

    /**
     * 更新鞋子花样记录
     * @param patternDto 记录实体
     * @return 更新成功条数
     */
    int update(ShoesPatternDto patternDto);
}

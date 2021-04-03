package com.codeleven.web.dao;

import com.codeleven.common.po.PatternChildPO;
import com.codeleven.common.po.UniPatternPO;
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
    List<UniPatternPO> queryForPage(Map<String, Object> pageParam);

    /**
     * 创建鞋子花样记录
     * @param patternDto 记录实体
     * @return 创建成功条数
     */
    int create(UniPatternPO patternDto);

    /**
     * 更新鞋子花样记录
     * @param patternDto 记录实体
     * @return 更新成功条数
     */
    int update(UniPatternPO patternDto);

    /**
     * 更新鞋子花样记录
     * @param childPOList
     * @return
     */
    int updateBatch(List<PatternChildPO> childPOList);

    /**
     * 查找花样详情
     * @param id
     * @return 花样详情
     */
    UniPatternPO findPatternById(@Param("id") int id);

    /**
     * 创建子花样
     * @param childDto 子花样DTO
     * @return 返回创建结果
     */
    int createChild(List<PatternChildPO> childPOList);
}

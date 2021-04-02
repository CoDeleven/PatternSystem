package com.codeleven.patternsystem.service.convert;

import cn.hutool.core.date.DateUtil;
import com.codeleven.patternsystem.common.ShoesSize;
import com.codeleven.patternsystem.config.MinioConfig;
import com.codeleven.patternsystem.dto.UniPatternPO;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PatternServiceConvert {
    private static final Logger LOGGER = LoggerFactory.getLogger(PatternServiceConvert.class);

    @Autowired
    private MinioConfig minioConfig;

    /**
     * 将 PatternVO 进行分组显示
     * @param patternVoList PatternVO 列表
     * @return 经过分组的PatternVO列表 SectionListVO
     */
    public List<SectionListVO> convertToPatternSectionList(List<PatternVO> patternVoList) {
        // 将 PatternVO 按照 创建日期 进行分组
        Map<String, List<PatternVO>> groups = patternVoList.stream().collect(Collectors.groupingBy(PatternVO::getCreateDate));
        List<SectionListVO> headerList = new ArrayList<>();
        // 循环分组
        for (String header : groups.keySet()) {
            SectionListVO sectionListVO = new SectionListVO();
            // 给 Section 设置 Header
            sectionListVO.setSectionHeader(header);

            // 给 Section 设置 内容
            List<PatternVO> patternVOList = groups.get(header);
            sectionListVO.setSectionList(patternVOList);

            headerList.add(sectionListVO);
        }
        return headerList;
    }

    /**
     * 转换至 花样VO 列表
     * @param patternDto 花样DTO
     * @return 返回 花样VO 列表
     */
    public List<PatternVO> convertToPatternVOList(List<UniPatternPO> patternDto) {
        return patternDto.stream().map(PatternServiceConvert.this::convertToPatternVO).collect(Collectors.toList());
    }

    /**
     * 转换单个PatternDTO 至 花样VO
     * @param patternDto 花样DTO
     * @return 返回 PatternVO
     */
    public PatternVO convertToPatternVO(UniPatternPO patternDto) {
        PatternVO patternVO = new PatternVO();
        // 花样的名称
        patternVO.setName(patternDto.getName());
        // 花样的尺寸大小 （用尺码描述）
        patternVO.setSize(ShoesSize.getShoesSize(patternDto.getShoesSize()).getSizeDesc());
        // 获取封面URL
        String coverUrl = minioConfig.getObjectUrl(patternDto.getCoverPath());
        // 获取花样的创建日期
        String formatCreateDate = DateUtil.format(patternDto.getCreateTime(), "yyyy-MM-dd");
        // 设置创建日期
        patternVO.setCreateDate(formatCreateDate);
        // 设置封面
        patternVO.setCoverUrl(coverUrl);
        patternVO.setId(patternDto.getId());
        return patternVO;
    }

}

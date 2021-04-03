package com.codeleven.web.service.convert;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.codeleven.common.constants.ShoesSize;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.web.config.MinioConfig;
import com.codeleven.web.vo.PatternChildVO;
import com.codeleven.web.vo.PatternCreateVO;
import com.codeleven.web.vo.PatternDetailVO;

import java.util.List;
import java.util.stream.Collectors;

public class DomainObj2VO {

    public static PatternDetailVO domainObj2DetailVO(UniPattern uniPattern, MinioConfig config) {
        PatternDetailVO detailVO = new PatternDetailVO();
        detailVO.setId(uniPattern.getId());
        detailVO.setName(uniPattern.getName());
        detailVO.setCoverUrl(config.getObjectUrl(uniPattern.getCoverPath()));
        detailVO.setSize(ShoesSize.getShoesSize(uniPattern.getShoesSize()).getSizeDesc());

        detailVO.setCreateDate(DateUtil.format(uniPattern.getCreateTime(), "yyyy-MM-dd"));
        List<PatternChildVO> result = uniPattern.getChildList().values().stream().map(DomainObj2VO::convertToChild).collect(Collectors.toList());
        detailVO.setChildFrameList(result);
        return detailVO;
    }


    public static UniPattern convertToPattern(PatternCreateVO vo){
        UniPattern pattern = new UniPattern();
        pattern.setName(vo.getName());
        pattern.setShoesSize(vo.getSize());
        // 设置花样数据文件的路径
        pattern.setDxfPath(vo.getPatternDataPath());
        // 上传到 对象存储服务 后获取到的封面URL
        pattern.setCoverPath(vo.getCoverPath());
        return pattern;
    }

    public static PatternChildVO convertToChild(UniChildPattern childPattern){
        PatternChildVO vo = new PatternChildVO();
        BeanUtil.copyProperties(childPattern, vo, true);
        return vo;
    }
}

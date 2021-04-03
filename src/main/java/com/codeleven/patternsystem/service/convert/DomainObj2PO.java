package com.codeleven.patternsystem.service.convert;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.codeleven.patternsystem.dto.PatternChildPO;
import com.codeleven.patternsystem.dto.UniPatternPO;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainObj2PO {
    public static UniPatternPO domainObj2PO(UniPattern uniPattern) {
        UniPatternPO po = new UniPatternPO();
        BeanUtil.copyProperties(uniPattern, po, true);

        ArrayList<PatternChildPO> poList = new ArrayList<>();
        for (UniChildPattern value : uniPattern.getChildList().values()) {
            List<UniFrame> frames = value.getPatternData();
            String json = JSONUtil.toJsonStr(frames);

            PatternChildPO childPo = new PatternChildPO();
            childPo.setId(value.getId());
            childPo.setPatternId(uniPattern.getId());
            childPo.setPatternDataJson(json);
            childPo.setWeight(value.getWeight());

            poList.add(childPo);
        }
        po.setChildDtoList(poList);

        return po;
    }

    public static UniPattern po2DomainObj(UniPatternPO po){
        UniPattern pattern = new UniPattern();
        BeanUtil.copyProperties(po, pattern, true);

        Map<Long, UniChildPattern> patterns = new HashMap<>();
        List<PatternChildPO> childDtoList = po.getChildDtoList();
        for (PatternChildPO patternChildPO : childDtoList) {
            UniChildPattern childPattern = po2DomainObj(patternChildPO);
            patterns.put(childPattern.getId(), childPattern);
        }

        pattern.setChildList(patterns);

        return pattern;
    }

    public static UniChildPattern po2DomainObj(PatternChildPO po){
        UniChildPattern pattern = new UniChildPattern();
        BeanUtil.copyProperties(po, pattern, true);
        String json = po.getPatternDataJson();
        Type type = new TypeReference<List<UniFrame>>(){}.getType();
        List<UniFrame> frames = JSONUtil.toBean(json, type, false);
        pattern.setPatternData(frames);
        return pattern;
    }
}

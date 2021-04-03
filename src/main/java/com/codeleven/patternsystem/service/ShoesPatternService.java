package com.codeleven.patternsystem.service;

import cn.hutool.core.date.DateUtil;
import com.codeleven.patternsystem.config.MinioConfig;
import com.codeleven.patternsystem.dao.ShoesPatternMapper;
import com.codeleven.patternsystem.dto.Page;
import com.codeleven.patternsystem.dto.PatternChildPO;
import com.codeleven.patternsystem.dto.UniPatternPO;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.patternsystem.parser.CADHelper;
import com.codeleven.patternsystem.parser.systemtop.PatternTransformHelper;
import com.codeleven.patternsystem.parser.transform.ITransformCommand;
import com.codeleven.patternsystem.service.convert.DomainObj2PO;
import com.codeleven.patternsystem.service.convert.DomainObj2VO;
import com.codeleven.patternsystem.service.convert.PatternServiceConvert;
import com.codeleven.patternsystem.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class ShoesPatternService extends BaseService<UniPatternPO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoesPatternService.class);

    @Autowired
    private ShoesPatternMapper shoesPatternMapper;
    @Autowired
    private PatternServiceConvert convert;
    @Autowired
    private MinioConfig config;

    /**
     * 查询列表
     *
     * @param page   页
     * @param length 一页的数量
     * @param param  查询的参数
     * @return
     */
    public List<SectionListVO> queryForPage(int page, int length, Map<String, Object> param) {
        // 查询花样
        Page<UniPatternPO> shoesPatternDtoPage = super.queryForPage(page, length, () -> shoesPatternMapper.queryForPage(param));
        List<UniPatternPO> patternDtoList = shoesPatternDtoPage.getRoot();
        // 将 DTO列表 转换为 VO列表
        List<PatternVO> patternVOList = convert.convertToPatternVOList(patternDtoList);
        // 将 花样VO列表 进行分组
        List<SectionListVO> sectionListVOS = convert.convertToPatternSectionList(patternVOList);
        return sectionListVOS;
    }

    /**
     * 获取花样详情
     *
     * @param patternId 花样ID
     * @return 花样详情数据
     */
    public PatternDetailVO detail(int patternId) {
        UniPatternPO patternPO = this.shoesPatternMapper.findPatternById(patternId);
        UniPattern pattern = DomainObj2PO.po2DomainObj(patternPO);
        PatternDetailVO vo = DomainObj2VO.domainObj2DetailVO(pattern, config);
        return vo;
    }

    /**
     * 根据 前端传来的花样VO 创建 花样DTO 并存储到数据库
     *
     * @param vo 花样VO
     * @return 返回创建结果
     */
    public boolean create(PatternCreateVO vo) {
        try {
            UniPattern pattern = DomainObj2VO.convertToPattern(vo);
            // 下载花样数据文件DXF
            InputStream is = config.getObjectInputStream(pattern.getDxfPath());
            CADHelper helper = new CADHelper(is);
            // 加载 DXF 数据到 UniPattern 内
            helper.loadChildPatternData(pattern);
            helper.loadRefPointData(pattern);
            helper.loadHeightWidthToPattern(pattern);
            // 插入创建时间、更新时间信息
            pattern.setCreateTime(DateUtil.date());
            pattern.setUpdateTime(DateUtil.date());

            UniPatternPO po = DomainObj2PO.domainObj2PO(pattern);
            this.shoesPatternMapper.create(po);

            for (PatternChildPO patternChildPO : po.getChildDtoList()) {
                patternChildPO.setPatternId(po.getId());
            }
            this.shoesPatternMapper.createChild(po.getChildDtoList());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 更新
     *
     * @param vo
     */
    public void update(ShoesPatternUpdateVO vo) {
        UniPatternPO patternPO = this.shoesPatternMapper.findPatternById(vo.getShoesPatternId());
        UniPattern pattern = DomainObj2PO.po2DomainObj(patternPO);
        PatternTransformHelper helper = new PatternTransformHelper(pattern);
        // 数据直接更改在Pattern上的
        for (CommandVO commandVO : vo.getPatternUpdateOperationList()) {
            ITransformCommand iTransformCommand = helper.genCommand(commandVO);
            iTransformCommand.execute();
        }
        UniPatternPO uniPatternPO = DomainObj2PO.domainObj2PO(pattern);
        shoesPatternMapper.update(uniPatternPO);

        List<PatternChildPO> childDtoList = uniPatternPO.getChildDtoList();
        shoesPatternMapper.updateBatch(childDtoList);
    }

}

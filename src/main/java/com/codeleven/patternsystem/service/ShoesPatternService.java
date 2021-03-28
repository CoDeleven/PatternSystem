package com.codeleven.patternsystem.service;

import cn.hutool.core.date.DateUtil;
import com.codeleven.patternsystem.common.ShoesSize;
import com.codeleven.patternsystem.config.MinioConfig;
import com.codeleven.patternsystem.dao.ShoesPatternMapper;
import com.codeleven.patternsystem.dto.Page;
import com.codeleven.patternsystem.dto.ShoesPatternDto;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.output.NPTOutputHelper;
import com.codeleven.patternsystem.output.NSPOutputHelper;
import com.codeleven.patternsystem.parser.PatternSystemVendor;
import com.codeleven.patternsystem.parser.dahao.DaHaoPatternParser;
import com.codeleven.patternsystem.parser.systemtop.PatternTransformHelper;
import com.codeleven.patternsystem.parser.UniParser;
import com.codeleven.patternsystem.service.convert.PatternServiceConvert;
import com.codeleven.patternsystem.vo.*;
import io.minio.MinioClient;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeleven.patternsystem.config.MinioConfig.MINIO_ACCESS_KEY;
import static com.codeleven.patternsystem.config.MinioConfig.MINIO_SECRET_KEY;

@Service
public class ShoesPatternService extends BaseService<ShoesPatternDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoesPatternService.class);

    @Autowired
    private ShoesPatternMapper shoesPatternMapper;
    @Autowired
    private PatternServiceConvert convert;

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
        Page<ShoesPatternDto> shoesPatternDtoPage = super.queryForPage(page, length, () -> shoesPatternMapper.queryForPage(param));
        List<ShoesPatternDto> patternDtoList = shoesPatternDtoPage.getRoot();
        // 将 DTO列表 转换为 VO列表
        List<PatternVO> patternVOList = convert.convertToPatternVOList(patternDtoList);
        // 将 花样VO列表 进行分组
        List<SectionListVO> sectionListVOS = convert.convertToPatternSectionList(patternVOList);
        return sectionListVOS;
    }


    public ShoesPatternDto detail(int patternId) {
        // 根据花样ID查询花样
        Map<String, Object> param = new HashMap<>();
        param.put("pattern_id", patternId);
        List<ShoesPatternDto> shoesPatternDtos = shoesPatternMapper.queryForPage(param);
        if (shoesPatternDtos.size() == 0) {
            LOGGER.debug("不存在花样ID为：{}", patternId);
            return null;
        }
        ShoesPatternDto shoesPatternDto = shoesPatternDtos.get(0);

        try {
            MinioClient minioClient = new MinioClient(MinioConfig.MINIO_DOMAIN, MINIO_ACCESS_KEY, MINIO_SECRET_KEY);
            // 下载数据文件
            InputStream is = minioClient.getObject(MinioConfig.PATTERN_SYSTEM_BUCKET, shoesPatternDto.getPatternDataUrl());
            UniPattern uniPattern = null;
            if (shoesPatternDto.getVendor().getValue() == PatternSystemVendor.SYSTEM_TOP.getValue()) {
                UniParser parser = new UniParser();
                uniPattern = parser.doParse(is);
            } else if (shoesPatternDto.getVendor().getValue() == PatternSystemVendor.DAHAO.getValue()) {
                DaHaoPatternParser parser = new DaHaoPatternParser(is);
                uniPattern = parser.readAll();
            } else {
                throw new RuntimeException("未知的厂商");
            }

            // 设置UniPattern
            shoesPatternDto.setUniPattern(uniPattern);
            return shoesPatternDto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean create(PatternCreateVO vo) {
        try {
            ShoesPatternDto dto = this.convertVO2DTO(vo);
            // 获取数据文件
            MinioClient minioClient = new MinioClient(MinioConfig.MINIO_DOMAIN, MINIO_ACCESS_KEY, MINIO_SECRET_KEY);
            // 下载数据文件
            InputStream is = minioClient.getObject(MinioConfig.PATTERN_SYSTEM_BUCKET, dto.getPatternDataUrl());
            UniPattern uniPattern = null;
            if (vo.getVendor() == PatternSystemVendor.SYSTEM_TOP.getValue()) {
                // 解析花样文件
                UniParser parser = new UniParser();
                uniPattern = parser.doParse(is);
            } else if (vo.getVendor() == PatternSystemVendor.DAHAO.getValue()) {
                DaHaoPatternParser parser = new DaHaoPatternParser(is);
                uniPattern = parser.readAll();
            } else {
                throw new RuntimeException("未知的厂商，请检查");
            }

            dto.setHeight(uniPattern.getHeight());
            dto.setWidth(uniPattern.getWidth());

            dto.setCreateTime(DateUtil.date());
            dto.setUpdateTime(DateUtil.date());

            int i = this.shoesPatternMapper.create(dto);
            return i == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private ShoesPatternDto convertVO2DTO(PatternCreateVO vo) {
        ShoesPatternDto dto = new ShoesPatternDto();
        dto.setName(vo.getName());
        dto.setShoesSize(ShoesSize.getShoesSize(vo.getSize()));
        // 设置花样数据文件的路径
        dto.setPatternDataUrl(vo.getPatternDataPath());
        // 设置服务商
        dto.setVendor(PatternSystemVendor.getVendor(vo.getVendor()));
        // 上传到 对象存储服务 里获取到的封面URL
        dto.setCoverUrl(vo.getCoverPath());
        return dto;
    }

    public void update(ShoesPatternUpdateVO vo) {
        ShoesPatternDto shoesPatternDto = this.shoesPatternMapper.findPatternById(vo.getShoesPatternId());
        String patternDataUrl = shoesPatternDto.getPatternDataUrl();
        try {
            MinioClient minioClient = new MinioClient(MinioConfig.MINIO_DOMAIN, MINIO_ACCESS_KEY, MINIO_SECRET_KEY);
            // 下载数据文件
            InputStream is = minioClient.getObject(MinioConfig.PATTERN_SYSTEM_BUCKET, patternDataUrl);
            UniPattern uniPattern = null;
            int val = shoesPatternDto.getVendor().getValue();
            // 解析花样文件
            if (val == PatternSystemVendor.SYSTEM_TOP.getValue()) {
                UniParser parser = new UniParser();
                uniPattern = parser.doParse(is);
            } else if (val == PatternSystemVendor.DAHAO.getValue()) {
                DaHaoPatternParser parser = new DaHaoPatternParser(is);
                uniPattern = parser.readAll();
            }

            PatternTransformHelper helper = new PatternTransformHelper(uniPattern, vo.getPatternUpdateOperationList());
            helper.setTargetPatternNo(vo.getChildPatternNo());
            helper.doTransform();

            ByteArrayOutputStream output;
            if (val == PatternSystemVendor.SYSTEM_TOP.getValue()) {
                output = NPTOutputHelper.output(uniPattern);
            } else if (val == PatternSystemVendor.DAHAO.getValue()) {
                output = NSPOutputHelper.output(uniPattern);
            } else {
                throw new RuntimeException("未知的厂商，请检查");
            }

            minioClient.putObject(MinioConfig.PATTERN_SYSTEM_BUCKET, patternDataUrl, new ByteArrayInputStream(output.toByteArray()), ContentType.DEFAULT_BINARY.getMimeType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

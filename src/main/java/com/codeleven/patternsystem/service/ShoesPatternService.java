package com.codeleven.patternsystem.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.codeleven.patternsystem.common.MinioConfig;
import com.codeleven.patternsystem.common.ShoesSize;
import com.codeleven.patternsystem.dao.ShoesPatternMapper;
import com.codeleven.patternsystem.dto.Page;
import com.codeleven.patternsystem.dto.ShoesPatternDto;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.output.NPTOutputHelper;
import com.codeleven.patternsystem.parser.PatternSystemVendor;
import com.codeleven.patternsystem.parser.systemtop.PatternTransformHelper;
import com.codeleven.patternsystem.parser.systemtop.SystemTopPatternParser;
import com.codeleven.patternsystem.vo.ShoesPatternUpdateVO;
import com.codeleven.patternsystem.vo.ShoesPatternVO;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static com.codeleven.patternsystem.common.MinioConfig.MINIO_ACCESS_KEY;
import static com.codeleven.patternsystem.common.MinioConfig.MINIO_SECRET_KEY;

@Service
public class ShoesPatternService extends BaseService<ShoesPatternDto> {

    @Autowired
    private ShoesPatternMapper shoesPatternMapper;

    public Page<ShoesPatternDto> queryForPage(int page, int length, Map<String, Object> param) {
        Page<ShoesPatternDto> shoesPatternDtoPage = this.queryForPage(page, length, () -> {
            shoesPatternMapper.queryForPage(param);
        });
        return shoesPatternDtoPage;
    }

    public boolean create(ShoesPatternVO vo) {
        try{
            ShoesPatternDto dto = this.convertVO2DTO(vo);
            // 获取数据文件
            MinioClient minioClient = new MinioClient(MinioConfig.MINIO_DOMAIN, MINIO_ACCESS_KEY, MINIO_SECRET_KEY);
            // 下载数据文件
            InputStream is = minioClient.getObject(MinioConfig.PATTERN_SYSTEM_BUCKET, dto.getPatternDataUrl());
            // 解析花样文件
            SystemTopPatternParser parser = new SystemTopPatternParser(is);
            UniPattern uniPattern = parser.readAll();
            dto.setHeight(uniPattern.getHeight());
            dto.setWidth(uniPattern.getWidth());

            dto.setCreateTime(DateUtil.date());
            dto.setUpdateTime(DateUtil.date());

            int i = this.shoesPatternMapper.create(dto);
            return i == 1;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    private ShoesPatternDto convertVO2DTO(ShoesPatternVO vo){
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
        try{
            MinioClient minioClient = new MinioClient(MinioConfig.MINIO_DOMAIN, MINIO_ACCESS_KEY, MINIO_SECRET_KEY);
            // 下载数据文件
            InputStream is = minioClient.getObject(MinioConfig.PATTERN_SYSTEM_BUCKET, patternDataUrl);
            // 解析花样文件
            SystemTopPatternParser parser = new SystemTopPatternParser(is);
            UniPattern uniPattern = parser.readAll();
            PatternTransformHelper helper = new PatternTransformHelper(uniPattern, vo.getPatternUpdateOperationList());
            helper.setTargetPatternNo(vo.getChildPatternNo());
            helper.doTransform();
            ByteArrayOutputStream output = NPTOutputHelper.output(uniPattern);
            minioClient.putObject(MinioConfig.PATTERN_SYSTEM_BUCKET, patternDataUrl, new ByteArrayInputStream(output.toByteArray()), ContentType.DEFAULT_BINARY.getMimeType());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

package com.codeleven.patternsystem.service;

import cn.hutool.core.date.DateUtil;
import com.codeleven.patternsystem.config.MinioConfig;
import com.codeleven.patternsystem.dao.ShoesPatternMapper;
import com.codeleven.patternsystem.dto.Page;
import com.codeleven.patternsystem.dto.PatternDto;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.output.NPTOutputHelper;
import com.codeleven.patternsystem.output.NSPOutputHelper;
import com.codeleven.patternsystem.parser.PatternSystemVendor;
import com.codeleven.patternsystem.parser.UniParser;
import com.codeleven.patternsystem.parser.dahao.DaHaoPatternParser;
import com.codeleven.patternsystem.parser.systemtop.PatternTransformHelper;
import com.codeleven.patternsystem.service.convert.PatternServiceConvert;
import com.codeleven.patternsystem.vo.PatternCreateVO;
import com.codeleven.patternsystem.vo.PatternVO;
import com.codeleven.patternsystem.vo.SectionListVO;
import com.codeleven.patternsystem.vo.ShoesPatternUpdateVO;
import io.minio.MinioClient;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static com.codeleven.patternsystem.config.MinioConfig.MINIO_ACCESS_KEY;
import static com.codeleven.patternsystem.config.MinioConfig.MINIO_SECRET_KEY;

@Service
public class ShoesPatternService extends BaseService<PatternDto> {
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
        Page<PatternDto> shoesPatternDtoPage = super.queryForPage(page, length, () -> shoesPatternMapper.queryForPage(param));
        List<PatternDto> patternDtoList = shoesPatternDtoPage.getRoot();
        // 将 DTO列表 转换为 VO列表
        List<PatternVO> patternVOList = convert.convertToPatternVOList(patternDtoList);
        // 将 花样VO列表 进行分组
        List<SectionListVO> sectionListVOS = convert.convertToPatternSectionList(patternVOList);
        return sectionListVOS;
    }

    /**
     * 获取花样详情
     * @param patternId 花样ID
     * @return 花样详情数据
     */
    public PatternDto detail(int patternId) {
        PatternDto patternDto = this.shoesPatternMapper.findPatternById(patternId);
        // 获取流
        InputStream is = config.getObjectInputStream(patternDto.getPatternPath4Minio());
        // 解析
        UniParser parser = new UniParser();
        UniPattern uniPattern = parser.doParse(is);
        // 设置UniPattern
        patternDto.setUniPattern(uniPattern);
        return patternDto;
    }

    /**
     * 根据 前端传来的花样VO 创建 花样DTO 并存储到数据库
     *
     * @param vo 花样VO
     * @return 返回创建结果
     */
    public boolean create(PatternCreateVO vo) {
        try {
            PatternDto dto = convert.convertToPatternDTO(vo);
            // 获取数据文件
            MinioClient minioClient = new MinioClient(MinioConfig.MINIO_DOMAIN, MINIO_ACCESS_KEY, MINIO_SECRET_KEY);
            // 下载花样数据文件
            InputStream is = config.getObjectInputStream(dto.getPatternPath4Minio());
            // 解析花样文件
            UniParser parser = new UniParser();
            // doParse 里自动处理 厂商相关信息
            UniPattern uniPattern = parser.doParse(is);
            // 设置宽高信息
            dto.setHeight(uniPattern.getHeight());
            dto.setWidth(uniPattern.getWidth());
            // 插入创建时间、更新时间信息
            dto.setCreateTime(DateUtil.date());
            dto.setUpdateTime(DateUtil.date());

            int i = this.shoesPatternMapper.create(dto);
            return i == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void update(ShoesPatternUpdateVO vo) {
        PatternDto patternDto = this.shoesPatternMapper.findPatternById(vo.getShoesPatternId());
        String patternDataUrl = patternDto.getPatternPath4Minio();
        try {
            MinioClient minioClient = new MinioClient(MinioConfig.MINIO_DOMAIN, MINIO_ACCESS_KEY, MINIO_SECRET_KEY);
            // 下载数据文件
            InputStream is = minioClient.getObject(MinioConfig.PATTERN_SYSTEM_BUCKET, patternDataUrl);
            UniPattern uniPattern = null;
            int val = patternDto.getVendor().getValue();
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

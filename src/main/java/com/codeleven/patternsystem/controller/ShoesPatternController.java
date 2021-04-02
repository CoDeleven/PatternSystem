package com.codeleven.patternsystem.controller;

import com.codeleven.patternsystem.common.HttpResponse;
import com.codeleven.patternsystem.dto.PatternDto;
import com.codeleven.patternsystem.service.ShoesPatternService;
import com.codeleven.patternsystem.utils.MinioUtil;
import com.codeleven.patternsystem.vo.PatternCreateVO;
import com.codeleven.patternsystem.vo.PatternDetailVO;
import com.codeleven.patternsystem.vo.SectionListVO;
import com.codeleven.patternsystem.vo.ShoesPatternUpdateVO;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RestController
public class ShoesPatternController {

    @Autowired
    private ShoesPatternService shoesPatternService;

    @ApiOperation(value = "获取花样列表")
    @GetMapping("/pattern/list/{page}/{length}")
    public HttpResponse<List<SectionListVO>> list(
            @RequestBody(required = false) Map<String, Object> param,
            @PathVariable("page") int page, @PathVariable("length") int length) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException, InvalidExpiresRangeException {
        List<SectionListVO> sectionList = shoesPatternService.queryForPage(page, length, param);
        return new HttpResponse<>("ok", 0, sectionList);
    }

    @ApiOperation(value = "创建花样")
    @PostMapping("/pattern/create")
    public HttpResponse<Boolean> create(@RequestBody PatternCreateVO vo) {
        boolean success = shoesPatternService.create(vo);
        return new HttpResponse<>("ok", 0, success);
    }

    @ApiOperation(value = "获取花样详情")
    @GetMapping("/pattern/detail/{id}")
    public HttpResponse<PatternDetailVO> detail(@PathVariable("id") int patternId) {
        PatternDetailVO detail = shoesPatternService.detail(patternId);
        return new HttpResponse<>("ok", 0, detail);
    }

    @ApiOperation(value = "更新花样")
    @PostMapping("/pattern/update")
    public HttpResponse<Boolean> update(@RequestBody ShoesPatternUpdateVO vo) {
        shoesPatternService.update(vo);
        return new HttpResponse<>("ok", 0, true);
    }

    @ApiOperation(value = "下载花样")
    @GetMapping("/pattern/data")
    public void data(@RequestParam("pattern_path") String patternPath, HttpServletResponse response) {
        InputStream is = MinioUtil.getPatternData(patternPath);
        byte[] bytes = new byte[1024];
        int len = -1;
        try {
            response.setCharacterEncoding("UTF-8");
            while (((len = is.read(bytes)) != -1)) {
                response.getOutputStream().write(bytes, 0, len);
            }
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.codeleven.patternsystem.controller;

import com.codeleven.patternsystem.common.AjaxResult;
import com.codeleven.patternsystem.common.MessageType;
import com.codeleven.patternsystem.dto.Page;
import com.codeleven.patternsystem.dto.ShoesPatternDto;
import com.codeleven.patternsystem.service.ShoesPatternService;
import com.codeleven.patternsystem.vo.ShoesPatternVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ShoesPatternController {

    @Autowired
    private ShoesPatternService shoesPatternService;

    @GetMapping("/pattern/list/{page}/{length}")
    public Page<ShoesPatternDto> list(
            @RequestBody(required = false) Map<String, Object> param,
            @PathVariable("page") int page, @PathVariable("length") int length) {
        Page<ShoesPatternDto> shoesPatternDtoPage = shoesPatternService.queryForPage(page, length, param);
        return shoesPatternDtoPage;
    }

    @PostMapping("/pattern/create")
    public AjaxResult create(@RequestBody ShoesPatternVO vo) {
        boolean success = shoesPatternService.create(vo);

        return new AjaxResult("ok", MessageType.CONFIRM.getCode(), null);
    }
}

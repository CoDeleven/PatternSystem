package com.codeleven.patternsystem.controller;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.codeleven.patternsystem.common.HttpResponse;
import com.codeleven.patternsystem.common.ShoesSize;
import com.codeleven.patternsystem.dto.PatternDto;
import com.codeleven.patternsystem.parser.PatternSystemVendor;
import com.codeleven.patternsystem.vo.PatternCreateVO;
import com.codeleven.patternsystem.vo.SectionListVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class PatternControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();  //构造MockMvc
    }

    @Test
    public void testListWithSection() throws Exception {
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/pattern/list/0/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Type type = new TypeReference<HttpResponse<List<SectionListVO>>>() {
        }.getType();
        HttpResponse<List<SectionListVO>> response = JSONUtil.toBean(contentAsString, type, false);

        Assertions.assertEquals(0, response.getErrorCode());
        Assertions.assertNotEquals(0, response.getData().size());
    }

    @Test
    public void testCreate() throws Exception {
        PatternCreateVO createVO = new PatternCreateVO();
        createVO.setName("测试花样");
        createVO.setVendor(PatternSystemVendor.SYSTEM_TOP.getValue());
        createVO.setSize(ShoesSize.SHOES_SIZE_38.getSize());
        createVO.setPatternDataPath("pattern-file/1616339844409-002.NPT");
        createVO.setCoverPath("cover-file/1616339839644-img047.jpg");

        String contentJson = JSONUtil.toJsonStr(createVO);

        String contentAsString = mockMvc.perform(
                MockMvcRequestBuilders.post("/pattern/create").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Type type = new TypeReference<HttpResponse<Boolean>>() {
        }.getType();
        HttpResponse<Boolean> response = JSONUtil.toBean(contentAsString, type, false);

        Assertions.assertTrue(response.getData());
        Assertions.assertEquals(0, response.getErrorCode());
    }

    @Test
    public void detail() throws Exception {
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/pattern/detail/8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Type type = new TypeReference<HttpResponse<PatternDto>>() {
        }.getType();
        HttpResponse<PatternDto> response = JSONUtil.toBean(contentAsString, type, false);

        Assertions.assertEquals(0, response.getErrorCode());
        Assertions.assertEquals(8, response.getData().getId());
    }
}

package com.codeleven.patternsystem.controller;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.codeleven.patternsystem.common.HttpResponse;
import com.codeleven.patternsystem.common.ShoesSize;
import com.codeleven.patternsystem.dto.PatternDto;
import com.codeleven.patternsystem.parser.transform.TransformReceiver;
import com.codeleven.patternsystem.vo.CommandVO;
import com.codeleven.patternsystem.vo.PatternCreateVO;
import com.codeleven.patternsystem.vo.SectionListVO;
import com.codeleven.patternsystem.vo.ShoesPatternUpdateVO;
import org.junit.Assert;
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
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class PatternControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    private static PatternCreateVO genPatternCreateVO() {
        PatternCreateVO createVO = new PatternCreateVO();
        createVO.setName("测试花样");
        createVO.setSize(ShoesSize.SHOES_SIZE_38.getSize());
        createVO.setPatternDataPath("pattern-file/Drawing3.dxf");
        createVO.setCoverPath("cover-file/1616339839644-img047.jpg");
        return createVO;
    }

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
        PatternCreateVO createVO = genPatternCreateVO();

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
    public void testDetail() throws Exception {
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/pattern/detail/9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Type type = new TypeReference<HttpResponse<PatternDto>>() {
        }.getType();
        HttpResponse<PatternDto> response = JSONUtil.toBean(contentAsString, type, false);

        Assertions.assertEquals(0, response.getErrorCode());
        Assertions.assertEquals(9, response.getData().getId());
    }

    @Test
    public void testShoesPatternUpdate() throws Exception {
        ShoesPatternUpdateVO vo = new ShoesPatternUpdateVO();
        vo.setShoesPatternId(9);
        List<CommandVO> operationList = new ArrayList<>();
        CommandVO operation = new CommandVO(TransformReceiver.Operation.MOVE_X, 1, 20);
        operationList.add(operation);
        vo.setPatternUpdateOperationList(operationList);

        String json = JSONUtil.toJsonStr(vo);

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.post("/pattern/update")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Type type = new TypeReference<HttpResponse<Boolean>>() {
        }.getType();

        HttpResponse<Boolean> response = JSONUtil.toBean(contentAsString, type, false);

        Assert.assertNotNull(contentAsString);
        Assert.assertEquals(response.getMessage(), "ok");
    }
}

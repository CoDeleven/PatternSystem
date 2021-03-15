package com.codeleven.patternsystem.systemtop;

import com.codeleven.patternsystem.common.AjaxResult;
import com.codeleven.patternsystem.common.ShoesSize;
import com.codeleven.patternsystem.controller.ShoesPatternController;
import com.codeleven.patternsystem.parser.PatternSystemVendor;
import com.codeleven.patternsystem.vo.ShoesPatternVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SystemTopCreateTest {
    private static final String PATH = "/pattern/create";

    @LocalServerPort
    private int port;

    @Autowired
    private ShoesPatternController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void controllerExistTest(){
        assert controller != null;
    }

    @Test
    public void shoesPatternCreateTest() {
        ShoesPatternVO vo = new ShoesPatternVO();
        vo.setName("test");
        vo.setVendor(PatternSystemVendor.SYSTEM_TOP.getValue());
        vo.setSize(ShoesSize.SHOES_SIZE_36.getValue());
        vo.setPatternDataPath("pattern-file/002.NPT");
        vo.setCoverPath("cover-file/img032.jpg");
        AjaxResult ajaxResult = this.restTemplate.postForObject("http://localhost:" + port + PATH, vo, AjaxResult.class);
        Assert.assertNotNull(ajaxResult);
        Assert.assertEquals(ajaxResult.getMessage(), "ok");
    }
}

package com.codeleven.patternsystem.dao;

import com.codeleven.patternsystem.dto.PatternChildPO;
import com.codeleven.patternsystem.dto.UniPatternPO;
import com.codeleven.patternsystem.dto.UniPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class ShoesPatternMapperTest {
    private UniPatternPO uniPatternPO;
    private PatternChildPO childDto;

    @Before
    public void setUp(){
        uniPatternPO = new UniPatternPO();

        uniPatternPO.setName("test" + System.currentTimeMillis());
        uniPatternPO.setWidth(30);
        uniPatternPO.setHeight(50);
        uniPatternPO.setShoesSize(360);
        uniPatternPO.setCoverPath("testCoverPath");
        uniPatternPO.setDxfPath("dxfPath");
        uniPatternPO.setCreateTime(new Date());
        uniPatternPO.setUpdateTime(new Date());
        uniPatternPO.setRefOrigin(new UniPoint(99, 99));
        uniPatternPO.setOffsetX(11);
        uniPatternPO.setOffsetY(22);

        childDto = new PatternChildPO();
        childDto.setPatternId(1);
        childDto.setWeight(10);
        childDto.setPatternDataJson("testJsonData");
    }

    @Autowired
    ShoesPatternMapper mapper;

    @Test
    public void queryForPage() {
        List<UniPatternPO> uniPatternPOS = mapper.queryForPage(new HashMap<>());
        assert uniPatternPOS.size() > 0;
        for (UniPatternPO patternDto : uniPatternPOS) {
            assert patternDto.getId() != 0;
        }
    }

    @Test
    public void create() {
        int i = mapper.create(uniPatternPO);
        assert i == 1;
    }

    @Test
    public void update() {
    }

    @Test
    public void findPatternById() {
        UniPatternPO dto = mapper.findPatternById(1);
        System.out.println(dto);
    }

    @Test
    public void createChild() {
//        int child = mapper.createChild(childDto);
//        assert child == 1;
    }
}
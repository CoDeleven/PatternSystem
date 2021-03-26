package com.codeleven.patternsystem;

import cn.hutool.core.util.HexUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class PatternSystemApplicationTests {

    @Test
    void contextLoads() {
        short temp = (short)0xffeb;
        System.out.println(temp);
    }


    @Test
    void contextLoads2() {
        short temp = (short)-16;
        System.out.println(HexUtil.toHex(temp));
    }
}

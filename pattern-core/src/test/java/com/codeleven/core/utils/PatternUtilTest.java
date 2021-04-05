package com.codeleven.core.utils;

import com.codeleven.common.entity.UniFrame;
import com.codeleven.core.data.LogicTestData;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static com.codeleven.core.data.LogicTestData.initData;

public class PatternUtilTest {

    @Test
    public void testLockStart(){
        List<UniFrame> init = initData();
        List<UniFrame> result = LogicTestData.afterLockStartForLen4Count4();

        PatternUtil.lockStart(init, 4, 4);
        for (int i = 0; i < init.size(); i++) {
            UniFrame foo = result.get(i);
            UniFrame bar = init.get(i);
            Assert.assertEquals(foo.getX(), bar.getX());
            Assert.assertEquals(foo.getY(), bar.getY());
            Assert.assertEquals(foo.getControlCode(), bar.getControlCode());
        }
    }

    @Test
    public void testLockEnd(){
        List<UniFrame> init = initData();
        List<UniFrame> result = LogicTestData.afterLockEndForLen4Count4();

        PatternUtil.lockEnd(init, 4, 4);
        for (int i = 0; i < init.size(); i++) {
            UniFrame foo = result.get(i);
            UniFrame bar = init.get(i);
            Assert.assertEquals(foo.getX(), bar.getX());
            Assert.assertEquals(foo.getY(), bar.getY());
            Assert.assertEquals(foo.getControlCode(), bar.getControlCode());
        }

    }
}

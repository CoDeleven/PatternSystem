package com.codeleven.core.utils;

import com.codeleven.common.entity.UniFrame;
import com.codeleven.core.data.LogicTestData;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static com.codeleven.core.data.LogicTestData.*;

public class PatternUtilTest {

    @Test
    public void testLockStart(){
        List<UniFrame> init = initData();
        List<UniFrame> result = LogicTestData.afterLockStartForLen4Count4();

        PatternLockUtil.lockStart(init, 4, 4);
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

        PatternLockUtil.lockEnd(init, 4, 4);
        for (int i = 0; i < init.size(); i++) {
            UniFrame foo = result.get(i);
            UniFrame bar = init.get(i);
            Assert.assertEquals(foo.getX(), bar.getX());
            Assert.assertEquals(foo.getY(), bar.getY());
            Assert.assertEquals(foo.getControlCode(), bar.getControlCode());
        }
    }

    @Test
    public void testLockJoin(){
        List<UniFrame> init = initData4Join();
        List<UniFrame> result = LogicTestData.afterLockJoin();

        PatternLockUtil.lockJoin(init);
        for (int i = 0; i < init.size(); i++) {
            UniFrame foo = result.get(i);
            UniFrame bar = init.get(i);
            Assert.assertEquals(foo.getX(), bar.getX());
            Assert.assertEquals(foo.getY(), bar.getY());
            Assert.assertEquals(foo.getControlCode(), bar.getControlCode());
        }
    }

    @Test
    public void testLockJoin2(){
        List<UniFrame> init = initData4Join2();
        List<UniFrame> result = LogicTestData.afterLockJoin2();

        PatternLockUtil.lockJoin(init);
        for (int i = 0; i < init.size(); i++) {
            UniFrame foo = result.get(i);
            UniFrame bar = init.get(i);
            Assert.assertEquals(foo.getX(), bar.getX());
            Assert.assertEquals(foo.getY(), bar.getY());
            Assert.assertEquals(foo.getControlCode(), bar.getControlCode());
        }
    }
}

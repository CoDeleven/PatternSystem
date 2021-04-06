package com.codeleven.core.common;

import cn.hutool.core.util.RandomUtil;
import com.codeleven.common.constants.SystemTopControlCode;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;

import java.util.*;

public class TransformData {
    /**
     * 初始数据
     * @return
     */
    public static UniPattern normalFrames(){
        UniPattern uniPattern = new UniPattern();

        long l = RandomUtil.randomLong();
        UniChildPattern childPattern = new UniChildPattern();
        childPattern.setId(l);
        Map<Long, UniChildPattern> childs = new HashMap<>();
        childs.put(l, childPattern);

        uniPattern.setChildList(childs);

        List<UniFrame> frames = new ArrayList<>();
        frames.add(new UniFrame(-213, 159, 27));
        frames.add(new UniFrame(-219, 122, 97));
        frames.add(new UniFrame(-224, 85, 97));
        frames.add(new UniFrame(-230, 47, 97));
        frames.add(new UniFrame(-224, 85, 97));
        frames.add(new UniFrame(-219, 122, 97));
        frames.add(new UniFrame(-213, 159, 97));
        frames.add(new UniFrame(-219, 122, 97));
        frames.add(new UniFrame(-224, 85, 97));
        frames.add(new UniFrame(-229, 47, 97));
        frames.add(new UniFrame(-229, 47, 2));
        frames.add(new UniFrame(-229, 47, 4));
        childPattern.setPatternData(frames);


        return uniPattern;
    }

    /**
     * 首位交换后的数据
     * @return
     */
    public static List<UniFrame> changeStartEndFrames(){

        List<UniFrame> frames = new ArrayList<>();
        frames.add(new UniFrame(-229, 47, 27));
        frames.add(new UniFrame(-224, 85, 97));
        frames.add(new UniFrame(-219, 122, 97));
        frames.add(new UniFrame(-213, 159, 97));
        frames.add(new UniFrame(-219, 122, 97));
        frames.add(new UniFrame(-224, 85, 97));
        frames.add(new UniFrame(-230, 47, 97));
        frames.add(new UniFrame(-224, 85, 97));
        frames.add(new UniFrame(-219, 122, 97));
        frames.add(new UniFrame(-213, 159, 97));
        frames.add(new UniFrame(-213, 159, 2));
        frames.add(new UniFrame(-213, 159, 4));


        return frames;
    }

    /**
     * 讲 normalFrames() 的下标为 6 的数据设置为起始点
     * @return
     */
    public static List<UniFrame> afterChangeFirstSewingFrame(){
        List<UniFrame> frames = new ArrayList<>();
        frames.add(new UniFrame(-213, 159, 27)); // 从这里挪到前面
        frames.add(new UniFrame(-219, 122, 97));
        frames.add(new UniFrame(-224, 85, 97));
        frames.add(new UniFrame(-229, 47, 97));
        frames.add(new UniFrame(-213, 159, 97));
        frames.add(new UniFrame(-219, 122, 97));
        frames.add(new UniFrame(-224, 85, 97));
        frames.add(new UniFrame(-230, 47, 97));
        frames.add(new UniFrame(-224, 85, 97));
        frames.add(new UniFrame(-219, 122, 97));
        frames.add(new UniFrame(-219, 122, 2));
        frames.add(new UniFrame(-219, 122, 4));
        return frames;
    }

    public static UniPattern initPatternPositionList(){

        UniChildPattern childPattern1 = new UniChildPattern();
        List<UniFrame> child1 = new ArrayList<>();
        child1.add(new UniFrame(0,0,0));
        child1.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));
        childPattern1.setPatternData(child1);
        childPattern1.setWeight(Byte.MAX_VALUE);

        UniChildPattern childPattern2 = new UniChildPattern();
        List<UniFrame> child2 = new ArrayList<>();
        child2.add(new UniFrame(0,0,0));
        child2.add(new UniFrame(0,0,0));
        child2.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));
        childPattern2.setPatternData(child2);
        childPattern2.setWeight(Byte.MAX_VALUE - 1);

        UniChildPattern childPattern3 = new UniChildPattern();
        List<UniFrame> child3 = new ArrayList<>();
        child3.add(new UniFrame(0,0,0));
        child3.add(new UniFrame(0,0,0));
        child3.add(new UniFrame(0,0,0));
        child3.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));
        childPattern3.setPatternData(child3);
        childPattern3.setWeight(Byte.MAX_VALUE - 2);

        UniChildPattern childPattern4 = new UniChildPattern();
        List<UniFrame> child4 = new ArrayList<>();
        child4.add(new UniFrame(0,0,0));
        child4.add(new UniFrame(0,0,0));
        child4.add(new UniFrame(0,0,0));
        child4.add(new UniFrame(0,0,0));
        child4.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));
        childPattern4.setPatternData(child4);
        childPattern4.setWeight(Byte.MAX_VALUE - 3);


        Map<Long, UniChildPattern> childs = new HashMap<>();
        childs.put(RandomUtil.randomLong(), childPattern1);
        childs.put(RandomUtil.randomLong(), childPattern2);
        childs.put(RandomUtil.randomLong(), childPattern3);
        childs.put(RandomUtil.randomLong(), childPattern4);

        UniPattern uniPattern = new UniPattern();
        uniPattern.setChildList(childs);
        return uniPattern;
    }

    public static List<UniFrame> changePatternPositionPatternList42(){
        List<UniFrame> child1 = new ArrayList<>();
        child1.add(new UniFrame(0,0,0));
        List<UniFrame> child2 = new ArrayList<>();
        child2.add(new UniFrame(0,0,0));
        child2.add(new UniFrame(0,0,0));
        List<UniFrame> child3 = new ArrayList<>();
        child3.add(new UniFrame(0,0,0));
        child3.add(new UniFrame(0,0,0));
        child3.add(new UniFrame(0,0,0));
        List<UniFrame> child4 = new ArrayList<>();
        child4.add(new UniFrame(0,0,0));
        child4.add(new UniFrame(0,0,0));
        child4.add(new UniFrame(0,0,0));
        child4.add(new UniFrame(0,0,0));

        List<UniFrame> result = new ArrayList<>();
        result.addAll(child1);
        result.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));
        result.addAll(child4);
        result.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));
        result.addAll(child2);
        result.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));
        result.addAll(child3);
        result.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));

        return result;
    }

    public static List<UniFrame> changePatternPositionPatternList13(){
        List<UniFrame> child1 = new ArrayList<>();
        child1.add(new UniFrame(0,0,0));
        List<UniFrame> child2 = new ArrayList<>();
        child2.add(new UniFrame(0,0,0));
        child2.add(new UniFrame(0,0,0));
        List<UniFrame> child3 = new ArrayList<>();
        child3.add(new UniFrame(0,0,0));
        child3.add(new UniFrame(0,0,0));
        child3.add(new UniFrame(0,0,0));
        List<UniFrame> child4 = new ArrayList<>();
        child4.add(new UniFrame(0,0,0));
        child4.add(new UniFrame(0,0,0));
        child4.add(new UniFrame(0,0,0));
        child4.add(new UniFrame(0,0,0));

        List<UniFrame> result = new ArrayList<>();
        result.addAll(child2);
        result.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));
        result.addAll(child3);
        result.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));
        result.addAll(child1);
        result.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));
        result.addAll(child4);
        result.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));

        return result;
    }
}

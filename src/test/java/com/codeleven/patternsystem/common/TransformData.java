package com.codeleven.patternsystem.common;

import com.codeleven.patternsystem.entity.UniFrame;
import com.codeleven.patternsystem.parser.systemtop.SystemTopControlCode;

import java.util.ArrayList;
import java.util.List;

public class TransformData {
    /**
     * 初始数据
     * @return
     */
    public static List<UniFrame> normalFrames(){
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
        return frames;
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

    public static List<UniFrame> initPatternPositionList(){
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
        result.addAll(child2);
        result.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));
        result.addAll(child3);
        result.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));
        result.addAll(child4);
        result.add(new UniFrame(0,0, SystemTopControlCode.CUT.getCode()));

        return result;
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

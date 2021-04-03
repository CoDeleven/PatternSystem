package com.codeleven.parser.utils;

import com.aspose.cad.fileformats.cad.cadobjects.Cad2DPoint;
import com.aspose.cad.fileformats.cad.cadobjects.CadLwPolyline;
import com.aspose.cad.fileformats.cad.cadobjects.ICadBaseEntity;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.parser.shangyi.SystemTopControlCode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PatternUtil {

    /**
     * 复制花样最后一帧，并复制一个全新的针，追加在尾部，修改这个新针的控制码
     *
     * @param pattern  花样
     * @param controlCode 控制码
     */
//    public static void appendLastFrameWithDiffControlCode(UniPattern pattern, SystemTopControlCode controlCode){
//        List<UniFrame> frames = pattern.getFrames();
//        UniFrame lastFrame = frames.get(frames.size() - 1);
//
//        UniFrame appendFrame = lastFrame.copyFrame();
//        appendFrame.setControlCode(controlCode.getCode());
//
//        appendFrameToLast(pattern, appendFrame);
//    }
//
//    public static void appendFrameToLast(UniPattern pattern, UniFrame frame){
//        pattern.getFrames().add(frame);
//        incPatternFramesNumber(pattern, 1);
//    }
//
//    public static void joinChildPattern(ChildPattern child, UniPattern pattern){
//        // 如果不存在childPattern列表，则新增一个
//        if(pattern.getChildPatterns() == null){
//            pattern.setChildPatterns(new ArrayList<>());
//        }
//        // 如果不存在Frames列表，则新建一个
//        if(pattern.getFrames() == null){
//            pattern.setFrames(new ArrayList<>());
//        }
//        // 添加child到 childPattern 列表
//        pattern.getChildPatterns().add(child);
//        // 添加child的Frames 到 UniPattern的Frames里
//        pattern.getFrames().addAll(child.getFrameList());
//        // 增加FrameCount
//        incPatternFramesNumber(pattern, child.getFrameCount());
//    }
//
//    public static void incPatternFramesNumber(UniPattern pattern, int step){
//        int frameCount = pattern.getFrameNumber();
//        frameCount += step;
//        pattern.setFrameNumber(frameCount);
//    }
//
    public static List<UniFrame> mergeChildPattern(List<UniChildPattern> childPatterns){
        List<UniFrame> totalFrames = new ArrayList<>();
        for (UniChildPattern childPattern : childPatterns) {
            totalFrames.addAll(childPattern.getPatternData());
        }
        return totalFrames;
    }

    public static List<UniFrame> convertChildPattern(ICadBaseEntity entity) {
        List<UniFrame> frames = new LinkedList<>();
        boolean isFirst = true;
        SystemTopControlCode controlCode;
        if(entity instanceof CadLwPolyline) {
            List<Cad2DPoint> coordinates = ((CadLwPolyline) entity).getCoordinates();
            for (Cad2DPoint coordinate : coordinates) {
                if(isFirst){
                    controlCode = SystemTopControlCode.SKIP;
                    isFirst = false;
                } else {
                    controlCode = SystemTopControlCode.HIGH_SEWING;
                }
                frames.add(new UniFrame((int)(coordinate.getX() * 10), (int)(coordinate.getY() * 10), controlCode.getCode()));
            }
            UniFrame lastFrame = frames.get(frames.size() - 1).copyFrame();
            lastFrame.setControlCode(SystemTopControlCode.CUT.getCode());
            frames.add(lastFrame);
        }else {
            throw new RuntimeException("暂不支持其他类型...");
        }
        return frames;
    }
}

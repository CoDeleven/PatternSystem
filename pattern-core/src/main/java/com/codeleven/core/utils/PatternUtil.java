package com.codeleven.core.utils;

import cn.hutool.core.collection.ListUtil;
import com.aspose.cad.fileformats.cad.cadobjects.Cad2DPoint;
import com.aspose.cad.fileformats.cad.cadobjects.Cad3DPoint;
import com.aspose.cad.fileformats.cad.cadobjects.CadLwPolyline;
import com.aspose.cad.fileformats.cad.cadobjects.ICadBaseEntity;
import com.aspose.cad.fileformats.cad.cadobjects.polylines.CadPolyline;
import com.aspose.cad.fileformats.cad.cadobjects.vertices.Cad3DVertex;
import com.codeleven.common.constants.LockMethod;
import com.codeleven.common.constants.TransformOperation;
import com.codeleven.common.entity.*;
import com.codeleven.common.constants.SystemTopControlCode;
import com.codeleven.core.TransformHelper;
import com.codeleven.core.transform.PatternTransformHelper;
import com.codeleven.core.transform.TransformReceiver;
import com.codeleven.core.transform.command.ChangeFirstSewingCommand;
import com.codeleven.core.transform.command.ITransformCommand;

import java.util.*;
import java.util.stream.Collectors;

public class PatternUtil {

//    /**
//     * 复制花样最后一帧，并复制一个全新的针，追加在尾部，修改这个新针的控制码
//     *
//     * @param pattern  花样
//     * @param controlCode 控制码
//     */
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

    public static boolean isFengBiShape(UniChildPattern childPattern){
        UniFrame firstFrame = childPattern.getPatternData().get(0);
        UniFrame lastFrame = childPattern.getPatternData().get(childPattern.getPatternData().size() - 1);

        return firstFrame.getX() == lastFrame.getX() && firstFrame.getY() == lastFrame.getY();
    }

    /**
     * 移除相同的Frame，仅留下一个。（存在功能码和车缝时，仅保留车缝）
     */
    public static void removeSameCoordinatorFrameForSewing(List<UniFrame> frameList){
        frameList.remove(frameList.size() - 1);
//        UniFrame lastFrame = null;
//        int endIndex = -1;
//        for (int i = 0; i < frameList.size(); i++) {
//            UniFrame cur = frameList.get(i);
//            if(lastFrame == null){
//                lastFrame = cur;
//                continue;
//            }
//
//            if(cur.equals(lastFrame)){
//                endIndex = i;
//                break;
//            }
//            lastFrame = cur;
//        }
//        if(endIndex == -1){
//            return;
//        }
//        int initSize = frameList.size();
//        for (int i = endIndex; i < initSize; i++) {
//            frameList.remove(endIndex);
//        }
    }

    public static boolean isOriginalPoint(UniFrame frame){
        return frame.getX() == 0 && frame.getY() == 0;
    }

    public static List<UniFrame> getLastControlCodeFrames(List<UniFrame> frameList){
        int lastSewingFrameIndex = PatternPointUtil.getLastSewingFrameIndex(frameList);
        List<UniFrame> result = new ArrayList<>();
        for (int i = lastSewingFrameIndex + 1; i < frameList.size(); i++) {
            result.add( frameList.get(i) );
        }
        return result;
    }

    public static void incWeight(UniChildPattern childPattern, int step){
        int weight = childPattern.getWeight();
        childPattern.setWeight(weight + step);
    }

    public static void decWeight(UniChildPattern childPattern, int step){
        int weight = childPattern.getWeight();
        childPattern.setWeight(weight - step);
    }


    public static List<UniChildPattern> sortChildPatternByWeight(Collection<UniChildPattern> childPatterns) {
        return  childPatterns.stream()
                .sorted((o1, o2) -> o2.getWeight() - o1.getWeight())
                .collect(Collectors.toList());
    }

    public static UniChildPattern getChildPatternBySortedIndex(List<UniChildPattern> childPatterns, int index){
        List<UniChildPattern> collect = sortChildPatternByWeight(childPatterns);
        return collect.get(index);
    }

    public static UniChildPattern getChildPatternBySortedIndex(UniPattern pattern, int index){
        return getChildPatternBySortedIndex(new ArrayList<>(pattern.getChildList().values()), index);
    }

    public static List<UniFrame> mergeChildPattern(List<UniChildPattern> childPatterns){
        List<UniChildPattern> collect = sortChildPatternByWeight(childPatterns);

        List<UniFrame> totalFrames = new ArrayList<>();
        for (UniChildPattern childPattern : collect) {
            totalFrames.addAll(childPattern.getPatternData());
        }
        return totalFrames;
    }

    public static List<UniFrame> mergeChildPattern(UniPattern pattern){
        List<UniChildPattern> childPatterns = ListUtil.of(pattern.getChildList().values().toArray(new UniChildPattern[]{}));

        List<UniChildPattern> collect = sortChildPatternByWeight(childPatterns);

        List<UniFrame> totalFrames = new ArrayList<>();
        for (UniChildPattern childPattern : collect) {
            totalFrames.addAll(childPattern.getPatternData());
        }
        return totalFrames;
    }

    public static List<UniFrame> findAllSkipFrames(UniPattern pattern){
        List<UniFrame> skipFrames = new ArrayList<>();
        for (int i = 0; i < pattern.getChildList().size(); i++) {
            UniChildPattern child = getChildPatternBySortedIndex(pattern, i);
            UniFrame firstSkipFrame = getFirstSkipFrame(child.getPatternData());
            UniFrame lastCutFrame = child.getPatternData().get(child.getPatternData().size() - 1);
            if(firstSkipFrame != null){
                skipFrames.add(firstSkipFrame);
                // 一般是剪线
                UniFrame genSkipFrame = lastCutFrame.copyFrame();
                skipFrames.add(genSkipFrame);
            }
        }
        return skipFrames;
    }

    public static UniPattern generateSkipFrameWithSingleSewing(UniPattern entirePattern){
        UniPattern pattern = new UniPattern();
        List<UniFrame> allSkipFrames = findAllSkipFrames(entirePattern);
        List<UniFrame> result = new ArrayList<>();
        for (int i = 0; i < allSkipFrames.size() - 1;i++) {
            if(i == 0){
                result.addAll(handleSkipFrames(null, allSkipFrames.get(i)));
            } else {
                result.addAll(handleSkipFrames(allSkipFrames.get(i), allSkipFrames.get(i + 1)));
            }
        }

        UniChildPattern childPattern = new UniChildPattern();
        childPattern.setPatternData(result);
        childPattern.setLockMethod(LockMethod.NONE);
        childPattern.setPattern(pattern);

        Map<Long, UniChildPattern> uniChildPatternMap = new HashMap<>();
        uniChildPatternMap.put(new Random().nextLong(), childPattern);
        pattern.setChildList(uniChildPatternMap);

        return pattern;
    }

    private static List<UniFrame> handleSkipFrames(UniFrame f1, UniFrame f2) {
        List<UniFrame> frames = new ArrayList<>();
        if(f1 == null){
            f1 = new UniFrame(0, 0, SystemTopControlCode.CUT.code);
        }
        if(f1.getControlCode() == SystemTopControlCode.CUT.code && f2.getControlCode() == SystemTopControlCode.SKIP.code) {
            if(f1.getX() != 0 && f1.getY() != 0){
                // 处理第一个点是 原点得情况
                frames.add(new UniFrame(f1.getX(), f1.getY(), SystemTopControlCode.SKIP.code));
            }
            // 处理第一个点是 原点得情况
            frames.add(new UniFrame(f1.getX(), f1.getY(), SystemTopControlCode.DROP_NEEDLE.code));
            // 跳缝
            frames.add(new UniFrame(f2.getX(), f1.getY(), SystemTopControlCode.SKIP.code));
            // 中间点
            frames.add(new UniFrame(f2.getX(), f1.getY(), SystemTopControlCode.DROP_NEEDLE.code));
            // 跳缝
            frames.add(new UniFrame(f2.getX(), f2.getY(), SystemTopControlCode.SKIP.code));
            // 终点
            frames.add(new UniFrame(f2.getX(), f2.getY(), SystemTopControlCode.DROP_NEEDLE.code));
            // 剪线
            frames.add(new UniFrame(f2.getX(), f2.getY(), SystemTopControlCode.CUT.code));

            System.out.println("deltY:" + (f2.getY() - f1.getY()) + ", deltX:" + (f2.getX() -f1.getX()));
        }
        return frames;
    }

    public static UniFrame getFirstSkipFrame(List<UniFrame> frames){
        for (int i = 0; i < frames.size(); i++) {
            if(SystemTopControlCode.isSkipControlCode(frames.get(i).getControlCode())){
                return frames.get(i);
            }
        }
        return null;
    }

    public static int getFirstSewingIndex(List<UniFrame> frames){
        for (int i = 0; i < frames.size(); i++) {
            if(SystemTopControlCode.isSewingControlCode(frames.get(i).getControlCode())){
                return i;
            }
        }
        return -1;
    }

    public static List<UniFrame> convertChildPattern(ICadBaseEntity entity) {
        List<UniFrame> frames = new LinkedList<>();
        boolean isFirst = true;
        SystemTopControlCode controlCode;
        if(entity instanceof CadLwPolyline) {
            List<Cad2DPoint> coordinates = ((CadLwPolyline) entity).getCoordinates();
            for (Cad2DPoint coordinate : coordinates) {
                if(isFirst){
                    if(coordinate.getX() == 0 && coordinate.getY() == 0){
                        controlCode = SystemTopControlCode.HIGH_SEWING;
                    } else {
                        controlCode = SystemTopControlCode.SKIP;
                    }

                    isFirst = false;
                } else {
                    controlCode = SystemTopControlCode.HIGH_SEWING;
                }
                frames.add(new UniFrame((int)(coordinate.getX() * 10), (int)(coordinate.getY() * 10), controlCode.getCode()));
            }

        } else if(entity instanceof CadPolyline) {
            List<ICadBaseEntity> vertexList = entity.getChildObjects();
            for (ICadBaseEntity vertex : vertexList) {
                if(vertex instanceof Cad3DVertex) {
                    Cad3DPoint locationPoint = ((Cad3DVertex) vertex).getLocationPoint();
                    if(isFirst){
                        if(locationPoint.getX() == 0 && locationPoint.getY() == 0){
                            controlCode = SystemTopControlCode.HIGH_SEWING;
                        } else {
                            controlCode = SystemTopControlCode.SKIP;
                        }
                        isFirst = false;
                    } else {
                        controlCode = SystemTopControlCode.HIGH_SEWING;
                    }
                    frames.add(new UniFrame((int)(locationPoint.getX() * 10), (int)(locationPoint.getY() * 10), controlCode.getCode()));
                }
            }
        }
        else {
            throw new RuntimeException("暂不支持其他类型...");
        }

        UniFrame lastFrame = frames.get(frames.size() - 1).copyFrame();
        lastFrame.setControlCode(SystemTopControlCode.CUT.getCode());
        frames.add(lastFrame);
        return frames;
    }

    public static List<UniFrame> copyList(List<UniFrame> old){
        List<UniFrame> result = new ArrayList<>();
        for (UniFrame uniFrame : old) {
            result.add(uniFrame.copyFrame());
        }
        return result;
    }

    public static void changeWeightByLRTB(UniPattern uniPattern) {
        List<UniChildPattern> childs = sortChildPatternByWeight(uniPattern.getChildList().values());
        Map<UniPoint, UniChildPattern> waitChangeMap = new HashMap<>();
        // 收集每个子花样的最左上的点
        for (UniChildPattern child : childs) {
            UniPoint result;
            int minX = Integer.MAX_VALUE;
            int maxY = Integer.MIN_VALUE;
            int index = 0;
            for (int i = 0; i < child.getPatternData().size(); i++) {
                UniFrame patternDatum = child.getPatternData().get(i);
                if(patternDatum.getX() < minX && patternDatum.getY() > maxY){
                    minX = patternDatum.getX();
                    maxY = patternDatum.getY();
                    index = i;
                }
            }

            result = new UniPoint(minX, maxY);
            result.setIndex(index);
            waitChangeMap.put(result, child);
        }

        List<UniPoint> collect = waitChangeMap.keySet().stream().sorted((o1, o2) -> computeWeight(o2) - computeWeight(o1)).collect(Collectors.toList());

        int weight = Byte.MAX_VALUE;
        // 更新每个子花样的起缝点
        PatternTransformHelper helper = new PatternTransformHelper(uniPattern);
        for (UniPoint uniPoint : collect) {
            UniChildPattern uniChildPattern = waitChangeMap.get(uniPoint);
            PatternTransformCommand transformCommand = new PatternTransformCommand(TransformOperation.CHANGE_FIRST_SEWING,
                    uniChildPattern.getId(), uniPoint.getIndex());
            ITransformCommand transformCommand1 = helper.genCommand(transformCommand);
            transformCommand1.execute();

            uniChildPattern.setWeight(weight--);
        }

    }

    public static int computeWeight(UniPoint point){
        int weight = point.getX() * -100_0000 + point.getY() * -100;
        return weight;
    }
}

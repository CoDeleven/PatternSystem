package com.codeleven.core.utils;

import cn.hutool.core.collection.ListUtil;
import com.aspose.cad.fileformats.cad.cadobjects.Cad2DPoint;
import com.aspose.cad.fileformats.cad.cadobjects.Cad3DPoint;
import com.aspose.cad.fileformats.cad.cadobjects.CadLwPolyline;
import com.aspose.cad.fileformats.cad.cadobjects.ICadBaseEntity;
import com.aspose.cad.fileformats.cad.cadobjects.polylines.CadPolyline;
import com.aspose.cad.fileformats.cad.cadobjects.vertices.Cad3DVertex;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.constants.SystemTopControlCode;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.common.entity.UniPoint;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeleven.common.constants.SystemTopControlCode.SECOND_ORIGIN_POINT;

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

    public static boolean isFengBiShape(UniChildPattern childPattern){
        UniFrame firstFrame = childPattern.getPatternData().get(0);
        UniFrame lastFrame = childPattern.getPatternData().get(childPattern.getPatternData().size() - 1);

        return firstFrame.getX() == lastFrame.getX() && firstFrame.getY() == lastFrame.getY();
    }

    // 如果是封闭的图形，尾部节点需要往后继续打一些
    public static void joinShape(UniChildPattern childPattern){
        List<UniFrame> patternData = childPattern.getPatternData();
        int lastSewingFrameIndex = getLastSewingFrameIndex(childPattern);

        List<UniFrame> other = new ArrayList<>();
        // 如果最后一个车缝点不是最后一个
        if(lastSewingFrameIndex != patternData.size() - 1) {
            for (int i = lastSewingFrameIndex + 1; i < patternData.size(); i++) {
                other.add(patternData.get(i));
            }
        }

        int otherSize = other.size();
        for (int i = 1; i <= otherSize; i++) {
            patternData.remove(patternData.size() - 1);
        }

        List<UniFrame> joinList = patternData.subList(2, 5);
        patternData.addAll(joinList);

        UniFrame newLastFrame = patternData.get(patternData.size() - 1);

        for (UniFrame uniFrame : other) {
            UniFrame foo = newLastFrame.copyFrame();
            foo.setControlCode(uniFrame.getControlCode());
            patternData.add(foo);
        }
    }

    /**
     * 移除相同的Frame，仅留下一个。（存在功能码和车缝时，仅保留车缝）
     */
    public static void removeSameCoordinatorFrameForSewing(List<UniFrame> frameList){
        UniFrame lastFrame = null;
        int endIndex = -1;
        for (int i = 0; i < frameList.size(); i++) {
            UniFrame cur = frameList.get(i);
            if(lastFrame == null){
                lastFrame = cur;
                continue;
            }

            if(cur.equals(lastFrame)){
                endIndex = i;
                break;
            }
            lastFrame = cur;
        }
        if(endIndex == -1){
            return;
        }
        int initSize = frameList.size();
        for (int i = endIndex; i < initSize; i++) {
            frameList.remove(endIndex);
        }
    }

    public static boolean isOriginalPoint(UniFrame frame){
        return frame.getX() == 0 && frame.getY() == 0;
    }

    public static List<UniFrame> getLastControlCodeFrames(List<UniFrame> frameList){
        int lastSewingFrameIndex = getLastSewingFrameIndex(frameList);
        List<UniFrame> result = new ArrayList<>();
        for (int i = lastSewingFrameIndex + 1; i < frameList.size(); i++) {
            result.add( frameList.get(i) );
        }
        return result;
    }

    public static int getLastSewingFrameIndex(UniChildPattern childPattern){
        List<UniFrame> patternData = childPattern.getPatternData();
        return getLastSewingFrameIndex(patternData);
    }

    public static int getLastSewingFrameIndex(List<UniFrame> frameList){
        for (int i = frameList.size() - 1; i >= 0; i--) {
            if(SystemTopControlCode.isSewingControlCode(frameList.get(i).getControlCode())){
                return i;
            }
        }
        throw new RuntimeException("获取最后一个车缝点失败");
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
        List<UniChildPattern> collect = childPatterns.stream()
                .sorted(Comparator.comparingInt(UniChildPattern::getWeight))
                .collect(Collectors.toList());

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

    public static void repeatSewing(List<UniFrame> total, int start, int to, int count) {
        // [start, to]
        List<UniFrame> temp = copyList(total.subList(start, to + 1));
        //
        List<UniFrame> negative = copyList(ListUtil.reverseNew(temp));
        // 不包含最后一个（反转后不包含第一个）
        negative.remove(0);
        List<UniFrame> positive = copyList(temp);
        // 不包含第一个
        positive.remove(0);

        List<UniFrame> tempList = new ArrayList<>();
        assert count % 2 == 0;
        for (int i = 0; i < count / 2; i++) {
            tempList.addAll(copyList(negative));
            tempList.addAll(copyList(positive));
        }

        total.addAll(to + 1, tempList);
    }

    public static void lockStart(List<UniFrame> total, int len, int count){
        int beginIndex = -1;
        for (int i = 0; i < total.size(); i++) {
            UniFrame foo = total.get(i);
            if(foo.getControlCode() == SystemTopControlCode.HIGH_SEWING.code){
                beginIndex = i;
                break;
            }
        }
        repeatSewing(total, beginIndex, beginIndex + len - 1, count);
    }

    public static void lockEnd(List<UniFrame> total, int len, int count){
        int beginIndex = -1;
        for (int i = total.size() - 1; i >= 0; i--) {
            UniFrame foo = total.get(i);
            if(foo.getControlCode() == SystemTopControlCode.HIGH_SEWING.code){
                beginIndex = i;
                break;
            }
        }

        repeatSewing(total, beginIndex - len + 1, beginIndex, count);
    }

    public static List<UniFrame> copyList(List<UniFrame> old){
        List<UniFrame> result = new ArrayList<>();
        for (UniFrame uniFrame : old) {
            result.add(uniFrame.copyFrame());
        }
        return result;
    }

    public static void repeatStartSewing(List<UniFrame> total, int len, int count){
        int beginSewingIndex = -1;
        for (int i = 0; i < total.size(); i++) {
            if(total.get(i).getControlCode() == SystemTopControlCode.SKIP.code){
                continue;
            }
            if(total.get(i).getControlCode() == SystemTopControlCode.HIGH_SEWING.code){
                beginSewingIndex = i;
                break;
            }
        }
        List<UniFrame> positiveSeq = new ArrayList<>(total.subList(beginSewingIndex, beginSewingIndex + len));
        List<UniFrame> negativeSeq = ListUtil.reverseNew(new ArrayList<>(total.subList(beginSewingIndex, beginSewingIndex + len)));
        positiveSeq.remove(0);
        negativeSeq.remove(0);

        List<UniFrame> temp = new ArrayList<>();

        assert count % 2 == 0;
        for (int i = 0; i < count / 2; i++) {
            temp.addAll(copyList(negativeSeq));
            temp.addAll(copyList(positiveSeq));
        }
        total.addAll(beginSewingIndex + len, temp);
    }

    public static UniPoint convertFrame2Point(UniFrame frame){
        return new UniPoint(frame.getX(), frame.getY());
    }

    public static UniFrame getFirstFrame(UniPattern pattern){
        if(pattern == null){
            throw new RuntimeException("必须传入UniPattern，才能重新设置原点");
        }
        List<UniChildPattern> patterns = sortChildPatternByWeight(pattern.getChildList().values());
        List<UniFrame> patternData = patterns.get(0).getPatternData();
        return patternData.get(0);
    }
}

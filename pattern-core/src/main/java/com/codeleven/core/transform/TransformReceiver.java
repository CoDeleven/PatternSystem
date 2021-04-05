package com.codeleven.core.transform;

import cn.hutool.core.collection.ListUtil;
import com.codeleven.common.constants.SystemTopControlCode;
import com.codeleven.common.constants.TransformOperation;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.core.utils.PatternUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransformReceiver {

    // 判断当前作用的对象是否是 子花样
    private final boolean isChildPattern;
    // 帧迹列表，可能是整个花样的也可能是子花样的，取决于isChildPattern
    private final List<UniFrame> frames;
    // 用来接收结果的。。破坏了结构...
    private UniPattern uniPattern;

    private TransformReceiver(List<UniFrame> frames, boolean isChildPattern) {
        this.frames = frames;
        this.isChildPattern = isChildPattern;
    }

    public static TransformReceiver getInstance(List<UniFrame> frames, boolean isChildPattern) {
        return new TransformReceiver(frames, isChildPattern);
    }

    public void setUniPattern(UniPattern uniPattern) {
        this.uniPattern = uniPattern;
    }

    /**
     * 接收指令并分发指令执行
     *
     * @param operation 指令
     * @param data      指令的参数
     */
    public void receive(TransformOperation operation, int... data) {
        switch (operation) {
            case MOVE_X:
                doTranslateX(data[0]);
                break;
            case MOVE_Y:
                doTranslateY(data[0]);
                break;
            case CHANGE_START_END:
                doExchangeStartEnd();
                break;
            case CHANGE_FIRST_SEWING:
                doSetFirstSewingForChild(data[0]);
                updateSecondOriginalPoint(PatternUtil.getFirstFrame(uniPattern));
                break;
            case CHANGE_PATTERN_SEWING_SEQ:
                doChangeChildPatternSeq(data[0], data[1]);
                updateSecondOriginalPoint(PatternUtil.getFirstFrame(uniPattern));
                break;
        }
    }

    private void updateSecondOriginalPoint(UniFrame second) {
        if(uniPattern == null) throw new RuntimeException("必须传入UniPattern...");
        uniPattern.setSecondOrigin(PatternUtil.convertFrame2Point(second));
    }

    /**
     * 将 targetChildId 挪到 指定的 insertionId的位置
     * [insertionId, targetChildId)的内容往后移（前提是 targetChildId>insertionId)
     * (targetChildId, insertionId]的内容往后移（前提是 insertionId>targetChildId)
     *
     * @param targetChildId 目标花样的ID，比index大1
     * @param insertionId   要交换的花样的ID，比index大1
     */
    private void doChangeChildPatternSeq(int targetChildId, int insertionId) {
        if (targetChildId == insertionId) return;
        if(uniPattern == null) throw new RuntimeException("交换子花样的顺序必须要传入UniPattern");
        // 临时方案


        Map<Long, UniChildPattern> childFrames = uniPattern.getChildList();

        List<UniChildPattern> childPatternList = PatternUtil.sortChildPatternByWeight(new ArrayList<>(childFrames.values()));

        // 这种情况直接返回
        if (targetChildId > childFrames.size() || insertionId > childFrames.size() || targetChildId <= 0 || insertionId <= 0)
            return;

        --targetChildId;
        --insertionId;

        // 如果 目标子花样要往前挪动
        if (targetChildId > insertionId) {
            // 临时保存一个Entry
            UniChildPattern tempEntry = childPatternList.get(targetChildId);
            int count = 0;
            for (int i = targetChildId - 1; i >= insertionId; --i) {
                UniChildPattern item = childPatternList.get(i);
                // 权重小了，顺序就在后面了
                PatternUtil.decWeight(item, 1);
                // 往后挪动了几个， tempEntry增加相应的权重
                ++count;
            }
            PatternUtil.incWeight(tempEntry, count);
        } else {  // 如果 目标子花样要往后挪动
            // 临时保存一个Entry
            UniChildPattern tempEntry = childPatternList.get(targetChildId);
            int count = 0;
            for (int i = targetChildId + 1; i <= insertionId; ++i) {
                UniChildPattern item = childPatternList.get(i);
                PatternUtil.incWeight(item, 1);
                ++count;
            }
            PatternUtil.decWeight(tempEntry, count);
        }
    }

    /**
     * 本方法用于处理 子花样 里具体的某个点的调整
     *
     * @param targetPointIndexInChild 目标帧 在 childFrames 里的下标
     */
    private void doSetFirstSewingForChild(int targetPointIndexInChild) {
        // 临时存储的列表
        List<UniFrame> newFrameList = new ArrayList<>(frames.size());

        // 如果是子花样，开头是跳缝，结尾是剪线，暂时不考虑 结束符那东西
        // 如果起始点的位置大于花样的点数总数，抛出异常
        if (frames.size() <= targetPointIndexInChild) {
            throw new RuntimeException("花样的点数为：" + frames.size() + ", 起始点为：" + targetPointIndexInChild);
        }
        // 如果本来就是起始点，直接返回
        if (targetPointIndexInChild == 0) {
            return;
        }
        //          xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        //          x                                              x
        //    ┌─────x──────────────────────────────────────────────x─────────────────────────┐
        //    │     x                                              x                         │
        //    │   x x x                                    listFromTargetToEnd               │
        //    │    xxx                                       ────────────────                │
        //    │     x                                                                        │
        //    │   FRAME0    FRAME1     FRAME2     FRAME3     FRAME4    FRAME5    FRAME_CUT   │
        //    │                                                 ▲                            │
        //    │   ──────────────────────────────────────        │                            │
        //    │         listFromFirstToExcludedTarget        target                          │
        //    │                                                                              │
        //    └──────────────────────────────────────────────────────────────────────────────┘
        //

        // 对于子花样来说，最后一帧就是剪线（子花样的划分是我们决定的），注意，这里给的是拷贝的
        UniFrame cutFrame = frames.get(frames.size() - 1).copyFrame();
        // 一般这种情况最后一帧会是 剪线。这里暂时不需要处理剪线，剪线的X、Y和前一帧一样即可。
        List<UniFrame> listFromTargetToEnd = frames.subList(targetPointIndexInChild, frames.size() - 2);
        // subList 包含 0下标，不包含 targetPointIndex下标
        List<UniFrame> listFromFirstToExcludedTarget = frames.subList(0, targetPointIndexInChild);
        // 新的最后一帧
        UniFrame newEndFrame = listFromFirstToExcludedTarget.get(listFromFirstToExcludedTarget.size() - 1);

        // targetPointIndexInChild 设置为第一帧时必须是跳缝。更改 listFromTargetToEnd 里的第一帧，即targetPointIndex
        listFromTargetToEnd.get(0).setControlCode(SystemTopControlCode.SKIP.getCode());
        newFrameList.addAll(0, listFromTargetToEnd);

        // 更新第一帧，设置为高速缝
        listFromFirstToExcludedTarget.get(0).setControlCode(SystemTopControlCode.HIGH_SEWING.getCode());

        // 更新最后一帧的剪线
        cutFrame.setX(newEndFrame.getX());
        cutFrame.setY(newEndFrame.getY());

        // 替换，不要直接赋值
        frames.clear();
        frames.addAll(newFrameList);
    }

    /**
     * 更换起始点和终点的位置
     */
    private void doExchangeStartEnd() {
        // 将帧信息全部反转
        List<UniFrame> newFrameList = ListUtil.reverseNew(frames);

        // 这个index是第一个 车缝位置，它的值-1才是最后一个非车缝控制码
        int endOtherCodeCount = 0;
        // 找到起始到车缝之间的数值（原始针迹结尾的控制码
        for (int i = 0; i < newFrameList.size(); i++) {
            // 与车缝无关的控制码
            if(SystemTopControlCode.isSewingControlCode(newFrameList.get(i).getControlCode())){
                break;
            }
            endOtherCodeCount++;
        }
        for (int i = 0; i < frames.size() - endOtherCodeCount; i++) {
            UniFrame newFrame = newFrameList.get(i + endOtherCodeCount);
            // 因为两个List虽然不同，但是里面的元素都是相同的，所以要复制一个，不同使用原先的
            UniFrame oldFrame = frames.get(i).copyFrame();
            oldFrame.setX(newFrame.getX());
            oldFrame.setY(newFrame.getY());
            frames.set(i, oldFrame);
        }

        UniFrame lastSewingFrame = newFrameList.get(newFrameList.size() - 1);
        for (int i = (frames.size() - endOtherCodeCount); i < frames.size(); i++) {
            frames.get(i).setX(lastSewingFrame.getX());
            frames.get(i).setY(lastSewingFrame.getY());
        }
    }

    /**
     * X轴上的平移， 不需要考虑宽高的变化，因为平移不改变宽高
     *
     * @param num 平移的值
     */
    private void doTranslateX(int num) {
        for (UniFrame frame : frames) {
            frame.setX(frame.getX() + num);
        }
    }

    /**
     * Y轴上的平移， 不需要考虑宽高的变化，因为平移不改变宽高
     *
     * @param num 平移的值
     */
    private void doTranslateY(int num) {
        for (UniFrame frame : frames) {
            frame.setY(frame.getY() + num);
        }
    }
}
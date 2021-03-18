package com.codeleven.patternsystem.parser.systemtop;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.codeleven.patternsystem.common.PatternUpdateOperation;
import com.codeleven.patternsystem.entity.ChildPattern;
import com.codeleven.patternsystem.entity.UniFrame;
import com.codeleven.patternsystem.entity.UniPattern;

import java.util.List;

public class PatternTransformHelper {
    private UniPattern pattern;
    private List<PatternUpdateOperation> operations;
    private int targetPatternNo;

    public PatternTransformHelper(UniPattern pattern, List<PatternUpdateOperation> operations) {
        this.pattern = pattern;
        this.operations = operations;
    }

    public void doTransform(){
        if(targetPatternNo != 0 && targetPatternNo - 1 > pattern.getChildPatterns().size()){
            throw new RuntimeException("该花样的子花样个数为：" + pattern.getChildPatterns().size() + "，要修改的花样编号为：" + targetPatternNo);
        }
        for (PatternUpdateOperation operation : operations) {
            switch (operation.getOperationCode()){
                case "move-x":
                    moveX(operation.getNum());
                    break;
                case "move-y":
                    moveY(operation.getNum());
                    break;
                case "exchange-s-e":
                    exchangeStartEnd();
            }
            // 如果存在子花样，那么需要更新花样的帧数据
            if(targetPatternNo != 0){
                List<UniFrame> frames = pattern.getFrames();
                ChildPattern childPattern = pattern.getChildPatterns().get(targetPatternNo - 1);
                List<UniFrame> childFrameList = childPattern.getFrameList();
                int beginFrameIndex = childPattern.getBeginFrameIndex();
                int count = childPattern.getFrameCount();
                for (int i = beginFrameIndex, j = 0; j < count; i++, j++) {
                    frames.get(i).setX(childFrameList.get(j).getX());
                    frames.get(i).setY(childFrameList.get(j).getY());
                }
            }
        }
    }

    private void exchangeStartEnd() {
        List<ChildPattern> childPatterns = pattern.getChildPatterns();
        boolean onePattern = false;
        if(childPatterns != null && childPatterns.size() == 1){
            onePattern = true;
        }
        // 整体得起始点和终点交换， 先做反转。
        // 把原先 倒数的点删除；然后模拟追加点
        // 反转后：
        // 前面三个点分别是 ：0x1F、0x1B、0x02
        // 最后一个点：0x1B
        // 需求：
        // 前面只需要有一个点：0x1B（把第一个车缝点改成空送）
        // 后面需要三个点（按顺序）： 0x02（最后一个车缝复制，改控制码为0x02）、0x1B（挪到次元点的位置）、0x1F（位置应该和次元点一致）
        if(onePattern){
            List<UniFrame> frames = pattern.getFrames();
            // 将帧信息全部反转
            List<UniFrame> newFrameList = ListUtil.reverseNew(frames);
            // 移除 反转后：第一个（即原先倒数第一个，结尾标识符）
            UniFrame remove = newFrameList.remove(0);
            assert remove.getControlCode() == 0x1F;
            // 移除 反转后：第二个（即原先倒数第二个，空送）
            UniFrame remove1 = newFrameList.remove(0);
            assert remove1.getControlCode() == SystemTopControlCode.SKIP.getCode();
            // 移除 反转后：第三个（即原先倒数第三个，剪线）
            UniFrame remove2 = newFrameList.remove(0);
            assert remove2.getControlCode() == SystemTopControlCode.CUT.getCode();

            UniFrame firstSewingFrame = newFrameList.get(0);
            firstSewingFrame.setControlCode(SystemTopControlCode.SKIP.getCode());

            // 更新 反转后：最后一个（即原先第一个空送）    为车缝
            UniFrame remove3 = newFrameList.get(newFrameList.size() - 1);
            remove3.setControlCode(SystemTopControlCode.HIGH_SEWING.getCode());
            // 复制 反转后：最后一个（即原先第一个空送）    添加为剪线
            UniFrame cutFrame = remove3.copyFrame();
            // 剪线是在最后一个点上剪得，不需要移动
            cutFrame.setX(remove3.getX());
            cutFrame.setY(remove3.getY());
            cutFrame.setControlCode(SystemTopControlCode.CUT.getCode());
            newFrameList.add(newFrameList.size(), cutFrame);

            // 如果原先的 帧列表  第二个不是0，就是不是移动回原点的；那么认为是设置了次原点，一般次元点都是在起点 移动到起点
            if(remove1.getX() == 0 && remove1.getY() == 0){
                newFrameList.add(newFrameList.size(), remove1);
            } else {
                UniFrame firstFrame = newFrameList.get(0);
                newFrameList.add(newFrameList.size(), new UniFrame(firstFrame.getX(), firstFrame.getY(), SystemTopControlCode.SKIP.getCode()));
            }

            newFrameList.add(newFrameList.size(), remove);

            pattern.setFrames(newFrameList);
        }
    }

    public void setTargetPatternNo(int targetPatternNo) {
        this.targetPatternNo = targetPatternNo;
    }

    private void moveX(int num) {
        // 如果需要处理子花样
        if(targetPatternNo != 0){
            ChildPattern childPattern = pattern.getChildPatterns().get(targetPatternNo - 1);
            if(childPattern != null){
                for (UniFrame uniFrame : childPattern.getFrameList()) {
                    uniFrame.setX(uniFrame.getX() + num);
                    // 如果新的X 比最小值小，那么更新Pattern的最小值
                    if(uniFrame.getX() < pattern.getMinX()){
                        pattern.setMinX(uniFrame.getX());
                    }
                    // 如果新的X 比最大值大，那么更新Pattern的最大值
                    if(uniFrame.getX() > pattern.getMaxX()){
                        pattern.setMaxX(uniFrame.getX());
                    }
                }
            }
        } else {
            for (UniFrame frame : pattern.getFrames()) {
                frame.setX(frame.getX() + num);
            }
            pattern.setMinX(pattern.getMinX() + num);
            pattern.setMaxX(pattern.getMaxX() + num);
        }
    }

    private void moveY(int num){
        // 如果需要处理子花样
        if(targetPatternNo != 0){
            ChildPattern childPattern = pattern.getChildPatterns().get(targetPatternNo - 1);
            if(childPattern != null){
                for (UniFrame uniFrame : childPattern.getFrameList()) {
                    uniFrame.setY(uniFrame.getY() + num);
                    // 如果新的X 比最小值小，那么更新Pattern的最小值
                    if(uniFrame.getY() < pattern.getMinY()){
                        pattern.setMinY(uniFrame.getY());
                    }
                    // 如果新的X 比最大值大，那么更新Pattern的最大值
                    if(uniFrame.getY() > pattern.getMaxY()){
                        pattern.setMaxY(uniFrame.getY());
                    }
                }
            }
        }else {
            for (UniFrame frame : pattern.getFrames()) {
                frame.setY(frame.getY() + num);
            }
            pattern.setMinY(pattern.getMinY() + num);
            pattern.setMaxY(pattern.getMaxY() + num);
        }
    }
}

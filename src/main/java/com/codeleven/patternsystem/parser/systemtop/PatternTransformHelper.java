package com.codeleven.patternsystem.parser.systemtop;

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

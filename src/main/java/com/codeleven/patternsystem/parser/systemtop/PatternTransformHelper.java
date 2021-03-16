package com.codeleven.patternsystem.parser.systemtop;

import com.codeleven.patternsystem.common.PatternUpdateOperation;
import com.codeleven.patternsystem.entity.UniFrame;
import com.codeleven.patternsystem.entity.UniPattern;

import java.util.List;

public class PatternTransformHelper {
    private UniPattern pattern;
    private List<PatternUpdateOperation> operations;

    public PatternTransformHelper(UniPattern pattern, List<PatternUpdateOperation> operations) {
        this.pattern = pattern;
        this.operations = operations;
    }

    public void doTransform(){
        for (PatternUpdateOperation operation : operations) {
            switch (operation.getOperationCode()){
                case "move-x":
                    moveX(operation.getNum());
                    break;
                case "move-y":
                    moveY(operation.getNum());
                    break;
            }
        }
    }

    private void moveX(int num) {
        for (UniFrame frame : pattern.getFrames()) {
            frame.setX(frame.getX() + num);
        }
        pattern.setMinX(pattern.getMinX() + num);
        pattern.setMaxX(pattern.getMaxX() + num);
    }

    private void moveY(int num){
        for (UniFrame frame : pattern.getFrames()) {
            frame.setY(frame.getY() + num);
        }
    }
}

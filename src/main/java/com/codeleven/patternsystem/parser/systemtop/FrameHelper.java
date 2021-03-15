package com.codeleven.patternsystem.parser.systemtop;

import com.codeleven.patternsystem.entity.UniFrame;

import java.util.LinkedList;
import java.util.List;

public class FrameHelper {
    private List<UniFrame> frameResult = new LinkedList<>();
    private int lastX;
    private int lastY;
    private int lastControlCode;

    private int accX;
    private int accY;

    public FrameHelper addFrame(int controlCode, int x, int y){

        // 如果控制码为0x3，说明是持续性移动，最终结果取决于下一个非0x3得控制码
        if(controlCode == 0x3){
            // 控制码为0x3，表示累计X、Y的偏移
            accX += x;
            accY += y;
            // 如果控制码为0x3，实际上并没有移动lastX、lastY不需要增长
        } else{
            int absoluteX = lastX + x;
            int absoluteY = lastY + y;
            // 如果上一个控制码是0x3，现在控制码不是0x3，那么将累计的值加上
            if(lastControlCode == 0x3){
                absoluteX += accX;
                absoluteY += accY;
                accX = 0;           // 持续移动已经结束，清空
                accY = 0;           // 持续移动已经结束，清空
            }
            UniFrame frame = new UniFrame(absoluteX, absoluteY, controlCode);
            frameResult.add(frame);
            lastX = absoluteX;
            lastY = absoluteY;
        }
        this.lastControlCode = controlCode;
        return this;
    }

    public List<UniFrame> build(){
        return frameResult;
    }
}

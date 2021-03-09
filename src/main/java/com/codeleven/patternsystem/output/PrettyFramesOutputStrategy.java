package com.codeleven.patternsystem.output;

import com.codeleven.patternsystem.dto.UniFrame;
import com.codeleven.patternsystem.graphics.XYGraphics;
import com.codeleven.patternsystem.parser.systemtop.SystemTopControlCode;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class PrettyFramesOutputStrategy {

    public static String getTextOutput(List<UniFrame> frames, boolean showPrototype) {
        StringBuilder builder = new StringBuilder();
        if (showPrototype) {
            for (int i = 0; i < frames.size(); i++) {
                UniFrame frame = frames.get(i);
                builder.append("序号: ").append(i).append(",\tX: ")
                        .append(frame.getX()).append(",\tY:")
                        .append(frame.getY()).append(",\tCode:")
                        .append("0x" + Integer.toHexString(frame.getControlCode())).append(";\n");
            }
        } else {
            for (int i = 0; i < frames.size(); i++) {
                UniFrame frame = frames.get(i);
                SystemTopControlCode formatControlCode = SystemTopControlCode.getEnumByCode(frame.getControlCode());
                builder.append("序号: ").append(i).append(",\tX: ")
                        .append(frame.getX()).append(",\tY:")
                        .append(frame.getY()).append(",\tCode:")
                        .append(formatControlCode.getCodeName()).append(";\n");
            }
        }
        return builder.toString();
    }

    public static void getImageOutput(List<UniFrame> frames) throws IOException {
        XYGraphics graphics = XYGraphics.createGraphics(2461, 897);
        UniFrame lastFrame = UniFrame.ZERO_FRAME;
        for (UniFrame frame : frames) {
            if (frame.getControlCode() == SystemTopControlCode.HIGH_SEWING.getCode()) {
                graphics.setStroke(XYGraphics.NORMAL_LINE);
                graphics.drawLine(lastFrame.getX(), lastFrame.getY(), frame.getX(), frame.getY());
                graphics.drawRect(frame.getX(), frame.getY(), 6, 6);
            } else if (frame.getControlCode() == SystemTopControlCode.SKIP.getCode()) {
                graphics.setStroke(XYGraphics.DOT_LINE);
                graphics.drawLine(lastFrame.getX(), lastFrame.getY(), frame.getX(), frame.getY());
            }
            lastFrame = frame;
        }
        graphics.setStroke(XYGraphics.NORMAL_LINE);
        graphics.setColor(Color.BLUE);
        UniFrame firstFrame = frames.get(0);
        UniFrame secondFrame = frames.get(1);
        graphics.drawArrow(firstFrame.getX(), firstFrame.getY(), secondFrame.getX(), secondFrame.getY(), 20);

        graphics.outputTo("C:\\Users\\Administrator\\Desktop\\test.png");
    }
}

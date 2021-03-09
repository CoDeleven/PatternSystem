package com.codeleven.patternsystem.dto;

/**
 * 统一的针迹点
 *
 */
public class UniFrame {
    public static final UniFrame ZERO_FRAME = new UniFrame(0, 0);
    // X轴绝对值
    private int x;
    // Y轴绝对值
    private int y;
    private int controlCode;

    public UniFrame(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public UniFrame(int x, int y, int controlCode) {
        this.x = x;
        this.y = y;
        this.controlCode = controlCode;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getControlCode() {
        return controlCode;
    }

    public void setControlCode(int controlCode) {
        this.controlCode = controlCode;
    }

    @Override
    public String toString() {
        return "UniFrame{" +
                "x=" + x +
                ", y=" + y +
                ", controlCode=" + controlCode +
                '}';
    }
}

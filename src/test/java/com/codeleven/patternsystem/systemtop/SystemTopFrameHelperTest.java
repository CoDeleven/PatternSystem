package com.codeleven.patternsystem.systemtop;

import com.codeleven.patternsystem.entity.UniFrame;
import com.codeleven.patternsystem.parser.systemtop.FrameHelper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

public class SystemTopFrameHelperTest {
    /**
     * 检查 FrameHelper 能否正确处理 负数
     */
    @Test
    public void testNumberNegative(){
        byte x = (byte)0xF2;
        byte y = (byte)0x81;
        FrameHelper helper = new FrameHelper();
        helper.addFrame(0, x, y);
        List<UniFrame> frames = helper.build();
        UniFrame uniFrame = frames.get(0);
        assert uniFrame.getX() == Byte.MIN_VALUE + 0x72;
        assert uniFrame.getY() == Byte.MIN_VALUE + 0x01;
    }

    /**
     * 检查helper能正常生成对应数量的结果
     */
    @Test
    public void testHelperBuildResult() {
        int groupNum = 10;
        byte[][] bytes = createBytes(groupNum);
        FrameHelper helper = new FrameHelper();
        for (byte[] data : bytes) {
            helper.addFrame(data[0], data[2], data[3]);
        }
        List<UniFrame> result = helper.build();
        assert result.size() == groupNum;
        for (int i = 0; i < result.size(); i++) {
            UniFrame uniFrame = result.get(i);
            assert uniFrame.getControlCode() == bytes[i][0];
            assert uniFrame.getX() == bytes[i][2];
            assert uniFrame.getY() == bytes[i][3];
        }
    }

    private static byte[][] createBytes(int groupNum){
        Random random = new Random();
        byte[][] bytes = new byte[groupNum][4];
        for (byte[] aByte : bytes) {
            byte[] temp = new byte[4];
            random.nextBytes(temp);
            System.arraycopy(temp, 0, aByte, 0, 4);
        }
        return bytes;
    }
}

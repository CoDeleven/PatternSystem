package com.codeleven.patternsystem.parser.transform;

import cn.hutool.core.io.IoUtil;
import com.codeleven.patternsystem.common.TransformData;
import com.codeleven.patternsystem.entity.ChildPattern;
import com.codeleven.patternsystem.entity.UniFrame;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.parser.UniParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.InputStream;
import java.util.List;

/**
 * Test TransformReceiver
 * {@link TransformReceiver}
 */
public class TransformReceiverTest {
    UniPattern uniPattern;
    UniPattern uniPattern2;
    ChildPattern childPattern;
    ChildPattern childPattern2;

    @Before
    public void setUp() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/test.NPT");
        assert is != null;
        byte[] bytes = IoUtil.readBytes(is);
        UniParser parser = new UniParser();
        uniPattern = parser.doParse(bytes);
        uniPattern2 = parser.doParse(bytes);
        // 通过不一样的对象获取子花样，否则没有对照
        childPattern = uniPattern.getChildPatterns().get(3);
        childPattern2 = uniPattern2.getChildPatterns().get(3);
    }

    /**
     * 测试X正向值
     */
    @Test
    public void testTranslateXCommand() {
        // 获取 receiver
        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(uniPattern.getFrames(), false);
        // 获取 X轴平移
        ITransformCommand command = new TranslateXCommand(totalPatternReceiver, 10);
        // 执行 Command
        command.execute();

        assert uniPattern2.getFrames().size() == uniPattern.getFrames().size();

        for (int i = 0; i < uniPattern.getFrames().size(); i++) {
            assert uniPattern.getFrames().get(i).getY() == uniPattern2.getFrames().get(i).getY();
            assert uniPattern.getFrames().get(i).getX() == (uniPattern2.getFrames().get(i).getX() + 10);
            assert uniPattern.getFrames().get(i).getControlCode() == uniPattern2.getFrames().get(i).getControlCode();
        }
    }

    /**
     * 测试X反向值
     */
    @Test
    public void testTranslateXCommandForNegative() {
        // 获取 receiver
        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(uniPattern.getFrames(), false);
        // 获取 X轴平移
        ITransformCommand command = new TranslateXCommand(totalPatternReceiver, -10);
        // 执行 Command
        command.execute();

        assert uniPattern2.getFrames().size() == uniPattern.getFrames().size();

        for (int i = 0; i < uniPattern.getFrames().size(); i++) {
            Assertions.assertEquals(uniPattern2.getFrames().get(i).getY(), uniPattern.getFrames().get(i).getY());
            Assertions.assertEquals((uniPattern2.getFrames().get(i).getX() - 10), uniPattern.getFrames().get(i).getX());
            Assertions.assertEquals(uniPattern2.getFrames().get(i).getControlCode(), uniPattern.getFrames().get(i).getControlCode());
        }
    }

    /**
     * 测试X轴方向 子花样的偏移
     */
    @Test
    public void testTranslateXCommandForChild() {
        List<UniFrame> frames1 = childPattern.getFrameList();
        List<UniFrame> frames2 = childPattern2.getFrameList();

        // 获取 receiver
        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(frames1, true);
        // 获取 X轴平移
        ITransformCommand command = new TranslateXCommand(totalPatternReceiver, -10);
        // 执行 Command
        command.execute();

        assert childPattern.getFrameCount() == childPattern.getFrameList().size();
        // 保证仅在子花样内变动
        for (int i = 0; i < frames1.size(); i++) {
            Assertions.assertEquals(frames2.get(i).getY(), frames1.get(i).getY());
            Assertions.assertEquals(frames2.get(i).getX() - 10, frames1.get(i).getX());
            Assertions.assertEquals(frames2.get(i).getControlCode(), frames1.get(i).getControlCode());
        }
        // 保证其他元素没有变动
        for (int i = 0; i < uniPattern.getChildPatterns().size(); i++) {
            List<UniFrame> foo = uniPattern.getChildPatterns().get(i).getFrameList();
            List<UniFrame> bar = uniPattern2.getChildPatterns().get(i).getFrameList();
            if (i == this.childPattern.getPatternNo() - 1) {
                continue;
            }
            Assertions.assertEquals(bar.get(i).getY(), foo.get(i).getY());
            Assertions.assertEquals(bar.get(i).getX(), foo.get(i).getX());
            Assertions.assertEquals(bar.get(i).getControlCode(), foo.get(i).getControlCode());
        }
    }

    @Test
    public void testTranslateYCommand() {
        // 获取 receiver
        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(uniPattern.getFrames(), false);
        // 获取 Y轴平移
        ITransformCommand command = new TranslateYCommand(totalPatternReceiver, 10);
        // 执行 Command
        command.execute();

        assert uniPattern2.getFrames().size() == uniPattern.getFrames().size();

        for (int i = 0; i < uniPattern.getFrames().size(); i++) {
            assert uniPattern.getFrames().get(i).getY() == (uniPattern2.getFrames().get(i).getY() + 10);
            assert uniPattern.getFrames().get(i).getX() == (uniPattern2.getFrames().get(i).getX());
            assert uniPattern.getFrames().get(i).getControlCode() == uniPattern2.getFrames().get(i).getControlCode();
        }
    }

    @Test
    public void testChangeStartEndCommand() {
        // uniPattern1 执行了变换
        List<UniFrame> init = TransformData.normalFrames();
        List<UniFrame> after = TransformData.changeStartEndFrames();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init, true);
        ITransformCommand command = new ChangeStartEndCommand(totalPatternReceiver);
        command.execute();

        this.checkBeforeAndAfter(after, init);
    }

    @Test
    public void testSetFirstSewingForChild() {
        List<UniFrame> init = TransformData.normalFrames();
        List<UniFrame> after = TransformData.afterChangeFirstSewingFrame();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init, true);
        ITransformCommand command = new ChangeFirstSewingCommand(totalPatternReceiver, 6);
        command.execute();

        this.checkBeforeAndAfter(after, init);

    }

    @Test(expected = RuntimeException.class)
    public void testSetFirstSewingForErrorSize() {
        List<UniFrame> init = TransformData.normalFrames();
        List<UniFrame> after = TransformData.normalFrames();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init, true);
        ITransformCommand command = new ChangeFirstSewingCommand(totalPatternReceiver, 99);
        command.execute();

        this.checkBeforeAndAfter(after, init);
    }

    @Test
    public void testSetFirstSewingFor0Target() {
        List<UniFrame> init = TransformData.normalFrames();
        List<UniFrame> after = TransformData.normalFrames();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init, true);
        ITransformCommand command = new ChangeFirstSewingCommand(totalPatternReceiver, 0);
        command.execute();

        this.checkBeforeAndAfter(after, init);
    }

    @Test
    public void testChangePatternPositionPatternList1() {
        List<UniFrame> init = TransformData.initPatternPositionList();
        List<UniFrame> after = TransformData.changePatternPositionPatternList42();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init, false);
        ITransformCommand command = new ChangePatternSewingSeqCommand(totalPatternReceiver, 4, 2);
        command.execute();

        this.checkBeforeAndAfter(after, init);
    }

    @Test
    public void testChangePatternPositionPatternList2() {
        List<UniFrame> init = TransformData.initPatternPositionList();
        List<UniFrame> after = TransformData.changePatternPositionPatternList13();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init, false);
        ITransformCommand command = new ChangePatternSewingSeqCommand(totalPatternReceiver, 1, 3);
        command.execute();

        this.checkBeforeAndAfter(after, init);
    }

    @Test
    public void testChangePatternPositionPattern4ErrorSize() {
        // 测试越界情况，处理后也还是原样
        List<UniFrame> init = TransformData.initPatternPositionList();
        List<UniFrame> after = TransformData.initPatternPositionList();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init, false);
        ITransformCommand command = new ChangePatternSewingSeqCommand(totalPatternReceiver, 1, 99);
        command.execute();

        this.checkBeforeAndAfter(after, init);

        command = new ChangePatternSewingSeqCommand(totalPatternReceiver, 0, 0);
        command.execute();

        this.checkBeforeAndAfter(after, init);

    }

    @Test
    public void testChangePatternPositionPattern4SameTarget() {
        // 测试越界情况，处理后也还是原样
        List<UniFrame> init = TransformData.initPatternPositionList();
        List<UniFrame> after = TransformData.initPatternPositionList();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init, false);
        ITransformCommand command = new ChangePatternSewingSeqCommand(totalPatternReceiver, 1, 1);
        command.execute();

        this.checkBeforeAndAfter(after, init);
    }

    private void checkBeforeAndAfter(List<UniFrame> expected, List<UniFrame> actual){
        for (int i = 0; i < actual.size(); i++) {
            UniFrame frame1 = actual.get(i);
            UniFrame frame2 = expected.get(i);

            Assertions.assertEquals(frame1.getX(), frame2.getX());
            Assertions.assertEquals(frame1.getY(), frame2.getY());
            Assertions.assertEquals(frame1.getControlCode(), frame2.getControlCode());
        }
    }
}

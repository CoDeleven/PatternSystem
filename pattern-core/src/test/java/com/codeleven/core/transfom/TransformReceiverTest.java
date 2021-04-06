package com.codeleven.core.transfom;

import cn.hutool.core.io.IoUtil;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.core.utils.PatternUtil;
import com.codeleven.core.common.TransformData;
import com.codeleven.core.transform.TransformReceiver;
import com.codeleven.core.transform.command.*;
import com.codeleven.core.parser.UniParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Test TransformReceiver
 * {@link TransformReceiver}
 */
public class TransformReceiverTest {
    UniPattern uniPattern;
    UniPattern uniPattern2;
    UniChildPattern childPattern;
    UniChildPattern childPattern2;

    @Before
    public void setUp() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("systemtop/test.NPT");
        assert is != null;
        byte[] bytes = IoUtil.readBytes(is);
        UniParser parser = new UniParser();
        uniPattern = parser.doParse(bytes);
        uniPattern2 = parser.doParse(bytes);
        // 通过不一样的对象获取子花样，否则没有对照
        childPattern = PatternUtil.getChildPatternBySortedIndex(new ArrayList<>(uniPattern.getChildList().values()), 3);
        childPattern2 = PatternUtil.getChildPatternBySortedIndex(new ArrayList<>(uniPattern2.getChildList().values()), 3);
    }

    /**
     * 测试X正向值
     */
    @Test
    public void testTranslateXCommand() {
        List<UniFrame> frames2 = PatternUtil.mergeChildPattern(uniPattern2);

        // 获取 receiver
        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(uniPattern);
        // 获取 X轴平移
        ITransformCommand command = new TranslateXCommand(totalPatternReceiver, 10);
        // 执行 Command
        command.execute();

        List<UniFrame> frames1 = PatternUtil.mergeChildPattern(uniPattern);
        assert frames2.size() == frames1.size();

        for (int i = 0; i < frames1.size(); i++) {
            assert frames1.get(i).getY() == frames2.get(i).getY();
            assert frames1.get(i).getX() == (frames2.get(i).getX() + 10);
            assert frames1.get(i).getControlCode() == frames2.get(i).getControlCode();
        }
    }

    /**
     * 测试X反向值
     */
    @Test
    public void testTranslateXCommandForNegative() {
        List<UniFrame> frames2 = PatternUtil.mergeChildPattern(uniPattern2);

        //         获取 receiver
        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(uniPattern);
        //         获取 X轴平移
        ITransformCommand command = new TranslateXCommand(totalPatternReceiver, -10);
        //         执行 Command
        command.execute();

        List<UniFrame> frames1 = PatternUtil.mergeChildPattern(uniPattern);
        assert frames2.size() == frames1.size();

        for (int i = 0; i < frames1.size(); i++) {
            Assertions.assertEquals(frames2.get(i).getY(), frames1.get(i).getY());
            Assertions.assertEquals((frames2.get(i).getX() - 10), frames1.get(i).getX());
            Assertions.assertEquals(frames2.get(i).getControlCode(), frames1.get(i).getControlCode());
        }
    }

    /**
     * 测试X轴方向 子花样的偏移
     */
    @Test
    public void testTranslateXCommandForChild() {
        List<UniFrame> frames2 = childPattern2.getPatternData();

        // 获取 receiver
        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(uniPattern, childPattern.getId());
        // 获取 X轴平移
        ITransformCommand command = new TranslateXCommand(totalPatternReceiver, -10);
        // 执行 Command
        command.execute();

        assert childPattern.getPatternData().size() == childPattern.getPatternData().size();
        List<UniFrame> frames1 = childPattern.getPatternData();

        // 保证仅在子花样内变动
        for (int i = 0; i < frames1.size(); i++) {
            Assertions.assertEquals(frames2.get(i).getY(), frames1.get(i).getY());
            Assertions.assertEquals(frames2.get(i).getX() - 10, frames1.get(i).getX());
            Assertions.assertEquals(frames2.get(i).getControlCode(), frames1.get(i).getControlCode());
        }

        List<UniChildPattern> patterns1 = PatternUtil.sortChildPatternByWeight(new ArrayList<>(uniPattern.getChildList().values()));
        List<UniChildPattern> patterns2 = PatternUtil.sortChildPatternByWeight(new ArrayList<>(uniPattern2.getChildList().values()));
        for (int i = 0; i < patterns1.size(); i++) {
            UniChildPattern child1 = patterns1.get(i);
            if(child1.equals(childPattern)) continue;
            List<UniFrame> foo = child1.getPatternData();
            List<UniFrame> bar = patterns2.get(i).getPatternData();
            for (int j = 0; j < foo.size(); j++) {
                Assertions.assertEquals(bar.get(j).getY(), foo.get(j).getY());
                Assertions.assertEquals(bar.get(j).getX(), foo.get(j).getX());
                Assertions.assertEquals(bar.get(j).getControlCode(), foo.get(j).getControlCode());
            }
        }
    }

    @Test
    public void testTranslateYCommand() {
        List<UniFrame> frames2 = PatternUtil.mergeChildPattern(uniPattern2);

        // 获取 receiver
        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(uniPattern);
        // 获取 Y轴平移
        ITransformCommand command = new TranslateYCommand(totalPatternReceiver, 10);
        // 执行 Command
        command.execute();

        List<UniFrame> frames1 = PatternUtil.mergeChildPattern(uniPattern);
        assert frames1.size() == frames2.size();

        for (int i = 0; i < frames1.size(); i++) {
            assert frames1.get(i).getY() == (frames2.get(i).getY() + 10);
            assert frames1.get(i).getX() == (frames2.get(i).getX());
            assert frames1.get(i).getControlCode() == frames2.get(i).getControlCode();
        }
    }

    @Test
    public void testChangeStartEndCommand() {
        // uniPattern1 执行了变换
        UniPattern init = TransformData.normalFrames();
        List<UniFrame> after = TransformData.changeStartEndFrames();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init, PatternUtil.getChildPatternBySortedIndex(init, 0).getId());
        ITransformCommand command = new ChangeStartEndCommand(totalPatternReceiver);
        command.execute();

        this.checkBeforeAndAfter(after, PatternUtil.mergeChildPattern(init));
    }

    @Test
    public void testSetFirstSewingForChild() {
        UniPattern init = TransformData.normalFrames();
        List<UniFrame> after = TransformData.afterChangeFirstSewingFrame();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init, PatternUtil.getChildPatternBySortedIndex(init, 0).getId());
        ITransformCommand command = new ChangeFirstSewingCommand(totalPatternReceiver, 6);
        command.execute();

        this.checkBeforeAndAfter(after, PatternUtil.mergeChildPattern(init));

    }

    @Test(expected = RuntimeException.class)
    public void testSetFirstSewingForErrorSize() {
        UniPattern init = TransformData.normalFrames();
        UniPattern after = TransformData.normalFrames();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init, PatternUtil.getChildPatternBySortedIndex(init, 0).getId());
        ITransformCommand command = new ChangeFirstSewingCommand(totalPatternReceiver, 99);
        command.execute();

        this.checkBeforeAndAfter(PatternUtil.mergeChildPattern(after), PatternUtil.mergeChildPattern(init));
    }

    @Test
    public void testSetFirstSewingFor0Target() {
        UniPattern init = TransformData.normalFrames();
        UniPattern after = TransformData.normalFrames();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init, PatternUtil.getChildPatternBySortedIndex(init, 0).getId());
        ITransformCommand command = new ChangeFirstSewingCommand(totalPatternReceiver, 0);
        command.execute();

        this.checkBeforeAndAfter(PatternUtil.mergeChildPattern(after), PatternUtil.mergeChildPattern(init));
    }

    @Test
    public void testChangePatternPositionPatternList1() {
        UniPattern init = TransformData.initPatternPositionList();
        List<UniFrame> after = TransformData.changePatternPositionPatternList42();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init);
        ITransformCommand command = new ChangePatternSewingSeqCommand(totalPatternReceiver, 4, 2);
        command.execute();

        this.checkBeforeAndAfter(after, PatternUtil.mergeChildPattern(init));
    }

    @Test
    public void testChangePatternPositionPatternList2() {
        UniPattern init = TransformData.initPatternPositionList();
        List<UniFrame> after = TransformData.changePatternPositionPatternList13();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init);
        ITransformCommand command = new ChangePatternSewingSeqCommand(totalPatternReceiver, 1, 3);
        command.execute();

        this.checkBeforeAndAfter(after, PatternUtil.mergeChildPattern(init));
    }

    @Test
    public void testChangePatternPositionPattern4ErrorSize() {
        // 测试越界情况，处理后也还是原样
        UniPattern init = TransformData.initPatternPositionList();
        UniPattern after = TransformData.initPatternPositionList();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init);
        ITransformCommand command = new ChangePatternSewingSeqCommand(totalPatternReceiver, 1, 99);
        command.execute();

        this.checkBeforeAndAfter(PatternUtil.mergeChildPattern(after), PatternUtil.mergeChildPattern(init));

        command = new ChangePatternSewingSeqCommand(totalPatternReceiver, 0, 0);
        command.execute();

        this.checkBeforeAndAfter(PatternUtil.mergeChildPattern(after), PatternUtil.mergeChildPattern(init));

    }

    @Test
    public void testChangePatternPositionPattern4SameTarget() {
        // 测试越界情况，处理后也还是原样
        UniPattern init = TransformData.initPatternPositionList();
        UniPattern after = TransformData.initPatternPositionList();

        TransformReceiver totalPatternReceiver = TransformReceiver.getInstance(init);
        ITransformCommand command = new ChangePatternSewingSeqCommand(totalPatternReceiver, 1, 1);
        command.execute();

        this.checkBeforeAndAfter(PatternUtil.mergeChildPattern(after), PatternUtil.mergeChildPattern(init));
    }

    private void checkBeforeAndAfter(List<UniFrame> expected, List<UniFrame> actual) {
        for (int i = 0; i < actual.size(); i++) {
            UniFrame frame1 = actual.get(i);
            UniFrame frame2 = expected.get(i);

            Assertions.assertEquals(frame1.getX(), frame2.getX());
            Assertions.assertEquals(frame1.getY(), frame2.getY());
            Assertions.assertEquals(frame1.getControlCode(), frame2.getControlCode());
        }
    }
}

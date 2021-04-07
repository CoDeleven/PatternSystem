package com.codeleven.core.parser;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import com.codeleven.common.constants.LockMethod;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.common.entity.UniPoint;
import com.codeleven.core.parser.dahao.DaHaoPatternParser;
import com.codeleven.core.parser.dxf.DXFParserStrategy;
import com.codeleven.core.parser.shangyi.SystemTopParserStrategy;
import com.codeleven.core.utils.PatternUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(UniParser.class);
    private static final IParserStrategy[] PARSER_LIST = new IParserStrategy[]{new SystemTopParserStrategy(), new DXFParserStrategy(), new DaHaoPatternParser()};
    private IParserStrategy targetParserStrategy;

    public IParserStrategy getTargetParserStrategy() {
        return targetParserStrategy;
    }

    public UniPattern doParse(InputStream is) {
        byte[] totalFileBytes = IoUtil.readBytes(is);
        return this.doParse(totalFileBytes);
    }

    /**
     * 解析
     * @param totalFileBytes 输入流字节
     * @return 返回UniPattern
     */
    public UniPattern doParse(byte[] totalFileBytes) {
        UniPattern result = new UniPattern();

        // 1. 首先判断当前应该使用何种解析器
        targetParserStrategy = this.getTargetParserStrategy(totalFileBytes);
        if(targetParserStrategy == null){
            throw new RuntimeException("读取输入流失败，没有匹配的文件解析器...");
        }
        // 2. 执行读取针迹数
        List<UniFrame> uniFrames = targetParserStrategy.readFrames(totalFileBytes);
        // 3. 获取图形宽高、最大最小值
        int[] dimension = getDimension(uniFrames);
        // 4. 切分花样
        List<UniChildPattern> childFrames = targetParserStrategy.splitPattern(uniFrames);
        // 设置宽度
        result.setWidth(dimension[2] - dimension[0]);
        // 设置高度
        result.setHeight(dimension[1] - dimension[3]);
        // 设置名称（只对上亿有效）
        result.setName("NEW");
        // 设置子花样
        Map<Long, UniChildPattern> childPatterns = new HashMap<>();

        // 设置次元点，以第一针为起点
        UniFrame firstFrame = uniFrames.get(0);
        result.setSecondOrigin(new UniPoint(firstFrame.getX(), firstFrame.getY()));

        for (UniChildPattern childFrame : childFrames) {
            long tempId = RandomUtil.randomLong();
            childFrame.setId(tempId);
            childPatterns.put(tempId, childFrame);
        }
        result.setChildList(childPatterns);
        // 处理锁针方式
        this.handleLockMethod(childFrames);

        return result;
    }

    private void handleLockMethod(List<UniChildPattern> childs){
        // 设置锁针方式
        for (UniChildPattern child : childs) {
            if(PatternUtil.isFengBiShape(child)){
                child.setLockMethod(LockMethod.LOCK_JOIN);
            } else {
                child.setLockMethod(LockMethod.LOCK_BACK);
            }
        }
    }

    /**
     * 获取针迹的左上右下的最小/最大值
     * @return
     */
    protected int[] getDimension(List<UniFrame> frameList){
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (UniFrame uniFrame : frameList) {
            int uniFrameX = uniFrame.getX();
            int uniFrameY = uniFrame.getY();
            if(uniFrameX > maxX) {
                maxX = uniFrameX;
            } else if(uniFrameX < minX) {
                minX = uniFrameX;
            }
            if(uniFrameY > maxY){
                maxY = uniFrameY;
            } else if(uniFrameY < minY){
                minY = uniFrameY;
            }
        }
        return new int[]{minX, maxY, maxX, minY};
    }


    private IParserStrategy getTargetParserStrategy(byte[] totalFileBytes){
        for (IParserStrategy iParserStrategy : PARSER_LIST) {
            if(iParserStrategy.isSupport(totalFileBytes)) {
                return iParserStrategy;
            }
        }
        return null;
    }
}

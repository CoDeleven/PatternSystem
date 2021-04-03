package com.codeleven.parser;

import cn.hutool.core.io.IoUtil;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPattern;
import com.codeleven.parser.shangyi.SystemTopParserStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class UniParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(UniParser.class);
    private static final IParserStrategy[] PARSER_LIST = new IParserStrategy[]{new SystemTopParserStrategy()};

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
        IParserStrategy targetParserStrategy = this.getTargetParserStrategy(totalFileBytes);
        if(targetParserStrategy == null){
            throw new RuntimeException("读取输入流失败，没有匹配的文件解析器...");
        }
        // 2. 获取 针迹开始偏移位置
        int frameOffset = targetParserStrategy.getFrameStartOffset(totalFileBytes);
        // 3. 获取 针迹字节数量
        int frameUsedBytes = targetParserStrategy.getAvailableBytesWithBytes(totalFileBytes);
        // 4. 执行读取针迹数
        List<UniFrame> uniFrames = targetParserStrategy.readFrames(totalFileBytes, frameOffset);
        // 5. 获取图形宽高、最大最小值
        int[] dimension = getDimension(uniFrames);
        // 6. 切分花样
        List<UniChildPattern> childFrames = targetParserStrategy.splitPattern(uniFrames);

        // 设置花样数据
//        result.setPatternName("NEW");
        // 设置针迹
//        result.setFrames(uniFrames);
        // 设置针数
//        result.setFrameNumber(uniFrames.size());
        // 左
//        result.setMinX(dimension[0]);
        // 上
//        result.setMaxY(dimension[1]);
        // 右
//        result.setMaxX(dimension[2]);
        // 下
//        result.setMinY(dimension[3]);
        // 子花样
//        result.setChildPatterns(childFrames);

        return result;
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

    protected IParserStrategy getTargetParserStrategy(byte[] totalFileBytes){
        for (IParserStrategy strategy : PARSER_LIST) {
            int len = strategy.getFileStartCodeLen();
            byte[] fileStartCodeFromInput = Arrays.copyOfRange(totalFileBytes, 0, strategy.getFileStartCodeLen());
            if(Arrays.equals(strategy.getFileStartCode(), fileStartCodeFromInput)) {
                return strategy;
            }
        }
        return null;
    }
}

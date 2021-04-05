package com.codeleven.core.parser.dxf;

import com.aspose.cad.fileformats.cad.DxfImage;
import com.aspose.cad.fileformats.cad.cadobjects.Cad2DPoint;
import com.aspose.cad.fileformats.cad.cadobjects.CadLine;
import com.aspose.cad.fileformats.cad.cadobjects.CadLwPolyline;
import com.aspose.cad.fileformats.cad.cadobjects.ICadBaseEntity;
import com.codeleven.common.entity.UniChildPattern;
import com.codeleven.common.entity.UniFrame;
import com.codeleven.common.entity.UniPoint;
import com.codeleven.core.utils.PatternUtil;
import com.codeleven.core.parser.IParserStrategy;
import com.codeleven.core.utils.DXFUtil;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * DXF解析的策略，只保证将点的数据读入，不保证能正确处理控制码，控制码得后期进行修正
 */
public class DXFParserStrategy implements IParserStrategy, IDXFFileParserStrategy {
    private static final String LAYER_PATTERN = "0";      // 花样数据
    private static final String LAYER_REF_POINT = "refer_point";      // 参考原点
    private static final String LAYER_BOARD_BORDER = "board_border";          // 板子的边框
    private static final String LAYER_BACKGROUND = "background";          // 板子的边框
    private DxfImage dxfImage;
    // 暂存分割的子花样，因为接口有点不合身，哈哈哈哈
    private final List<UniChildPattern> childList = new ArrayList<>();

    @Override
    public List<UniFrame> readFrames(byte[] totalBytes) {
        dxfImage = (DxfImage) DxfImage.load(new ByteArrayInputStream(totalBytes));
        List<ICadBaseEntity> entitiesFromLayer = DXFUtil.getEntitiesFromLayer(dxfImage, LAYER_PATTERN);

        // 初始的权重值
        int initWeight = Byte.MAX_VALUE;
        // 循环 PATTERN Layer里的Entity
        for (ICadBaseEntity iCadBaseEntity : entitiesFromLayer) {
            // 将Entity转换为 针迹列表
            List<UniFrame> childFrames = PatternUtil.convertChildPattern(iCadBaseEntity);
            // 创建子花样
            UniChildPattern child = new UniChildPattern();
            // 设置属性
            child.setPatternData(childFrames);
            child.setWeight(--initWeight);
            childList.add(child);
        }

        // 对子花样进行合并返回
        return PatternUtil.mergeChildPattern(childList);
    }

    @Override
    public List<UniChildPattern> splitPattern(List<UniFrame> frames) {
        return childList;
    }

    @Override
    public boolean isSupport(InputStream is) {
        return DxfImage.canLoad(is);
    }

    @Override
    public UniPoint getRefOriginPoint() {
        if(dxfImage != null){
            List<ICadBaseEntity> entitiesFromLayer = DXFUtil.getEntitiesFromLayer(dxfImage, LAYER_REF_POINT);
            if(entitiesFromLayer.size() == 2) {
                CadLine cadLine = (CadLine) entitiesFromLayer.get(0);
                CadLine cadLine1 = (CadLine) entitiesFromLayer.get(1);

                if(DXFUtil.isTwoLineIntersection(cadLine, cadLine1)){
                    return DXFUtil.getIntersectionPoint(cadLine, cadLine1);
                }
            } else if(entitiesFromLayer.size() == 1) {
                CadLwPolyline polyline = (CadLwPolyline) entitiesFromLayer.get(0);
                List<Cad2DPoint> coordinates = polyline.getCoordinates();
                int minX = Integer.MAX_VALUE;
                int maxX = Integer.MIN_VALUE;
                int minY = Integer.MAX_VALUE;
                int maxY = Integer.MIN_VALUE;
                for (Cad2DPoint coordinate : coordinates) {
                    if(coordinate.getX() > maxX){
                        maxX = (int) (coordinate.getX() * 100);
                    } else if(coordinate.getX() < minX){
                        minX = (int) (coordinate.getX() * 100);
                    }

                    if(coordinate.getY() > maxY) {
                        maxY = (int) (coordinate.getY() * 100);
                    } else if(coordinate.getY() < minY){
                        minY = (int) (coordinate.getY() * 100);
                    }
                }
                int deltX = (maxX - minX) / 2;
                int deltY = (maxY - minY) / 2;
                int centerX = minX + deltX;
                int centerY = minY + deltY;
                return new UniPoint((centerX / 10), (centerY / 10));
            }
        }
        throw new RuntimeException("没有找到参考原点的位置，请检查");
    }

    @Override
    public InputStream getBackgroundImage() {
        try {
            return DXFUtil.getBackgroundImage(dxfImage, LAYER_BACKGROUND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.codeleven.patternsystem.parser;

import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.DxfImage;
import com.aspose.cad.fileformats.cad.cadobjects.CadBaseObject;
import com.aspose.cad.fileformats.cad.cadobjects.CadLine;
import com.aspose.cad.fileformats.cad.cadobjects.CadRasterImageDef;
import com.aspose.cad.fileformats.cad.cadobjects.ICadBaseEntity;
import com.aspose.cad.fileformats.cad.cadtables.CadLayerTable;
import com.codeleven.patternsystem.dto.PatternChildPO;
import com.codeleven.patternsystem.dto.UniPatternPO;
import com.codeleven.patternsystem.dto.UniPoint;
import com.codeleven.patternsystem.entity.ChildPattern;
import com.codeleven.patternsystem.entity.UniChildPattern;
import com.codeleven.patternsystem.entity.UniFrame;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.utils.DXFUtil;
import com.codeleven.patternsystem.utils.PatternUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CADHelper {
    private static final String LAYER_PATTERN = "0";      // 花样数据
    private static final String LAYER_REF_POINT = "refer_point";      // 参考原点
    private static final String LAYER_BOARD_BORDER = "board_border";          // 板子的边框
    private static final String LAYER_BACKGROUND = "background";          // 板子的边框
    private DxfImage image;

    public CADHelper(InputStream is) {
        image = (DxfImage) Image.load(is);
    }

    public void loadChildPatternData(UniPattern uniPattern){
        Map<Long, UniChildPattern> childList = new HashMap<>();
        // 获取Pattern
        List<ICadBaseEntity> entitiesFromLayer = getEntitiesFromLayer(image, LAYER_PATTERN);
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
            child.setPattern(uniPattern);
            childList.put(0L, child);
        }
        // 将数据
        uniPattern.setChildList(childList);
    }

    public void loadRefPointData(UniPattern pattern) {
        // 获取Pattern
        List<ICadBaseEntity> entitiesFromLayer = getEntitiesFromLayer(image, LAYER_REF_POINT);
        if(entitiesFromLayer.size() != 2) {
            throw new RuntimeException("参考原点位置异常");
        }
        CadLine cadLine = (CadLine) entitiesFromLayer.get(0);
        CadLine cadLine1 = (CadLine) entitiesFromLayer.get(1);

        if(DXFUtil.isTwoLineIntersection(cadLine, cadLine1)){
            UniPoint intersectionPoint = DXFUtil.getIntersectionPoint(cadLine, cadLine1);
            pattern.setRefOrigin(intersectionPoint);
            return;
        }
        throw new RuntimeException("没有放置参考原点，请处理");
    }


    public void loadHeightWidthToPattern(UniPattern pattern) {
        pattern.setWidth(0);
        pattern.setHeight(0);
    }

    private static List<ICadBaseEntity> getEntitiesFromLayer(DxfImage image, String layerName) {
        List<ICadBaseEntity> result = new ArrayList<>();
        for (ICadBaseEntity entity : image.getEntities()) {
            if (entity.getLayerName().equals(layerName)) {
                result.add(entity);
            }
        }
        return result;
    }
}

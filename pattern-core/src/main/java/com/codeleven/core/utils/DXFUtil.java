package com.codeleven.core.utils;

import com.aspose.cad.fileformats.cad.DxfImage;
import com.aspose.cad.fileformats.cad.cadobjects.*;
import com.codeleven.common.entity.UniPoint;
import com.codeleven.common.entity.UniFrame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DXFUtil {

    /**
     * 处理 CAD 多段线
     *
     * @param entity
     * @return
     */
    public static List<UniFrame> convertLwPolyline(ICadBaseEntity entity) {
        assert entity instanceof CadLwPolyline;
        List<Cad2DPoint> coordinates = ((CadLwPolyline) entity).getCoordinates();
        List<UniFrame> uniFrameList = new LinkedList<>();
        for (int i = 0; i < coordinates.size(); i++) {
            Cad2DPoint point = coordinates.get(i);
            // 精确到0.1mm
            UniFrame frame = new UniFrame((int)(point.getX() * 10), (int)(point.getY() * 10));
            uniFrameList.add(frame);
        }
        return uniFrameList;
    }

    public static List<ICadBaseEntity> getEntitiesFromLayer(DxfImage image, String layerName) {
        List<ICadBaseEntity> result = new ArrayList<>();
        for (ICadBaseEntity entity : image.getEntities()) {
            if (entity.getLayerName().equals(layerName)) {
                result.add(entity);
            }
        }
        return result;
    }

    public static InputStream getBackgroundImage(DxfImage image, String layerName) throws FileNotFoundException {
        for (CadBaseObject object : image.getObjects()) {
            if(object instanceof CadRasterImageDef) {
                CadRasterImageDef imageDef = (CadRasterImageDef) object;
                if(imageDef.getImageIsLoadedFlag() == 1) {
                    return new FileInputStream(imageDef.getFileName());
                }
            }
        }
        throw new FileNotFoundException("文件没有找到");
    }

    /**
     * 判断两条直线是否相交
     * @param line1 线段1
     * @param line2 线段2
     * @return 是否相交
     */
    public static boolean isTwoLineIntersection(CadLine line1, CadLine line2) {
        Cad3DPoint first1 = line1.getFirstPoint();
        Cad3DPoint last1 = line1.getSecondPoint();

        Cad3DPoint first2 = line2.getFirstPoint();
        Cad3DPoint last2 = line2.getSecondPoint();

        double l1x1 = first1.getX(); double l1x2 = last1.getX();
        double l1y1 = first1.getY(); double l1y2 = last1.getY();

        double l2x1 = first2.getX(); double l2x2 = last2.getX();
        double l2y1 = first2.getY(); double l2y2 = last2.getY();

        //快速排斥实验
        if ((l1x1 > l1x2 ? l1x1 : l1x2) < (l2x1 < l2x2 ? l2x1 : l2x2) ||
                (l1y1 > l1y2 ? l1y1 : l1y2) < (l2y1 < l2y2 ? l2y1 : l2y2) ||
                (l2x1 > l2x2 ? l2x1 : l2x2) < (l1x1 < l1x2 ? l1x1 : l1x2) ||
                (l2y1 > l2y2 ? l2y1 : l2y2) < (l1y1 < l1y2 ? l1y1 : l1y2))
        {
            return false;
        }
        //跨立实验
        if ((((l1x1 - l2x1)*(l2y2 - l2y1) - (l1y1 - l2y1)*(l2x2 - l2x1))*
                ((l1x2 - l2x1)*(l2y2 - l2y1) - (l1y2 - l2y1)*(l2x2 - l2x1))) > 0 ||
                (((l2x1 - l1x1)*(l1y2 - l1y1) - (l2y1 - l1y1)*(l1x2 - l1x1))*
                        ((l2x2 - l1x1)*(l1y2 - l1y1) - (l2y2 - l1y1)*(l1x2 - l1x1))) > 0)
        {
            return false;
        }
        return true;
    }

    /**
     * 如果两条直线有相交，则获取相交的点
     * @param line1 线段1
     * @param line2 线段2
     * @return 相交的点
     */
    public static UniPoint getIntersectionPoint(CadLine line1, CadLine line2){
        Cad3DPoint first1 = line1.getFirstPoint();
        Cad3DPoint last1 = line1.getSecondPoint();

        Cad3DPoint first2 = line2.getFirstPoint();
        Cad3DPoint last2 = line2.getSecondPoint();

        double p1x = first1.getX(); double p2x = last1.getX();
        double p1y = first1.getY(); double p2y = last1.getY();

        double q1x = first2.getX(); double q2x = last2.getX();
        double q1y = first2.getY(); double q2y = last2.getY();

        //求交点
        double tmpLeft,tmpRight;
        tmpLeft = (q2x - q1x) * (p1y - p2y) - (p2x - p1x) * (q1y - q2y);
        tmpRight = (p1y - q1y) * (p2x - p1x) * (q2x - q1x) + q1x * (q2y - q1y) * (p2x - p1x) - p1x * (p2y - p1y) * (q2x - q1x);

        double x = tmpRight/tmpLeft;

        tmpLeft = (p1x - p2x) * (q2y - q1y) - (p2y - p1y) * (q1x - q2x);
        tmpRight = p2y * (p1x - p2x) * (q2y - q1y) + (q2x- p2x) * (q2y - q1y) * (p1y - p2y) - q2y * (q1x - q2x) * (p2y - p1y);
        double y = tmpRight/tmpLeft;

        UniPoint point = new UniPoint((int)(x * 10), (int)(y * 10));
        return point;
    }
}

package com.codeleven.patternsystem.dxf;

import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.DxfImage;
import com.aspose.cad.fileformats.cad.cadobjects.Cad2DPoint;
import com.aspose.cad.fileformats.cad.cadobjects.CadLwPolyline;
import com.aspose.cad.fileformats.cad.cadobjects.CadRasterImage;
import com.aspose.cad.fileformats.cad.cadobjects.ICadBaseEntity;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class DXFParserTest {
    @Test
    public void testDXFFileRead() throws FileNotFoundException {
//        DxfImage dxfImage = (DxfImage)Image.load(new FileInputStream("C:\\Users\\Administrator\\Desktop\\花样\\2021.3.22\\img059.dxf"));
        DxfImage dxfImage = (DxfImage)Image.load(new FileInputStream("C:\\Users\\Administrator\\Desktop\\Drawing3.dxf"));
        ICadBaseEntity[] entities = dxfImage.getEntities();
        for (ICadBaseEntity entity : entities) {
            if(entity instanceof CadLwPolyline) {
                List<Cad2DPoint> coordinates = ((CadLwPolyline) entity).getCoordinates();
                double sum = 0;
                for (int i = 0; i < coordinates.size() && (i + 2) < coordinates.size(); i++) {
                    Cad2DPoint p1 = coordinates.get(i);
                    Cad2DPoint p2 = coordinates.get(i + 1);
                    Cad2DPoint p3 = coordinates.get(i + 2);
                    double angleByThreeP = this.getAngleByThreeP(new double[]{p1.getX(), p2.getX(), p3.getX()}, new double[]{p1.getY(), p2.getY(), p3.getY()});
//                    if(Math.toDegrees(angleByThreeP) <90 ) {
                        System.out.println("三个点：" + i + "~" + (i + 2) + "形成角度：" + Math.toDegrees(angleByThreeP));
                        if( i < 27){
                            sum += (180 - Math.toDegrees(angleByThreeP));
                        }

//                    }
                }
                System.out.println(sum);
            } else if(entity instanceof CadRasterImage){
                System.out.println(entity);
            }
        }
        DxfImage dxfImage1 = new DxfImage();

    }

    double getAngleByThreeP(double[] pointx, double[] pointy)
    {
        double a_b_x = pointx[0] - pointx[1];

        double a_b_y = pointy[0] - pointy[1];

        double c_b_x = pointx[2] - pointx[1];

        double c_b_y = pointy[2] - pointy[1];

        double ab_mul_cb = a_b_x * c_b_x + a_b_y * c_b_y;

        double dist_ab = Math.sqrt(a_b_x * a_b_x + a_b_y * a_b_y);

        double dist_cd = Math.sqrt(c_b_x * c_b_x + c_b_y * c_b_y);

        double cosValue = ab_mul_cb / (dist_ab * dist_cd);

        return Math.acos(cosValue);

    }
}

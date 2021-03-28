package com.codeleven.patternsystem.dxf;

import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.DxfImage;
import com.aspose.cad.fileformats.cad.cadobjects.ICadBaseEntity;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DXFParserTest {
    @Test
    public void testDXFFileRead() throws FileNotFoundException {
        DxfImage dxfImage = (DxfImage)Image.load(new FileInputStream("C:\\Users\\Administrator\\Desktop\\花样\\plus2021.3.25\\391.dxf"));
        ICadBaseEntity[] entities = dxfImage.getEntities();
        System.out.println(entities);
    }
}

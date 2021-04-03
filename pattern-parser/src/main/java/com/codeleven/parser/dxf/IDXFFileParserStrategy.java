package com.codeleven.parser.dxf;

import com.codeleven.common.entity.UniPoint;

import java.io.InputStream;

public interface IDXFFileParserStrategy {
    /**
     * 获取参考原点得位置
     * @return 参考原点得数据
     */
    UniPoint getRefOriginPoint();

    /**
     * !Beta!
     * 获取背景图片的流（DXFImage通常只保存一个路径，并获取不到数据流）
     * @return
     */
    InputStream getBackgroundImage();
}

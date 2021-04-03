package com.codeleven.patternsystem.dto;

import com.codeleven.common.entity.SimpleUser;
import com.codeleven.common.entity.UniPoint;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UniPatternPO {
    private long id;
    private int width;                  // 单位 毫米
    private int height;                 // 单位 毫米
    private Date createTime;            // 创建时间
    private Date updateTime;            // 更新时间
    private int shoesSize;              // 花样对应的鞋子尺码
    private String name;                // 标识花样
    private SimpleUser user;            // 用户
    private UniPoint refOrigin;
    private String coverPath;                       // 封面路径（来自文件对象服务）
    private String dxfPath;                         // DXF花样数据路径（来自文件对象服务）
    private List<PatternChildPO> childDtoList;     // 子花样
    private int offsetX;
    private int offsetY;
}
package com.codeleven.common.entity;

import com.codeleven.common.constants.LockMethod;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class UniPattern {
    private long id;
    private int width;                  // 单位 毫米
    private int height;                 // 单位 毫米
    private Date createTime;            // 创建时间
    private Date updateTime;            // 更新时间
    private int shoesSize;              // 花样对应的鞋子尺码
    private String name;                // 标识花样
    private SimpleUser user;            // 用户
    private UniPoint refOrigin;
    private UniPoint secondOrigin;      // 次元点（基本以第一帧作为次元点）
    private String coverPath;                       // 封面路径（来自文件对象服务）
    private String dxfPath;                         // DXF花样数据路径（来自文件对象服务）
    private Map<Long, UniChildPattern> childList;     // 子花样
    private boolean checkSimilarPoint;  // 是否检查相似点（距离只有1的），弹窗提示，如果确认就继续，否认就结束
    private int offsetX;                // 记录用户给定的偏移数值，用于记录
    private int offsetY;                // 记录用户给定的偏移，
}

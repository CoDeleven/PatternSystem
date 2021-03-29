package com.codeleven.patternsystem.dto;

import com.codeleven.patternsystem.common.ShoesSize;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.parser.PatternSystemVendor;

import java.util.Date;

/**
 * 鞋子花样DTO
 * @author CoDeleven
 */
public class PatternDto {
    private long id;
    private PatternSystemVendor vendor;
    private int width;          // 单位 毫米
    private int height;         // 单位 毫米
    private Date createTime;    // 创建时间
    private Date updateTime;    // 更新时间
    private ShoesSize shoesSize;// 花样对应的鞋子尺码
    private String name;        // 标识花样
    private SimpleUser user;    // 用户
    private String coverPath4Minio;       // 封面
    private String patternPath4Minio;  // 花样数据Url（来自文件对象服务）
    private UniPattern uniPattern;

    public UniPattern getUniPattern() {
        return uniPattern;
    }

    public void setUniPattern(UniPattern uniPattern) {
        this.uniPattern = uniPattern;
    }

    public String getPatternPath4Minio() {
        return patternPath4Minio;
    }

    public void setPatternPath4Minio(String patternPath4Minio) {
        this.patternPath4Minio = patternPath4Minio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PatternSystemVendor getVendor() {
        return vendor;
    }

    public void setVendor(PatternSystemVendor vendor) {
        this.vendor = vendor;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public ShoesSize getShoesSize() {
        return shoesSize;
    }

    public void setShoesSize(ShoesSize shoesSize) {
        this.shoesSize = shoesSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SimpleUser getUser() {
        return user;
    }

    public void setUser(SimpleUser user) {
        this.user = user;
    }

    public String getCoverPath4Minio() {
        return coverPath4Minio;
    }

    public void setCoverPath4Minio(String coverPath4Minio) {
        this.coverPath4Minio = coverPath4Minio;
    }
}

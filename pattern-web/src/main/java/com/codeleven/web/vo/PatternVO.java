package com.codeleven.web.vo;

import lombok.Data;

/**
 * 正常来说的列表 Entry VO
 * 一般不单独使用，配合列表类使用
 */
@Data
public class PatternVO {
    private long id;
    private String name;
    private String size;
    private String coverUrl;
    private String createDate;
}

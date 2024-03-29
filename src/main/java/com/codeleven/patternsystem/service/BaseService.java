package com.codeleven.patternsystem.service;

import cn.hutool.core.util.StrUtil;
import com.codeleven.patternsystem.dto.Page;
import com.codeleven.patternsystem.utils.RequestUtils;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import org.apache.catalina.util.RequestUtil;

public class BaseService<T> {
    private static final String PARAM_SORT_KEY_NAME = "sort_key";
    private static final String PARAM_SORT_ORDER_NAME = "sort_order";

    protected Page<T> queryForPage(int page, int length, ISelect select){
        return new Page<>(this.internalQueryForPage(page, length, select));
    }

    private com.github.pagehelper.Page<T> internalQueryForPage(int pageNum, int pageSize, ISelect select){
        sortPage();
        return PageHelper.startPage(pageNum, pageSize).doSelectPage(select);
    }

    private void sortPage(){
        String sortKey = RequestUtils.getParam(PARAM_SORT_KEY_NAME);
        String sortOrder = RequestUtils.getParam(PARAM_SORT_ORDER_NAME);
        if(StrUtil.isNotEmpty(sortKey)){
            String orderStr = sortKey;
            if(StrUtil.isNotEmpty(sortOrder)){
                orderStr += StrUtil.SPACE +sortOrder;
            }
            PageHelper.orderBy(orderStr);
        }
    }

}

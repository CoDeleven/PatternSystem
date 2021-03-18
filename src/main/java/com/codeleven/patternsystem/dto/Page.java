package com.codeleven.patternsystem.dto;

import java.util.List;

public class Page<T> {

    /**
     * 当前页码
     */
    private int page;

    /**
     * 总页数
     */
    private int total;

    /**
     * 总记录数
     */
    private long records;

    /**
     * 分页数据
     */
    private List<T> root;


    public Page(com.github.pagehelper.Page<T> githubPage) {
        this.page = githubPage.getPageNum();
        this.total = githubPage.getPages();
        this.root = githubPage;
        this.records = githubPage.getTotal();
    }

    public Page() {
        this.page = 1;
        this.total = 0;
        this.records = 0;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
    }

    public List<T> getRoot() {
        return root;
    }

    public void setRoot(List<T> root) {
        this.root = root;
    }
}

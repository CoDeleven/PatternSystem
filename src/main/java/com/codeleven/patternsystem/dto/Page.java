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
     * 异常消息
     */
    private List<String> message;

    /**
     * 分页数据
     */
    private List<T> root;

    /**
     * 状态
     */
    private int code;

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
        this.code = 0;
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

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public List<T> getRoot() {
        return root;
    }

    public void setRoot(List<T> root) {
        this.root = root;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

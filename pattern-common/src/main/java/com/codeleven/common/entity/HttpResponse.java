package com.codeleven.common.entity;

import java.io.Serializable;

/**
 * Ajax 返回结构
 * @param <T>
 */
public class HttpResponse<T> implements Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 1L;

    /**
     * 消息
     */
    private String message;

    /**
     * 消息类型
     */
    private int errorCode;

    /**
     * 数据对象
     */
    private T data;

    public HttpResponse(String message, int errorCode, T data) {
        this.message = message;
        this.errorCode = errorCode;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

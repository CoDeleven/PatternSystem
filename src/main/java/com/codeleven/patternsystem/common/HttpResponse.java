package com.codeleven.patternsystem.common;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class HttpResponse implements Serializable {

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
    private Object data;

    public HttpResponse(String message, int errorCode, Object data) {
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

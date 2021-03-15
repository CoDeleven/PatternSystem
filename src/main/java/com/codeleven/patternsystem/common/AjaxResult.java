package com.codeleven.patternsystem.common;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class AjaxResult implements Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 1L;

    /**
     * 状态码，默认为200
     */
    private final int statuCode = HttpStatus.OK.value();

    /**
     * 消息
     */
    private String message;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 数据对象
     */
    private Object data;

    public AjaxResult(String message, String messageType, Object data) {
        this.message = message;
        this.messageType = messageType;
        this.data = data;
    }

    public int getStatuCode() {
        return statuCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

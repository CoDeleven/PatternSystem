package com.codeleven.patternsystem.common;

public enum MessageType {
    /**
     * 提示消息
     */
    INFO("info"),
    /**
     * 警告消息
     */
    WARNING("warning"),
    /**
     * 错误消息
     */
    ERROR("error"),
    /**
     * 确认消息
     */
    CONFIRM("confirm");

    /**
     * 消息代码
     */
    private final String code;

    /**
     * 构造方法
     *
     * @param code 消息代码
     */
    MessageType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}

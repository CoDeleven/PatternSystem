package com.codeleven.patternsystem.dto;

public class SimpleUser {
    private int id;             // 账号ID 方便检索
    private String nick;        // 昵称，用于界面显示
    private String avatar;      // 头像

    public SimpleUser(int id, String nick, String avatar) {
        this.id = id;
        this.nick = nick;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

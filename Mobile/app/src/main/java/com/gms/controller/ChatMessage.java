package com.gms.controller;



public class ChatMessage {

    private String nickname;
    private String message ;

    public ChatMessage(){

    }
    public ChatMessage(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

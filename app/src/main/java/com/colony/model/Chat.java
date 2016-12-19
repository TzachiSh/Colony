package com.colony.model;

/**
 * Created by zahi on 17/09/2016.
 */
public class Chat {
    private int chatId;
    private String title;
    private String message;
    private String number;
    private boolean isGroup = false;
    public Chat()
    {



    }

    public Chat(String title, String message, String number,boolean isGroup) {
        this.title = title;
        this.message = message;
        this.number = number;
        this.isGroup = isGroup;
    }




    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public long getId() {
        return chatId;
    }

    public String getNumber() {
        return number;
    }

    public boolean isGroup() {
        return isGroup;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "ChatId=" + chatId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

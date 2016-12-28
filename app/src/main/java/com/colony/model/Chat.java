package com.colony.model;

/**
 * Created by zahi on 17/09/2016.
 */
public class Chat {
    private int chatId;
    private String title;
    private String message;
    private String number;
    private boolean group;
    public Chat()
    {



    }

    public Chat(String title, String message, String number,boolean group) {
        this.title = title;
        this.message = message;
        this.number = number;
        this.group = group;
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

    public boolean getGroup() {
        return group;
    }
}

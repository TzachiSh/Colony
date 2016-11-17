package com.colony.model;

/**
 * Created by zahi on 17/09/2016.
 */
public class Chat {

    private String title;
    private String message;
    private String number;
    private long chatId, dateCreatedMilli;

    public Chat(String title, String message, String number) {
        this.title = title;
        this.message = message;
        this.number = number;
    }

    public Chat(String title, String message, String number, long chatId, long dateCreatedMilli) {
        this.title = title;
        this.message = message;
        this.number = number;
        this.chatId = chatId;
        this.dateCreatedMilli = dateCreatedMilli;
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

    public long getDate() {
        return dateCreatedMilli;
    }

    public String getNumber() {
        return number;
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

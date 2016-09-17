package com.colony.model;

/**
 * Created by zahi on 17/09/2016.
 */
public class Chat {

    private  String title ,message;
    private  long chatId ,dateCreatedMilli;

    public Chat(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public Chat(String title, String message, long chatId, long dateCreatedMilli) {
        this.title = title;
        this.message = message;
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

    @Override
    public String toString() {
        return "Chat{" +
                "ChatId=" + chatId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

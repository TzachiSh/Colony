package com.colony.model;

/**
 * Created by zahi on 17/09/2016.
 */
public class Chat {

    private  String title ,message;
    private  long userId ,dateCreatedMilli;

    public Chat(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public Chat(String title, String message, long userId, long dateCreatedMilli) {
        this.title = title;
        this.message = message;
        this.userId = userId;
        this.dateCreatedMilli = dateCreatedMilli;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public long getId() {
        return userId;
    }

    public long getDate() {
        return dateCreatedMilli;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

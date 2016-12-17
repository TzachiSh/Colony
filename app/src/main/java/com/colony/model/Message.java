package com.colony.model;

public class Message {
    private int usersId;
    private String userNumber;
    private String message;
    private String sentAt;
    private String name;

    public Message(int usersId,String userNumber, String message, String sentAt, String name) {
        this.usersId = usersId;
        this.userNumber = userNumber;
        this.message = message;
        this.sentAt = sentAt;
        this.name = name;

    }



    public int getUsersId() {
        return usersId;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getSentAt() {
        return sentAt;
    }

    public String getName() {
        return name;
    }
}
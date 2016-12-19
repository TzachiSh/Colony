package com.colony.model;

public class Message {
    private String userNumber;
    private String message;
    private String sentAt;
    private String name;

    public Message()
    {


    }

    public Message(String userNumber, String message, String sentAt, String name) {
        this.userNumber = userNumber;
        this.message = message;
        this.sentAt = sentAt;
        this.name = name;

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
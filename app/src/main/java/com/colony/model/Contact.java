package com.colony.model;

/**
 * Created by zahi on 04/11/2016.
 */

public class Contact {

    private int contactId;
    private String name;
    private String number;
    private String picture;

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getContactId() {
        return contactId;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getPicture() {
        return picture;
    }
}

package com.colony.model;

/**
 * Created by zahi on 10/01/2017.
 */

public class CheckList {
    private int checkListId;
    private String name;
    private int count;
    private String userUpdate;
    private boolean checked;

    public CheckList()
    {



    }
    public CheckList(int checkListId ,String name,int count,String userUpdate)
    {
        this.checkListId = checkListId;
        this.name = name;
        this.count = count;
        this.userUpdate = userUpdate;

    }

    public int getCheckListId() {
        return checkListId;
    }

    public void setCheckListId(int checkListId) {
        this.checkListId = checkListId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(String userUpdate) {
        this.userUpdate = userUpdate;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

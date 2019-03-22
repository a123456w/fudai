package com.ruirong.chefang.bean;

/**
 * Created by chenlipeng on 2018/1/4 0004
 * describe:  商家头条 预定房间
 */
public class ReserceRoomNumberBean {
    private String name;
    private boolean isChoice;

    public ReserceRoomNumberBean() {
        this.isChoice = false;
    }

    public boolean isChoice() {
        return isChoice;
    }

    public void setChoice(boolean choice) {
        isChoice = choice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

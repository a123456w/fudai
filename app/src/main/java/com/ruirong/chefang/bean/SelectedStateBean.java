package com.ruirong.chefang.bean;

/**
 * Created by Administrator on 2018/3/23.
 */

public class SelectedStateBean {

    private String title;
    private String name;
    private boolean isTrue;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SelectedStateBean() {
        this.isTrue = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }
}

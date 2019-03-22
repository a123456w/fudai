package com.ruirong.chefang.bean;

/*
 *  Created by 李  on 2018/11/21.
 */
public class ScreenItem {


    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    int isSelect;
    public ScreenItem() {
    }

    public ScreenItem(int isSelect, String name, String id) {
        this.isSelect = isSelect;
        this.name = name;
        this.id = id;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
}

package com.ruirong.chefang.bean;

/**
 * Created by dillon on 2017/4/5.
 */

public class OrderBean {

    /**
     * create_time : 1492586371
     * type : 1
     * type_id : 1
     * money : 12
     * typeName : 商家消费
     */

    private String create_time;
    private String type;
    private String type_id;
    private String money;
    private String typeName;
    public String id;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}

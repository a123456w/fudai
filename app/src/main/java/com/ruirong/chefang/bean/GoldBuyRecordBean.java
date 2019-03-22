package com.ruirong.chefang.bean;

/**
 * Created by EDZ on 2018/3/23.
 */

public class GoldBuyRecordBean {
    /**
     * id : 178
     * create_time : 1498629591
     * type : 3
     * type_id : 111
     * money : 1.00
     * typeName : 金豆购买
     */

    private String id;
    private String create_time;
    private String type;
    private String type_id;
    private String money;
    private String typeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

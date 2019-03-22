package com.ruirong.chefang.bean;

/**
 * Created by EDZ on 2018/3/23.
 */

public class OrderDetailBean {
    /**
     * order_id : 49123420170628015944
     * status : 充值成功
     * create_time : 1498629584
     * money : 1.00
     * typeName : 金豆购买
     * type : 3
     */

    private String order_id;
    private String status;
    private String create_time;
    private String money;
    private String typeName;
    private String type;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

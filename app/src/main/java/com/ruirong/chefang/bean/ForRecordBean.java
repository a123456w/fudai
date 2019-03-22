package com.ruirong.chefang.bean;

/**
 * Created by 16690 on 2018/3/23.
 * 兑换记录实体
 */

public class ForRecordBean {

    /**
     * id : 11813
     * create_time : 1521180874
     * type : 4
     * type_id : 1346
     * money : 500.00
     * typeName : 金豆兑现
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

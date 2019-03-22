package com.ruirong.chefang.bean;

/**
 * Created by dillon on 2017/5/5.
 */

public class SilverTransferDetailBean {
    /**
     * order_id : 1277050280      订单编号
     * transfer_mob : 13693614681    收款人手机号
     * transfer_money : 90       转账金额
     * create_time : 1493867110     转账时间
     * type : 1     转账类型
     */

    private String order_id;
    private String transfer_mob;
    private String transfer_money;
    private String create_time;
    private int type;
    private String race_mob ;

    public String getRace_mob() {
        return race_mob;
    }

    public void setRace_mob(String race_mob) {
        this.race_mob = race_mob;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTransfer_mob() {
        return transfer_mob;
    }

    public void setTransfer_mob(String transfer_mob) {
        this.transfer_mob = transfer_mob;
    }

    public String getTransfer_money() {
        return transfer_money;
    }

    public void setTransfer_money(String transfer_money) {
        this.transfer_money = transfer_money;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

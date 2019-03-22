package com.ruirong.chefang.bean;

/**
 * Created by Administrator on 2018/4/8.
 */

public class GoodsBuyConfirmBean {

    /**
     * is_has_pay_pass : 1
     * shop_id : 37
     * name : 好声音11
     * silver : 0.00
     */

    private int is_has_pay_pass;
    private String shop_id;
    private String name;
    private String silver;

    public int getIs_has_pay_pass() {
        return is_has_pay_pass;
    }

    public void setIs_has_pay_pass(int is_has_pay_pass) {
        this.is_has_pay_pass = is_has_pay_pass;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSilver() {
        return silver;
    }

    public void setSilver(String silver) {
        this.silver = silver;
    }
}

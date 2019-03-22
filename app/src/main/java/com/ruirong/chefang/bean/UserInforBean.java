package com.ruirong.chefang.bean;

import java.io.Serializable;

/**
 * Created by dillon on 2017/5/8.
 */

public class UserInforBean implements Serializable{


    /**
     * mobile : 18210700961
     * pic : /Uploads/Picture/2016-09-19/57dfa11ed4873.png
     * level : 1
     * is_card : 0
     * role : 1
     * name : 易家福袋9081
     * sex : null
     * create_time : 1505291393
     * pay_pass : e10adc3949ba59abbe56e057f20f883e
     * is_partner : 0
     * back_money : 11800
     * yester_money : 0
     * is_has_pay_pass : 1
     */

    private String mobile;
    private String pic;
    private String level;
    private String is_card;
    private String role;
    private String name;
    private String sex;
    private String create_time;
    private String pay_pass;
    private String is_partner;
    private String back_money;
    private String yester_money;
    private int is_has_pay_pass;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIs_card() {
        return is_card;
    }

    public void setIs_card(String is_card) {
        this.is_card = is_card;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPay_pass() {
        return pay_pass;
    }

    public void setPay_pass(String pay_pass) {
        this.pay_pass = pay_pass;
    }

    public String getIs_partner() {
        return is_partner;
    }

    public void setIs_partner(String is_partner) {
        this.is_partner = is_partner;
    }

    public String getBack_money() {
        return back_money;
    }

    public void setBack_money(String back_money) {
        this.back_money = back_money;
    }

    public String getYester_money() {
        return yester_money;
    }

    public void setYester_money(String yester_money) {
        this.yester_money = yester_money;
    }

    public int getIs_has_pay_pass() {
        return is_has_pay_pass;
    }

    public void setIs_has_pay_pass(int is_has_pay_pass) {
        this.is_has_pay_pass = is_has_pay_pass;
    }
}

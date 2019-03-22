package com.ruirong.chefang.bean;

/**
 * Created by dillon on 2017/5/12.
 */

public class WalletBean {

    /**
     * gold : 2.00
     * silver : 0.00
     * house_fund : 0.00
     * total_money : 2.00
     * cash_earning : 0.00
     */

    private String gold;
    private String silver;
    private String house_fund;
    private String total_money;
    private String cash_earning;

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getSilver() {
        return silver;
    }

    public void setSilver(String silver) {
        this.silver = silver;
    }

    public String getHouse_fund() {
        return house_fund;
    }

    public void setHouse_fund(String house_fund) {
        this.house_fund = house_fund;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getCash_earning() {
        return cash_earning;
    }

    public void setCash_earning(String cash_earning) {
        this.cash_earning = cash_earning;
    }
}

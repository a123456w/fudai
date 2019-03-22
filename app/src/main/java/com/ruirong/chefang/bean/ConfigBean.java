package com.ruirong.chefang.bean;

/**
 * Created by dillon on 2017/6/11.
 */

public class ConfigBean {


    /**
     * recharge_bow_fund : 0.5      送用户福币的比例
     * recomm_back_bow : 0.02       送推荐人的福币的比例
     * day_back_bow : 2.0E-4        福币每日返的比例
     * developer_bow_fund : 0.1     送楼盘开发商福币比例
     * shop_bow_fund : 0.1          送商家福币比例
     * withdraw_user_cash : 0.1     用户提现收比例佣金
     * withdraw_shop_cash : 0.1     商户提现收比例佣金
     */

    private double recharge_bow_fund;
    private double recomm_back_bow;
    private double day_back_bow;
    private double developer_bow_fund;
    private double shop_bow_fund;
    private double withdraw_user_cash;
    private double withdraw_shop_cash;

    public double getRecharge_bow_fund() {
        return recharge_bow_fund;
    }

    public void setRecharge_bow_fund(double recharge_bow_fund) {
        this.recharge_bow_fund = recharge_bow_fund;
    }

    public double getRecomm_back_bow() {
        return recomm_back_bow;
    }

    public void setRecomm_back_bow(double recomm_back_bow) {
        this.recomm_back_bow = recomm_back_bow;
    }

    public double getDay_back_bow() {
        return day_back_bow;
    }

    public void setDay_back_bow(double day_back_bow) {
        this.day_back_bow = day_back_bow;
    }

    public double getDeveloper_bow_fund() {
        return developer_bow_fund;
    }

    public void setDeveloper_bow_fund(double developer_bow_fund) {
        this.developer_bow_fund = developer_bow_fund;
    }

    public double getShop_bow_fund() {
        return shop_bow_fund;
    }

    public void setShop_bow_fund(double shop_bow_fund) {
        this.shop_bow_fund = shop_bow_fund;
    }

    public double getWithdraw_user_cash() {
        return withdraw_user_cash;
    }

    public void setWithdraw_user_cash(double withdraw_user_cash) {
        this.withdraw_user_cash = withdraw_user_cash;
    }

    public double getWithdraw_shop_cash() {
        return withdraw_shop_cash;
    }

    public void setWithdraw_shop_cash(double withdraw_shop_cash) {
        this.withdraw_shop_cash = withdraw_shop_cash;
    }
}

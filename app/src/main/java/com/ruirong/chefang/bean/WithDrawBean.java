package com.ruirong.chefang.bean;

/**
 * 提现bean
 */
public class WithDrawBean {

    /**
     * order_id : 91940720170620032242
     * status : 1
     * create_time : 1497943362
     * money : 2000.00
     * final_money : 1800.00
     * typename : 余额提现
     * shouxufei : 200
     */

    private String order_id;
    private String status;
    private String create_time;
    private String money;
    private String final_money;
    private String typename;
    private String shouxufei;

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

    public String getFinal_money() {
        return final_money;
    }

    public void setFinal_money(String final_money) {
        this.final_money = final_money;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getShouxufei() {
        return shouxufei;
    }

    public void setShouxufei(String shouxufei) {
        this.shouxufei = shouxufei;
    }
}

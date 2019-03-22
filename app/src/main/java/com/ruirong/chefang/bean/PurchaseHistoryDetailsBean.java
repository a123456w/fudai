package com.ruirong.chefang.bean;

/**
 * Created by EDZ on 2018/3/29.
 */

public class PurchaseHistoryDetailsBean{
    /**
     * price : 0.00
     * msg : 购买店铺商品,订单号:73937420180320102109
     * symbol : -
     * create_time : 1458719710
     * order_id : 73937420180320102109
     * type : 1
     * pay_type : 1
     */

    private String price;
    private String msg;
    private String symbol;
    private String create_time;
    private String order_id;
    private String type;
    private String pay_type;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }
}

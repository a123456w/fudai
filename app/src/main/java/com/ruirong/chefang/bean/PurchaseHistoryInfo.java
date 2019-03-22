package com.ruirong.chefang.bean;

/**
 * Created by EDZ on 2018/3/29.
 */

public class PurchaseHistoryInfo {
    /**
     * id : 15
     * price : 39.00
     * symbol : -
     * msg : 购买店铺商品,订单号:73937420180320102109
     * type : 1
     * create_time : 1461398110
     */

    private String id;
    private String price;
    private String symbol;
    private String msg;
    private String type;
    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}

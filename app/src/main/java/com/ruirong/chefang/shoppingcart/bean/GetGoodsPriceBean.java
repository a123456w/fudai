package com.ruirong.chefang.shoppingcart.bean;

/**
 * Created by guo on 2017/8/22.
 */

public class GetGoodsPriceBean {

    /**
     * price : 12.00
     * store_count : 13
     */

    private String price;
    private int store_count;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getStore_count() {
        return store_count;
    }

    public void setStore_count(int store_count) {
        this.store_count = store_count;
    }
}

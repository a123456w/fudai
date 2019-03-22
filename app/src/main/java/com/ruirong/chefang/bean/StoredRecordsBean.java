package com.ruirong.chefang.bean;

/**
 * Created by 16690 on 2018/3/23.
 * 储值记录实体类
 */

public class StoredRecordsBean {

    /**
     * shop_name : 文海酒店
     * price : 100.00
     * actualPrice : 120.00
     * create_time : 2147483647
     * logo : /Uploads/Picture/2018-01-13/5a59fc53d9d4a.jpg
     */

    private String shop_name;
    private String price;
    private String actualPrice;
    private String create_time;
    private String logo;

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}

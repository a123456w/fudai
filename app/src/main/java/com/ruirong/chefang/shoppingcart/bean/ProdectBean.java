package com.ruirong.chefang.shoppingcart.bean;

/**
 * Created by dillon on 2017/7/25.
 */

public class ProdectBean extends CartBaseBean {
    private String imageUrl;
    private String desc;
    private double price;
    private int count;
    private int position;
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ProdectBean() {
        super();
    }

    public ProdectBean(String id, String name, String imageUrl, String desc, double price, int count, int position) {
        super(id, name);
        this.imageUrl = imageUrl;
        this.desc = desc;
        this.price = price;
        this.count = count;
        this.position = position;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

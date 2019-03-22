package com.ruirong.chefang.bean;

import java.util.List;

/**
 * 特产详情
 * Created by BX on 2018/2/27.
 */

public class SpecialtyDetailsBean {

    /**
     * id : 49
     * type_id : 14
     * lb : ["/Uploads/Picture/2018-01-13/5a59b4da86e77.jpg"]
     * now_price : 23.00
     * before_price : 23.00
     * name : 啊二个如果
     * xnum : 34
     * pinglun : 1
     * goods : [{"key":"二个如果","value":"333"},{"key":"色如何热合热听话","value":"32453"},{"key":"色个人股","value":"345"}]
     * collection : 0
     * yieldd : /index?s=Home/Index/yieldd/id/49
     */

    private String id;
    private String type_id;
    private String now_price;
    private String before_price;
    private String name;
    private String xnum;
    private String pinglun;
    private int collection;
    private String yieldd;
    private List<String> lb;
    private List<GoodsBean> goods;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getNow_price() {
        return now_price;
    }

    public void setNow_price(String now_price) {
        this.now_price = now_price;
    }

    public String getBefore_price() {
        return before_price;
    }

    public void setBefore_price(String before_price) {
        this.before_price = before_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXnum() {
        return xnum;
    }

    public void setXnum(String xnum) {
        this.xnum = xnum;
    }

    public String getPinglun() {
        return pinglun;
    }

    public void setPinglun(String pinglun) {
        this.pinglun = pinglun;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public String getYieldd() {
        return yieldd;
    }

    public void setYieldd(String yieldd) {
        this.yieldd = yieldd;
    }

    public List<String> getLb() {
        return lb;
    }

    public void setLb(List<String> lb) {
        this.lb = lb;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * key : 二个如果
         * value : 333
         */

        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

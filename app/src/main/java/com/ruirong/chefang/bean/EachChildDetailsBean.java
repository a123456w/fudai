package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/7.
 */

public class EachChildDetailsBean {


    /**
     * id : 1
     * title : 风云诡谲弓发挥稳定
     * loop_pics : ["/Uploads/Picture/2018-01-13/5a59b4da86e77.jpg","/Uploads/Picture/2018-01-13/5a59b5120ebb4.jpg"]
     * before_price : 456.00
     * now_price : 34.00
     * sales : 45
     * attr : 45/k
     * deadline : 0
     * goods : [{"key":"阿二个如果","value":"345"},{"key":"认同感和家人通话","value":"345"},{"key":"物色个","value":"46"}]
     * ts_num : 0
     * bagdetails : /index?s=Home/Index/bagdetails/id/1
     */

    private String id;
    private String title;
    private String before_price;
    private String now_price;
    private String sales;
    private String attr;
    private int deadline;
    private String ts_num;
    private String bagdetails;
    private List<String> loop_pics;
    private List<GoodsBean> goods;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBefore_price() {
        return before_price;
    }

    public void setBefore_price(String before_price) {
        this.before_price = before_price;
    }

    public String getNow_price() {
        return now_price;
    }

    public void setNow_price(String now_price) {
        this.now_price = now_price;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public String getTs_num() {
        return ts_num;
    }

    public void setTs_num(String ts_num) {
        this.ts_num = ts_num;
    }

    public String getBagdetails() {
        return bagdetails;
    }

    public void setBagdetails(String bagdetails) {
        this.bagdetails = bagdetails;
    }

    public List<String> getLoop_pics() {
        return loop_pics;
    }

    public void setLoop_pics(List<String> loop_pics) {
        this.loop_pics = loop_pics;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * key : 阿二个如果
         * value : 345
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

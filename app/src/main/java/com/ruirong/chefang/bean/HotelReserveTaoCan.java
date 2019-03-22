package com.ruirong.chefang.bean;

import java.util.List;

/**预订订单套餐
 * Created by dillon on 2018/5/11.
 */

public class HotelReserveTaoCan {

    /**
     * id : 13
     * name : 清雅大床房（不含早）
     * specif : ["无早餐可单独选购","免费WiFi和宽带","代客泊车"]
     * attribute : [{"key":"床型","value":"2.0×2.0米大床1张"},{"key":"早餐","value":"无（可在单品中选购）"},{"key":"窗户","value":"有"},{"key":"卫浴","value":"独立"},{"key":"可住","value":"2人"},{"key":"面积","value":"28㎡"},{"key":"停车位","value":"免费停车"}]
     * loop_pics : ["/Uploads/Picture/2018-05-09/5af28dc083f32.jpg","/Uploads/Picture/2018-05-09/5af28db6eaf49.jpg","/Uploads/Picture/2018-05-09/5af28dbb99379.jpg"]
     * price : 238.00
     * yuan_price : 358.00
     * cover : /Uploads/Picture/2018-05-09/5af28dc083f32.jpg
     * num : 6
     * is_kai : 1
     * is_full : 1
     */

    private String id;
    private String name;
    private String price;
    private String yuan_price;
    private String cover;
    private String num;
    private String is_kai;
    private int is_full;
    private List<String> specif;
    private List<AttributeBean> attribute;
    private List<String> loop_pics;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getYuan_price() {
        return yuan_price;
    }

    public void setYuan_price(String yuan_price) {
        this.yuan_price = yuan_price;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getIs_kai() {
        return is_kai;
    }

    public void setIs_kai(String is_kai) {
        this.is_kai = is_kai;
    }

    public int getIs_full() {
        return is_full;
    }

    public void setIs_full(int is_full) {
        this.is_full = is_full;
    }

    public List<String> getSpecif() {
        return specif;
    }

    public void setSpecif(List<String> specif) {
        this.specif = specif;
    }

    public List<AttributeBean> getAttribute() {
        return attribute;
    }

    public void setAttribute(List<AttributeBean> attribute) {
        this.attribute = attribute;
    }

    public List<String> getLoop_pics() {
        return loop_pics;
    }

    public void setLoop_pics(List<String> loop_pics) {
        this.loop_pics = loop_pics;
    }

    public static class AttributeBean {
        /**
         * key : 床型
         * value : 2.0×2.0米大床1张
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

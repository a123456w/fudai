package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/23.
 */

public class ReservepackageBean {

    /**
     * id : 7
     * name : 抱枕大甩卖
     * goodids : [{"id":"50","name":"大号仿真创意烟盒抱枕靠垫","num":"888"},{"id":"49","name":"僵小鱼玩具公仔抱枕","num":"888"}]
     * shop_price : 300.00
     * all_price : 150.00
     * pic : /Uploads/Picture/2018-03-20/5ab0a9651c955.jpg
     * max_buy : 888
     * dicinfo : 抱枕大甩卖
     * shop_id : 241
     * dao_time : ["11.22-22.33","11.22-22.33","11.22-22.33"]
     * moban : 1
     * shopname : 郭江美食店
     * logo : /Uploads/Picture/2018-03-14/5aa8d098039e9.jpg
     */

    private String id;
    private String name;
    private String shop_price;
    private String all_price;
    private String pic;
    private String max_buy;
    private String dicinfo;
    private String shop_id;
    private String moban;
    private String shopname;
    private String logo;
    private List<GoodidsBean> goodids;
    private List<String> dao_time;
    private String biaoqian ;

    public String getBiaoqian() {
        return biaoqian;
    }

    public void setBiaoqian(String biaoqian) {
        this.biaoqian = biaoqian;
    }

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

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public String getAll_price() {
        return all_price;
    }

    public void setAll_price(String all_price) {
        this.all_price = all_price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getMax_buy() {
        return max_buy;
    }

    public void setMax_buy(String max_buy) {
        this.max_buy = max_buy;
    }

    public String getDicinfo() {
        return dicinfo;
    }

    public void setDicinfo(String dicinfo) {
        this.dicinfo = dicinfo;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getMoban() {
        return moban;
    }

    public void setMoban(String moban) {
        this.moban = moban;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<GoodidsBean> getGoodids() {
        return goodids;
    }

    public void setGoodids(List<GoodidsBean> goodids) {
        this.goodids = goodids;
    }

    public List<String> getDao_time() {
        return dao_time;
    }

    public void setDao_time(List<String> dao_time) {
        this.dao_time = dao_time;
    }

    public static class GoodidsBean {
        /**
         * id : 50
         * name : 大号仿真创意烟盒抱枕靠垫
         * num : 888
         */

        private String id;
        private String name;
        private String num;

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

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}

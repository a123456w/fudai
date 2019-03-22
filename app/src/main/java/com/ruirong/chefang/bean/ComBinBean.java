package com.ruirong.chefang.bean;

import java.util.List;

/**商家详情里边套餐详情的数据
 * Created by dillon on 2018/5/9.
 */

public class ComBinBean {


    /**
     * id : 40
     * name : 果仁
     * goodids : [{"title":"茶","content":[{"name":"绿茶","num":"1","price":"1","cate":"茶"},{"name":"红茶","num":"2","price":"2","cate":"茶"}]},{"title":"饮料","content":[{"name":"冰红茶","num":"3","price":"3","cate":"饮料"},{"name":"芬达","num":"4","price":"4","cate":"饮料"}]}]
     * shop_price : 20.00
     * all_price : 2.00
     * pic :
     * max_buy : 1
     * dicinfo :
     * shop_id : 16
     * dao_time : [""]
     * moban : 2
     * shopname : 朋友圈冰淇淋
     * logo : /Uploads/Picture/2017-07-12/5965da42b371e.jpg
     */
    private String biaoqian ;
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
         * title : 茶
         * content : [{"name":"绿茶","num":"1","price":"1","cate":"茶"},{"name":"红茶","num":"2","price":"2","cate":"茶"}]
         */

        private String title;
        private List<ContentBean> content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        public static class ContentBean {
            /**
             * name : 绿茶
             * num : 1
             * price : 1
             * cate : 茶
             */

            private String name;
            private String num;
            private String price;
            private String cate;

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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getCate() {
                return cate;
            }

            public void setCate(String cate) {
                this.cate = cate;
            }
        }
    }
}

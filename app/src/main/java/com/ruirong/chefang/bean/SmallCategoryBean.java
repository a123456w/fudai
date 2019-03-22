package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */

public class SmallCategoryBean {


    /**
     * list : [{"id":"35","name":"维达蓝色经典系列卫生纸巾3层140g27卷有芯卷纸 箱装","specif":["不易破","140g/卷*27卷/箱"],"price":"45","cover":"/Uploads/Picture/2018-02-27/5a952a87473d2.jpg","is_hot":"1","cartcount":0},{"id":"32","name":"盼盼法式小面包奶香味1.5KG早餐食品 整箱 蛋糕点心办公室零食","specif":["570ml/瓶","预定满意度99%"],"price":"55","cover":"/Uploads/Picture/2018-02-05/5a77b0f762823.jpg","is_discount":"1","cartcount":"3"},{"id":"31","name":"君乐宝纯享酸奶原味","specif":["配料/生牛乳","20元/每箱"],"price":"90","cover":"/Uploads/Picture/2018-02-02/5a744de3e048c.png","is_hot":"1","cartcount":"4"}]
     * total : 1
     * shopinfo : {"id":"235","sp_name":"文海测试店","dp_grade":"5.0","dp_num":"11","distance":7320982}
     */

    private String total;
    private ShopinfoBean shopinfo;
    private List<ListBean> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ShopinfoBean getShopinfo() {
        return shopinfo;
    }

    public void setShopinfo(ShopinfoBean shopinfo) {
        this.shopinfo = shopinfo;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ShopinfoBean {
        /**
         * id : 235
         * sp_name : 文海测试店
         * dp_grade : 5.0
         * dp_num : 11
         * distance : 7320982
         */

        private String id;
        private String sp_name;
        private String dp_grade;
        private String dp_num;
        private String distance;
        private String logo;

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSp_name() {
            return sp_name;
        }

        public void setSp_name(String sp_name) {
            this.sp_name = sp_name;
        }

        public String getDp_grade() {
            return dp_grade;
        }

        public void setDp_grade(String dp_grade) {
            this.dp_grade = dp_grade;
        }

        public String getDp_num() {
            return dp_num;
        }

        public void setDp_num(String dp_num) {
            this.dp_num = dp_num;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }
    }

    public static class ListBean {
        /**
         * id : 35
         * name : 维达蓝色经典系列卫生纸巾3层140g27卷有芯卷纸 箱装
         * specif : ["不易破","140g/卷*27卷/箱"]
         * price : 45
         * cover : /Uploads/Picture/2018-02-27/5a952a87473d2.jpg
         * is_hot : 1
         * cartcount : 0
         * is_discount : 1
         */

        private String id;
        private String name;
        private String price;
        private String cover;
        private String is_hot;
        private String cartcount;
        private String is_discount;
        private List<String> specif;

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

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getIs_hot() {
            return is_hot;
        }

        public void setIs_hot(String is_hot) {
            this.is_hot = is_hot;
        }

        public String getCartcount() {
            return cartcount;
        }

        public void setCartcount(String cartcount) {
            this.cartcount = cartcount;
        }

        public String getIs_discount() {
            return is_discount;
        }

        public void setIs_discount(String is_discount) {
            this.is_discount = is_discount;
        }

        public List<String> getSpecif() {
            return specif;
        }

        public void setSpecif(List<String> specif) {
            this.specif = specif;
        }
    }
}

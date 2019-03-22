package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public class JiesuanBean {

    /**
     * address : [{"id":"220","rec_name":"黑光剑圣","mobile":"78965412308","address":"光看第一段"}]
     * shop_id : 241
     * shop_name : 郭江美食店
     * logo : /Uploads/Picture/2018-03-14/5aa8d098039e9.jpg
     * goods : [{"price":"45","id":"57","name":"口水娃口水鱼","cover":"/Uploads/Picture/2018-03-20/5ab0b0507f264.jpg","num":"1","shopid":"241"}]
     * total_price : 45
     * yun_fee : 0
     * amount_price : 45
     */

    private String shop_id;
    private String shop_name;
    private String logo;
    private String total_price;
    private String yun_fee;
    private String amount_price;
    private String is_distribute;
    private List<AddressBean> address;
    private List<GoodsBean> goods;


    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getYun_fee() {
        return yun_fee;
    }

    public void setYun_fee(String yun_fee) {
        this.yun_fee = yun_fee;
    }

    public String getAmount_price() {
        return amount_price;
    }

    public void setAmount_price(String amount_price) {
        this.amount_price = amount_price;
    }

    public String getIs_distribute() {
        return is_distribute;
    }

    public void setIs_distribute(String is_distribute) {
        this.is_distribute = is_distribute;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<AddressBean> getAddress() {
        return address;
    }

    public void setAddress(List<AddressBean> address) {
        this.address = address;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class AddressBean {
        /**
         * id : 220
         * rec_name : 黑光剑圣
         * mobile : 78965412308
         * address : 光看第一段
         */

        private String id;
        private String rec_name;
        private String mobile;
        private String address;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRec_name() {
            return rec_name;
        }

        public void setRec_name(String rec_name) {
            this.rec_name = rec_name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static class GoodsBean {
        /**
         * price : 45
         * id : 57
         * name : 口水娃口水鱼
         * cover : /Uploads/Picture/2018-03-20/5ab0b0507f264.jpg
         * num : 1
         * shopid : 241
         */

        private String price;
        private String id;
        private String name;
        private String cover;
        private String num;
        private String shopid;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }
    }
}

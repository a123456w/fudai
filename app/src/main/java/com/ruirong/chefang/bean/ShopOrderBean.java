package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by wu on 2018/3/27.
 * 商城订单  实体类
 */

public class ShopOrderBean {


    /**
     * shop_name : 文海酒店
     * logo : /Uploads/Picture/2018-01-13/5a59fc53d9d4a.jpg
     * shopstatus : 1
     * order_id : 38589120180329094211
     * shi_money : 198.00
     * gq_status ："0"
     * comment_state :0
     * goodslist : [{"pic":"","good_price":"99.00","num":"2","goods_name":"运动衣","is_sell":"1"}]
     * goodsnum : 2
     * status : 2
     * goods_type ：1
     * shop_status
     * cancel
     * code_path : "/Uploads/OrderCode128/1e31bb33967e784e500253968c06eff7png"
     */

    private String shop_name;
    private String logo;
    private String shopstatus;
    private String order_id;
    private String shi_money;
    private int goodsnum;
    private String is_hx ;           // 是否去核销
    private String distri_price ;   //配送费
    private String gq_status;
    private String status;
    private String goods_type;
    private String shop_status;
    private String cancel;
    private String comment_state;
    private String code_path;

    public String getDistri_price() {
        return distri_price;
    }

    public void setDistri_price(String distri_price) {
        this.distri_price = distri_price;
    }

    public String getIs_hx() {
        return is_hx;
    }

    public void setIs_hx(String is_hx) {
        this.is_hx = is_hx;
    }

    private List<GoodslistBean> goodslist;

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

    public String getShopstatus() {
        return shopstatus;
    }

    public String getComment_state() {
        return comment_state;
    }

    public void setComment_state(String comment_state) {
        this.comment_state = comment_state;
    }

    public void setShopstatus(String shopstatus) {
        this.shopstatus = shopstatus;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getShi_money() {
        return shi_money;
    }

    public void setShi_money(String shi_money) {
        this.shi_money = shi_money;
    }

    public int getGoodsnum() {
        return goodsnum;
    }

    public void setGoodsnum(int goodsnum) {
        this.goodsnum = goodsnum;
    }

    public String getGq_status() {
        return gq_status;
    }

    public void setGq_status(String gq_status) {
        this.gq_status = gq_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public String getShop_status() {
        return shop_status;
    }

    public void setShop_status(String shop_status) {
        this.shop_status = shop_status;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public String getCode_path() {
        return code_path;
    }

    public void setCode_path(String code_path) {
        this.code_path = code_path;
    }

    public List<GoodslistBean> getGoodslist() {
        return goodslist;
    }

    public void setGoodslist(List<GoodslistBean> goodslist) {
        this.goodslist = goodslist;
    }

    public static class GoodslistBean {
        /**
         * pic :
         * good_price : 99.00
         * num : 2
         * goods_name : 运动衣
         * is_sell : 1
         * specif : 材质|合金/镀银/镀金
         * shop_id : 240
         */

        private String pic;
        private String good_price;
        private String num;
        private String goods_name;
        private String specif;
        private String is_sell;
        private String shop_id;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getGood_price() {
            return good_price;
        }

        public void setGood_price(String good_price) {
            this.good_price = good_price;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getIs_sell() {
            return is_sell;
        }

        public void setIs_sell(String is_sell) {
            this.is_sell = is_sell;
        }

        public String getSpecif() {
            return specif;
        }

        public void setSpecif(String specif) {
            this.specif = specif;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }
    }
}

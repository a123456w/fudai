package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by 16690 on 2018/3/29.
 * describe:
 */

public class ShopOrderDetailBean {


    /**
     * list : {"total_money":"99.00","create_time":"1522717883","status":"2","gq_status":"1","mobile":"15732631234","name":"我","address":"啊啊啊","order_id":"85182920180403091123","shop_status":"0","comment_state":"0","cancel":"0"}
     * goods_info : [{"id":"38","cover":"/Uploads/Picture/2018-03-20/5ab0745be870e.jpg","price":"99","yuan_price":"0","specif":"尺码|4XL（165-190斤） M L XL","goods_name":"运动衣","num":"1","all_price":"99.00","sp_name":"文海酒店","shop_id":"240","type":"0"}]
     * gqTime : 86400
     */

    private ListBean list;
    private String gqTime;
    private List<GoodsInfoBean> goods_info;

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public String getGqTime() {
        return gqTime;
    }

    public void setGqTime(String gqTime) {
        this.gqTime = gqTime;
    }

    public List<GoodsInfoBean> getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(List<GoodsInfoBean> goods_info) {
        this.goods_info = goods_info;
    }

    public static class ListBean {
        /**
         * total_money : 99.00
         * create_time : 1522717883
         * status : 2
         * gq_status : 1
         * mobile : 15732631234
         * name : 我
         * address : 啊啊啊
         * order_id : 85182920180403091123
         * shop_status : 0
         * comment_state : 0
         * cancel : 0
         * goods_type:1
         * is_since : 0
         * shopname :
         */

        private String total_money;
        private String is_hx ;
        private String is_hexiao ;
        private String create_time;
        private String status;
        private String gq_status;
        private String mobile;
        private String name;
        private String address;
        private String order_id;
        private String shop_status;
        private String comment_state;
        private String goods_type;
        private String cancel;
        private String is_since;
        private String code_path;
        private String shopname;

        public String getIs_hexiao() {
            return is_hexiao;
        }

        public void setIs_hexiao(String is_hexiao) {
            this.is_hexiao = is_hexiao;
        }

        public String getIs_hx() {
            return is_hx;
        }

        public void setIs_hx(String is_hx) {
            this.is_hx = is_hx;
        }

        public String getTotal_money() {
            return total_money;
        }

        public void setTotal_money(String total_money) {
            this.total_money = total_money;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGq_status() {
            return gq_status;
        }

        public void setGq_status(String gq_status) {
            this.gq_status = gq_status;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getShop_status() {
            return shop_status;
        }

        public void setShop_status(String shop_status) {
            this.shop_status = shop_status;
        }

        public String getComment_state() {
            return comment_state;
        }

        public void setComment_state(String comment_state) {
            this.comment_state = comment_state;
        }

        public String getCancel() {
            return cancel;
        }

        public void setCancel(String cancel) {
            this.cancel = cancel;
        }

        public String getGoods_type() {
            return goods_type;
        }

        public void setGoods_type(String goods_type) {
            this.goods_type = goods_type;
        }

        public String getIs_since() {
            return is_since;
        }

        public void setIs_since(String is_since) {
            this.is_since = is_since;
        }

        public String getCode_path() {
            return code_path;
        }

        public void setCode_path(String code_path) {
            this.code_path = code_path;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }
    }

    public static class GoodsInfoBean {
        /**
         * id : 38
         * cover : /Uploads/Picture/2018-03-20/5ab0745be870e.jpg
         * price : 99
         * yuan_price : 0
         * specif : 尺码|4XL（165-190斤） M L XL
         * goods_name : 运动衣
         * num : 1
         * all_price : 99.00
         * sp_name : 文海酒店
         * shop_id : 240
         * type : 0
         */

        private String id;
        private String cover;
        private String price;
        private String yuan_price;
        private String specif;
        private String goods_name;
        private String num;
        private String all_price;
        private String sp_name;
        private String shop_id;
        private String type;
        private String code_path ;

        public String getCode_path() {
            return code_path;
        }

        public void setCode_path(String code_path) {
            this.code_path = code_path;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
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

        public String getSpecif() {
            return specif;
        }

        public void setSpecif(String specif) {
            this.specif = specif;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getAll_price() {
            return all_price;
        }

        public void setAll_price(String all_price) {
            this.all_price = all_price;
        }

        public String getSp_name() {
            return sp_name;
        }

        public void setSp_name(String sp_name) {
            this.sp_name = sp_name;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

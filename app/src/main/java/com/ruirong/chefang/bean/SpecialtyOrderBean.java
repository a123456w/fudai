package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class SpecialtyOrderBean {


    /**
     * number_bh : 20180402111635519384155
     * order_amount : 48.00
     * pay_state : 0
     * express : 0
     * hair_type : 0
     * state : 0
     * gq_status : 1
     * number : 1
     * shi_money : 48.00
     * goods_List : [{"tui_status":"0","goods_id":"31","num":"9","all_price":"216.00","create_time":"1522638995","comment_state":"0","pic":"/Uploads/Picture/2018-03-28/5abb381be626f.jpg","name":"陕西特产魔芋丝面方便面米线","attr":[],"before_price":"34.00","now_price":"24.00"},{"tui_status":"0","goods_id":"24","num":"8","all_price":"192.00","create_time":"1522638995","comment_state":"0","pic":"/Uploads/Picture/2018-03-28/5abb3bba9f429.jpg","name":"牛耕小麦正宗五谷山药宽粉条","attr":[],"before_price":"56.00","now_price":"24.00"}]
     */

    private String number_bh;
    private String order_amount;
    private String pay_state;
    private String express;
    private String hair_type;
    private String state;
    private String gq_status;
    private String number;
    private String shi_money;
    private String distri_price;
    private List<GoodsListBean> goods_List;


    public String getDistri_price() {
        return distri_price;
    }

    public void setDistri_price(String distri_price) {
        this.distri_price = distri_price;
    }

    public String getNumber_bh() {
        return number_bh;
    }

    public void setNumber_bh(String number_bh) {
        this.number_bh = number_bh;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getPay_state() {
        return pay_state;
    }

    public void setPay_state(String pay_state) {
        this.pay_state = pay_state;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getHair_type() {
        return hair_type;
    }

    public void setHair_type(String hair_type) {
        this.hair_type = hair_type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGq_status() {
        return gq_status;
    }

    public void setGq_status(String gq_status) {
        this.gq_status = gq_status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getShi_money() {
        return shi_money;
    }

    public void setShi_money(String shi_money) {
        this.shi_money = shi_money;
    }

    public List<GoodsListBean> getGoods_List() {
        return goods_List;
    }

    public void setGoods_List(List<GoodsListBean> goods_List) {
        this.goods_List = goods_List;
    }

    public static class GoodsListBean {
        /**
         * tui_status : 0
         * goods_id : 31
         * num : 9
         * all_price : 216.00
         * create_time : 1522638995
         * comment_state : 0
         * pic : /Uploads/Picture/2018-03-28/5abb381be626f.jpg
         * name : 陕西特产魔芋丝面方便面米线
         * attr : []
         * before_price : 34.00
         * now_price : 24.00
         */

        private String tui_status;
        private String goods_id;
        private String num;
        private String all_price;
        private String create_time;
        private String comment_state;
        private String pic;
        private String name;
        private String before_price;
        private String now_price;
        private List<String> attr;

        public String getTui_status() {
            return tui_status;
        }

        public void setTui_status(String tui_status) {
            this.tui_status = tui_status;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
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

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getComment_state() {
            return comment_state;
        }

        public void setComment_state(String comment_state) {
            this.comment_state = comment_state;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public List<String> getAttr() {
            return attr;
        }

        public void setAttr(List<String> attr) {
            this.attr = attr;
        }
    }
}

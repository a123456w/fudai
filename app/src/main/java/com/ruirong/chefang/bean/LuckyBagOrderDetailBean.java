package com.ruirong.chefang.bean;

/**
 * Created by 16690 on 2018/4/4.
 * describe: 福袋订单详情实体
 */

public class LuckyBagOrderDetailBean {

    /**
     * list : {"number_bh":"20180403202414519973537","pay_time":"1522758254","pay_state":"0","state":"0","gq_status":"1"}
     * goods_info : {"id":"5","title":"三井十里香","cover":"/Uploads/Picture/2018-03-29/5abc8d3a49b1e.jpg","now_price":"360.00","before_price":"600.00","attr":"720g","num":"1","order_amount":"360.00"}
     * addr : {"rec_name":"我","mobile":"15732631234","address":"啊啊啊"}
     * gqTime : 86400
     */

    private ListBean list;
    private GoodsInfoBean goods_info;
    private AddrBean addr;
    private String gqTime;

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public GoodsInfoBean getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(GoodsInfoBean goods_info) {
        this.goods_info = goods_info;
    }

    public AddrBean getAddr() {
        return addr;
    }

    public void setAddr(AddrBean addr) {
        this.addr = addr;
    }

    public String getGqTime() {
        return gqTime;
    }

    public void setGqTime(String gqTime) {
        this.gqTime = gqTime;
    }

    public static class ListBean {
        /**
         * number_bh : 20180403202414519973537
         * pay_time : 1522758254
         * pay_state : 0
         * state : 0
         * gq_status : 1
         */

        private String number_bh;
        private String pay_time;
        private String pay_state;
        private String state;
        private String gq_status;

        public String getNumber_bh() {
            return number_bh;
        }

        public void setNumber_bh(String number_bh) {
            this.number_bh = number_bh;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getPay_state() {
            return pay_state;
        }

        public void setPay_state(String pay_state) {
            this.pay_state = pay_state;
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
    }

    public static class GoodsInfoBean {
        /**
         * id : 5
         * title : 三井十里香
         * cover : /Uploads/Picture/2018-03-29/5abc8d3a49b1e.jpg
         * now_price : 360.00
         * before_price : 600.00
         * attr : 720g
         * num : 1
         * order_amount : 360.00
         */

        private String id;
        private String title;
        private String cover;
        private String now_price;
        private String before_price;
        private String attr;
        private String num;
        private String order_amount;

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

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
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

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getOrder_amount() {
            return order_amount;
        }

        public void setOrder_amount(String order_amount) {
            this.order_amount = order_amount;
        }
    }

    public static class AddrBean {
        /**
         * rec_name : 我
         * mobile : 15732631234
         * address : 啊啊啊
         */

        private String rec_name;
        private String mobile;
        private String address;

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
}

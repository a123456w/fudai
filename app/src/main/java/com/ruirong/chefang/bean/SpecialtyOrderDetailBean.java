package com.ruirong.chefang.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 */

public class SpecialtyOrderDetailBean implements Serializable {


    /**
     * list : {"number_bh":"20180410170832519312443","order_amount":"200.00","pay_time":"1523351312","pay_state":"1","express":"0","hair_type":"0","state":"0","gq_status":"1","express_name":"0","shi_money":"200.00"}
     * addr : {"rec_name":"朱二磊","mobile":"13145211069","address":"大马猴村"}
     * goods_info : [{"id":"51","pic":"/Uploads/Picture/2018-03-28/5abb3f43a4eb2.jpg","now_price":"10.00","before_price":"40.00","attr":["0.2"],"name":"土特产农家马铃薯新鲜洋芋宽粉条","num":"20","all_price":"200.00","tui_status":"0"}]
     * gqTime : 86400
     */

    private ListBean list;
    private AddrBean addr;
    private String gqTime;
    private List<GoodsInfoBean> goods_info;

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
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

    public List<GoodsInfoBean> getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(List<GoodsInfoBean> goods_info) {
        this.goods_info = goods_info;
    }

    public static class ListBean {
        /**
         * number_bh : 20180410170832519312443
         * order_amount : 200.00
         * pay_time : 1523351312
         * pay_state : 1
         * express : 0
         * hair_type : 0
         * state : 0
         * gq_status : 1
         * express_name : 0
         * shi_money : 200.00
         */

        private String number_bh;
        private String order_amount;
        private String pay_time;
        private String pay_state;
        private String express;
        private String hair_type;
        private String state;
        private String gq_status;
        private String express_name;
        private String shi_money;

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

        public String getExpress_name() {
            return express_name;
        }

        public void setExpress_name(String express_name) {
            this.express_name = express_name;
        }

        public String getShi_money() {
            return shi_money;
        }

        public void setShi_money(String shi_money) {
            this.shi_money = shi_money;
        }
    }

    public static class AddrBean {
        /**
         * rec_name : 朱二磊
         * mobile : 13145211069
         * address : 大马猴村
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

    public static class GoodsInfoBean {
        /**
         * id : 51
         * pic : /Uploads/Picture/2018-03-28/5abb3f43a4eb2.jpg
         * now_price : 10.00
         * before_price : 40.00
         * attr : ["0.2"]
         * name : 土特产农家马铃薯新鲜洋芋宽粉条
         * num : 20
         * all_price : 200.00
         * tui_status : 0
         */

        private String id;
        private String pic;
        private String now_price;
        private String before_price;
        private String name;
        private String num;
        private String all_price;
        private String tui_status;
        private List<String> attr;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
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

        public String getAll_price() {
            return all_price;
        }

        public void setAll_price(String all_price) {
            this.all_price = all_price;
        }

        public String getTui_status() {
            return tui_status;
        }

        public void setTui_status(String tui_status) {
            this.tui_status = tui_status;
        }

        public List<String> getAttr() {
            return attr;
        }

        public void setAttr(List<String> attr) {
            this.attr = attr;
        }
    }
}

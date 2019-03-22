package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */

public class PurchaseBean {


    /**
     * list : [{"id":"31","name":"陕西特产魔芋丝面方便面米线","pic":"/Uploads/Picture/2018-03-28/5abb381be626f.jpg","now_price":"24.00","before_price":"34.00","state":"1","num":1}]
     * address : [{"id":"302","uid":"5193","rec_name":"朱二磊","mobile":"13145211069","address":"大马猴村","pos_code":"1000000","is_defa":"1","ssx":"北京市-北京市-门头沟区"}]
     * yunfen : 5.00
     * banyou : 200.00
     */

    private String yunfen;
    private String banyou;
    private List<ListBean> list;
    private List<AddressBean> address;

    public String getYunfen() {
        return yunfen;
    }

    public void setYunfen(String yunfen) {
        this.yunfen = yunfen;
    }

    public String getBanyou() {
        return banyou;
    }

    public void setBanyou(String banyou) {
        this.banyou = banyou;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<AddressBean> getAddress() {
        return address;
    }

    public void setAddress(List<AddressBean> address) {
        this.address = address;
    }

    public static class ListBean {
        /**
         * id : 31
         * name : 陕西特产魔芋丝面方便面米线
         * pic : /Uploads/Picture/2018-03-28/5abb381be626f.jpg
         * now_price : 24.00
         * before_price : 34.00
         * state : 1
         * num : 1
         */

        private String id;
        private String name;
        private String pic;
        private String now_price;
        private String before_price;
        private String state;
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }

    public static class AddressBean {
        /**
         * id : 302
         * uid : 5193
         * rec_name : 朱二磊
         * mobile : 13145211069
         * address : 大马猴村
         * pos_code : 1000000
         * is_defa : 1
         * ssx : 北京市-北京市-门头沟区
         */

        private String id;
        private String uid;
        private String rec_name;
        private String mobile;
        private String address;
        private String pos_code;
        private String is_defa;
        private String ssx;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getPos_code() {
            return pos_code;
        }

        public void setPos_code(String pos_code) {
            this.pos_code = pos_code;
        }

        public String getIs_defa() {
            return is_defa;
        }

        public void setIs_defa(String is_defa) {
            this.is_defa = is_defa;
        }

        public String getSsx() {
            return ssx;
        }

        public void setSsx(String ssx) {
            this.ssx = ssx;
        }
    }
}

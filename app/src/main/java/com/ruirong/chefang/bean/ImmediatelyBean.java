package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 */

public class ImmediatelyBean {

    /**
     * id : 5
     * title : 三井十里香
     * attr : 720g
     * before_price : 600.00
     * now_price : 360.00
     * pic : /Uploads/Picture/2018-03-29/5abc8d3a49b1e.jpg
     * num : 1
     * address : [{"id":"245","uid":"5193","rec_name":"朱磊","mobile":"17701080544","address":"第三方萨达发","pos_code":"1324567","ssx":"天津市-天津市-河东区"}]
     */

    private String id;
    private String title;
    private String attr;
    private String before_price;
    private String now_price;
    private String pic;
    private int num;
    private List<AddressBean> address;

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

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<AddressBean> getAddress() {
        return address;
    }

    public void setAddress(List<AddressBean> address) {
        this.address = address;
    }

    public static class AddressBean {
        /**
         * id : 245
         * uid : 5193
         * rec_name : 朱磊
         * mobile : 17701080544
         * address : 第三方萨达发
         * pos_code : 1324567
         * ssx : 天津市-天津市-河东区
         */

        private String id;
        private String uid;
        private String rec_name;
        private String mobile;
        private String address;
        private String pos_code;
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

        public String getSsx() {
            return ssx;
        }

        public void setSsx(String ssx) {
            this.ssx = ssx;
        }
    }
}

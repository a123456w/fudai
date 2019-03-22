package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by BX on 2018/2/28.
 */

public class HousedetailsBean {


    /**
     * id : 20
     * id : 20
     * content : [{"key":"面积","value":"91.9㎡"},{"key":"居室","value":"两室一厅一厨一卫"},{"key":"楼/单元/门号","value":"6号楼4单元502"}]
     * average : 21888
     * mobile : 15314758632
     * address : 北京市昌平区昌盛园小区一区
     * sp_name : 丘禾国际
     * dp_grade : 0.0
     * pic : ["/Uploads/Picture/2018-03-28/5abb03c170618.png","/Uploads/Picture/2018-03-28/5abb03c246280.png"]
     */

    private String id;
    private String average;
    private String mobile;
    private String address;
    private String title;
    private String sp_name;
    private String dp_grade;
    private String pricetitle;
    private int type;
    private List<ContentBean> content;
    private List<String> pic;

    public String getPricetitle() {
        return pricetitle;
    }

    public void setPricetitle(String pricetitle) {
        this.pricetitle = pricetitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
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

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public static class ContentBean {
        /**
         * key : 面积
         * value : 91.9㎡
         */

        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

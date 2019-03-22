package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by BX on 2018/3/1.
 */

public class ReservationsBean {


    /**
     * id : 259
     * sp_name : 华润置地
     * dp_grade : 0.0
     * sp_address : 香港湾仔港湾道26号华润大厦46层
     * key : [{"key":"均价","value":"面议"},{"key":"楼层","value":"3层"}]
     * pic : /Uploads/Picture/2018-03-28/5abaed6d0572c.jpg
     */

    private String id;
    private String sp_name;
    private String dp_grade;
    private String sp_address;
    private String pic;
    private List<KeyBean> key;

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

    public String getSp_address() {
        return sp_address;
    }

    public void setSp_address(String sp_address) {
        this.sp_address = sp_address;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<KeyBean> getKey() {
        return key;
    }

    public void setKey(List<KeyBean> key) {
        this.key = key;
    }

    public static class KeyBean {
        /**
         * key : 均价
         * value : 面议
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

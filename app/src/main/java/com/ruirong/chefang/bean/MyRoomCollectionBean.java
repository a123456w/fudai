package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by 16690 on 2018/3/26.
 */

public class MyRoomCollectionBean {


    /**
     * list : [{"id":"39","sp_name":"任永超","pic":"/Uploads/Picture/2018-01-13/5a59b4ec27fec.jpg","dp_grade":"0.0","distance":6.72,"sp_address":"汉滨区"},{"id":"38","sp_name":"我更改巅峰官方电话","pic":"/Uploads/Picture/2018-01-13/5a59b5120ebb4.jpg","dp_grade":"3.0","distance":6.42,"sp_address":"岚皋县"},{"id":"37","sp_name":"任永超","pic":"/Uploads/Picture/2018-01-13/5a59b4ec27fec.jpg","dp_grade":"0.0","distance":1.19,"sp_address":"白河县"},{"id":"15","sp_name":"我更改巅峰官方电话","shop_check_id":"238","pic":"4454","dp_grade":"3.0"}]
     * total : 1
     */

    private int total;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 39
         * sp_name : 任永超
         * pic : /Uploads/Picture/2018-01-13/5a59b4ec27fec.jpg
         * dp_grade : 0.0
         * distance : 6.72
         * sp_address : 汉滨区
         * shop_check_id : 238
         */

        private String id;
        private String sp_name;
        private String pic;
        private String dp_grade;
        private double distance;
        private String sp_address;
        private String shop_check_id;
        private boolean checked;

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

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getDp_grade() {
            return dp_grade;
        }

        public void setDp_grade(String dp_grade) {
            this.dp_grade = dp_grade;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getSp_address() {
            return sp_address;
        }

        public void setSp_address(String sp_address) {
            this.sp_address = sp_address;
        }

        public String getShop_check_id() {
            return shop_check_id;
        }

        public void setShop_check_id(String shop_check_id) {
            this.shop_check_id = shop_check_id;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}

package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class FuBackHotelBean {


    /**
     * list : [{"id":"169","sp_name":"安果Ｃ私房烘培","dp_num":"23","dp_grade":"4.5","q_price":"441.00","cover":"/Uploads/Picture/2018-01-13/5a59b91c4f5b4.jpg","distances":"0.52"},{"id":"212","sp_name":"文海测试店","dp_num":"11","dp_grade":"5.0","q_price":"120.00","cover":"/Uploads/Picture/2018-01-15/5a5ca601564d0.jpg","distances":"3.06"}]
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
         * id : 169
         * sp_name : 安果Ｃ私房烘培
         * dp_num : 23
         * dp_grade : 4.5
         * q_price : 441.00
         * cover : /Uploads/Picture/2018-01-13/5a59b91c4f5b4.jpg
         * distances : 0.52
         */

        private String id;
        private String sp_name;
        private String dp_num;
        private String dp_grade;
        private String q_price;
        private String cover;
        private String distances;

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

        public String getDp_num() {
            return dp_num;
        }

        public void setDp_num(String dp_num) {
            this.dp_num = dp_num;
        }

        public String getDp_grade() {
            return dp_grade;
        }

        public void setDp_grade(String dp_grade) {
            this.dp_grade = dp_grade;
        }

        public String getQ_price() {
            return q_price;
        }

        public void setQ_price(String q_price) {
            this.q_price = q_price;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getDistances() {
            return distances;
        }

        public void setDistances(String distances) {
            this.distances = distances;
        }
    }
}

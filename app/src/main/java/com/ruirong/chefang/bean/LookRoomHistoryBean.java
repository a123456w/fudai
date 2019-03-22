package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by EDZ on 2018/3/27.
 */

public class LookRoomHistoryBean {

    /**
     * list : [{"id":"19","look_time":"2147483647","cover":"/Uploads/Picture/2018-01-13/5a59b5120ebb4.jpg","sp_address":"北京市昌平区","sp_name":"我更改巅峰官方电话"}]
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
         * id : 19
         * look_time : 2147483647
         * cover : /Uploads/Picture/2018-01-13/5a59b5120ebb4.jpg
         * sp_address : 北京市昌平区
         * sp_name : 我更改巅峰官方电话
         */

        private String id;
        private String pid;
        private String look_time;
        private String cover;
        private String sp_address;
        private String sp_name;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLook_time() {
            return look_time;
        }

        public void setLook_time(String look_time) {
            this.look_time = look_time;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getSp_address() {
            return sp_address;
        }

        public void setSp_address(String sp_address) {
            this.sp_address = sp_address;
        }

        public String getSp_name() {
            return sp_name;
        }

        public void setSp_name(String sp_name) {
            this.sp_name = sp_name;
        }
    }
}

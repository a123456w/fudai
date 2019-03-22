package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class HousePropertyBean {


    /**
     * list : [{"id":"238","content":"全娃儿合格社如何让退回","sp_name":"我更改巅峰官方电话","sp_address":"北京市昌平区","pic":"/Uploads/Picture/2018-01-13/5a59b5120ebb4.jpg"}]
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
         * id : 238
         * content : 全娃儿合格社如何让退回
         * sp_name : 我更改巅峰官方电话
         * sp_address : 北京市昌平区
         * pic : /Uploads/Picture/2018-01-13/5a59b5120ebb4.jpg
         */

        private String id;
        private String content;
        private String sp_name;
        private String sp_address;
        private String pic;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSp_name() {
            return sp_name;
        }

        public void setSp_name(String sp_name) {
            this.sp_name = sp_name;
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
    }
}

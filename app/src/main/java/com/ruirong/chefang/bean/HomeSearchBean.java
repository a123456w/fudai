package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by 16690 on 2018/3/27.
 * describe:
 */

public class HomeSearchBean {


    /**
     * list : [{"id":"77","sp_name":"黑白格Bar","cover":"/Uploads/Picture/2017-09-28/59ccc47720bff.JPG","sp_address":"陕西省安康市汉滨区东大街44号","perpeo":"","type":"0","content":"黑白格酒吧坐落于安康市汉滨区东大街44号一楼一层，打造音乐文化为理念的美式酒吧！提供各种茶类 啤酒 饮料和各种小吃。适合于大中小型聚会（生日Party 企业年会 Q群聚餐 ）。同时也是朋友畅谈  情侣谈情的温馨场所。黑白格酒吧卖的不是啤酒，而是一种文化！用音乐打造友谊的平台，让我们一起雕刻时光！","pl_num":"0","grades":"0.0","distance":78.34}]
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
         * id : 77
         * sp_name : 黑白格Bar
         * cover : /Uploads/Picture/2017-09-28/59ccc47720bff.JPG
         * sp_address : 陕西省安康市汉滨区东大街44号
         * perpeo :
         * type : 0
         * content : 黑白格酒吧坐落于安康市汉滨区东大街44号一楼一层，打造音乐文化为理念的美式酒吧！提供各种茶类 啤酒 饮料和各种小吃。适合于大中小型聚会（生日Party 企业年会 Q群聚餐 ）。同时也是朋友畅谈  情侣谈情的温馨场所。黑白格酒吧卖的不是啤酒，而是一种文化！用音乐打造友谊的平台，让我们一起雕刻时光！
         * pl_num : 0
         * grades : 0.0
         * distance : 78.34
         */

        private String id;
        private String sp_name;
        private String cover;
        private String sp_address;
        private String perpeo;
        private String type;
        private String content;
        private String pl_num;
        private String grades;
        private double distance;

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

        public String getPerpeo() {
            return perpeo;
        }

        public void setPerpeo(String perpeo) {
            this.perpeo = perpeo;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPl_num() {
            return pl_num;
        }

        public void setPl_num(String pl_num) {
            this.pl_num = pl_num;
        }

        public String getGrades() {
            return grades;
        }

        public void setGrades(String grades) {
            this.grades = grades;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }
}

package com.ruirong.chefang.bean;

import java.util.List;

/**
 * 车房首页的数据
 * Created by chenlipeng on 2018/3/16.
 */

public class CarHouseBean {

    private List<String> lunbo;
    private List<CatesBean> cates;
    private List<FangchanBean> fangchan;

    public List<String> getLunbo() {
        return lunbo;
    }

    public void setLunbo(List<String> lunbo) {
        this.lunbo = lunbo;
    }

    public List<CatesBean> getCates() {
        return cates;
    }

    public void setCates(List<CatesBean> cates) {
        this.cates = cates;
    }

    public List<FangchanBean> getFangchan() {
        return fangchan;
    }

    public void setFangchan(List<FangchanBean> fangchan) {
        this.fangchan = fangchan;
    }

    public static class CatesBean {
        /**
         * id : 39
         * name : 新房
         * pic : /Uploads/Picture/2018-04-10/5acc91909734c.png
         */

        private String id;
        private String name;
        private String pic;
        private boolean isFile = false;
        public CatesBean(){

        }
        public CatesBean(String id,String name,String pic,boolean isFile){
            this.id= id;
            this.name = name;
            this.pic = pic;
            this.isFile = isFile;
        }
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

        public boolean isFile() {
            return isFile;
        }

        public void setFile(boolean file) {
            isFile = file;
        }
    }

    public static class FangchanBean {
        /**
         * cover : /Uploads/Picture/2018-04-11/5acdb3a49e168.jpg
         * dp_grade : 0.0
         * id : 217
         * sp_name : 平利花园
         * distances : 1065.4841045704238
         * address1 : 安康市
         * address2 : 新房
         */

        private String cover;
        private String dp_grade;
        private String id;
        private String sp_name;
        private String distances;
        private String address1;
        private String address2;

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getDp_grade() {
            return dp_grade;
        }

        public void setDp_grade(String dp_grade) {
            this.dp_grade = dp_grade;
        }

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

        public String getDistances() {
            return distances;
        }

        public void setDistances(String distances) {
            this.distances = distances;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }
    }
}

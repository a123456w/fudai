package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/26 0026.
 */

public class CarAndRoomsLevelBean {


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
         * pic : /Uploads/Picture/2018-03-14/5aa8cf9bb4212.jpg
         */

        private String id;
        private String name;
        private String pic;

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
    }

    public static class FangchanBean {
        /**
         * id : 242
         * cover : /Uploads/Picture/2018-01-13/5a59b4ec27fec.jpg
         * sp_name : 任永超
         * title : 额外若无若翁
         * address1 : 安康市
         * address2 : 健身
         */

        private String id;
        private String cover;
        private String sp_name;
        private String title;
        private String address1;
        private String address2;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getSp_name() {
            return sp_name;
        }

        public void setSp_name(String sp_name) {
            this.sp_name = sp_name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

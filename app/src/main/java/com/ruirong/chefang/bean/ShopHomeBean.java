package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class ShopHomeBean {


    private List<String> images;
    private List<TradeBean> trade;
    private List<ShopBean> shop;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<TradeBean> getTrade() {
        return trade;
    }

    public void setTrade(List<TradeBean> trade) {
        this.trade = trade;
    }

    public List<ShopBean> getShop() {
        return shop;
    }

    public void setShop(List<ShopBean> shop) {
        this.shop = shop;
    }

    public static class TradeBean {
        /**
         * id : 4
         * name : 美食
         * pic : /Uploads/Picture/2018-03-14/5aa8cfe7bd84f.jpg
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

    public static class ShopBean {
        /**
         * cover : /Uploads/Picture/2017-06-16/5943bbf6a0488.png
         * dp_grade : 9.9
         * area_id : 汉滨区
         * classify_id : 娱乐
         * id : 1
         * sp_name : K秀影音派对KTV
         * distances : 0
         */

        private String cover;
        private String dp_grade;
        private String area_id;
        private String classify_id;
        private String id;
        private String sp_name;
        private String distances;

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

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getClassify_id() {
            return classify_id;
        }

        public void setClassify_id(String classify_id) {
            this.classify_id = classify_id;
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
    }
}

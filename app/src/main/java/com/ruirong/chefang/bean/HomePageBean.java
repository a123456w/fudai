package com.ruirong.chefang.bean;

import java.util.List;

/**
 * 首页所有数据的实体
 * Created by dillon on 2017/12/26.
 */

public class HomePageBean {

    /**
     * text : 特大喜讯，易家福袋携手饕餮宴遇火锅，年终“惠爆”全城！12月15日至12月23日，凡使用易家福袋支付，全场素菜1元购（30余样），荤菜5.8折！另享50%的消费返还！！！
     * shop_id : 240
     * num : 12332
     * img : /Uploads/Picture/2018-01-13/5a59fc53d9d4a.jpg
     * thumb : ["/Uploads/Picture/2018-03-15/5aa9d3bdb00da.jpg","/Uploads/Picture/2018-03-15/5aa9d3c5510e0.jpg","/Uploads/Picture/2018-03-15/5aa9d3d239ff8.jpg","/Uploads/Picture/2018-03-15/5aa9d4792de4c.jpg"]
     * tuishop : [{"id":"125","cover":"/Uploads/Picture/2017-11-23/5a1688723c8da.png","sp_name":"饕餮宴遇火锅","dp_grade":"0.0","distance":1039.32,"cate":"美食","area":"汉滨区"},{"id":"136","cover":"/Uploads/Picture/2017-12-05/5a26083c3bbf3.jpg","sp_name":"百客烘焙","dp_grade":"0.0","distance":1040.97,"cate":"美食","area":"汉滨区"}]
     * fangchan : [{"id":"242","cover":"/Uploads/Picture/2018-01-13/5a59b4ec27fec.jpg","sp_name":"任永超","title":null,"address1":"安康市","address2":"健身"}]
     * fudai : [{"id":"1","title":"风云诡谲弓发挥稳定","cover":"/Uploads/Picture/2018-01-13/5a59b4da86e77.jpg","now_price":"34.00"},{"id":"2","title":"俺只是打个比方说现代化","cover":"/Uploads/Picture/2018-01-13/5a59b4da86e77.jpg","now_price":"45.00"}]
     * rexiao : [{"id":"31","name":"单份番茄鸡蛋","price":"7","cover":"/Uploads/Picture/2018-01-13/5a59fcfcd9fe4.jpg","pingfen":"0","info":"驱蚊器二"},{"id":"33","name":"梅菜扣肉","price":"18","cover":"/Uploads/Picture/2018-03-09/5aa24c416ef7d.jpg","pingfen":"0","info":"驱蚊器二群"}]
     */

    private String text;
    private String shop_id;
    private String num;
    private String img;
    private String classify;
    private List<String> thumb;
    private List<TuishopBean> tuishop;
    private List<FangchanBean> fangchan;
    private List<FudaiBean> fudai;
    private List<RexiaoBean> rexiao;

    private List<HomeHeadBean> gglunbo;

    public List<HomeHeadBean> getGglunbo() {
        return gglunbo;
    }

    public void setGglunbo(List<HomeHeadBean> gglunbo) {
        this.gglunbo = gglunbo;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<String> getThumb() {
        return thumb;
    }

    public void setThumb(List<String> thumb) {
        this.thumb = thumb;
    }

    public List<TuishopBean> getTuishop() {
        return tuishop;
    }

    public void setTuishop(List<TuishopBean> tuishop) {
        this.tuishop = tuishop;
    }

    public List<FangchanBean> getFangchan() {
        return fangchan;
    }

    public void setFangchan(List<FangchanBean> fangchan) {
        this.fangchan = fangchan;
    }

    public List<FudaiBean> getFudai() {
        return fudai;
    }

    public void setFudai(List<FudaiBean> fudai) {
        this.fudai = fudai;
    }

    public List<RexiaoBean> getRexiao() {
        return rexiao;
    }

    public void setRexiao(List<RexiaoBean> rexiao) {
        this.rexiao = rexiao;
    }

    public static class TuishopBean {
        /**
         * id : 125
         * cover : /Uploads/Picture/2017-11-23/5a1688723c8da.png
         * sp_name : 饕餮宴遇火锅
         * dp_grade : 0.0
         * distance : 1039.32
         * cate : 美食
         * area : 汉滨区
         */

        private String id;
        private String cover;
        private String sp_name;
        private String dp_grade;
        private String distance;
        private String cate;
        private String area;

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

        public String getDp_grade() {
            return dp_grade;
        }

        public void setDp_grade(String dp_grade) {
            this.dp_grade = dp_grade;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getCate() {
            return cate;
        }

        public void setCate(String cate) {
            this.cate = cate;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }
    }

    public static class FangchanBean {
        /**
         * id : 242
         * cover : /Uploads/Picture/2018-01-13/5a59b4ec27fec.jpg
         * sp_name : 任永超
         * title : null
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

    public static class FudaiBean {
        /**
         * id : 1
         * title : 风云诡谲弓发挥稳定
         * cover : /Uploads/Picture/2018-01-13/5a59b4da86e77.jpg
         * now_price : 34.00
         */

        private String id;
        private String title;
        private String cover;
        private String now_price;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getNow_price() {
            return now_price;
        }

        public void setNow_price(String now_price) {
            this.now_price = now_price;
        }
    }

    public static class RexiaoBean {
        /**
         * id : 31
         * name : 单份番茄鸡蛋
         * price : 7
         * cover : /Uploads/Picture/2018-01-13/5a59fcfcd9fe4.jpg
         * pingfen : 0
         * info : 驱蚊器二
         */

        private String id;
        private String name;
        private String price;
        private String cover;
        private String pingfen;
        private String info;
        private String shop_id;

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getPingfen() {
            return pingfen;
        }

        public void setPingfen(String pingfen) {
            this.pingfen = pingfen;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}

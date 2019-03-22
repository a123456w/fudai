package com.ruirong.chefang.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public class FuBackHotelDetailsBean implements Serializable {

    /**
     * id : 240
     * sp_name : 文海酒店
     * sp_address : 北京市昌平区
     * mobile : 18233166261
     * dp_grade : 0.0
     * perpeo : 11
     * position_y : 109.049862
     * position_x : 32.69969
     * pics : 4461,4465,4542
     * classify_id : 21
     * browse : 12332
     * gonggao : 我问问热热地方
     * distance : 2.15
     * lunbo : ["/Uploads/Picture/2018-01-13/5a59fc53d9d4a.jpg","/Uploads/Picture/2018-01-15/5a5c8b38e2272.jpg","/Uploads/Picture/2018-03-14/5aa8d0c834e17.jpg"]
     * goods : [{"id":"37","name":"运动休闲鞋","price":"285","cover":"0"},{"id":"58","name":"凤椒泡爪","price":"30","cover":"/Uploads/Picture/2018-03-20/5ab0b08838c10.jpg"},{"id":"54","name":"旺旺雪饼","price":"23","cover":"/Uploads/Picture/2018-03-20/5ab0ae92ec482.jpg"},{"id":"46","name":"酸辣汤","price":"28","cover":"/Uploads/Picture/2018-03-20/5ab09e8c45586.jpg"},{"id":"45","name":"荔枝饮料","price":"30","cover":"/Uploads/Picture/2018-03-20/5ab086223ac91.jpg"},{"id":"44","name":"海鲜汇","price":"17","cover":"/Uploads/Picture/2018-03-20/5ab083635effb.jpg"}]
     * hotels : [{"id":"34","name":"标准房间","specif":["有空调","有电视","独立卫生间"],"attribute":[{"key":"面积","value":"15㎡"},{"key":"设备","value":"电视机、空调"}],"loop_pics":["/Uploads/Picture/2018-03-09/5aa24c3ec2abd.jpg"],"price":"80","yuan_price":"120","is_full":1},{"id":"32","name":"大床房","specif":["双人早餐","浴池","桑拿"],"attribute":[{"key":"30","value":"空调"}],"price":"268","yuan_price":"366","is_full":1}]
     * package : [{"id":"6","name":"茶叶大放送","shop_price":"3000.00","all_price":"2222.00","pic":"/Uploads/Picture/2018-03-20/5ab0a99db2147.jpg","dicinfo":"茶叶大放送","goodids":[{"id":"51","name":"普洱熟茶","num":"50"},{"id":"52","name":"浓香型冻顶乌龙茶叶","num":"50"}]},{"id":"7","name":"抱枕大甩卖","shop_price":"300.00","all_price":"150.00","pic":"/Uploads/Picture/2018-03-20/5ab0a9651c955.jpg","dicinfo":"抱枕大甩卖","goodids":[{"id":"50","name":"大号仿真创意烟盒抱枕靠垫","num":"888"},{"id":"49","name":"僵小鱼玩具公仔抱枕","num":"888"}]}]
     * pl_num : 20
     * grades : 1.2
     * pingluna : [{"id":"70","username":"虽然跟黑人反光板如果","start_num":"2","create_time":"1516691771","content":"是否合并分析对比方改变阳台鸡同鸭讲都发给你电饭锅","pics":["/Uploads/Picture/2018-01-23/5a66e12146ac6.png"],"reply":""},{"id":"71","username":"虽然跟黑人反光板如果","start_num":"2","create_time":"1516691771","content":"是否合并分析对比方改变阳台鸡同鸭讲都发给你电饭锅","pics":["/Uploads/Picture/2018-01-23/5a66e12146ac6.png"],"reply":""},{"id":"3","username":"guojaing","start_num":"5","create_time":"1516169130","content":"货品不错，发货及时\r\n\r\n颜色和图片一样 就是有一点色差 不过总体还好\r\n\r\n精致，漂亮，绝对物超所值啊\r\n\r\n好卖家发貨快速\r\n\r\n这样的价钱，这样的货，还是可以的","pics":["/Uploads/Picture/2018-01-23/5a66dc654968f.png"],"reply":""},{"id":"4","username":"jianghai","start_num":"5","create_time":"1516169130","content":"货品不错，发货及时\r\n\r\n颜色和图片一样 就是有一点色差 不过总体还好\r\n\r\n精致，漂亮，绝对物超所值啊\r\n\r\n好卖家发貨快速\r\n\r\n这样的价钱，这样的货，还是可以的","pics":["/Uploads/Picture/2018-01-23/5a66dc654968f.png"],"reply":""},{"id":"1","username":"永不言败","start_num":"5","create_time":"12343456","content":"货品不错，发货及时\r\n\r\n颜色和图片一样 就是有一点色差 不过总体还好\r\n\r\n精致，漂亮，绝对物超所值啊\r\n\r\n好卖家发貨快速\r\n\r\n这样的价钱，这样的货，还是可以的","pics":["/Uploads/Picture/2018-01-13/5a59b91417d25.jpg","/Uploads/Picture/2018-01-13/5a59fbbedc250.jpg","/Uploads/Picture/2018-01-13/5a59fc53d9d4a.jpg"],"reply":""},{"id":"98","username":"小高","start_num":"0","create_time":"1521531944","content":"高端大气上档次","pics":["/Uploads/Picture/2018-03-20/5ab07a13a8af2.jpg"],"reply":""},{"id":"97","username":"小菊","start_num":"0","create_time":"1521531922","content":"很实用，也很耐用，必须好评","pics":["/Uploads/Picture/2018-03-20/5ab078558688e.jpg"],"reply":""},{"id":"96","username":"小蓝","start_num":"0","create_time":"1521531868","content":"很帅气，很NICE。赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞赞","pics":["/Uploads/Picture/2018-03-20/5ab074653db5e.jpg"],"reply":""},{"id":"95","username":"小安","start_num":"0","create_time":"1521531824","content":"穿起来很舒服","pics":[],"reply":""},{"id":"94","username":"小小","start_num":"0","create_time":"1521531425","content":"味道不错，但肉肥肉太多了","pics":[],"reply":""}]
     */

    private String id;
    private String sp_name;
    private String sp_address;
    private String mobile;
    private String dp_grade;
    private String perpeo;
    private String position_y;
    private String position_x;
    private String pics;
    private String classify_id;
    private String browse;
    private String gonggao;
    private String distance;
    private String pl_num;
    private String grades;
    private String is_start ;
    private List<KeytitleBean> keytitle;
    private List<String> lunbo;
    private List<GoodsBean> goods;
    private List<HotelsBean> hotels;
    @SerializedName("package")
    private List<PackageBean> packageX;
    private List<PinglunaBean> pingluna;

    public List<KeytitleBean> getKeytitle() {
        return keytitle;
    }

    public void setKeytitle(List<KeytitleBean> keytitle) {
        this.keytitle = keytitle;
    }

    public String getIs_start() {
        return is_start;
    }

    public void setIs_start(String is_start) {
        this.is_start = is_start;
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

    public String getSp_address() {
        return sp_address;
    }

    public void setSp_address(String sp_address) {
        this.sp_address = sp_address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDp_grade() {
        return dp_grade;
    }

    public void setDp_grade(String dp_grade) {
        this.dp_grade = dp_grade;
    }

    public String getPerpeo() {
        return perpeo;
    }

    public void setPerpeo(String perpeo) {
        this.perpeo = perpeo;
    }

    public String getPosition_y() {
        return position_y;
    }

    public void setPosition_y(String position_y) {
        this.position_y = position_y;
    }

    public String getPosition_x() {
        return position_x;
    }

    public void setPosition_x(String position_x) {
        this.position_x = position_x;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getClassify_id() {
        return classify_id;
    }

    public void setClassify_id(String classify_id) {
        this.classify_id = classify_id;
    }

    public String getBrowse() {
        return browse;
    }

    public void setBrowse(String browse) {
        this.browse = browse;
    }

    public String getGonggao() {
        return gonggao;
    }

    public void setGonggao(String gonggao) {
        this.gonggao = gonggao;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public List<String> getLunbo() {
        return lunbo;
    }

    public void setLunbo(List<String> lunbo) {
        this.lunbo = lunbo;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public List<HotelsBean> getHotels() {
        return hotels;
    }

    public void setHotels(List<HotelsBean> hotels) {
        this.hotels = hotels;
    }

    public List<PackageBean> getPackageX() {
        return packageX;
    }

    public void setPackageX(List<PackageBean> packageX) {
        this.packageX = packageX;
    }

    public List<PinglunaBean> getPingluna() {
        return pingluna;
    }

    public void setPingluna(List<PinglunaBean> pingluna) {
        this.pingluna = pingluna;
    }
    public static class KeytitleBean {
        /**
         * title : 不含早餐
         */

        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class GoodsBean implements Serializable {
        /**
         * id : 37
         * name : 运动休闲鞋
         * price : 285
         * cover : 0
         */

        private String id;
        private String name;
        private String price;
        private String cover;

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
    }

    public static class HotelsBean implements Serializable {
        /**
         * id : 34
         * name : 标准房间
         * specif : ["有空调","有电视","独立卫生间"]
         * attribute : [{"key":"面积","value":"15㎡"},{"key":"设备","value":"电视机、空调"}]
         * loop_pics : ["/Uploads/Picture/2018-03-09/5aa24c3ec2abd.jpg"]
         * price : 80
         * yuan_price : 120
         * is_full : 1
         */

        private String id;
        private String name;
        private String price;
        private String yuan_price;
        private String cover;
        private int is_full;
        private List<String> specif;
        private List<AttributeBean> attribute;
        private List<String> loop_pics;

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
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

        public String getYuan_price() {
            return yuan_price;
        }

        public void setYuan_price(String yuan_price) {
            this.yuan_price = yuan_price;
        }

        public int getIs_full() {
            return is_full;
        }

        public void setIs_full(int is_full) {
            this.is_full = is_full;
        }

        public List<String> getSpecif() {
            return specif;
        }

        public void setSpecif(List<String> specif) {
            this.specif = specif;
        }

        public List<AttributeBean> getAttribute() {
            return attribute;
        }

        public void setAttribute(List<AttributeBean> attribute) {
            this.attribute = attribute;
        }

        public List<String> getLoop_pics() {
            return loop_pics;
        }

        public void setLoop_pics(List<String> loop_pics) {
            this.loop_pics = loop_pics;
        }

        public static class AttributeBean implements Serializable {
            /**
             * key : 面积
             * value : 15㎡
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

    public static class PackageBean implements Serializable {
        /**
         * id : 6
         * name : 茶叶大放送
         * shop_price : 3000.00
         * all_price : 2222.00
         * pic : /Uploads/Picture/2018-03-20/5ab0a99db2147.jpg
         * dicinfo : 茶叶大放送
         * goodids : [{"id":"51","name":"普洱熟茶","num":"50"},{"id":"52","name":"浓香型冻顶乌龙茶叶","num":"50"}]
         */

        private String id;
        private String name;
        private String shop_price;
        private String all_price;
        private String pic;
        private String dicinfo;
        private String moban;
        private List<GoodidsBean> goodids;

        public String getMoban() {
            return moban;
        }

        public void setMoban(String moban) {
            this.moban = moban;
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

        public String getShop_price() {
            return shop_price;
        }

        public void setShop_price(String shop_price) {
            this.shop_price = shop_price;
        }

        public String getAll_price() {
            return all_price;
        }

        public void setAll_price(String all_price) {
            this.all_price = all_price;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getDicinfo() {
            return dicinfo;
        }

        public void setDicinfo(String dicinfo) {
            this.dicinfo = dicinfo;
        }

        public List<GoodidsBean> getGoodids() {
            return goodids;
        }

        public void setGoodids(List<GoodidsBean> goodids) {
            this.goodids = goodids;
        }

        public static class GoodidsBean implements Serializable {
            /**
             * id : 51
             * name : 普洱熟茶
             * num : 50
             */

            private String id;
            private String name;
            private String num;

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

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }
        }
    }

    public static class PinglunaBean implements Serializable {
        /**
         * id : 70
         * username : 虽然跟黑人反光板如果
         * start_num : 2
         * create_time : 1516691771
         * content : 是否合并分析对比方改变阳台鸡同鸭讲都发给你电饭锅
         * pics : ["/Uploads/Picture/2018-01-23/5a66e12146ac6.png"]
         * reply :
         */

        private String id;
        private String username;
        private String start_num;
        private String create_time;
        private String content;
        private String reply;
        private List<String> pics;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getStart_num() {
            return start_num;
        }

        public void setStart_num(String start_num) {
            this.start_num = start_num;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public List<String> getPics() {
            return pics;
        }

        public void setPics(List<String> pics) {
            this.pics = pics;
        }
    }
}

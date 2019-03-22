package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/7.
 */

public class GoodDetailsBean {

    /**
     * cartNum : 12
     * info : {"id":"31","name":"单份番茄鸡蛋","price":"7","specif":["个"],"salevalue":"6","loop_pics":["/Uploads/Picture/2018-03-09/5aa24c3ec2abd.jpg","/Uploads/Picture/2018-03-09/5aa24c416ef7d.jpg"],"attribute":[{"key":"份数","value":"1份"}],"showinfo":"/index?s=Home/Index/showinfo/id/31"}
     * pllist : [{"id":"1","username":"永不言败","content":"货品不错，发货及时\r\n\r\n颜色和图片一样 就是有一点色差 不过总体还好\r\n\r\n精致，漂亮，绝对物超所值啊\r\n\r\n好卖家发貨快速\r\n\r\n这样的价钱，这样的货，还是可以的","pics":["/Uploads/Picture/2018-01-13/5a59b91417d25.jpg","/Uploads/Picture/2018-01-13/5a59fbbedc250.jpg","/Uploads/Picture/2018-01-13/5a59fc53d9d4a.jpg"],"create_time":"1970-05-24 04:05","start_num":"5","user_pic":"/Uploads/Picture/2018-03-15/5aaa21a00589f.jpg"},{"id":"2","username":"易家福袋3268","content":"货品不错，发货及时\r\n\r\n颜色和图片一样 就是有一点色差 不过总体还好\r\n\r\n精致，漂亮，绝对物超所值啊\r\n\r\n好卖家发貨快速\r\n\r\n这样的价钱，这样的货，还是可以的","pics":["/Uploads/Picture/2018-01-15/5a5c5b6c65784.jpg"],"create_time":"2018-01-17 14:01","start_num":"5","user_pic":"/Uploads/Picture/2017-11-15/5a0bab6ba0fc6.jpg"},{"id":"3","username":"guojaing","content":"货品不错，发货及时\r\n\r\n颜色和图片一样 就是有一点色差 不过总体还好\r\n\r\n精致，漂亮，绝对物超所值啊\r\n\r\n好卖家发貨快速\r\n\r\n这样的价钱，这样的货，还是可以的","pics":["/Uploads/Picture/2018-01-23/5a66dc654968f.png"],"create_time":"2018-01-17 14:01","start_num":"5"},{"id":"4","username":"易家福袋4338","content":"货品不错，发货及时\r\n\r\n颜色和图片一样 就是有一点色差 不过总体还好\r\n\r\n精致，漂亮，绝对物超所值啊\r\n\r\n好卖家发貨快速\r\n\r\n这样的价钱，这样的货，还是可以的","pics":["/Uploads/Picture/2018-01-23/5a66dc654968f.png"],"create_time":"2018-01-17 14:01","start_num":"5","user_pic":"/Uploads/Picture/2017-09-16/59bc70c3a0d4d.jpg"},{"id":"93","username":"肖志杰","content":"味道不错，家人很喜欢","create_time":"2018-03-20 15:03","start_num":"0"}]
     */

    private String cartNum;
    private InfoBean info;
    private List<PllistBean> pllist;

    public String getCartNum() {
        return cartNum;
    }

    public void setCartNum(String cartNum) {
        this.cartNum = cartNum;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<PllistBean> getPllist() {
        return pllist;
    }

    public void setPllist(List<PllistBean> pllist) {
        this.pllist = pllist;
    }

    public static class InfoBean {
        /**
         * id : 31
         * name : 单份番茄鸡蛋
         * price : 7
         * specif : ["个"]
         * salevalue : 6
         * loop_pics : ["/Uploads/Picture/2018-03-09/5aa24c3ec2abd.jpg","/Uploads/Picture/2018-03-09/5aa24c416ef7d.jpg"]
         * attribute : [{"key":"份数","value":"1份"}]
         * showinfo : /index?s=Home/Index/showinfo/id/31
         */

        private String id;
        private String name;
        private String price;
        private String salevalue;
        private String showinfo;
        private List<String> specif;
        private List<String> loop_pics;
        private List<AttributeBean> attribute;

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

        public String getSalevalue() {
            return salevalue;
        }

        public void setSalevalue(String salevalue) {
            this.salevalue = salevalue;
        }

        public String getShowinfo() {
            return showinfo;
        }

        public void setShowinfo(String showinfo) {
            this.showinfo = showinfo;
        }

        public List<String> getSpecif() {
            return specif;
        }

        public void setSpecif(List<String> specif) {
            this.specif = specif;
        }

        public List<String> getLoop_pics() {
            return loop_pics;
        }

        public void setLoop_pics(List<String> loop_pics) {
            this.loop_pics = loop_pics;
        }

        public List<AttributeBean> getAttribute() {
            return attribute;
        }

        public void setAttribute(List<AttributeBean> attribute) {
            this.attribute = attribute;
        }

        public static class AttributeBean {
            /**
             * key : 份数
             * value : 1份
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

    public static class PllistBean {
        /**
         * id : 1
         * username : 永不言败
         * content : 货品不错，发货及时

         颜色和图片一样 就是有一点色差 不过总体还好

         精致，漂亮，绝对物超所值啊

         好卖家发貨快速

         这样的价钱，这样的货，还是可以的
         * pics : ["/Uploads/Picture/2018-01-13/5a59b91417d25.jpg","/Uploads/Picture/2018-01-13/5a59fbbedc250.jpg","/Uploads/Picture/2018-01-13/5a59fc53d9d4a.jpg"]
         * create_time : 1970-05-24 04:05
         * start_num : 5
         * user_pic : /Uploads/Picture/2018-03-15/5aaa21a00589f.jpg
         */

        private String id;
        private String username;
        private String content;
        private String create_time;
        private String start_num;
        private String user_pic;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getStart_num() {
            return start_num;
        }

        public void setStart_num(String start_num) {
            this.start_num = start_num;
        }

        public String getUser_pic() {
            return user_pic;
        }

        public void setUser_pic(String user_pic) {
            this.user_pic = user_pic;
        }

        public List<String> getPics() {
            return pics;
        }

        public void setPics(List<String> pics) {
            this.pics = pics;
        }
    }
}

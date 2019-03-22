package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by chenlipeng on 2017/12/26 0026
 * describe:  车房的
 */

public class CarAndRoomsBean {


    /**
     * id : 257
     * lb_title : 中信股份发布公告 2017年度房地产溢利79.41亿港元
     * sp_name : 我爱我家
     * sp_address : 北京市朝阳区朝来科技园二期创远路36号院8号楼
     * content : 创立于2000年的我爱我家，是伟业我爱我家集团旗下专门从事二手房经纪和房屋租赁业务的专业业务品牌，从2000年5月份，我爱我家全国第一家店面“北京甜水园店”开业，到2002年5月份，我爱我家业务在上海落地，短短两年时间，我爱我家迅速完成了包括北京、天津、太原、南京、苏州、上海、杭州7个城市在内的第一轮全国布局，成为国内知名的大型品牌连锁经纪品牌。
     2013年，我爱我家在全国范围内开启第二轮强势扩张，直营业务覆盖城市由原来的7个，扩展到包括四川成都和广西南宁在内的9个城市。与此同时，时隔十二年后，我爱我家重启加盟业务，通过加盟模式，将业务迅速扩展至江西南昌、湖南长沙、湖北武汉3大城市，2016年又将直营业务拓展至河南郑州、山东青岛和江苏无锡。
     凭借优质的房地产经纪服务，2012~2014年，“我爱我家”品牌连续三届被工信部下属的中国企业品牌研究中心评为中国房产中介服务行业C-BPI品牌力第一名。
     * browse : 0
     * dp_grade : 3.0
     * mobile : 4008-515-515
     * position_x : 32.687536
     * position_y : 109.006743
     * distence : 1057.98
     * pic : ["/Uploads/Picture/2018-03-27/5aba0f8484cca.jpg","/Uploads/Picture/2018-03-27/5aba0f84d6d50.jpg","/Uploads/Picture/2018-03-27/5aba0f8540551.jpg"]
     * canshu : [{"key":"外观","value":"03-06年分三期交房，欧式楼栋的外观很新"},{"key":"户型","value":"主力户型为110至160平米的两居和三居，户型全是南向或南北通透"},{"key":"环境","value":"绿化率32%，树木植物繁密，绿化带遍布小区内部，水系造景是一大亮点"},{"key":"车位","value":"小区内部人车分流，全部为地下停车，停车位紧张，地下车位600元/月"},{"key":"市政配套","value":"集中供暖"}]
     * collection : 1
     * goods : [{"id":"19","title":"康都南排楼双卧","canshu":"3室 2厅 1厨 2卫","desc":"这个房子的情况我很了解，房子满五年只有一套住房，东南双卫3居，保持好，客餐厅分离，卧室客厅分离互不打扰私密性好，3个卧室都能采光老人孩子都能有阳光，全部落地大波玻璃窗视野采光好。很诚心卖，看房方便。","pics":"/Uploads/Picture/2018-03-28/5abaeb0b96dc9.JPG"}]
     * pinglun : [{"id":"259","content":"地方比较繁华，邻居也都挺热情","username":"我爱家","start_num":"5","create_time":"2018-03-30","picsd":[],"name":null},{"id":"260","content":"如名字一样，我爱我家啊","username":"噼里啪啦","start_num":"4","create_time":"2018-03-30","picsd":[],"name":null},{"id":"261","content":"白天都好，晚上挺吵","username":"嘻嘻哈哈","start_num":"3","create_time":"2018-03-30","picsd":[],"name":null},{"id":"262","content":"夜生活丰富，很不错","username":"想想就好","start_num":"5","create_time":"2018-03-30","picsd":[],"name":null},{"id":"263","content":"不适合我这个性格居住，但是地方确实不错的","username":"做梦都在想","start_num":"3","create_time":"2018-03-30","picsd":[],"name":null}]
     * number : 5
     * score : 4
     */

    private String id;
    private String lb_title;
    private String sp_name;
    private String sp_address;
    private String content;
    private String browse;
    private String dp_grade;
    private String mobile;
    private String position_x;
    private String position_y;
    private String distence;
    private int collection;
    private String number;
    private String score;
    private String gonggao;
    private List<String> pic;
    private List<CanshuBean> canshu;
    private List<GoodsBean> goods;
    private List<PinglunBean> pinglun;

    public String getGonggao() {
        return gonggao;
    }

    public void setGonggao(String gonggao) {
        this.gonggao = gonggao;
    }

    public String getDistence() {
        return distence;
    }

    public void setDistence(String distence) {
        this.distence = distence;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLb_title() {
        return lb_title;
    }

    public void setLb_title(String lb_title) {
        this.lb_title = lb_title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBrowse() {
        return browse;
    }

    public void setBrowse(String browse) {
        this.browse = browse;
    }

    public String getDp_grade() {
        return dp_grade;
    }

    public void setDp_grade(String dp_grade) {
        this.dp_grade = dp_grade;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPosition_x() {
        return position_x;
    }

    public void setPosition_x(String position_x) {
        this.position_x = position_x;
    }

    public String getPosition_y() {
        return position_y;
    }

    public void setPosition_y(String position_y) {
        this.position_y = position_y;
    }


    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public List<CanshuBean> getCanshu() {
        return canshu;
    }

    public void setCanshu(List<CanshuBean> canshu) {
        this.canshu = canshu;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public List<PinglunBean> getPinglun() {
        return pinglun;
    }

    public void setPinglun(List<PinglunBean> pinglun) {
        this.pinglun = pinglun;
    }

    public static class CanshuBean {
        /**
         * key : 外观
         * value : 03-06年分三期交房，欧式楼栋的外观很新
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

    public static class GoodsBean {
        /**
         * id : 19
         * title : 康都南排楼双卧
         * canshu : 3室 2厅 1厨 2卫
         * desc : 这个房子的情况我很了解，房子满五年只有一套住房，东南双卫3居，保持好，客餐厅分离，卧室客厅分离互不打扰私密性好，3个卧室都能采光老人孩子都能有阳光，全部落地大波玻璃窗视野采光好。很诚心卖，看房方便。
         * pics : /Uploads/Picture/2018-03-28/5abaeb0b96dc9.JPG
         */

        private String id;
        private String title;
        private String canshu;
        private String desc;
        private String pics;

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

        public String getCanshu() {
            return canshu;
        }

        public void setCanshu(String canshu) {
            this.canshu = canshu;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPics() {
            return pics;
        }

        public void setPics(String pics) {
            this.pics = pics;
        }
    }

    public static class PinglunBean {
        /**
         * id : 259
         * content : 地方比较繁华，邻居也都挺热情
         * username : 我爱家
         * start_num : 5
         * create_time : 2018-03-30
         * picsd : []
         * name : null
         */

        private String id;
        private String content;
        private String username;
        private String start_num;
        private String create_time;
        private String name;
        private List<String> picsd;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getPicsd() {
            return picsd;
        }

        public void setPicsd(List<String> picsd) {
            this.picsd = picsd;
        }
    }
}

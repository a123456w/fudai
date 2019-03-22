package com.ruirong.chefang.bean;

/**
 * 首页头条
 * Created by BX on 2018/2/27.
 */

public class HomepageHeadlinesBean {

    /**
     * text : 特大喜讯，易家福袋携手饕餮宴遇火锅，年终“惠爆”全城！12月15日至12月23日，凡使用易家福袋支付，全场素菜1元购（30余样），荤菜5.8折！另享50%的消费返还！！！
     * shop_id : 212
     * num : 2221131
     * img : /Uploads/Picture/2018-01-15/5a5c5b6c65784.jpg
     */

    private String text;
    private String shop_id;
    private String num;
    private String img;

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
}

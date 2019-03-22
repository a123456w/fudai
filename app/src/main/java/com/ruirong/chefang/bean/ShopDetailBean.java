package com.ruirong.chefang.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by guo on 2017/4/27.
 */

public class ShopDetailBean {

    /**
     * cover : /Uploads/Picture/2017-04-26/590057f239f05.png
     * pics : ["/Uploads/Picture/2017-04-22/58fb04ab27e4d.png","/Uploads/Picture/2017-04-22/58fb04ab5f8fa.jpg","/Uploads/Picture/2017-04-26/5900839855ccf.png","/Uploads/Picture/2017-04-26/590057f239f05.png"]
     * area_id : 18
     * classify_id : 24
     * perpeo : 500
     * content : 测试地址111
     * is_good : 1
     * position-x : 32.694221
     * position-y : 109.072858
     * mobile : 0321-20184512
     * sp_name : 测试店铺
     * sp_address : 5号公寓
     * area_name : 汉滨区
     * trade_name : 美发
     * recomm : [{"id":"9","perpeo":"500","sp_name":"测试店铺","cover":"/Uploads/Picture/2017-04-26/590057f239f05.png"},{"id":"7","perpeo":"101","sp_name":"测试2132132132132132132132131321","cover":"/Uploads/Picture/2017-04-21/58f9932080b62.jpg"},{"id":"6","perpeo":"100","sp_name":" 正宗陕西风味餐馆","cover":"/Uploads/Picture/2017-04-22/58faf82bbc552.jpg"}]
     */

    private String cover;
    private String area_id;
    private String classify_id;
    private String perpeo;
    private String content;
    private String is_good;
    @SerializedName("position_x")
    private String positionx;
    @SerializedName("position_y")
    private String positiony;
    private String mobile;
    private String sp_name;
    private String sp_address;
    private String area_name;
    private String trade_name;
    private List<String> pics;
    private List<ShopItemBean> recomm;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getPerpeo() {
        return perpeo;
    }

    public void setPerpeo(String perpeo) {
        this.perpeo = perpeo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIs_good() {
        return is_good;
    }

    public void setIs_good(String is_good) {
        this.is_good = is_good;
    }

    public String getPositionx() {
        return positionx;
    }

    public void setPositionx(String positionx) {
        this.positionx = positionx;
    }

    public String getPositiony() {
        return positiony;
    }

    public void setPositiony(String positiony) {
        this.positiony = positiony;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getTrade_name() {
        return trade_name;
    }

    public void setTrade_name(String trade_name) {
        this.trade_name = trade_name;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public List<ShopItemBean> getRecomm() {
        return recomm;
    }

    public void setRecomm(List<ShopItemBean> recomm) {
        this.recomm = recomm;
    }


}

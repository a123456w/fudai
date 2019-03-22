package com.ruirong.chefang.bean;

/**首页的推荐房产和店铺的实体
 * Created by chenlipeng on 2018/4/8.
 */

public class ShopAndHouseBean {
    //房产

    private String title;
    private String address1;
    private String address2;
    //店铺
    private String id;
    private String cover;
    private String sp_name;


    private String dp_grade;
    private String distance;
    private String cate;
    private String area;

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

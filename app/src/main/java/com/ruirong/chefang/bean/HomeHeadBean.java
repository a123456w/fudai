package com.ruirong.chefang.bean;

/**
 * Created by 李 on 2018/4/3.
 */

public class HomeHeadBean {
    /**
     * id : 1
     * type_id : 1
     * type : 1
     * liulan : 123
     * pic : /Uploads/Picture/2018-04-03/5ac33ec049969.jpg
     * content : wqeqwewqeq
     * status : 0
     */

    private String id;
    private String type_id;
    private String type;
    private String liulan;
    private String pic;
    private String content;
    private String status;
    private String title;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLiulan() {
        return liulan;
    }

    public void setLiulan(String liulan) {
        this.liulan = liulan;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

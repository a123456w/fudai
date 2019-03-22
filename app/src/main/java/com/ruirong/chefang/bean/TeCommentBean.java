package com.ruirong.chefang.bean;

/**
 * Created by Administrator on 2018/3/27.
 */

public class TeCommentBean {

    /**
     * pic : /Uploads/Picture/2018-01-13/5a59b4b22ba68.jpg
     * goods_name : 我说他和五人同行
     */

    private String pic;
    private String goods_name;
    private String goods_id;
    private String content;
    private boolean isTrue;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public TeCommentBean() {
        this.isTrue = true;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }
}

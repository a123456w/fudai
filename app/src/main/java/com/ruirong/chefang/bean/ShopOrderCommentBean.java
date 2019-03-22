package com.ruirong.chefang.bean;

/**
 * Created by wu on 2018/4/1.
 * describe: 商城订单评价实体
 */

public class ShopOrderCommentBean {


    /**
     * pic : /Uploads/Picture/2018-01-13/5a59b4da86e77.jpg
     * goods_name : 君乐宝纯享酸奶原味
     */

    private String pic;
    private String goods_name;
    private String goods_id;
    private String content;
    private boolean isTrue;

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

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }
}

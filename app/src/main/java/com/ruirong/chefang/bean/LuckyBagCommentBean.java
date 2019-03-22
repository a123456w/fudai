package com.ruirong.chefang.bean;

/**
 * Created by 16690 on 2018/4/4.
 * describe:
 */

public class LuckyBagCommentBean {

    /**
     * pic : /Uploads/Picture/2018-01-13/5a59b4b22ba68.jpg
     * goods_name : 让头发韩国十多个色如果
     * goods_id :2
     * private boolean isTrue;
     */

    private String pic;
    private String goods_name;
    private String goods_id;
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

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }
}

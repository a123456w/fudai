package com.ruirong.chefang.event;

/**
 * Created by 16690 on 2018/4/2.
 * describe:
 */

public class CommentPicDeleteEvent {
    private String goodsId;
    private int position;

    public CommentPicDeleteEvent(String goodsId, int position) {
        this.goodsId = goodsId;
        this.position = position;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

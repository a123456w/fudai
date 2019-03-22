package com.ruirong.chefang.bean;

/**
 * Created by 16690 on 2018/3/27.
 * 预定订单评论实体类
 */

public class ReserveOrderCommentBean {


    /**
     * hotel_id : 66
     * shop_id : 240
     * pinglun : {"name":"总统套房","cover":""}
     */

    private String hotel_id;
    private String shop_id;
    private boolean isTrue;
    private PinglunBean pinglun;

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }

    public PinglunBean getPinglun() {
        return pinglun;
    }

    public void setPinglun(PinglunBean pinglun) {
        this.pinglun = pinglun;
    }


    public static class PinglunBean {
        /**
         * name : 总统套房
         * cover :
         */

        private String name;
        private String cover;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
    }
}

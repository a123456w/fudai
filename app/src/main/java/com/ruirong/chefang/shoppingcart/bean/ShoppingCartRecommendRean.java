package com.ruirong.chefang.shoppingcart.bean;

import java.util.List;

/**
 * Created by guo on 2017/8/23.
 */

public class ShoppingCartRecommendRean {

    /**
     * list : [{"goods_id":"49","goods_name":"荣耀畅玩5X 双卡双待 移动版 智能手机（破晓银）","shop_price":"999.00","index_pic":"/Uploads/Picture/2017-07-14/59681d4f4f648.png"}]
     * total : 11
     */

    private int total;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * goods_id : 49
         * goods_name : 荣耀畅玩5X 双卡双待 移动版 智能手机（破晓银）
         * shop_price : 999.00
         * index_pic : /Uploads/Picture/2017-07-14/59681d4f4f648.png
         */

        private String goods_id;
        private String goods_name;
        private String shop_price;
        private String index_pic;

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getShop_price() {
            return shop_price;
        }

        public void setShop_price(String shop_price) {
            this.shop_price = shop_price;
        }

        public String getIndex_pic() {
            return index_pic;
        }

        public void setIndex_pic(String index_pic) {
            this.index_pic = index_pic;
        }
    }
}

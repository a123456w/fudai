package com.ruirong.chefang.shoppingcart.bean;

import java.util.List;

/**
 * Created by guo on 2017/8/8.
 */

public class ShoppingCartBean {




    public boolean isChecked;


    /**
     * goods : [{"id":"26","goods_id":"173","goods_num":"3","selected":"1","goods_name":"美食世界","index_pic":"/Uploads/Picture/2017-08-21/599a9218acbc9.jpg","shop_price":"12.00","spec_xuan":"3_167","spec_xuan_name":"颜色3:绿色 大小:小","shixiao":0},{"id":"25","goods_id":"172","goods_num":"1","selected":"1","goods_name":"商品发布123","index_pic":"/Uploads/Picture/2017-08-19/5997e5d1579f9.png","shop_price":"12.00","spec_xuan":"1_165","spec_xuan_name":"颜色3:红色 大小:大","shixiao":0}]
     * name : 纪存希
     * store_id : 1
     */

    private String name;
    private String store_id;
    private List<GoodsBean> goods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * id : 26
         * goods_id : 173
         * goods_num : 3
         * selected : 1
         * goods_name : 美食世界
         * index_pic : /Uploads/Picture/2017-08-21/599a9218acbc9.jpg
         * shop_price : 12.00
         * spec_xuan : 3_167
         * spec_xuan_name : 颜色3:绿色 大小:小
         * shixiao : 0
         */

        private String id;
        private String goods_id;
        private int goods_num;
        private int selected;//1是选中
        private String goods_name;
        private String index_pic;
        private String shop_price;
        private String spec_xuan;
        private String spec_xuan_name;
        private int shixiao;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public int getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(int goods_num) {
            this.goods_num = goods_num;
        }

        public int getSelected() {
            return selected;
        }

        public void setSelected(int selected) {
            this.selected = selected;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getIndex_pic() {
            return index_pic;
        }

        public void setIndex_pic(String index_pic) {
            this.index_pic = index_pic;
        }

        public String getShop_price() {
            return shop_price;
        }

        public void setShop_price(String shop_price) {
            this.shop_price = shop_price;
        }

        public String getSpec_xuan() {
            return spec_xuan;
        }

        public void setSpec_xuan(String spec_xuan) {
            this.spec_xuan = spec_xuan;
        }

        public String getSpec_xuan_name() {
            return spec_xuan_name;
        }

        public void setSpec_xuan_name(String spec_xuan_name) {
            this.spec_xuan_name = spec_xuan_name;
        }

        public int getShixiao() {
            return shixiao;
        }

        public void setShixiao(int shixiao) {
            this.shixiao = shixiao;
        }
    }
}

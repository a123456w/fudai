package com.ruirong.chefang.adapter;

import java.util.List;

/**
 * Created by Administrator on 2018/3/7.
 */

public class CartListBean {

    /**
     * zong_price : 570
     * cartlist : [{"id":"32","num":"3","goods_name":"盼盼法式小面包奶香味1.5KG早餐食品 整箱 蛋糕点心办公室零食","price":"55"},{"id":"31","num":"3","goods_name":"君乐宝纯享酸奶原味","price":"90"},{"id":"35","num":"3","goods_name":"维达蓝色经典系列卫生纸巾3层140g27卷有芯卷纸 箱装","price":"45"}]
     * count : 9
     */

    private String zong_price;
    private String count;
    private List<CartlistBean> cartlist;

    public String getZong_price() {
        return zong_price;
    }

    public void setZong_price(String zong_price) {
        this.zong_price = zong_price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<CartlistBean> getCartlist() {
        return cartlist;
    }

    public void setCartlist(List<CartlistBean> cartlist) {
        this.cartlist = cartlist;
    }

    public static class CartlistBean {
        /**
         * id : 32
         * num : 3
         * goods_name : 盼盼法式小面包奶香味1.5KG早餐食品 整箱 蛋糕点心办公室零食
         * price : 55
         */

        private String id;
        private String num;
        private String goods_name;
        private String price;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}

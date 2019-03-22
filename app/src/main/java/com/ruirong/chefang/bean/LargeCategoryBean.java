package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */

public class LargeCategoryBean {

    /**
     * cate : [{"id":"1","name":"饮品"},{"id":"2","name":"膨化食品"},{"id":"9","name":"饼干糕点"},{"id":"10","name":"纸品"}]
     * cartcounut : 7
     * price : 525
     */

    private String cartcounut;
    private String price;
    private List<CateBean> cate;

    public String getCartcounut() {
        return cartcounut;
    }

    public void setCartcounut(String cartcounut) {
        this.cartcounut = cartcounut;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<CateBean> getCate() {
        return cate;
    }

    public void setCate(List<CateBean> cate) {
        this.cate = cate;
    }

    public static class CateBean {
        /**
         * id : 1
         * name : 饮品
         */

        private String id;
        private String name;
        private boolean isTrue;

        public boolean isTrue() {
            return isTrue;
        }

        public void setTrue(boolean aTrue) {
            isTrue = aTrue;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

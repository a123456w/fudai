package com.ruirong.chefang.shoppingcart.bean;

/**  购物车的基础类
 * Created by dillon on 2017/7/25.
 */

public class CartBaseBean {
    protected String id;
    protected String name;
    protected boolean isChoosed;


    public CartBaseBean() {
        super();
    }

    public CartBaseBean(String id, String name) {
        this.id = id;
        this.name = name;
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

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }
}

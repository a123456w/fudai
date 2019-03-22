package com.ruirong.chefang.event;

/**
 * Created by 87901 on 2016/10/29.
 */

public class OrderEvent {
    public boolean success;
    public String tag;

    public OrderEvent(boolean success,String tag) {
        this.success=success;
        this.tag = tag;
    }
}

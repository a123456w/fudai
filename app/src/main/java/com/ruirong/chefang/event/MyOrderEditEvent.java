package com.ruirong.chefang.event;

/**
 * Created by guo on 2017/5/22.
 */

public class MyOrderEditEvent {
    public int type;//0全部，1商家消费，2商品消费，3充值，4提现
    public int action;//1进入编辑状态，2取消编辑状态，3全选，4取消全选，5删除

    public MyOrderEditEvent(int action, int type) {
        this.action = action;
        this.type = type;
    }
}

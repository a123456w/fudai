package com.ruirong.chefang.bean;

/**银豆转账列表的item
 * Created by dillon on 2017/5/5.
 */

public class SilverTransferBean {

    /**
     * id : 14
     * create_time : 1493867110
     * transfer_money : 90
     * type : 1
     */

    private String id;
    private String create_time;
    private String transfer_money;
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTransfer_money() {
        return transfer_money;
    }

    public void setTransfer_money(String transfer_money) {
        this.transfer_money = transfer_money;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

package com.ruirong.chefang.bean;

import java.io.Serializable;

/**
 * 地址管理 地址列表单条
 * Created by dillon on 2017/4/25.
 */

public class AddressItemBean implements Serializable {


    /**
     * id : 213
     * uid : 5130
     * rec_name : 朱磊
     * mobile : 13474208311
     * address : 陕西省安康市汉阴县城关镇三元村六组
     * pos_code : 125000
     * is_defa : 1
     * ssx :
     */

    private String id;
    private String uid;
    private String rec_name;
    private String mobile;
    private String address;
    private String pos_code;
    private String is_defa;
    private String ssx;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRec_name() {
        return rec_name;
    }

    public void setRec_name(String rec_name) {
        this.rec_name = rec_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPos_code() {
        return pos_code;
    }

    public void setPos_code(String pos_code) {
        this.pos_code = pos_code;
    }

    public String getIs_defa() {
        return is_defa;
    }

    public void setIs_defa(String is_defa) {
        this.is_defa = is_defa;
    }

    public String getSsx() {
        return ssx;
    }

    public void setSsx(String ssx) {
        this.ssx = ssx;
    }

    @Override
    public String toString() {
        return "AddressItemBean{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", rec_name='" + rec_name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", pos_code='" + pos_code + '\'' +
                ", is_defa='" + is_defa + '\'' +
                ", ssx='" + ssx + '\'' +
                '}';
    }
}

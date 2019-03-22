package com.ruirong.chefang.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 16690 on 2018/3/26.
 */

public class ReserveOrderBean {


    /**
     * gqTime : 86400
     * list : [{"order_id":"13180920180309051324","create_time":"1517477817","status":"1","pay_status":"2","gq_status":"0","apyment_money":"160.00","money":"80.00","dao_time":"2147483647","li_time":"123131231","sp_name":"丽晶轩风味餐厅","cover":"/Uploads/Picture/2017-06-16/5943bd51081b9.png","specif":""}]
     */

    private String gqTime;
    private List<ListBean> list;

    public String getGqTime() {
        return gqTime;
    }

    public void setGqTime(String gqTime) {
        this.gqTime = gqTime;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        /**
         * order_id : 13180920180309051324
         * create_time : 1517477817
         * status : 1
         * pay_status : 2
         * gq_status : 0
         * apyment_money : 160.00
         * money : 80.00
         * comment_state: 0
         * dao_time : 2147483647
         * li_time : 123131231
         * sp_name : 丽晶轩风味餐厅
         * cover : /Uploads/Picture/2017-06-16/5943bd51081b9.png
         * specif :
         * cancel :0
         */

        private String order_id;
        private String create_time;
        private String status;
        private String pay_status;
        private String gq_status;
        private String apyment_money;
        private String money;
        private String dao_time;
        private String li_time;
        private String sp_name;
        private String cover;
        private String specif;
        private String cancel;
        private String comment_state;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPay_status() {
            return pay_status;
        }

        public void setPay_status(String pay_status) {
            this.pay_status = pay_status;
        }

        public String getGq_status() {
            return gq_status;
        }

        public void setGq_status(String gq_status) {
            this.gq_status = gq_status;
        }

        public String getApyment_money() {
            return apyment_money;
        }

        public void setApyment_money(String apyment_money) {
            this.apyment_money = apyment_money;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getDao_time() {
            return dao_time;
        }

        public void setDao_time(String dao_time) {
            this.dao_time = dao_time;
        }

        public String getLi_time() {
            return li_time;
        }

        public void setLi_time(String li_time) {
            this.li_time = li_time;
        }

        public String getSp_name() {
            return sp_name;
        }

        public void setSp_name(String sp_name) {
            this.sp_name = sp_name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getSpecif() {
            return specif;
        }

        public void setSpecif(String specif) {
            this.specif = specif;
        }

        public String getCancel() {
            return cancel;
        }

        public void setCancel(String cancel) {
            this.cancel = cancel;
        }

        public String getComment_state() {
            return comment_state;
        }

        public void setComment_state(String comment_state) {
            this.comment_state = comment_state;
        }
    }
}

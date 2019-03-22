package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by EDZ on 2018/3/23.
 */

public class CashBackBean {
    /**
     * residue_money : 855.47
     * list : [{"money":"855.47","today_cash":"0.17","create_time":"1513881005"}]
     */

    private String residue_money;
    private List<ListBean> list;

    public String getResidue_money() {
        return residue_money;
    }

    public void setResidue_money(String residue_money) {
        this.residue_money = residue_money;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * money : 855.47
         * today_cash : 0.17
         * create_time : 1513881005
         */

        private String money;
        private String today_cash;
        private String create_time;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getToday_cash() {
            return today_cash;
        }

        public void setToday_cash(String today_cash) {
            this.today_cash = today_cash;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}

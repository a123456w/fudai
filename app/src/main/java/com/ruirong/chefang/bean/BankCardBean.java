package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by dillon on 2017/5/2.
 */

public class BankCardBean {

    /**
     * list : [{"id":"1","bank":"中国建设银行","bank_card_number":"6228480402564890018"},{"id":"2","bank":"中国工商银行","bank_card_number":"6228480402564890456"},{"id":"3","bank":"中国银行","bank_card_number":"6228480648900181234"}]
     * status : 1
     */

    private String status;
    private List<ListBean> list;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 1
         * bank : 中国建设银行
         * bank_card_number : 6228480402564890018
         */

        private String id;
        private String bank;
        private String bank_card_number;
        private int bgcolor;

        public int getBgcolor() {
            return bgcolor;
        }

        public void setBgcolor(int bgcolor) {
            this.bgcolor = bgcolor;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBank_card_number() {
            return bank_card_number;
        }

        public void setBank_card_number(String bank_card_number) {
            this.bank_card_number = bank_card_number;
        }
    }
}

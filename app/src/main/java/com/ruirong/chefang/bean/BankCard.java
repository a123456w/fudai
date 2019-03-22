package com.ruirong.chefang.bean;

/**
 * Created by dillon on 2017/6/2.
 */

public class BankCard {

    /**
     * id : 3
     * bank_card_number : 1234
     * bank : 中国银行
     * money : 900.00
     */

    private String id;
    private String bank_card_number;
    private String bank;
    private String money;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank_card_number() {
        return bank_card_number;
    }

    public void setBank_card_number(String bank_card_number) {
        this.bank_card_number = bank_card_number;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}

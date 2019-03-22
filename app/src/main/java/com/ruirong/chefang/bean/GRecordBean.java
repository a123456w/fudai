package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by EDZ on 2018/3/27.
 * 消费记录
 */

public class GRecordBean {

    /**
     * year : 2016
     * month : [{"month":"06","info":[{"id":"21","price":"312.00","symbol":"+","msg":"订单 退款","type":"1","create_time":"1466668515"},{"id":"20","price":"39.00","symbol":"-","msg":"购买店铺商品,订单号:73937420180320102109","type":"1","create_time":"1466668510"}]},{"month":"04","info":[{"id":"15","price":"39.00","symbol":"-","msg":"购买店铺商品,订单号:73937420180320102109","type":"1","create_time":"1461398110"}]}]
     */

    private int year;
    private List<MonthBean> month;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<MonthBean> getMonth() {
        return month;
    }

    public void setMonth(List<MonthBean> month) {
        this.month = month;
    }

    public static class MonthBean {
        /**
         * month : 06
         * info : [{"id":"21","price":"312.00","symbol":"+","msg":"订单 退款","type":"1","create_time":"1466668515"},{"id":"20","price":"39.00","symbol":"-","msg":"购买店铺商品,订单号:73937420180320102109","type":"1","create_time":"1466668510"}]
         */

        private String month;
        private List<PurchaseHistoryInfo> info;

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public List<PurchaseHistoryInfo> getInfo() {
            return info;
        }

        public void setInfo(List<PurchaseHistoryInfo> info) {
            this.info = info;
        }
    }
}

package com.ruirong.chefang.bean;

import java.util.List;

/**金豆增值详情
 * Created by dillon on 2017/9/13.
 */

public class GoldAddDetailBean {


    /**
     * sum : 0.004
     * dateEarnings : 0
     * detail : [{"total":"0.001","time":"1","create_time":"1497675698"},{"total":"0.001","time":"2","create_time":"1497675698"},{"total":"0.001","time":"3","create_time":"1497675698"},{"total":"0.001","time":"4","create_time":"1497675698"}]
     * selfMoney : null
     */

    private String sum;
    private String dateEarnings;
    private String selfMoney;
    private List<DetailBean> detail;

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getDateEarnings() {
        return dateEarnings;
    }

    public void setDateEarnings(String dateEarnings) {
        this.dateEarnings = dateEarnings;
    }

    public String getSelfMoney() {
        return selfMoney;
    }

    public void setSelfMoney(String selfMoney) {
        this.selfMoney = selfMoney;
    }

    public List<DetailBean> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailBean> detail) {
        this.detail = detail;
    }

    public static class DetailBean {
        /**
         * total : 0.001
         * time : 1
         * create_time : 1497675698
         */

        private String total;
        private String time;
        private String create_time;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}

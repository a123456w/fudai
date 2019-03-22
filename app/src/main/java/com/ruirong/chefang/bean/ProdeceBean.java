package com.ruirong.chefang.bean;

import java.io.Serializable;
import java.util.List;

/**产品
 * Created by dillon on 2017/9/11.
 */

public class ProdeceBean implements Serializable{


    /**
     * data : [{"id":"4","item_name":"一年基金","date_line":"365","residue":"270000.00","total_amount":"300000.00","rate_interest":"0.004","explain":"存入后的365天连本带利返还","img":"/Uploads/Picture/2017-08-30/59a69d219462e.jpg","annual_interest_rate":1.46},{"id":"3","item_name":"半年基金","date_line":"180","residue":"230000.00","total_amount":"300000.00","rate_interest":"0.003","explain":"存入后的180天连本带利返还","img":"/Uploads/Picture/2017-08-30/59a69d219462e.jpg","annual_interest_rate":1.095},{"id":"2","item_name":"三个月基金","date_line":"90","residue":"150000.00","total_amount":"300000.00","rate_interest":"0.002","explain":"存入后的90天连本带利返还","img":"/Uploads/Picture/2017-08-30/59a69d219462e.jpg","annual_interest_rate":0.73},{"id":"1","item_name":"一个月基金","date_line":"30","residue":"250000.00","total_amount":"300000.00","rate_interest":"0.001","explain":"存入后的30天连本带利返还","img":"/Uploads/Picture/2017-08-30/59a69d219462e.jpg","annual_interest_rate":0.365}]
     * advertising : 一年基金今日投放额度：300000.00,剩余投放额度：270000.00，半年基金今日投放额度：300000.00,剩余投放额度：230000.00，三个月基金今日投放额度：300000.00,剩余投放额度：150000.00，一个月基金今日投放额度：300000.00,剩余投放额度：250000.00
     */

    private String advertising;
    private List<DataBean> data;

    public String getAdvertising() {
        return advertising;
    }

    public void setAdvertising(String advertising) {
        this.advertising = advertising;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 4
         * item_name : 一年基金
         * date_line : 365
         * residue : 270000.00
         * total_amount : 300000.00
         * rate_interest : 0.004
         * explain : 存入后的365天连本带利返还
         * img : /Uploads/Picture/2017-08-30/59a69d219462e.jpg
         * annual_interest_rate : 1.46
         */

        private String id;
        private String item_name;
        private String date_line;
        private String residue;
        private String total_amount;
        private String rate_interest;
        private String explain;
        private String img;
        private double annual_interest_rate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        public String getDate_line() {
            return date_line;
        }

        public void setDate_line(String date_line) {
            this.date_line = date_line;
        }

        public String getResidue() {
            return residue;
        }

        public void setResidue(String residue) {
            this.residue = residue;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getRate_interest() {
            return rate_interest;
        }

        public void setRate_interest(String rate_interest) {
            this.rate_interest = rate_interest;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public double getAnnual_interest_rate() {
            return annual_interest_rate;
        }

        public void setAnnual_interest_rate(double annual_interest_rate) {
            this.annual_interest_rate = annual_interest_rate;
        }
    }
}

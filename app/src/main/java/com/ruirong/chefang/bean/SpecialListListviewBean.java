package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by chenlipeng on 2017/12/28 0028
   describe:  特产列表
 */


public class SpecialListListviewBean {

    /**
     * list : [{"id":"31","pic":"/Uploads/Picture/2018-01-13/5a59b4da86e77.jpg","now_price":"0.00","before_price":"3453.00","name":"大豆腐","xnum":"3"},{"id":"24","pic":"/Uploads/Picture/2018-01-13/5a59b5120ebb4.jpg","now_price":"0.00","before_price":"23.00","name":"豆腐222","xnum":"23"},{"id":"37","pic":"/Uploads/Picture/2018-01-15/5a5c62d70bca7.jpg","now_price":"0.00","before_price":"345.00","name":"爱而已","xnum":"34"}]
     * total : 1
     */

    private int total;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 31
         * pic : /Uploads/Picture/2018-01-13/5a59b4da86e77.jpg
         * now_price : 0.00
         * before_price : 3453.00
         * name : 大豆腐
         * xnum : 3
         */

        private String id;
        private String pic;
        private String now_price;
        private String before_price;
        private String name;
        private String xnum;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getNow_price() {
            return now_price;
        }

        public void setNow_price(String now_price) {
            this.now_price = now_price;
        }

        public String getBefore_price() {
            return before_price;
        }

        public void setBefore_price(String before_price) {
            this.before_price = before_price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getXnum() {
            return xnum;
        }

        public void setXnum(String xnum) {
            this.xnum = xnum;
        }
    }
}
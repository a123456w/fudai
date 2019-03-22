package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by EDZ on 2018/3/28.
 */

public class ShoppingCartBean {
    /**
     * list : [{"id":"34","comid":"42","name":"我说他和五人同行","state":"1","id_del":"-1","now_price":"23.00","numbers":"5","pic":"/Uploads/Picture/2018-01-13/5a59b4b22ba68.jpg"},{"id":"31","comid":"46","name":"5天人合一让他","state":"1","id_del":"0","now_price":"298.00","numbers":"2","pic":"/Uploads/Picture/2018-03-28/5abb470bc8709.jpg"},{"id":"24","comid":"45","name":"5天人合一让他","state":"1","id_del":"0","now_price":"54.00","numbers":"4","pic":"/Uploads/Picture/2018-01-13/5a59b4ff1f07d.jpg"},{"id":"21","comid":"44","name":"我说他和五人同行","state":"1","id_del":"0","now_price":"235.00","numbers":"5","pic":"/Uploads/Picture/2018-03-28/5abb5015eb92d.jpg"}]
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
         * id : 34
         * comid : 42
         * name : 我说他和五人同行
         * state : 1
         * id_del : -1
         * now_price : 23.00
         * numbers : 5
         * pic : /Uploads/Picture/2018-01-13/5a59b4b22ba68.jpg
         */

        private String id;
        private String comid;
        private String name;
        private String state;
        private String id_del;
        private String now_price;
        private String numbers;
        private String pic;
        private Boolean isChoosed = false; //是否选中

        public Boolean getChoosed() {
            return isChoosed;
        }

        public void setChoosed(Boolean choosed) {
            isChoosed = choosed;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getComid() {
            return comid;
        }

        public void setComid(String comid) {
            this.comid = comid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getId_del() {
            return id_del;
        }

        public void setId_del(String id_del) {
            this.id_del = id_del;
        }

        public String getNow_price() {
            return now_price;
        }

        public void setNow_price(String now_price) {
            this.now_price = now_price;
        }

        public String getNumbers() {
            return numbers;
        }

        public void setNumbers(String numbers) {
            this.numbers = numbers;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}

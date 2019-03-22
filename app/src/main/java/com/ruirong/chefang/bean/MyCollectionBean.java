package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by 16690 on 2018/3/25.
 */

public class MyCollectionBean {

    /**
     * list : [{"id":"32","name":"豆腐222","money":"234.00","pic":"/Uploads/Picture/2018-01-13/5a59b5120ebb4.jpg","fenshu":2.5}]
     * total : 2
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
         * id : 32
         * name : 豆腐222
         * money : 234.00
         * pic : /Uploads/Picture/2018-01-13/5a59b5120ebb4.jpg
         * fenshu : 2.5
         */

        private String id;
        private String pid;
        private String name;
        private String money;
        private String pic;
        private double fenshu;
        private boolean checked;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public double getFenshu() {
            return fenshu;
        }

        public void setFenshu(double fenshu) {
            this.fenshu = fenshu;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}

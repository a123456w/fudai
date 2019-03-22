package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/1.
 */

public class SpecialtyCommodityEvaluateBean {

    /**
     * list : [{"id":"309","content":"xxx","username":"德玛西亚","start_num":"4","create_time":"2018-04-02","pic":["/Uploads/Album/2018-04-02/5ac1d7aed05ba.jpg","/Uploads/Album/2018-04-02/5ac1d7aed3dc4.jpg","/Uploads/Album/2018-04-02/5ac1d7aed4a01.jpg"]},{"id":"308","content":"fff","username":"德玛西亚","start_num":"2","create_time":"2018-04-02","pic":["/Uploads/Album/2018-04-02/5ac1d79e5673d.jpg","/Uploads/Album/2018-04-02/5ac1d79e5997b.jpg","/Uploads/Album/2018-04-02/5ac1d79e5a44c.jpg"]},{"id":"222","content":"asdsdffsda","username":"德玛西亚","start_num":"5","create_time":"2018-03-29","pic":[]}]
     * total : 1
     * score : 3.6666666666667
     */

    private int total;
    private double score;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 309
         * content : xxx
         * username : 德玛西亚
         * start_num : 4
         * create_time : 2018-04-02
         * pic : ["/Uploads/Album/2018-04-02/5ac1d7aed05ba.jpg","/Uploads/Album/2018-04-02/5ac1d7aed3dc4.jpg","/Uploads/Album/2018-04-02/5ac1d7aed4a01.jpg"]
         */

        private String id;
        private String content;
        private String username;
        private String start_num;
        private String create_time;
        private List<String> pic;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getStart_num() {
            return start_num;
        }

        public void setStart_num(String start_num) {
            this.start_num = start_num;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public List<String> getPic() {
            return pic;
        }

        public void setPic(List<String> pic) {
            this.pic = pic;
        }
    }
}

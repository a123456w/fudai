package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/1.
 */

public class EachChildCommentBean {


    /**
     * list : [{"id":"401","pics":[],"content":"酒不错，很好喝~","start_num":"4","create_time":"1523342459","username":"天天"},{"id":"384","pics":["/Uploads/Album/2018-04-09/5acb2bdcd3eba.jpg","/Uploads/Album/2018-04-10/5acc0eb14a34a.jpg"],"content":"好酒，好喝～～","start_num":"3","create_time":"1523322545","username":"福粉4101"},{"id":"373","pics":["/Uploads/Album/2018-04-09/5acb2bdcd3eba.jpg"],"content":"不错(*๓´╰╯`๓)♡","start_num":"2","create_time":"1523265573","username":"福粉4101"},{"id":"372","pics":["/Uploads/Album/2018-04-09/5acb2bdcd3eba.jpg"],"content":"不错不错(*๓´╰╯`๓)♡","start_num":"4","create_time":"1523264476","username":"福粉4101"},{"id":"360","pics":[],"content":"挖切割费违法","start_num":"3","create_time":"1523240905","username":"求安慰郭文贵"},{"id":"347","pics":[],"content":"无法是我的错","start_num":"1","create_time":"1523237799","username":"沙发斯蒂芬是否"},{"id":"345","pics":[],"content":"好的","start_num":"1","create_time":"1523193674","username":"哈哈"},{"id":"334","pics":[],"content":"呃呃呃额额的","start_num":"4","create_time":"1522839646","username":"福粉5846"},{"id":"317","pics":[],"content":"哈哈哈大法师法师的方式发送到发","start_num":"0","create_time":"1522718822","username":"哈哈哈哈"}]
     * total : 1
     * pingjun : 2.4444444444444
     */

    private String total;
    private String pingjun;
    private List<ListBean> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPingjun() {
        return pingjun;
    }

    public void setPingjun(String pingjun) {
        this.pingjun = pingjun;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 401
         * pics : []
         * content : 酒不错，很好喝~
         * start_num : 4
         * create_time : 1523342459
         * username : 天天
         */

        private String id;
        private String content;
        private String start_num;
        private String create_time;
        private String username;
        private List<String> pics;

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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<String> getPics() {
            return pics;
        }

        public void setPics(List<String> pics) {
            this.pics = pics;
        }
    }
}

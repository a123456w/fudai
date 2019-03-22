package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by BX on 2018/2/26.
 */

public class RankingBean {


    /**
     * list : [{"id":"11","sp_name":"韵达快递","content":"韵达快递电子商务快递服务团队为您提供综合供应链管理服务，充分利用韵达各种优质资源，有效的降低了您的物流管理成本，确保服务质量。","uidname":"/Uploads/Picture/2016-09-19/57dfa11ed4873.png"},{"id":"10","sp_name":"爱施华灯饰","content":"爱施华灯饰安康最专业最高端的灯饰品牌！爱施华灯饰有品、有质、有故事！如果有灯饰需求的朋友请选购爱施华灯饰！","uidname":"/Uploads/Picture/2016-09-19/57dfa11ed4873.png"},{"id":"7","sp_name":"陕西熙恩置业有限责任公司","content":"陕西熙恩置业有限公司秉承\u201c和乐生活 情感传承\u201d的理念，致力于通过全产业链条的上下游综合服务，在产品和服务上超越客户预期，为客户带来生活方式的改变。为业主提供和谐喜乐的生活环境，同时时刻抱着情感倾注的心，以真心服务客户，实现精神共鸣。","uidname":"/Uploads/Picture/2016-09-19/57dfa11ed4873.png"},{"id":"4","sp_name":"豪爵专业男士养生spa","content":"男士高端SPA","uidname":"/Uploads/Picture/2017-06-19/5946a9931c75d.jpg"},{"id":"2","sp_name":"丽晶轩风味餐厅","content":"丽晶轩风味餐厅，邀请国内顶级营养师加盟，精心调配出适合不同人群的养生膳食，特色陕南风味菜肴无微不至的关心着您的味觉及健康。","uidname":"/Uploads/Picture/2016-09-19/57dfa11ed4873.png"},{"id":"1","sp_name":"K秀影音派对KTV","content":"K~秀影音派对KTV（英文：K~SHOW），顾名思义是集电影、K歌、烧烤为一体，可以为顾客量身定制各类聚会Patry，符合当下订制化、个性化消费趋势的，具有颠覆性思维的全新概念的量贩式KTV，可以让您在闲暇之余同时体验K歌和观影的不同乐趣。","uidname":"/Uploads/Picture/2017-09-07/59b089df3d2c6.jpeg"},{"id":"22","sp_name":"升达家居馆","content":"升达公司自建立以来就以\"关注环境，关爱人生、回归自然、持续发展\"为基本理念，根植于林业产业领域，始终坚持\u201c林板一体化\u201d发展战略，到目前已经成为国内林业行业\u201c林板-体化\u201d经营模式代表企业之一，经营范围为木门、衣柜、墙纸、地板。","uidname":"/Uploads/Picture/2016-09-19/57dfa11ed4873.png"},{"id":"14","sp_name":"宏升堂大药房","content":"宏升堂大药房经营产品包括中药、西药、营养保健、医疗器械等，购药选择宏升堂大药房100%正品保证、安全放心。","uidname":"/Uploads/Picture/2016-09-19/57dfa11ed4873.png"},{"id":"15","sp_name":"太子灯饰","content":"本店经营销售飞雕开关、飞雕家装水管、红旗电线，各种中高低档灯饰、卫浴、等","uidname":"/Uploads/Picture/2016-09-19/57dfa11ed4873.png"},{"id":"153","sp_name":"安果Ｃ私房烘培","content":"经营项目：生日蛋糕私人定制生日蛋糕、饼干、巧克力diy各种主题甜品台，等等。","uidname":"/Uploads/Picture/2016-09-19/57dfa11ed4873.png"}]
     * total : 16
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
         * id : 11
         * sp_name : 韵达快递
         * content : 韵达快递电子商务快递服务团队为您提供综合供应链管理服务，充分利用韵达各种优质资源，有效的降低了您的物流管理成本，确保服务质量。
         * uidname : /Uploads/Picture/2016-09-19/57dfa11ed4873.png
         */

        private String id;
        private String sp_name;
        private String content;
        private String uidname;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSp_name() {
            return sp_name;
        }

        public void setSp_name(String sp_name) {
            this.sp_name = sp_name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUidname() {
            return uidname;
        }

        public void setUidname(String uidname) {
            this.uidname = uidname;
        }
    }
}

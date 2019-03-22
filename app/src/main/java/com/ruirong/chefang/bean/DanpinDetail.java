package com.ruirong.chefang.bean;

import java.util.List;

/**单品详情
 * Created by dillon on 2018/4/25.
 */

public class DanpinDetail {

    /**
     * cartNum : 0
     * info : {"id":"28","name":"王老吉","price":"8.00","salevalue":"0","loop_pics":["/Uploads/Picture/2018-04-19/5ad89b9b6d8cc.png"],"attribute":[{"key":"品牌","value":"王老吉"},{"key":"产品类别","value":"凉茶"},{"key":"商品条形码","value":"6901424333948"},{"key":"保质期","value":"720（天）"},{"key":"净含量","value":"310ml"},{"key":"包装","value":"听装"}],"content":""}
     * pllist : []
     * url : /index.php?s=Home/Index/detail/id/28
     */

    private String cartNum;
    private InfoBean info;
    private String url;
    private List<?> pllist;

    public String getCartNum() {
        return cartNum;
    }

    public void setCartNum(String cartNum) {
        this.cartNum = cartNum;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<?> getPllist() {
        return pllist;
    }

    public void setPllist(List<?> pllist) {
        this.pllist = pllist;
    }

    public static class InfoBean {
        /**
         * id : 28
         * name : 王老吉
         * price : 8.00
         * salevalue : 0
         * loop_pics : ["/Uploads/Picture/2018-04-19/5ad89b9b6d8cc.png"]
         * attribute : [{"key":"品牌","value":"王老吉"},{"key":"产品类别","value":"凉茶"},{"key":"商品条形码","value":"6901424333948"},{"key":"保质期","value":"720（天）"},{"key":"净含量","value":"310ml"},{"key":"包装","value":"听装"}]
         * content :
         */

        private String id;
        private String name;
        private String price;
        private String salevalue;
        private String content;
        private List<String> loop_pics;
        private List<AttributeBean> attribute;

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSalevalue() {
            return salevalue;
        }

        public void setSalevalue(String salevalue) {
            this.salevalue = salevalue;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getLoop_pics() {
            return loop_pics;
        }

        public void setLoop_pics(List<String> loop_pics) {
            this.loop_pics = loop_pics;
        }

        public List<AttributeBean> getAttribute() {
            return attribute;
        }

        public void setAttribute(List<AttributeBean> attribute) {
            this.attribute = attribute;
        }

        public static class AttributeBean {
            /**
             * key : 品牌
             * value : 王老吉
             */

            private String key;
            private String value;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}

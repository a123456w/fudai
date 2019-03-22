package com.ruirong.chefang.bean;

/**分享的实体
 * Created by dillon on 2018/4/24.
 */

public class ShareBean {

    /**
     * id : 1
     * title : 优质吃、住、玩一网打尽，消费可享会员价，注册就送188元
     * content : 优质吃、住、玩一网打尽，消费可享会员价，注册就送188元
     * pic : /Uploads/Picture/2018-04-24/5adf13ccb2d50.jpg
     * url : http://fudai.rr-xcx.com/index.php?s=/api/Login/register.html&invite_phone=13005632348&platform=1&from=singlemessage&isappinstalled=0
     */

    private String id;
    private String title;
    private String content;
    private String pic;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

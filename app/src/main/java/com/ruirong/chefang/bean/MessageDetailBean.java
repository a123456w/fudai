package com.ruirong.chefang.bean;

/**
 * Created by dillon on 2017/6/11.
 */

public class MessageDetailBean {

    /**
     * id : 6844                                                  消息ID
     * content : 您的福币今日返现1.68,请注意查收                  标题
     * is_read : 1                                                是否以读  0 未读    1  已读
     * pic : null                                                 消息图片
     * text : null                                                消息描述
     * create_time : 1497168121                                   生成时间的时间戳
     */

    private String id;
    private String content;
    private String is_read;
    private String pic;
    private String text;
    private String create_time;

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

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}

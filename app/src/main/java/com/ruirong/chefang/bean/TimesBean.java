package com.ruirong.chefang.bean;

/**
 * Created by Administrator on 2018/3/5.
 */

public class TimesBean {
    private String time;//时间
    private String title_time;//展示时间

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle_time() {
        return title_time;
    }

    public void setTitle_time(String title_time) {
        this.title_time = title_time;
    }

    @Override
    public String toString() {
        return getTitle_time();
    }
}

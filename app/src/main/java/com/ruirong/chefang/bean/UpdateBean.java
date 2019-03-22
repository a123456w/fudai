package com.ruirong.chefang.bean;

/**
 * Created by guo on 2017/6/5.
 */

public class UpdateBean {

    /**
     * status : 1
     * version : 1.1
     * is_install : 0
     * url : http://test.qlzhx.cn/index.php/Version/appUrl/type/1
     * update_log : 最新的暂时
     */

    private int status;
    private String version;
    private String is_install;
    private String url;
    private String update_log;
    private String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIs_install() {
        return is_install;
    }

    public void setIs_install(String is_install) {
        this.is_install = is_install;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdate_log() {
        return update_log;
    }

    public void setUpdate_log(String update_log) {
        this.update_log = update_log;
    }
}

package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 */

public class Showlogistics {


    /**
     * LogisticCode : 70554506498995
     * ShipperCode : HTKY
     * Traces : [{"AcceptStation":"许昌市【许昌市区六部】，【刘向民/0374-6031330】已揽收","AcceptTime":"2018-03-20 08:04:04"},{"AcceptStation":"到许昌市【许昌市区五部集货点】","AcceptTime":"2018-03-20 08:04:15"},{"AcceptStation":"许昌市【许昌市区五部】，正发往【漯河转运中心】","AcceptTime":"2018-03-20 17:49:57"},{"AcceptStation":"到漯河市【漯河转运中心】","AcceptTime":"2018-03-20 23:45:17"},{"AcceptStation":"漯河市【漯河转运中心】，正发往【郑州转运中心】","AcceptTime":"2018-03-21 04:03:17"},{"AcceptStation":"到郑州市【郑州转运中心】","AcceptTime":"2018-03-21 07:20:04"},{"AcceptStation":"郑州市【郑州转运中心】，正发往【重庆转运中心】","AcceptTime":"2018-03-21 23:05:42"},{"AcceptStation":"到重庆市【重庆转运中心】","AcceptTime":"2018-03-23 01:19:23"},{"AcceptStation":"重庆市【重庆转运中心】，正发往【北京转运中心】","AcceptTime":"2018-03-24 01:24:56"},{"AcceptStation":"北京市【北京转运中心】，正发往【北京京顺转运中心】","AcceptTime":"2018-03-25 08:31:13"},{"AcceptStation":"到北京市【北京转运中心】","AcceptTime":"2018-03-25 08:58:07"},{"AcceptStation":"北京市【北京转运中心】，正发往【北京京顺转运中心】","AcceptTime":"2018-03-25 14:57:05"},{"AcceptStation":"到北京市【北京京顺转运中心】","AcceptTime":"2018-03-25 19:59:06"},{"AcceptStation":"北京市【北京京顺转运中心】，正发往【北京昌平区昌平二部】","AcceptTime":"2018-03-25 20:36:25"},{"AcceptStation":"到北京市【北京昌平区昌平二部】","AcceptTime":"2018-03-25 23:32:05"},{"AcceptStation":"到北京市【北京昌平区昌平二部】","AcceptTime":"2018-03-26 06:13:00"},{"AcceptStation":"北京市【北京昌平区昌平二部】，【昌二董超/13311206319】正在派件","AcceptTime":"2018-03-26 07:16:41"},{"AcceptStation":"北京市【北京昌平区昌平二部】，前台 已签收","AcceptTime":"2018-03-26 13:35:48"}]
     * State : 3
     * EBusinessID : 1297068
     * Success : true
     * expressname : 百世快递
     * expressphone : 95320
     * pic : /Uploads/Picture/2018-03-28/5abb3bba9f429.jpg
     */

    private String LogisticCode;
    private String ShipperCode;
    private String State;
    private String EBusinessID;
    private boolean Success;
    private String expressname;
    private String expressphone;
    private String pic;
    private List<TracesBean> Traces;

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String LogisticCode) {
        this.LogisticCode = LogisticCode;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String ShipperCode) {
        this.ShipperCode = ShipperCode;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public String getExpressname() {
        return expressname;
    }

    public void setExpressname(String expressname) {
        this.expressname = expressname;
    }

    public String getExpressphone() {
        return expressphone;
    }

    public void setExpressphone(String expressphone) {
        this.expressphone = expressphone;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<TracesBean> getTraces() {
        return Traces;
    }

    public void setTraces(List<TracesBean> Traces) {
        this.Traces = Traces;
    }

    public static class TracesBean {
        /**
         * AcceptStation : 许昌市【许昌市区六部】，【刘向民/0374-6031330】已揽收
         * AcceptTime : 2018-03-20 08:04:04
         */

        private String AcceptStation;
        private String AcceptTime;

        public String getAcceptStation() {
            return AcceptStation;
        }

        public void setAcceptStation(String AcceptStation) {
            this.AcceptStation = AcceptStation;
        }

        public String getAcceptTime() {
            return AcceptTime;
        }

        public void setAcceptTime(String AcceptTime) {
            this.AcceptTime = AcceptTime;
        }
    }
}

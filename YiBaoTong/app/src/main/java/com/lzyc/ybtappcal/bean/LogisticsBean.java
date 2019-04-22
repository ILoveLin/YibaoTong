package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lxx on 2017/4/21.
 */

public class LogisticsBean {

    /**
     * EBusinessID : 1284655
     * ShipperCode : ZTO
     * Success : true
     * LogisticCode : 433935602968
     * State : 3
     * Traces : [{"AcceptTime":"2017-04-10 20:40:52","AcceptStation":"[杭州市] [中吉杭州仓]的港仔文艺钱龙园已收件 电话:15819880243"},{"AcceptTime":"2017-04-11 20:36:22","AcceptStation":"[杭州市] 快件离开 [中吉杭州仓]已发往[石家庄中转]"},{"AcceptTime":"2017-04-12 02:30:57","AcceptStation":"[杭州市] 快件到达 [杭州汽运部]"},{"AcceptTime":"2017-04-12 02:32:20","AcceptStation":"[嘉兴市] 快件离开 [杭州中转部]已发往[石家庄中转]"},{"AcceptTime":"2017-04-13 01:47:01","AcceptStation":"[石家庄市] 快件到达 [石家庄]"},{"AcceptTime":"2017-04-13 02:04:42","AcceptStation":"[石家庄市] 快件离开 [石家庄]已发往[邢台]"},{"AcceptTime":"2017-04-13 10:31:54","AcceptStation":"[邢台市] 快件到达 [邢台]"},{"AcceptTime":"2017-04-13 11:06:31","AcceptStation":"[邢台市] 快件离开 [邢台]已发往[邢台会宁]"},{"AcceptTime":"2017-04-13 12:10:21","AcceptStation":"[邢台市] 快件到达 [邢台会宁]"},{"AcceptTime":"2017-04-13 13:41:31","AcceptStation":"[邢台市] [邢台会宁]的农业大学洗衣房正在第1次派件 电话:13180281505 请保持电话畅通、耐心等待"},{"AcceptTime":"2017-04-13 16:01:16","AcceptStation":"[邢台市] [邢台会宁]的派件已签收 感谢使用中通快递,期待再次为您服务!"}]
     */
    private String EBusinessID;
    private String ShipperCode;
    private boolean Success;
    private String LogisticCode;
    private String State;
    private List<TracesBean> Traces;

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String ShipperCode) {
        this.ShipperCode = ShipperCode;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String LogisticCode) {
        this.LogisticCode = LogisticCode;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public List<TracesBean> getTraces() {
        return Traces;
    }

    public void setTraces(List<TracesBean> Traces) {
        this.Traces = Traces;
    }

    public static class TracesBean {
        /**
         * AcceptTime : 2017-04-10 20:40:52
         * AcceptStation : [杭州市] [中吉杭州仓]的港仔文艺钱龙园已收件 电话:15819880243
         */

        private String AcceptTime;
        private String AcceptStation;

        public String getAcceptTime() {
            return AcceptTime;
        }

        public void setAcceptTime(String AcceptTime) {
            this.AcceptTime = AcceptTime;
        }

        public String getAcceptStation() {
            return AcceptStation;
        }

        public void setAcceptStation(String AcceptStation) {
            this.AcceptStation = AcceptStation;
        }

        @Override
        public String toString() {
            return "TracesBean{" +
                    "AcceptTime='" + AcceptTime + '\'' +
                    ", AcceptStation='" + AcceptStation + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LogisticsBean{" +
                "EBusinessID='" + EBusinessID + '\'' +
                ", ShipperCode='" + ShipperCode + '\'' +
                ", Success=" + Success +
                ", LogisticCode='" + LogisticCode + '\'' +
                ", State='" + State + '\'' +
                ", Traces=" + Traces +
                '}';
    }
}

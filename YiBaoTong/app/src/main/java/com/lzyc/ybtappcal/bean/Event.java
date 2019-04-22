package com.lzyc.ybtappcal.bean;

/**
 * 用于做事件统计
 * Created by yang on 2016/9/7.
 */
public class Event {

    private String eventCode;

    public Event(String eventCode){
        this.eventCode=eventCode;
    }

    public String getEventCode(){
        return eventCode;
    }

}

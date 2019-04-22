package com.lzyc.ybtappcal.bean;

import java.io.Serializable;

/**
 * 比药搜索选择城市的省市对应
 * Created by yang on 2017/04/24.
 */
public class PCity implements Serializable{
    private String proviceName;
    private String cityName;

    public String getProviceName() {
        return proviceName;
    }

    public void setProviceName(String proviceName) {
        this.proviceName = proviceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}

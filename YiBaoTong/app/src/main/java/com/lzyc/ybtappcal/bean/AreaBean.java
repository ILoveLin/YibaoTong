package com.lzyc.ybtappcal.bean;

/**
 * Created by xujm on 2016/3/7.
 */
public class AreaBean {

    private String id;
    private String city;

    public AreaBean(String id, String city) {
        this.id = id;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

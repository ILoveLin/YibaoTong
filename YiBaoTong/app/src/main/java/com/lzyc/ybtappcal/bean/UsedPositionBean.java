package com.lzyc.ybtappcal.bean;

/**
 * Created by xujm on 2016/3/7.
 */
public class UsedPositionBean {
    private String id;
    private String phone;
    private String address;
    private String lat;
    private String lng;
    private String is_del;


    public UsedPositionBean(String id, String phone, String address, String lat, String lng, String is_del) {
        this.id = id;
        this.phone = phone;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.is_del = is_del;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }
}

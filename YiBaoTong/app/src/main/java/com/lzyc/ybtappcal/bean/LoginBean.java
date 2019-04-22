package com.lzyc.ybtappcal.bean;

/**
 * Created by xujm on 2016/3/2.
 */
public class LoginBean {
    private String hostops;
    private String phone;
    private String username;
    private String nickname;
    private String city;
    private String ybtype;
    private String regionName;
    private String cityName;

    private String UID;


    public LoginBean(String hostops, String phone, String username, String nickname, String city) {
        this.hostops = hostops;
        this.phone = phone;
        this.username = username;
        this.nickname = nickname;
        this.city = city;
    }

    public String getHostops() {
        return hostops;
    }

    public void setHostops(String hostops) {
        this.hostops = hostops;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getYbtype() {
        return ybtype;
    }

    public void setYbtype(String ybtype) {
        this.ybtype = ybtype;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}

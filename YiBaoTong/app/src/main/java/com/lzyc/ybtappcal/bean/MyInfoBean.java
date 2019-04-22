package com.lzyc.ybtappcal.bean;

import java.io.Serializable;

/**
 * Created by xujm on 2016/3/7.
 */
public class MyInfoBean implements Serializable {

    private static final long serialVersionUID = 42L;


    private String sex;
    private String phone;
    private String username;
    private String cityId;
    private String nickname;
    private String cityName;
    private String age;
    private String regionName;
    private String invitCount;
    private String is_message;
    private String img;
    private String ybtype;
    private String regionId;
    private String inviteUrl;
    private String androidImg;
    private String shareUrl;
    private String iosImg;
    private String Balance;//余额
    private int plan;

    public MyInfoBean(String sex, String phone, String username, String cityId, String nickname, String cityName, String age, String regionName, String invitCount, String is_message, String img, String ybtype, String regionId, String inviteUrl, String androidImg, String shareUrl, String iosImg) {
        this.sex = sex;
        this.phone = phone;
        this.username = username;
        this.cityId = cityId;
        this.nickname = nickname;
        this.cityName = cityName;
        this.age = age;
        this.regionName = regionName;
        this.invitCount = invitCount;
        this.is_message = is_message;
        this.img = img;
        this.ybtype = ybtype;
        this.regionId = regionId;
        this.inviteUrl = inviteUrl;
        this.androidImg = androidImg;
        this.shareUrl = shareUrl;
        this.iosImg = iosImg;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getInvitCount() {
        return invitCount;
    }

    public void setInvitCount(String invitCount) {
        this.invitCount = invitCount;
    }

    public String getIs_message() {
        return is_message;
    }

    public void setIs_message(String is_message) {
        this.is_message = is_message;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getYbtype() {
        return ybtype;
    }

    public void setYbtype(String ybtype) {
        this.ybtype = ybtype;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getInviteUrl() {
        return inviteUrl;
    }

    public void setInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
    }

    public String getAndroidImg() {
        return androidImg;
    }

    public void setBalance(String Balance){
        this.Balance = Balance;
    }

    public String getBalance(){
        return Balance;
    }

    @Override
    public String toString() {
        return "MyInfoBean{" +
                "sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", cityId='" + cityId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", cityName='" + cityName + '\'' +
                ", age='" + age + '\'' +
                ", regionName='" + regionName + '\'' +
                ", invitCount='" + invitCount + '\'' +
                ", is_message='" + is_message + '\'' +
                ", img='" + img + '\'' +
                ", ybtype='" + ybtype + '\'' +
                ", regionId='" + regionId + '\'' +
                ", inviteUrl='" + inviteUrl + '\'' +
                ", androidImg='" + androidImg + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", iosImg='" + iosImg + '\'' +
                ", Balance='" + Balance + '\'' +
                '}';
    }

    public void setAndroidImg(String androidImg) {
        this.androidImg = androidImg;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getIosImg() {
        return iosImg;
    }

    public void setIosImg(String iosImg) {
        this.iosImg = iosImg;
    }


}

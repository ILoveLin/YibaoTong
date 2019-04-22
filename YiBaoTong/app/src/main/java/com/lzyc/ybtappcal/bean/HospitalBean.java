package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by xujm on 2016/3/4.
 * 搜索结果医保医院
 */
public class HospitalBean extends BaseBean {
    private boolean isClick = false;
    private String drugId;
    private String Name;
    private String Distance;

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    private String Price;
    private String number50;
    private String number20;
    private String number10;
    private String Yyid;
    private int typeAddress;   //是否社区,
    private int Reim;        //1为报销0为不报销
    private String zifei;    //自费价格      不报销的药,社区或者医院价格更自费的价格是一样的
    private String shequ;    //社区价格   none说明社区没有此药
    private String yiyuan;   //医院价格
    private String lat;
    private String lng;
    private String drugType;
    private String Jibie;
    private String Level;
    private String average;
    private int commentCount;
    private String phone;
    private List<String> hospitalImages;
    private String yyType;
    private String yuanJia;
    public HospitalBean(){}

    public List<String> getHospitalImages() {
        return hospitalImages;
    }

    public void setHospitalImages(List<String> hospitalImages) {
        this.hospitalImages = hospitalImages;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getJibie() {
        return Jibie;
    }

    public void setJibie(String jibie) {
        Jibie = jibie;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }


    @Override
    public String toString() {
        return "HospitalBean{" +
                "isClick=" + isClick +
                ", Name='" + Name + '\'' +
                ", Distance='" + Distance + '\'' +
                ", Price='" + Price + '\'' +
                ", number50='" + number50 + '\'' +
                ", number20='" + number20 + '\'' +
                ", number10='" + number10 + '\'' +
                ", Yyid='" + Yyid + '\'' +
                ", typeAddress=" + typeAddress +
                ", Reim=" + Reim +
                ", zifei='" + zifei + '\'' +
                ", shequ='" + shequ + '\'' +
                ", yiyuan='" + yiyuan + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", drugType='" + drugType + '\'' +
                ", Jibie='" + Jibie + '\'' +
                ", Level='" + Level + '\'' +
                ", average='" + average + '\'' +
                ", commentCount=" + commentCount +
                ", phone='" + phone + '\'' +
                '}';
    }

    public HospitalBean(String name, String distance, String price, String number50, String number20, String number10, String yyid) {
        Name = name;
        Distance = distance;
        Price = price;
        this.number50 = number50;
        this.number20 = number20;
        this.number10 = number10;
        Yyid = yyid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getNumber50() {
        return number50;
    }

    public void setNumber50(String number50) {
        this.number50 = number50;
    }

    public String getNumber20() {
        return number20;
    }

    public void setNumber20(String number20) {
        this.number20 = number20;
    }

    public String getNumber10() {
        return number10;
    }

    public void setNumber10(String number10) {
        this.number10 = number10;
    }

    public String getYyid() {
        return Yyid;
    }

    public void setYyid(String yyid) {
        Yyid = yyid;
    }

    public int getTypeAddress() {
        return typeAddress;
    }

    public void setTypeAddress(int typeAddress) {
        this.typeAddress = typeAddress;
    }

    public int getReim() {
        return Reim;
    }

    public void setReim(int reim) {
        Reim = reim;
    }

    public String getZifei() {
        return zifei;
    }

    public void setZifei(String zifei) {
        this.zifei = zifei;
    }

    public String getShequ() {
        return shequ;
    }

    public void setShequ(String shequ) {
        this.shequ = shequ;
    }

    public String getyiyuan() {
        return yiyuan;
    }

    public void setyiyuan(String yiyuan) {
        this.yiyuan = yiyuan;
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

    public String getYiyuan() {
        return yiyuan;
    }

    public void setYiyuan(String yiyuan) {
        this.yiyuan = yiyuan;
    }

    public String getDrugType() {
        return drugType;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }

    public String getYyType() {
        return yyType;
    }

    public void setYyType(String yyType) {
        this.yyType = yyType;
    }

    public String getYuanJia() {
        return yuanJia;
    }

    public void setYuanJia(String yuanJia) {
        this.yuanJia = yuanJia;
    }
}

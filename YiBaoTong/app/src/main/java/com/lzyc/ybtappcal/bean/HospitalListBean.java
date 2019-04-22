package com.lzyc.ybtappcal.bean;

/**
 * Created by xujm on 2016/3/4.
 * 医院列表
 */
public class HospitalListBean {
    /**
     * Yyid : 00100311000101
     * yytu : 0
     * HosName : 首都医科大学附属北京天坛医院
     * Level : 三级
     * Jibie : 甲等
     * url : http://new.yibaotongapp.com/
     * phone : 010-67096611
     * address : 北京市东城区东城区天坛西里6号
     * Type : 2
     */

    private String Yyid;
    private String yytu;
    private String HosName;
    private String Level;
    private String Jibie;
    private String url;
    private String phone;
    private String address;
    private String Type;

    public String getYyid() {
        return Yyid;
    }

    public void setYyid(String Yyid) {
        this.Yyid = Yyid;
    }

    public String getYytu() {
        return yytu;
    }

    public void setYytu(String yytu) {
        this.yytu = yytu;
    }

    public String getHosName() {
        return HosName;
    }

    public void setHosName(String HosName) {
        this.HosName = HosName;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String Level) {
        this.Level = Level;
    }

    public String getJibie() {
        return Jibie;
    }

    public void setJibie(String Jibie) {
        this.Jibie = Jibie;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    @Override
    public String toString() {
        return "HospitalListBean{" +
                "Yyid='" + Yyid + '\'' +
                ", yytu='" + yytu + '\'' +
                ", HosName='" + HosName + '\'' +
                ", Level='" + Level + '\'' +
                ", Jibie='" + Jibie + '\'' +
                ", url='" + url + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }

    /*


    private String HosName;
    private String yytu;
    private String Level;
    private String url;
    private String Jibie;
    private String Yyid;


    public HospitalListBean(String hosName, String yytu, String level, String url, String jibie, String yyid) {
        HosName = hosName;
        this.yytu = yytu;
        Level = level;
        this.url = url;
        Jibie = jibie;
        Yyid = yyid;
    }

    public String getHosName() {
        return HosName;
    }

    public void setHosName(String hosName) {
        HosName = hosName;
    }

    public String getYytu() {
        return yytu;
    }

    public void setYytu(String yytu) {
        this.yytu = yytu;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJibie() {
        return Jibie;
    }

    public void setJibie(String jibie) {
        Jibie = jibie;
    }

    public String getYyid() {
        return Yyid;
    }

    public void setYyid(String yyid) {
        Yyid = yyid;
    }

    @Override
    public String toString() {
        return "HospitalListBean{" +
                "HosName='" + HosName + '\'' +
                ", yytu='" + yytu + '\'' +
                ", Level='" + Level + '\'' +
                ", url='" + url + '\'' +
                ", Jibie='" + Jibie + '\'' +
                ", Yyid='" + Yyid + '\'' +
                '}';
    }*/
}

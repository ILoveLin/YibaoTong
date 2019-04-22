package com.lzyc.ybtappcal.bean;

/**
 * 创建者： 丁立鹏
 * 创建日期： 2016/3/8
 * 创建时间： 14:29
 * 包名： com.lzyc.ybtappcal.bean
 */
public class HospitalSearchBean {

    private String Yyid;
    //名字
    private String Name;
    //距离
    private String juli;
    //
    private String ShortName;

    public HospitalSearchBean(String yyid, String name, String juli, String shortName) {
        Yyid = yyid;
        Name = name;
        this.juli = juli;
        ShortName = shortName;
    }

    public String getYyid() {
        return Yyid;
    }

    public void setYyid(String yyid) {
        Yyid = yyid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getJuli() {
        return juli;
    }

    public void setJuli(String juli) {
        this.juli = juli;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }
}

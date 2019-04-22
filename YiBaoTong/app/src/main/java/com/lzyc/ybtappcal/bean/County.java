package com.lzyc.ybtappcal.bean;

/**
 * Created by lovelin on 2017/1/20.
 */
public class County {
    private String county;
    private int county_id;

    private String provinceName;
    private int provinceId;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public int getCounty_id() {
        return county_id;
    }

    public void setCounty_id(int county_id) {
        this.county_id = county_id;
    }

    @Override
    public String toString() {
        return "County{" +
                "county='" + county + '\'' +
                '}';
    }
}

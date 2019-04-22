package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lovelin on 2017/1/20.
 */
public class City {
    private String city;
    private int city_id;
    private List<County> countys;

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

    @Override
    public String toString() {
        return "City{" +
                "city='" + city + '\'' +
                '}';
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public List<County> getCountys() {
        return countys;
    }

    public void setCountys(List<County> countys) {
        this.countys = countys;
    }
}

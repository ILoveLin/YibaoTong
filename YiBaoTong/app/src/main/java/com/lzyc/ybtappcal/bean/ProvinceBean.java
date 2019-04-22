package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lovelin on 2017/1/20.
 */

public class ProvinceBean extends BaseBean {

    private String province;
    private int province_id;
    private List<City> citys;

    public List<City> getCitys() {
        return citys;
    }

    public void setCitys(List<City> citys) {
        this.citys = citys;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }
}

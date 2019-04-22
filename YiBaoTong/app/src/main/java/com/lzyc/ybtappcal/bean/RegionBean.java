package com.lzyc.ybtappcal.bean;


import java.util.List;

/**
 * 省份
 * package_name com.lzyc.ybtappcal.bean
 * Created by yang on 2016/9/2.
 */
public class RegionBean extends BaseBean {

    private String region_id;
    private String region_name;
    private List<CityBean> region;


    public RegionBean(String region_id, String region_name, List<CityBean> region) {
        this.region_id = region_id;
        this.region_name = region_name;
        this.region = region;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public List<CityBean> getRegion() {
        return region;
    }

    public void setRegion(List<CityBean> region) {
        this.region = region;
    }
}

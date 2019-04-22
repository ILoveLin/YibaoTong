package com.lzyc.ybtappcal.bean;

/**
 * Created by xujm on 2016/3/8.
 * 市
 */
public class CityBean {

    private String region_id;
    private String region_name;
    private boolean isSelected;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

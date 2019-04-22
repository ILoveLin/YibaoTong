package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by yang on 2016/11/18.
 */
public class SresultAreaBean extends BaseBean{
    private String area;
    private List<SresultStreetBean> streetList;
    private boolean isSelected;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<SresultStreetBean> getStreetList() {
        return streetList;
    }

    public void setStreetList(List<SresultStreetBean> streetList) {
        this.streetList = streetList;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

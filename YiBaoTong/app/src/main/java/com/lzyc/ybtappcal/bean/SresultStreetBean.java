package com.lzyc.ybtappcal.bean;

/**
 * Created by yang on 2016/11/18.
 */
public class SresultStreetBean extends BaseBean{
    private String street;
    private boolean isSelected;

    public SresultStreetBean() {}

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

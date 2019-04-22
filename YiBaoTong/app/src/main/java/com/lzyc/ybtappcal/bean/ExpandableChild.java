package com.lzyc.ybtappcal.bean;

import java.io.Serializable;

/**
 * 子item
 * Created by yang on 2017/05/31.
 */
public class ExpandableChild implements Serializable{
    private int childPosition;
    private boolean isChildSelected;

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    public boolean isChildSelected() {
        return isChildSelected;
    }

    public void setChildSelected(boolean childSelected) {
        isChildSelected = childSelected;
    }
}

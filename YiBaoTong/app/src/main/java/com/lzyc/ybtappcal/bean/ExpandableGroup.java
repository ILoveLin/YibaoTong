package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * 组
 * Created by yang on 2017/05/31.
 */
public class ExpandableGroup {
    private int groupPosition;
    private boolean isGroupSelected;
    private boolean isOtherType;//是否是其他不同类别（组item不同，子item也不同）
    private String groupDesc;//组描述
    public int getGroupPosition() {
        return groupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    public boolean isGroupSelected() {
        return isGroupSelected;
    }

    public void setGroupSelected(boolean groupSelected) {
        isGroupSelected = groupSelected;
    }

    public void setOtherType(boolean otherType) {
        isOtherType = otherType;
    }

    public boolean isOtherType() {
        return isOtherType;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }
}

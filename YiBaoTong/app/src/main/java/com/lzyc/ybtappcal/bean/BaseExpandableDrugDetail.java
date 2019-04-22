package com.lzyc.ybtappcal.bean;

/**
 * Expandable界面model基类
 * package_name com.lzyc.ybtappcal.bean
 * Created by yang on 2016/8/1.
 */
public class BaseExpandableDrugDetail extends BaseBean{
    private String groupTitle;//group标题
    private String groupDesc;//group描述数据
    private boolean isGroupSelected;//group点击事件

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public boolean isGroupSelected() {
        return isGroupSelected;
    }

    public void setGroupSelected(boolean groupSelected) {
        isGroupSelected = groupSelected;
    }
}

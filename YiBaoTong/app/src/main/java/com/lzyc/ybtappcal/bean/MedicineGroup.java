package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * 备注：全选删除崩溃问题解决方案；<br/>
 * 描述：之前写的方式是通过static方法设置group与child的关联，<br/>
 * 导致程序删除崩溃，static调的group下标始终就只会是0和最后一个，<br/>
 * group与child的数据关联是失败的.<br/>
 * 解决方案：用一个entity，在数据层建立关联<br/>
 * package_name com.lzyc.ybtappcal.bean
 * Created by yang on 2016/6/23.
 */
public class MedicineGroup extends BaseBean{
    private String DrugsTypeId;
    private String name;
    private int groupPosition;//group下标
    private boolean isShow;//是否展开
    private List<MedicineChild> childList;//child节点数据

    public int getGroupPosition() {
        return groupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }


    public String getDrugsTypeId() {
        return DrugsTypeId;
    }

    public void setDrugsTypeId(String drugsTypeId) {
        DrugsTypeId = drugsTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public List<MedicineChild> getChildList() {
        return childList;
    }

    public void setChildList(List<MedicineChild> childList) {
        this.childList = childList;
    }
}

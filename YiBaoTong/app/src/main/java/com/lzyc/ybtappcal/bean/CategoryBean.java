package com.lzyc.ybtappcal.bean;

import java.io.Serializable;

/**
 * Created by lxx on 2017/5/25.
 */

public class CategoryBean implements Serializable {

    /**
     * ID : 3
     * Name : 风湿骨科
     * Icon : http://new.yibaotongapp.com//uploads/image/categoryicon/cate_icon_3.png
     * Type : 1
     */

    private String ID;
    private String Name;
    private String Icon;
    private int Type;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    @Override
    public String toString() {
        return "CategoryBean{" +
                "ID='" + ID + '\'' +
                ", Name='" + Name + '\'' +
                ", Icon='" + Icon + '\'' +
                ", Type=" + Type +
                '}';
    }
}
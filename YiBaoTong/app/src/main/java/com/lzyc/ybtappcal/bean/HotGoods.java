package com.lzyc.ybtappcal.bean;

/**
 * 热门商品
 * Created by yang on 2017/05/12.
 */

public class HotGoods extends BaseBean{

    private int colorIndex;
    private String name;

    public int getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

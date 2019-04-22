package com.lzyc.ybtappcal.bean;

/**
 * 创建者： 丁立鹏
 * 创建日期： 2016/3/9
 * 创建时间： 14:09
 * 包名： com.lzyc.ybtappcal.bean
 */
public class PercentageBean extends BaseBean{
    //钱
    private String Price;
    //百分比
    private String Percent;

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPercent() {
        return Percent;
    }

    public void setPercent(String percent) {
        Percent = percent;
    }
}

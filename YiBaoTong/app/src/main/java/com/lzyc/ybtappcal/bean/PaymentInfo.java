package com.lzyc.ybtappcal.bean;

/**
 *
 *  支付信息的拼接
 * Created by yang on 2017/05/04.
 */

public class PaymentInfo extends BaseBean{
    private String name;//
    private String goodsName;
    private String orderId;//可以是线上支付的合并订单ID，和线上支付与线下支付的合并订单ID
    private String price;//商品价格
    private String returnMoney;//返现金额

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(String returnMoney) {
        this.returnMoney = returnMoney;
    }
}

package com.lzyc.ybtappcal.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yang on 2017/03/15.
 */
public class OrderInfo extends BaseBean implements Serializable{



    private String Count;
    private String Type;//1非处方otc，2处方，rx
    /**
     * otc，rx(state没有1)
     * 1.等待付款
     * 2.等待发货
     * 3.等待收货（未送到）
     * 4.等待收货（送达）
     * 5.交易成功
     * 6.小于0，交易关闭
     */
    private String State;//otc
    private String Image;
    private String AddressID;
    private String CreateTime;
    private String Balance;
    private String Specifications;
    private String GoodsName;
    private String Name;
    private String DeKaiPrice;
    /**
     * LogisticsCode :
     * Conversion :
     * Unit :
     * Logo :
     */

    private String Conversion;
    private String Unit;
    private String Logo;
    private String OrderID;
    private String PayPrice;
    private String PayType;
    private String TotalPrice;
    private String Freight;
    private String Logistics;
    private String LogisticsCom;
    private String LogisticsCode;
    private String Phone;
    private String OnlinePay;
    private String ReturnMoney;
    private String Detail;
    private String AddressRegion;
    private String AddressDetail;
    private List<GoodsBean> GoodsList;
    private String MerchantName;
    private String MerchantLogo;
    private String CancelMsg;
    private String RefundTime;


    public String getLogisticsCom() {
        return LogisticsCom;
    }

    public void setLogisticsCom(String logisticsCom) {
        LogisticsCom = logisticsCom;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getFreight() {
        return Freight;
    }

    public void setFreight(String freight) {
        Freight = freight;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPayPrice() {
        return PayPrice;
    }

    public void setPayPrice(String payPrice) {
        PayPrice = payPrice;
    }

    public String getSpecifications() {
        return Specifications;
    }

    public void setSpecifications(String specifications) {
        Specifications = specifications;
    }

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLogistics() {
        return Logistics;
    }

    public void setLogistics(String logistics) {
        Logistics = logistics;
    }

    public String getDeKaiPrice() {
        return DeKaiPrice;
    }

    public void setDeKaiPrice(String deKaiPrice) {
        DeKaiPrice = deKaiPrice;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getReturnMoney() {
        return ReturnMoney;
    }

    public void setReturnMoney(String returnMoney) {
        ReturnMoney = returnMoney;
    }

    public String getAddressID() {
        return AddressID;
    }

    public void setAddressID(String addressID) {
        AddressID = addressID;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getPayType() {
        return PayType;
    }

    public void setPayType(String payType) {
        PayType = payType;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getAddressRegion() {
        return AddressRegion;
    }

    public void setAddressRegion(String addressRegion) {
        AddressRegion = addressRegion;
    }

    public String getAddressDetail() {
        return AddressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        AddressDetail = addressDetail;
    }

    public String getLogisticsCode() {
        return LogisticsCode;
    }

    public void setLogisticsCode(String LogisticsCode) {
        this.LogisticsCode = LogisticsCode;
    }

    public String getOnlinePay() {
        return OnlinePay;
    }

    public void setOnlinePay(String onlinePay) {
        OnlinePay = onlinePay;
    }

    public List<GoodsBean> getGoodsList() {
        return GoodsList;
    }

    public void setGoodsList(List<GoodsBean> goodsList) {
        GoodsList = goodsList;
    }

    public String getConversion() {
        return Conversion;
    }

    public void setConversion(String Conversion) {
        this.Conversion = Conversion;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String Logo) {
        this.Logo = Logo;
    }

    public String getMerchantName() {
        return MerchantName;
    }

    public void setMerchantName(String merchantName) {
        MerchantName = merchantName;
    }

    public String getMerchantLogo() {
        return MerchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        MerchantLogo = merchantLogo;
    }

    public String getCancelMsg() {
        return CancelMsg;
    }

    public void setCancelMsg(String cancelMsg) {
        CancelMsg = cancelMsg;
    }

    public String getRefundTime() {
        return RefundTime;
    }

    public void setRefundTime(String refundTime) {
        RefundTime = refundTime;
    }
}

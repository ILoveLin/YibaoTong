package com.lzyc.ybtappcal.bean;

/**
 * Created by yang on 2017/04/27.
 */

public class DownShelvesBean extends BaseBean{
    private String DKID;
    private String Name;
    private String GoodsName;
    private String Specifications;
    private String Vender;
    private String Type;
    private String RetailPrice;
    private String DeKaiPrice;
    private String Image;
    private String TheShelves;
    private String MerchantID;
    private String OnlinePay;
    private String GoodsCount;

    public String getDKID() {
        return DKID;
    }

    public void setDKID(String DKID) {
        this.DKID = DKID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }

    public String getSpecifications() {
        return Specifications;
    }

    public void setSpecifications(String specifications) {
        Specifications = specifications;
    }

    public String getVender() {
        return Vender;
    }

    public void setVender(String vender) {
        Vender = vender;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getRetailPrice() {
        return RetailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        RetailPrice = retailPrice;
    }

    public String getDeKaiPrice() {
        return DeKaiPrice;
    }

    public void setDeKaiPrice(String deKaiPrice) {
        DeKaiPrice = deKaiPrice;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTheShelves() {
        return TheShelves;
    }

    public void setTheShelves(String theShelves) {
        TheShelves = theShelves;
    }

    public String getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(String merchantID) {
        MerchantID = merchantID;
    }

    public String getOnlinePay() {
        return OnlinePay;
    }

    public void setOnlinePay(String onlinePay) {
        OnlinePay = onlinePay;
    }

    public String getGoodsCount() {
        return GoodsCount;
    }

    public void setGoodsCount(String goodsCount) {
        GoodsCount = goodsCount;
    }
}

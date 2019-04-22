package com.lzyc.ybtappcal.bean;

/**
 * Created by yang on 2017/03/23.
 */

public class OrderDrugDetail extends BaseBean {
    private String Name;
    private String Count;
    private String DeKaiPrice;
    private String Type;
    private String Image;
    private String Vender;
    private String RetailPrice;
    private String Specifications;
    private String DKID;
    private String ReturnMoney;
    private String GoodsName;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getDeKaiPrice() {
        return DeKaiPrice;
    }

    public void setDeKaiPrice(String deKaiPrice) {
        DeKaiPrice = deKaiPrice;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getVender() {
        return Vender;
    }

    public void setVender(String vender) {
        Vender = vender;
    }

    public String getRetailPrice() {
        return RetailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        RetailPrice = retailPrice;
    }

    public String getSpecifications() {
        return Specifications;
    }

    public void setSpecifications(String specifications) {
        Specifications = specifications;
    }

    public String getDKID() {
        return DKID;
    }

    public void setDKID(String DKID) {
        this.DKID = DKID;
    }

    public String getReturnMoney() {
        return ReturnMoney;
    }

    public void setReturnMoney(String returnMoney) {
        ReturnMoney = returnMoney;
    }

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }
}

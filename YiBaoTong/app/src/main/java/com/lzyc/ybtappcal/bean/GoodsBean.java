package com.lzyc.ybtappcal.bean;

/**
 * Created by yang on 2017/04/27.
 */

public class GoodsBean extends BaseBean{
    private String TheShelves;
    private String MerchantID;
    private String OnlinePay;
    private boolean isChildSelected;//结算状态是否被选中
    private boolean isChildDeleteSelected;//删除状态是否被选中
    private int GoodsCount;
    private String Message;

    private String DKID;
    private String Name;
    private String GoodsName;
    private String Specifications;
    private String Vender;
    private String Type;
    private String DeKaiPrice;
    private String RetailPrice;
    private String Image;
    private String ReturnMoney;
    private String Count;
    private int groupPosition;
    private  int childPosition;

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

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

    public boolean isChildSelected() {
        return isChildSelected;
    }

    public void setChildSelected(boolean childSelected) {
        isChildSelected = childSelected;
    }

    public int getGoodsCount() {
        return GoodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.GoodsCount = goodsCount;
    }

    public String getDeKaiPrice() {
        return DeKaiPrice;
    }

    public void setDeKaiPrice(String deKaiPrice) {
        this.DeKaiPrice = deKaiPrice;
    }

    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    public String getRetailPrice() {
        return RetailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.RetailPrice = retailPrice;
    }

    public String getReturnMoney() {
        return ReturnMoney;
    }

    public void setReturnMoney(String returnMoney) {
        this.ReturnMoney = returnMoney;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isChildDeleteSelected() {
        return isChildDeleteSelected;
    }

    public void setChildDeleteSelected(boolean childDeleteSelected) {
        isChildDeleteSelected = childDeleteSelected;
    }


    public int getGroupPosition() {
        return groupPosition;
    }

    public int getChildPosition() {
        return childPosition;
    }

    @Override
    public String toString() {
        return "GoodsBean{" +
                "TheShelves='" + TheShelves + '\'' +
                ", MerchantID='" + MerchantID + '\'' +
                ", OnlinePay='" + OnlinePay + '\'' +
                ", isChildSelected=" + isChildSelected +
                ", isChildDeleteSelected=" + isChildDeleteSelected +
                ", GoodsCount=" + GoodsCount +
                ", Message='" + Message + '\'' +
                ", DKID='" + DKID + '\'' +
                ", Name='" + Name + '\'' +
                ", GoodsName='" + GoodsName + '\'' +
                ", Specifications='" + Specifications + '\'' +
                ", Vender='" + Vender + '\'' +
                ", Type='" + Type + '\'' +
                ", DeKaiPrice='" + DeKaiPrice + '\'' +
                ", RetailPrice='" + RetailPrice + '\'' +
                ", Image='" + Image + '\'' +
                ", ReturnMoney='" + ReturnMoney + '\'' +
                ", Count='" + Count + '\'' +
                '}';
    }
}

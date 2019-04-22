package com.lzyc.ybtappcal.bean;

/**
 * Created by yang on 17/06/21.
 */
public class RankingDrugDetail {
    private String YyPrice;
    private String BaoxiaoType;
    private String Price;
    private String Image;
    private String SqPrice;
    private int AdoptNum;
    private String AdoptMsg;
    private String Specifications;
    private String DrugID;
    private boolean isSelected;

    public String getYyPrice() {
        return YyPrice;
    }

    public void setYyPrice(String yyPrice) {
        YyPrice = yyPrice;
    }

    public String getBaoxiaoType() {
        return BaoxiaoType;
    }

    public void setBaoxiaoType(String baoxiaoType) {
        BaoxiaoType = baoxiaoType;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getSqPrice() {
        return SqPrice;
    }

    public void setSqPrice(String sqPrice) {
        SqPrice = sqPrice;
    }

    public int getAdoptNum() {
        return AdoptNum;
    }

    public void setAdoptNum(int adoptNum) {
        AdoptNum = adoptNum;
    }

    public String getSpecifications() {
        return Specifications;
    }

    public void setSpecifications(String specifications) {
        Specifications = specifications;
    }

    public String getDrugID() {
        return DrugID;
    }

    public void setDrugID(String drugID) {
        DrugID = drugID;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAdoptMsg() {
        return AdoptMsg;
    }

    public void setAdoptMsg(String adoptMsg) {
        AdoptMsg = adoptMsg;
    }
}

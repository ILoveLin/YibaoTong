package com.lzyc.ybtappcal.bean;

/**
 * package_name com.lzyc.ybtappcal.bean
 * Created by yang on 2016/6/23.
 */
public class MedicineChild extends BaseBean{
    private String id;
    private String DrugNameID;
    private String SpecificationsID;
    private String VenderID;
    private String Price;
    private String GoodsName;
    private String Conversion;
    private String Unit;
    private String Specifications;
    private String Vender;
    private String Images;
    private String ScanName;
    private boolean isSelected;//右上角选择框是否被选中，用于删除
    private int childPosition;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDrugNameID() {
        return DrugNameID;
    }

    public void setDrugNameID(String drugNameID) {
        DrugNameID = drugNameID;
    }

    public String getSpecificationsID() {
        return SpecificationsID;
    }

    public void setSpecificationsID(String specificationsID) {
        SpecificationsID = specificationsID;
    }

    public String getVenderID() {
        return VenderID;
    }

    public void setVenderID(String venderID) {
        VenderID = venderID;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }

    public String getConversion() {
        return Conversion;
    }

    public void setConversion(String conversion) {
        Conversion = conversion;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
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

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }

    public String getScanName() {
        return ScanName;
    }

    public void setScanName(String scanName) {
        ScanName = scanName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }
}

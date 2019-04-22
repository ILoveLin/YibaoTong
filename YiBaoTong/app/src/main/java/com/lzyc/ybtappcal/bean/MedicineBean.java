package com.lzyc.ybtappcal.bean;

/**
 * Created by xujm on 2016/3/8.
 */
public class MedicineBean {

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

    public MedicineBean(String id, String drugNameID, String specificationsID, String venderID, String price, String goodsName, String conversion, String unit, String specifications, String vender, String images) {
        this.id = id;
        DrugNameID = drugNameID;
        SpecificationsID = specificationsID;
        VenderID = venderID;
        Price = price;
        GoodsName = goodsName;
        Conversion = conversion;
        Unit = unit;
        Specifications = specifications;
        Vender = vender;
        Images = images;
    }

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
}

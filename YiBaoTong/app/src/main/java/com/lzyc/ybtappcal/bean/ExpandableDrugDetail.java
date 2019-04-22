package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * package_name com.lzyc.ybtappcal.bean
 * Created by yang on 2016/8/1.
 */
public class ExpandableDrugDetail extends BaseExpandableDrugDetail{
    private String drug_id;
    private String DrugNameID;
    private String SpecificationsID;
    private String VenderID;
    private String Name;
    private String Specifications;
    private String Vender;
    private String zy;
    private String Images;
    private String ScanName;
    private String GoodsName;
    private String Conversion;
    private String Unit;
    private String HosCount;
    private String yType;
    private String conditions;
    private String specification;
    private String venderdetailurl;
    private List<String> listChild;//child的数据，注：当前child的数据只有一个，这么定义方便以后扩展

    public String getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(String drug_id) {
        this.drug_id = drug_id;
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
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

    public String getHosCount() {
        return HosCount;
    }

    public void setHosCount(String hosCount) {
        HosCount = hosCount;
    }

    public String getyType() {
        return yType;
    }

    public void setyType(String yType) {
        this.yType = yType;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getVenderdetailurl() {
        return venderdetailurl;
    }

    public void setVenderdetailurl(String venderdetailurl) {
        this.venderdetailurl = venderdetailurl;
    }

    public List<String> getListChild() {
        return listChild;
    }

    public void setListChild(List<String> listChild) {
        this.listChild = listChild;
    }
}

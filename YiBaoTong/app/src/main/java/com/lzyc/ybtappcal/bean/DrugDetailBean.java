package com.lzyc.ybtappcal.bean;

import java.util.ArrayList;

/**
 * Created by xujm on 2016/3/4.
 * 药品详情
 */
public class DrugDetailBean  extends BaseBean{

    private String HosCount;
    private String zy;
    private String yType;
    private ArrayList<String> Images;
    private String ScanName;
    private String specification;
    private String venderdetailurl;
    private String sCount;
    private String yCount;

    private String venderCountry;
    private String SpecificationsID;
    private int Adopt;
    private int renkedu;
    private String Specifications;
    private String GoodsName;
    private String Name;
    private String Indication;
    private String drugImage;
    private String drugUsage;
    private String DrugNameID;
    private String Sell;
    private String Price;
    private String Vender;
    private String drug_id;
    private String VenderID;

    public String getsCount() {
        return sCount;
    }

    public void setsCount(String sCount) {
        this.sCount = sCount;
    }

    public String getyCount() {
        return yCount;
    }

    public void setyCount(String yCount) {
        this.yCount = yCount;
    }

    //药瓶详情页Message 动画的字段
    private String shequ;   //自付
    private String sPrice;  //原价

    private String yiyuan;  //自付
    private String yPrice;  //原价

    public String getShequ() {
        return shequ;
    }

    public void setShequ(String shequ) {
        this.shequ = shequ;
    }

    public String getsPrice() {
        return sPrice;
    }

    public void setsPrice(String sPrice) {
        this.sPrice = sPrice;
    }

    public String getYiyuan() {
        return yiyuan;
    }

    public void setYiyuan(String yiyuan) {
        this.yiyuan = yiyuan;
    }

    public String getyPrice() {
        return yPrice;
    }

    public void setyPrice(String yPrice) {
        this.yPrice = yPrice;
    }

    private ArrayList<String> conditions;

    public DrugDetailBean(){}

    public DrugDetailBean(String name, String hosCount, String zy, String drugNameID, String specificationsID, String vender, String yType, String venderID, ArrayList<String> images, String specifications, String specification, String venderdetailurl, String goodsName, ArrayList<String> conditions) {
        Name = name;
        HosCount = hosCount;
        this.zy = zy;
        DrugNameID = drugNameID;
        SpecificationsID = specificationsID;
        Vender = vender;
        this.yType = yType;
        VenderID = venderID;
        Images = images;
        Specifications = specifications;
        this.specification = specification;
        this.venderdetailurl = venderdetailurl;
        GoodsName = goodsName;
        this.conditions = conditions;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getHosCount() {
        return HosCount;
    }

    public void setHosCount(String hosCount) {
        HosCount = hosCount;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
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

    public String getVender() {
        return Vender;
    }

    public void setVender(String vender) {
        Vender = vender;
    }

    public String getyType() {
        return yType;
    }

    public void setyType(String yType) {
        this.yType = yType;
    }

    public String getVenderID() {
        return VenderID;
    }

    public void setVenderID(String venderID) {
        VenderID = venderID;
    }

    public ArrayList<String> getImages() {
        return Images;
    }

    public void setImages(ArrayList<String> images) {
        Images = images;
    }

    public String getScanName() {
        return ScanName;
    }

    public void setScanName(String scanName) {
        ScanName = scanName;
    }

    public String getSpecifications() {
        return Specifications;
    }

    public void setSpecifications(String specifications) {
        Specifications = specifications;
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

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }

    public ArrayList<String> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<String> conditions) {
        this.conditions = conditions;
    }

    public String getVenderCountry() {
        return venderCountry;
    }

    public void setVenderCountry(String venderCountry) {
        this.venderCountry = venderCountry;
    }

    public int getAdopt() {
        return Adopt;
    }

    public void setAdopt(int adopt) {
        Adopt = adopt;
    }

    public int getRenkedu() {
        return renkedu;
    }

    public void setRenkedu(int renkedu) {
        this.renkedu = renkedu;
    }

    public String getIndication() {
        return Indication;
    }

    public void setIndication(String indication) {
        Indication = indication;
    }

    public String getDrugImage() {
        return drugImage;
    }

    public void setDrugImage(String drugImage) {
        this.drugImage = drugImage;
    }

    public String getDrugUsage() {
        return drugUsage;
    }

    public void setDrugUsage(String drugUsage) {
        this.drugUsage = drugUsage;
    }

    public String getSell() {
        return Sell;
    }

    public void setSell(String sell) {
        Sell = sell;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(String drug_id) {
        this.drug_id = drug_id;
    }
}

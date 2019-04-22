package com.lzyc.ybtappcal.bean;

/**
 * Created by yang on 2016/9/9.
 */
public class PlacedDataBean extends BaseBean {

    private String Name;
    private String DrugNameID;
    private String SpecificationsID;
    private String Price;
    private String Vender;
    private String DrugHostopID;
    private String VenderID;
    private String Specifications;
    private String GoodsName;
    private String baoxiao;
    private String drugType;
    private int renkedu;
    private int Adopt;
    private int tuijian;
    private String drug_id;


    public PlacedDataBean(String name, String drugNameID, String specificationsID, String price, String vender, String drugHostopID, String venderID, String specifications, String goodsName, String baoxiao) {
        Name = name;
        DrugNameID = drugNameID;
        SpecificationsID = specificationsID;
        Price = price;
        Vender = vender;
        DrugHostopID = drugHostopID;
        VenderID = venderID;
        Specifications = specifications;
        GoodsName = goodsName;
        this.baoxiao = baoxiao;
    }

    public String getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(String drug_id) {
        this.drug_id = drug_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getVender() {
        return Vender;
    }

    public void setVender(String vender) {
        Vender = vender;
    }

    public String getDrugHostopID() {
        return DrugHostopID;
    }

    public void setDrugHostopID(String drugHostopID) {
        DrugHostopID = drugHostopID;
    }

    public String getVenderID() {
        return VenderID;
    }

    public void setVenderID(String venderID) {
        VenderID = venderID;
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

    public String getBaoxiao() {
        return baoxiao;
    }

    public void setBaoxiao(String baoxiao) {
        this.baoxiao = baoxiao;
    }

    public String getDrugType() {
        return drugType;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }

    public int getRenkedu() {
        return renkedu;
    }

    public void setRenkedu(int renkedu) {
        this.renkedu = renkedu;
    }

    public int getAdopt() {
        return Adopt;
    }

    public void setAdopt(int adopt) {
        Adopt = adopt;
    }

    public int getTuijian() {
        return tuijian;
    }

    public void setTuijian(int tuijian) {
        this.tuijian = tuijian;
    }

    @Override
    public String toString() {
        return "PlacedDataBean{" +
                "Name='" + Name + '\'' +
                ", DrugNameID='" + DrugNameID + '\'' +
                ", SpecificationsID='" + SpecificationsID + '\'' +
                ", Price='" + Price + '\'' +
                ", Vender='" + Vender + '\'' +
                ", DrugHostopID='" + DrugHostopID + '\'' +
                ", VenderID='" + VenderID + '\'' +
                ", Specifications='" + Specifications + '\'' +
                ", GoodsName='" + GoodsName + '\'' +
                ", baoxiao='" + baoxiao + '\'' +
                ", drugType='" + drugType + '\'' +
                ", renkedu=" + renkedu +
                ", Adopt=" + Adopt +
                ", tuijian=" + tuijian +
                '}';
    }
}

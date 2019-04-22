package com.lzyc.ybtappcal.bean;

/**
 * Created by xujm on 2016/3/4.
 * 药品认可度
 */
public class DrugConfirmBean {
    private String SpecificationsID;
    private String code;
    private String GoodsName;
    private String Name;
    private String Specifications;
    private String Unit;
    private String HosNum;
    private int num;
    private String DrugNameID;
    private String ComNum;
    private String Price;
    private String zPrice;
    private String NewPrice;
    private String Vender;
    private String VenderID;
    private String DrugHostopID;
    private String Conversion;
    private String ScanCode6;
    private String ScanCode;
    private String Images;
    private String ScanName;
    private String drug_id;
    private boolean isFirst =false;

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    /**
     * 新增的字段区分是否第一名药品的图标显示
     */
    private Boolean isNO1;

    public Boolean getNO1() {
        return isNO1;
    }

    public void setNO1(Boolean NO1) {
        isNO1 = NO1;
    }

    public DrugConfirmBean(String specificationsID, String code, String goodsName, String name, String specifications, String unit, String hosNum, int num, String drugNameID, String comNum, String price, String zPrice, String newPrice, String vender, String venderID, String drugHostopID, String conversion, String scanCode6, String scanCode, String images) {
        SpecificationsID = specificationsID;
        this.code = code;
        GoodsName = goodsName;
        Name = name;
        Specifications = specifications;
        Unit = unit;
        HosNum = hosNum;
        this.num = num;
        DrugNameID = drugNameID;
        ComNum = comNum;
        Price = price;
        this.zPrice = zPrice;
        NewPrice = newPrice;
        Vender = vender;
        VenderID = venderID;
        DrugHostopID = drugHostopID;
        Conversion = conversion;
        ScanCode6 = scanCode6;
        ScanCode = scanCode;
        Images = images;
    }

    public String getSpecificationsID() {
        return SpecificationsID;
    }

    public void setSpecificationsID(String specificationsID) {
        SpecificationsID = specificationsID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getSpecifications() {
        return Specifications;
    }

    public void setSpecifications(String specifications) {
        Specifications = specifications;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getHosNum() {
        return HosNum;
    }

    public void setHosNum(String hosNum) {
        HosNum = hosNum;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDrugNameID() {
        return DrugNameID;
    }

    public void setDrugNameID(String drugNameID) {
        DrugNameID = drugNameID;
    }

    public String getComNum() {
        return ComNum;
    }

    public void setComNum(String comNum) {
        ComNum = comNum;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getzPrice() {
        return zPrice;
    }

    public void setzPrice(String zPrice) {
        this.zPrice = zPrice;
    }

    public String getNewPrice() {
        return NewPrice;
    }

    public void setNewPrice(String newPrice) {
        NewPrice = newPrice;
    }

    public String getVender() {
        return Vender;
    }

    public void setVender(String vender) {
        Vender = vender;
    }

    public String getVenderID() {
        return VenderID;
    }

    public void setVenderID(String venderID) {
        VenderID = venderID;
    }

    public String getDrugHostopID() {
        return DrugHostopID;
    }

    public void setDrugHostopID(String drugHostopID) {
        DrugHostopID = drugHostopID;
    }

    public String getConversion() {
        return Conversion;
    }

    public void setConversion(String conversion) {
        Conversion = conversion;
    }

    public String getScanCode6() {
        return ScanCode6;
    }

    public void setScanCode6(String scanCode6) {
        ScanCode6 = scanCode6;
    }

    public String getScanCode() {
        return ScanCode;
    }

    public void setScanCode(String scanCode) {
        ScanCode = scanCode;
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

    @Override
    public String toString() {
        return "DrugConfirmBean{" +
                "SpecificationsID='" + SpecificationsID + '\'' +
                ", code='" + code + '\'' +
                ", GoodsName='" + GoodsName + '\'' +
                ", Name='" + Name + '\'' +
                ", Specifications='" + Specifications + '\'' +
                ", Unit='" + Unit + '\'' +
                ", HosNum='" + HosNum + '\'' +
                ", num='" + num + '\'' +
                ", DrugNameID='" + DrugNameID + '\'' +
                ", ComNum='" + ComNum + '\'' +
                ", Price='" + Price + '\'' +
                ", zPrice='" + zPrice + '\'' +
                ", NewPrice='" + NewPrice + '\'' +
                ", Vender='" + Vender + '\'' +
                ", VenderID='" + VenderID + '\'' +
                ", DrugHostopID='" + DrugHostopID + '\'' +
                ", Conversion='" + Conversion + '\'' +
                ", ScanCode6='" + ScanCode6 + '\'' +
                ", ScanCode='" + ScanCode + '\'' +
                ", Images='" + Images + '\'' +
                ", ScanName='" + ScanName + '\'' +
                '}';
    }

    public String getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(String drug_id) {
        this.drug_id = drug_id;
    }
}

package com.lzyc.ybtappcal.bean;

import java.io.Serializable;

/**
 * Created by xujm on 2016/3/7.
 */
public class PlanBean  implements Serializable{


    private static final long serialVersionUID = 43L;


    private String id;
    private String DrugNameID;
    private String SpecificationsID;
    private String VenderID;
    private String Price;
    private String GoodsName;
    private String HostopShortName;
    private String Conversion;
    private String Unit;
    private String Specifications;
    private String Vender;
    private String AmountMonryForPay;
    private String number10;
    private String number20;
    private String number50;
    private String Images;
    private String city;
    private String Name;
    private String Yyid;
    private String lng;
    private String lat;


    public PlanBean(String id, String drugNameID, String specificationsID, String venderID, String price, String goodsName, String hostopShortName, String conversion, String unit, String specifications, String vender, String amountMonryForPay, String number10, String number20, String number50, String images, String city, String name, String yyid, String lng, String lat) {
        this.id = id;
        DrugNameID = drugNameID;
        SpecificationsID = specificationsID;
        VenderID = venderID;
        Price = price;
        GoodsName = goodsName;
        HostopShortName = hostopShortName;
        Conversion = conversion;
        Unit = unit;
        Specifications = specifications;
        Vender = vender;
        AmountMonryForPay = amountMonryForPay;
        this.number10 = number10;
        this.number20 = number20;
        this.number50 = number50;
        Images = images;
        this.city = city;
        Name = name;
        Yyid = yyid;
        this.lng = lng;
        this.lat = lat;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getHostopShortName() {
        return HostopShortName;
    }

    public void setHostopShortName(String hostopShortName) {
        HostopShortName = hostopShortName;
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

    public String getAmountMonryForPay() {
        return AmountMonryForPay;
    }

    public void setAmountMonryForPay(String amountMonryForPay) {
        AmountMonryForPay = amountMonryForPay;
    }

    public String getNumber10() {
        return number10;
    }

    public void setNumber10(String number10) {
        this.number10 = number10;
    }

    public String getNumber20() {
        return number20;
    }

    public void setNumber20(String number20) {
        this.number20 = number20;
    }

    public String getNumber50() {
        return number50;
    }

    public void setNumber50(String number50) {
        this.number50 = number50;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getYyid() {
        return Yyid;
    }

    public void setYyid(String yyid) {
        Yyid = yyid;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}

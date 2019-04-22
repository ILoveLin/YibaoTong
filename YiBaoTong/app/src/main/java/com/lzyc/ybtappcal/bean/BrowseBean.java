package com.lzyc.ybtappcal.bean;

import java.io.Serializable;

/**
 * Created by yang on 2017/06/02.
 */

public class BrowseBean implements Serializable {
    private String LastTime;
    private String Specifications;
    private String DKID;
    private String GoodsName;
    private String DrugID;
    private String Name;
    private String DeKaiPrice;
    private String Image;
    private String Vender;
    private String ID;
    private String Msg;
    private String ReturnMoney;
    private String TheShelves;
    private String MedicineChest;
    private boolean isSelectedGoods;
    private boolean isSelectedDrug;
    private boolean isEquals;
    private boolean isAdded;

    public String getLastTime() {
        return LastTime;
    }

    public void setLastTime(String lastTime) {
        LastTime = lastTime;
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

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }

    public String getDrugID() {
        return DrugID;
    }

    public void setDrugID(String drugID) {
        DrugID = drugID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDeKaiPrice() {
        return DeKaiPrice;
    }

    public void setDeKaiPrice(String deKaiPrice) {
        DeKaiPrice = deKaiPrice;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getReturnMoney() {
        return ReturnMoney;
    }

    public void setReturnMoney(String returnMoney) {
        ReturnMoney = returnMoney;
    }

    public String getTheShelves() {
        return TheShelves;
    }

    public void setTheShelves(String theShelves) {
        TheShelves = theShelves;
    }

    public boolean isSelectedGoods() {
        return isSelectedGoods;
    }

    public void setSelectedGoods(boolean selectedGoods) {
        isSelectedGoods = selectedGoods;
    }

    public boolean isSelectedDrug() {
        return isSelectedDrug;
    }

    public void setSelectedDrug(boolean selectedDrug) {
        isSelectedDrug = selectedDrug;
    }

    public boolean isEquals() {
        return isEquals;
    }

    public void setEquals(boolean equals) {
        isEquals = equals;
    }

    public String getMedicineChest() {
        return MedicineChest;
    }

    public void setMedicineChest(String medicineChest) {
        MedicineChest = medicineChest;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }
}

package com.lzyc.ybtappcal.bean;

/**
 * Created by lxx on 2017/5/1.
 */

public class OrderCheckSubmitBean {
    
    private String imgUrl;
    private String pharmacy;
    private int drugNum;
    private String total;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }

    public int getDrugNum() {
        return drugNum;
    }

    public void setDrugNum(int drugNum) {
        this.drugNum = drugNum;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderCheckSubmitBean{" +
                "imgUrl='" + imgUrl + '\'' +
                ", pharmacy='" + pharmacy + '\'' +
                ", drugNum='" + drugNum + '\'' +
                ", total='" + total + '\'' +
                '}';
    }

}

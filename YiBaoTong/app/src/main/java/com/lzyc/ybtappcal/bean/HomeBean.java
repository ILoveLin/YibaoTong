package com.lzyc.ybtappcal.bean;

/**
 * Created by Lxx on 2017/3/23.
 */
public class HomeBean {
    private String imgUrl;
    private String drugPrice;
    private String returnPrice;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDrugPrice() {
        return drugPrice;
    }

    public void setDrugPrice(String drugPrice) {
        this.drugPrice = drugPrice;
    }

    public String getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(String returnPrice) {
        this.returnPrice = returnPrice;
    }

    @Override
    public String toString() {
        return "HomeBean{" +
                "imgUrl='" + imgUrl + '\'' +
                ", drugPrice='" + drugPrice + '\'' +
                ", returnPrice='" + returnPrice + '\'' +
                '}';
    }
}

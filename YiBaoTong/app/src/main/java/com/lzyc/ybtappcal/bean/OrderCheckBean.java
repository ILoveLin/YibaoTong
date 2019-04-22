package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lxx on 2017/4/26.
 */

public class OrderCheckBean {

    private String pharmacy;
    private int pharmacyImg;
    private String drugPrice;
    private String freightPrice;
    private String payType;
    private List<DrugInfo> drugs;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }

    public int getPharmacyImg() {
        return pharmacyImg;
    }

    public void setPharmacyImg(int pharmacyImg) {
        this.pharmacyImg = pharmacyImg;
    }

    public String getDrugPrice() {
        return drugPrice;
    }

    public void setDrugPrice(String drugPrice) {
        this.drugPrice = drugPrice;
    }

    public String getFreightPrice() {
        return freightPrice;
    }

    public void setFreightPrice(String freightPrice) {
        this.freightPrice = freightPrice;
    }

    public List<DrugInfo> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<DrugInfo> drugs) {
        this.drugs = drugs;
    }

    public static class DrugInfo{
        private String img;
        private String type;
        private String goodsName;
        private String drugName;
        private String miaoshu;
        private String newPrice;
        private String oldPrice;
        private String num;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getDrugName() {
            return drugName;
        }

        public void setDrugName(String drugName) {
            this.drugName = drugName;
        }

        public String getMiaoshu() {
            return miaoshu;
        }

        public void setMiaoshu(String miaoshu) {
            this.miaoshu = miaoshu;
        }

        public String getNewPrice() {
            return newPrice;
        }

        public void setNewPrice(String newPrice) {
            this.newPrice = newPrice;
        }

        public String getOldPrice() {
            return oldPrice;
        }

        public void setOldPrice(String oldPrice) {
            this.oldPrice = oldPrice;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        @Override
        public String toString() {
            return "DrugInfo{" +
                    "img='" + img + '\'' +
                    ", type='" + type + '\'' +
                    ", goodsName='" + goodsName + '\'' +
                    ", drugName='" + drugName + '\'' +
                    ", miaoshu='" + miaoshu + '\'' +
                    ", newPrice='" + newPrice + '\'' +
                    ", oldPrice='" + oldPrice + '\'' +
                    ", num='" + num + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OrderCheckBean{" +
                "pharmacy='" + pharmacy + '\'' +
                ", pharmacyImg=" + pharmacyImg +
                ", drugPrice='" + drugPrice + '\'' +
                ", freightPrice='" + freightPrice + '\'' +
                ", payType='" + payType + '\'' +
                ", drugs=" + drugs +
                '}';
    }
}

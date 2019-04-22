package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lxx on 2017/4/20.
 */

public class DrugInfo {

    /**
     * DrugInfo : {"drug_id":"9612","DrugNameID":"XJ01CAA040A006","SpecificationsID":"007","VenderID":"0351047","Name":"阿莫西林分散片","GoodsName":"","Specifications":"0.5g*24/盒","Vender":"山西同达药业有限公司","Images":"http://app.yibaotongapp.com//pic/16037.png","Price":"0.00"}
     * Zifei : 0
     * BaoxiaoType : 甲类
     * HospitalCount : 0
     * BeijingBaoxiaoDetail : [{"type":"医院","yPrice":"0.00","ratio":"0.7","bxPrice":"0.00"},{"type":"社区","yPrice":"0.00","ratio":"0.9","bxPrice":"0.00"}]
     * YidiBaoxiaoDetail :
     * Conditions : ["无"]
     */

    private DrugInfoBean DrugInfo;
    private int Zifei;
    private String BaoxiaoType;
    private int HospitalCount;
    private String YidiBaoxiaoDetail;
    private List<BeijingBaoxiaoDetailBean> BeijingBaoxiaoDetail;
    private List<String> Conditions;

    public DrugInfoBean getDrugInfo() {
        return DrugInfo;
    }

    public void setDrugInfo(DrugInfoBean DrugInfo) {
        this.DrugInfo = DrugInfo;
    }

    public int getZifei() {
        return Zifei;
    }

    public void setZifei(int Zifei) {
        this.Zifei = Zifei;
    }

    public String getBaoxiaoType() {
        return BaoxiaoType;
    }

    public void setBaoxiaoType(String BaoxiaoType) {
        this.BaoxiaoType = BaoxiaoType;
    }

    public int getHospitalCount() {
        return HospitalCount;
    }

    public void setHospitalCount(int HospitalCount) {
        this.HospitalCount = HospitalCount;
    }

    public String getYidiBaoxiaoDetail() {
        return YidiBaoxiaoDetail;
    }

    public void setYidiBaoxiaoDetail(String YidiBaoxiaoDetail) {
        this.YidiBaoxiaoDetail = YidiBaoxiaoDetail;
    }

    public List<BeijingBaoxiaoDetailBean> getBeijingBaoxiaoDetail() {
        return BeijingBaoxiaoDetail;
    }

    public void setBeijingBaoxiaoDetail(List<BeijingBaoxiaoDetailBean> BeijingBaoxiaoDetail) {
        this.BeijingBaoxiaoDetail = BeijingBaoxiaoDetail;
    }

    public List<String> getConditions() {
        return Conditions;
    }

    public void setConditions(List<String> Conditions) {
        this.Conditions = Conditions;
    }

    public static class DrugInfoBean {
        /**
         * drug_id : 9612
         * DrugNameID : XJ01CAA040A006
         * SpecificationsID : 007
         * VenderID : 0351047
         * Name : 阿莫西林分散片
         * GoodsName :
         * Specifications : 0.5g*24/盒
         * Vender : 山西同达药业有限公司
         * Images : http://app.yibaotongapp.com//pic/16037.png
         * Price : 0.00
         */

        private String drug_id;
        private String DrugNameID;
        private String SpecificationsID;
        private String VenderID;
        private String Name;
        private String GoodsName;
        private String Specifications;
        private String Vender;
        private String Images;
        private String Price;

        public String getDrug_id() {
            return drug_id;
        }

        public void setDrug_id(String drug_id) {
            this.drug_id = drug_id;
        }

        public String getDrugNameID() {
            return DrugNameID;
        }

        public void setDrugNameID(String DrugNameID) {
            this.DrugNameID = DrugNameID;
        }

        public String getSpecificationsID() {
            return SpecificationsID;
        }

        public void setSpecificationsID(String SpecificationsID) {
            this.SpecificationsID = SpecificationsID;
        }

        public String getVenderID() {
            return VenderID;
        }

        public void setVenderID(String VenderID) {
            this.VenderID = VenderID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getGoodsName() {
            return GoodsName;
        }

        public void setGoodsName(String GoodsName) {
            this.GoodsName = GoodsName;
        }

        public String getSpecifications() {
            return Specifications;
        }

        public void setSpecifications(String Specifications) {
            this.Specifications = Specifications;
        }

        public String getVender() {
            return Vender;
        }

        public void setVender(String Vender) {
            this.Vender = Vender;
        }

        public String getImages() {
            return Images;
        }

        public void setImages(String Images) {
            this.Images = Images;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String Price) {
            this.Price = Price;
        }
    }

    public static class BeijingBaoxiaoDetailBean {
        /**
         * type : 医院
         * yPrice : 0.00
         * ratio : 0.7
         * bxPrice : 0.00
         */

        private String type;
        private String yPrice;
        private String ratio;
        private String bxPrice;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getYPrice() {
            return yPrice;
        }

        public void setYPrice(String yPrice) {
            this.yPrice = yPrice;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }

        public String getBxPrice() {
            return bxPrice;
        }

        public void setBxPrice(String bxPrice) {
            this.bxPrice = bxPrice;
        }
    }
}

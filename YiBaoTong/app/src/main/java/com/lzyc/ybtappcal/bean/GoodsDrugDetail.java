package com.lzyc.ybtappcal.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lxx on 2017/3/17.
 */
public class GoodsDrugDetail {

    /**
     * Name : 甲钴胺片
     * DeKaiPrice : 28.00
     * Type : RX
     * Image : http://www.dkyao.com/images/201602/source_img/2915_G_1456106744842.jpg
     * Drug_ID : 530
     * Vender : 华北制药股份有限公司
     * ID : 10
     * RetailPrice : 33.60
     * Specifications : 0.5mg*30片
     * DKID : 2915
     * GoodsName : 怡神保 甲钴胺片 0.5mg*30片
     */

    private GoodsDrugInfoBean goodsDrugInfo;
    /**
     * Freight : 8
     * returnMoney : 3.16
     * Flag : 299
     */

    private OtherBean other;
    /**
     * instructions : ["测试用法用量","测试适应症","测试禁忌","测试不良反应"]
     * goodsDrugInfo : {"Name":"甲钴胺片","DeKaiPrice":"28.00","Type":"RX","Image":"http://www.dkyao.com/images/201602/source_img/2915_G_1456106744842.jpg","Drug_ID":"530","Vender":"华北制药股份有限公司","ID":"10","RetailPrice":"33.60","Specifications":"0.5mg*30片","DKID":"2915","GoodsName":"怡神保 甲钴胺片 0.5mg*30片"}
     * other : {"Freight":"8","returnMoney":"3.16","Flag":"299"}
     */

    private List<String> instructions;

    public GoodsDrugInfoBean getGoodsDrugInfo() {
        return goodsDrugInfo;
    }

    public void setGoodsDrugInfo(GoodsDrugInfoBean goodsDrugInfo) {
        this.goodsDrugInfo = goodsDrugInfo;
    }

    public OtherBean getOther() {
        return other;
    }

    public void setOther(OtherBean other) {
        this.other = other;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public static class GoodsDrugInfoBean implements Serializable{
        private String Name;
        private String DeKaiPrice;
        private String Type;
        private String Image;
        private String Drug_ID;
        private String Vender;
        private String ID;
        private String RetailPrice;
        private String Specifications;
        private String DKID;
        private String GoodsName;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getDeKaiPrice() {
            return DeKaiPrice;
        }

        public void setDeKaiPrice(String DeKaiPrice) {
            this.DeKaiPrice = DeKaiPrice;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }

        public String getDrug_ID() {
            return Drug_ID;
        }

        public void setDrug_ID(String Drug_ID) {
            this.Drug_ID = Drug_ID;
        }

        public String getVender() {
            return Vender;
        }

        public void setVender(String Vender) {
            this.Vender = Vender;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getRetailPrice() {
            return RetailPrice;
        }

        public void setRetailPrice(String RetailPrice) {
            this.RetailPrice = RetailPrice;
        }

        public String getSpecifications() {
            return Specifications;
        }

        public void setSpecifications(String Specifications) {
            this.Specifications = Specifications;
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

        public void setGoodsName(String GoodsName) {
            this.GoodsName = GoodsName;
        }
    }

    public static class OtherBean implements Serializable {
        private String Freight;
        private String returnMoney;
        private String Flag;

        public String getFreight() {
            return Freight;
        }

        public void setFreight(String Freight) {
            this.Freight = Freight;
        }

        public String getReturnMoney() {
            return returnMoney;
        }

        public void setReturnMoney(String returnMoney) {
            this.returnMoney = returnMoney;
        }

        public String getFlag() {
            return Flag;
        }

        public void setFlag(String Flag) {
            this.Flag = Flag;
        }
    }

    @Override
    public String toString() {
        return "GoodsDrugDetail{" +
                "goodsDrugInfo=" + goodsDrugInfo +
                ", other=" + other +
                ", instructions=" + instructions +
                '}';
    }
}

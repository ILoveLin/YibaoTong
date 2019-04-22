package com.lzyc.ybtappcal.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lxx on 2017/3/17.
 */
public class GoodsDrugDetail2 implements Serializable{

    /**
     * ID : 78
     * DKID : 78
     * Drug_ID : 0
     * Name : 复方丹参滴丸
     * GoodsName :
     * Specifications : 27mg*180丸*1瓶
     * Conversion : 1瓶
     * Unit : 盒
     * Vender : 天士力制药集团股份有限公司
     * Type : 2
     * RetailPrice : 34.80
     * DeKaiPrice : 29.00
     * Image : http://app.yibaotongapp.com//pic/1354_P_1456078954361.png
     */

    private GoodsDrugInfoBean goodsDrugInfo;
    /**
     * returnMoney : 6
     * Freight : 8
     * Flag : 299
     */

    private OtherBean other;
    /**
     * goodsDrugInfo : {"ID":"78","DKID":"78","Drug_ID":"0","Name":"复方丹参滴丸","GoodsName":"","Specifications":"27mg*180丸*1瓶","Conversion":"1瓶","Unit":"盒","Vender":"天士力制药集团股份有限公司","Type":"2","RetailPrice":"34.80","DeKaiPrice":"29.00","Image":"http://app.yibaotongapp.com//pic/1354_P_1456078954361.png"}
     * instructions : ["用于气滞血瘀所致的胸痹，症见胸闷、心前区刺痛；冠心病心绞痛见上述证候者。","口服或舌下含服，一次10丸，一日3次，4周为一个疗程；或遵医嘱。","偶见胃肠道不适。"]
     * other : {"returnMoney":"6","Freight":"8","Flag":"299"}
     */

    private List<String> instructions;

    private MerchantInfo merchantInfo;

    public MerchantInfo getMerchantInfo() {
        return merchantInfo;
    }

    public void setMerchantInfo(MerchantInfo merchantInfo) {
        this.merchantInfo = merchantInfo;
    }

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
        private String ID;
        private String DKID;
        private String Drug_ID;
        private String Name;
        private String GoodsName;
        private String Specifications;
        private String Conversion;
        private String Unit;
        private String Vender;
        private String Type;
        private String RetailPrice;
        private String DeKaiPrice;
        private String Image;
        private String Gyzz;
        private String OnlinePay;
        private String InstrUrl;

        public String getInstrUrl() {
            return InstrUrl;
        }

        public void setInstrUrl(String instrUrl) {
            InstrUrl = instrUrl;
        }

        public String getOnlinePay() {
            return OnlinePay;
        }

        public void setOnlinePay(String onlinePay) {
            OnlinePay = onlinePay;
        }

        public String getGyzz() {
            return Gyzz;
        }

        public void setGyzz(String gyzz) {
            Gyzz = gyzz;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getDKID() {
            return DKID;
        }

        public void setDKID(String DKID) {
            this.DKID = DKID;
        }

        public String getDrug_ID() {
            return Drug_ID;
        }

        public void setDrug_ID(String Drug_ID) {
            this.Drug_ID = Drug_ID;
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

        public String getConversion() {
            return Conversion;
        }

        public void setConversion(String Conversion) {
            this.Conversion = Conversion;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String Unit) {
            this.Unit = Unit;
        }

        public String getVender() {
            return Vender;
        }

        public void setVender(String Vender) {
            this.Vender = Vender;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public String getRetailPrice() {
            return RetailPrice;
        }

        public void setRetailPrice(String RetailPrice) {
            this.RetailPrice = RetailPrice;
        }

        public String getDeKaiPrice() {
            return DeKaiPrice;
        }

        public void setDeKaiPrice(String DeKaiPrice) {
            this.DeKaiPrice = DeKaiPrice;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }

        @Override
        public String toString() {
            return "GoodsDrugInfoBean{" +
                    "ID='" + ID + '\'' +
                    ", DKID='" + DKID + '\'' +
                    ", Drug_ID='" + Drug_ID + '\'' +
                    ", Name='" + Name + '\'' +
                    ", GoodsName='" + GoodsName + '\'' +
                    ", Specifications='" + Specifications + '\'' +
                    ", Conversion='" + Conversion + '\'' +
                    ", Unit='" + Unit + '\'' +
                    ", Vender='" + Vender + '\'' +
                    ", Type='" + Type + '\'' +
                    ", RetailPrice='" + RetailPrice + '\'' +
                    ", DeKaiPrice='" + DeKaiPrice + '\'' +
                    ", Image='" + Image + '\'' +
                    ", Gyzz='" + Gyzz + '\'' +
                    ", OnlinePay='" + OnlinePay + '\'' +
                    '}';
        }
    }

    public static class OtherBean implements Serializable{
        private String returnMoney;
        private String Freight;
        private String Flag;
        private String Msg;
        private String PromptMessage;
        private String PromptMessage2;

        public String getPromptMessage() {
            return PromptMessage;
        }

        public void setPromptMessage(String promptMessage) {
            PromptMessage = promptMessage;
        }

        public String getPromptMessage2() {
            return PromptMessage2;
        }

        public void setPromptMessage2(String promptMessage2) {
            PromptMessage2 = promptMessage2;
        }

        public String getReturnMoney() {
            return returnMoney;
        }

        public void setReturnMoney(String returnMoney) {
            this.returnMoney = returnMoney;
        }

        public String getFreight() {
            return Freight;
        }

        public void setFreight(String Freight) {
            this.Freight = Freight;
        }

        public String getFlag() {
            return Flag;
        }

        public void setFlag(String Flag) {
            this.Flag = Flag;
        }

        public String getMsg() {
            return Msg;
        }

        public void setMsg(String msg) {
            Msg = msg;
        }

        @Override
        public String toString() {
            return "OtherBean{" +
                    "returnMoney='" + returnMoney + '\'' +
                    ", Freight='" + Freight + '\'' +
                    ", Flag='" + Flag + '\'' +
                    ", Msg='" + Msg + '\'' +
                    ", PromptMessage='" + PromptMessage + '\'' +
                    ", PromptMessage2='" + PromptMessage2 + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GoodsDrugDetail2{" +
                "goodsDrugInfo=" + goodsDrugInfo +
                ", other=" + other +
                ", instructions=" + instructions +
                '}';
    }
}

package com.lzyc.ybtappcal.bean;

/**
 * Created by lxx on 2017/5/25.
 */

public class GoodsListBean {
    /**
     * ID : 14
     * DKID : 14
     * Drug_ID : 0
     * Name : 多潘立酮片
     * GoodsName : 吗丁啉
     * Specifications : 30片
     * Conversion :
     * Unit :
     * Vender : 西安杨森制药有限公司
     * Type : 1
     * RetailPrice : 26.88
     * DeKaiPrice : 14.26
     * Image : http://app.yibaotongapp.com//pic/2383_P_1469144876289.png
     * YFYL : 1、成人：治疗上消化道动力失调，成人常用口服剂量为每次1片(10mg)，每日3～4次，饭前15～30分钟或就寝时服用。对病情严重或已产生耐受性的患者，可增至每次2片(20mg)，每日3～4次或遵医嘱。对因服用抗帕金森氏症多巴胺受体激动剂而导致的恶心呕吐，成人可采用口服剂量每次2片(20mg)，每日3～4次。对于因静滴抗帕金森氏症多巴胺受体激动剂引起的恶心呕吐，治疗剂量还需增加。 2、2岁及以上儿童：按体重每次0.2～0.3mg/kg，每日3～4次，最大剂量每日不超过30mg。
     * BLFY : 临床试验表明本品的不良反应发生率小于7%。大多数不良反应在本品使用过程中消失或被患者耐受。一些较严重的不良反应，如溢乳、男子乳房女性化、女性月经不调均与使用剂量有关，减少剂量或停止用药即可消失。 中枢神经系统：不良反应发生率为4.6%，包括口干(1.9%)，头痛或偏头痛(1.2%),失眠、神经过敏、头晕、饥渴感、嗜睡、易努(发生率均小于1%)。成人极少有锥体外系反应。当血脑屏障未发育完全(如婴儿)或遭到损伤时，不能排除产生中枢不良反应的可能性。 消化系统：不良反应发生率为2.4%，包括腹部痉挛、腹泻、反流
     * SYZ : 1、由胃排空延缓、胃食管反流、食道炎引起的消化不良症状;上腹胀闷感、腹胀、腹疼痛;嗳气、胃肠胀气;恶心、呕吐;由于反流引起的口腔和胃烧灼感。 2、各种原因引起的恶心、呕吐，如功能性、器质性、感染性、饮食性、放射性治疗或化疗、用多巴胺受体激动剂(如左旋多巴、溴隐亭等)治疗帕金森氏症所引起的恶心、呕吐。
     * ReturnMoney : 3
     */

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
    private String YFYL;
    private String BLFY;
    private String SYZ;
    private String ReturnMoney;
    private int column;

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

    public String getYFYL() {
        return YFYL;
    }

    public void setYFYL(String YFYL) {
        this.YFYL = YFYL;
    }

    public String getBLFY() {
        return BLFY;
    }

    public void setBLFY(String BLFY) {
        this.BLFY = BLFY;
    }

    public String getSYZ() {
        return SYZ;
    }

    public void setSYZ(String SYZ) {
        this.SYZ = SYZ;
    }

    public String getReturnMoney() {
        return ReturnMoney;
    }

    public void setReturnMoney(String ReturnMoney) {
        this.ReturnMoney = ReturnMoney;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "GoodsListBean{" +
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
                ", YFYL='" + YFYL + '\'' +
                ", BLFY='" + BLFY + '\'' +
                ", SYZ='" + SYZ + '\'' +
                ", ReturnMoney='" + ReturnMoney + '\'' +
                '}';
    }
}

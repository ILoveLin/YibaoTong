package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lxx on 2017/5/25.
 */

public class AttributeBean {

    /**
     * ID : 2
     * Name : 促销
     * Icon : http://new.yibaotongapp.com//uploads/image/attributeicon/attr_icon_2.png
     * showMore : 1
     * GoodsList : [{"ID":"14","DKID":"14","Drug_ID":"0","Name":"多潘立酮片","GoodsName":"吗丁啉","Specifications":"30片","Conversion":"","Unit":"","Vender":"西安杨森制药有限公司","Type":"1","RetailPrice":"26.88","DeKaiPrice":"14.26","Image":"http://app.yibaotongapp.com//pic/2383_P_1469144876289.png","YFYL":"1、成人：治疗上消化道动力失调，成人常用口服剂量为每次1片(10mg)，每日3～4次，饭前15～30分钟或就寝时服用。对病情严重或已产生耐受性的患者，可增至每次2片(20mg)，每日3～4次或遵医嘱。对因服用抗帕金森氏症多巴胺受体激动剂而导致的恶心呕吐，成人可采用口服剂量每次2片(20mg)，每日3～4次。对于因静滴抗帕金森氏症多巴胺受体激动剂引起的恶心呕吐，治疗剂量还需增加。 2、2岁及以上儿童：按体重每次0.2～0.3mg/kg，每日3～4次，最大剂量每日不超过30mg。","BLFY":"临床试验表明本品的不良反应发生率小于7%。大多数不良反应在本品使用过程中消失或被患者耐受。一些较严重的不良反应，如溢乳、男子乳房女性化、女性月经不调均与使用剂量有关，减少剂量或停止用药即可消失。 中枢神经系统：不良反应发生率为4.6%，包括口干(1.9%)，头痛或偏头痛(1.2%),失眠、神经过敏、头晕、饥渴感、嗜睡、易努(发生率均小于1%)。成人极少有锥体外系反应。当血脑屏障未发育完全(如婴儿)或遭到损伤时，不能排除产生中枢不良反应的可能性。 消化系统：不良反应发生率为2.4%，包括腹部痉挛、腹泻、反流","SYZ":"1、由胃排空延缓、胃食管反流、食道炎引起的消化不良症状;上腹胀闷感、腹胀、腹疼痛;嗳气、胃肠胀气;恶心、呕吐;由于反流引起的口腔和胃烧灼感。 2、各种原因引起的恶心、呕吐，如功能性、器质性、感染性、饮食性、放射性治疗或化疗、用多巴胺受体激动剂(如左旋多巴、溴隐亭等)治疗帕金森氏症所引起的恶心、呕吐。","ReturnMoney":"3"},{"ID":"13","DKID":"13","Drug_ID":"0","Name":"枯草杆菌二联活菌颗粒","GoodsName":"妈咪爱","Specifications":"1g*10袋","Conversion":"10袋","Unit":"","Vender":"北京韩美药品有限公司","Type":"1","RetailPrice":"31.20","DeKaiPrice":"21.00","Image":"http://app.yibaotongapp.com//pic/1467_P_1454524884343.png","YFYL":"本品为儿童专用药品，2岁以下儿童，一次1袋，一日1-2次；2岁以上儿童，一次1-2袋，一日1-2次，用41℃以下温开水或牛奶冲服，也可直接服用","BLFY":"推荐剂量未见明显不良反应，罕见腹泻次数增加，停药后可恢复","SYZ":"适用于因肠道菌群失调引起的腹泻、便秘、胀气、消化不良等。","ReturnMoney":"5"},{"ID":"17","DKID":"17","Drug_ID":"0","Name":"复方青橄榄利咽含片","GoodsName":"慢严舒柠","Specifications":"0.5g*60片","Conversion":"60片","Unit":"","Vender":"桂龙药业（安徽）有限公司","Type":"1","RetailPrice":"27.60","DeKaiPrice":"23.00","Image":"http://app.yibaotongapp.com//pic/2175_P_1456253963271.png","YFYL":"含服，一次1～2片，每小时一次，一日10～20片。","BLFY":"尚不明确。","SYZ":"滋阴清热，利咽解毒。本品适用于咽部灼热，疼痛，咽干不适","ReturnMoney":"5"},{"ID":"19","DKID":"19","Drug_ID":"0","Name":"六味地黄丸(浓缩丸)","GoodsName":"","Specifications":"200丸*1瓶/盒","Conversion":"1瓶","Unit":"盒","Vender":"河南省宛西制药股份有限公司","Type":"1","RetailPrice":"21.59","DeKaiPrice":"17.00","Image":"http://app.yibaotongapp.com//pic/645_P_1456446833878.png","YFYL":"口服。一次8丸，一日3次。","BLFY":"尚不明确。","SYZ":"滋阴补肾。用于肾阴亏损，头晕耳鸣，腰膝酸软，骨蒸潮热，盗汗遗精。","ReturnMoney":"4"}]
     */

    private String ID;
    private String Name;
    private String Icon;
    private int showMore;
    private int column;
    private List<GoodsListBean> GoodsList;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public int getShowMore() {
        return showMore;
    }

    public void setShowMore(int showMore) {
        this.showMore = showMore;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public List<GoodsListBean> getGoodsList() {
        return GoodsList;
    }

    public void setGoodsList(List<GoodsListBean> GoodsList) {
        this.GoodsList = GoodsList;
    }

    @Override
    public String toString() {
        return "AttributeBean{" +
                "ID='" + ID + '\'' +
                ", Name='" + Name + '\'' +
                ", Icon='" + Icon + '\'' +
                ", showMore=" + showMore +
                ", GoodsList=" + GoodsList +
                '}';
    }
}
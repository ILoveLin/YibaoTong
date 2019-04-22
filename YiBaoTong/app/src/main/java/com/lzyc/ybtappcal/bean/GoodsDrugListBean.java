package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lxx on 2017/5/13.
 */

public class GoodsDrugListBean {


    /**
     * list : [{"ID":"67","DKID":"67","Drug_ID":"0","Name":"云南白药膏","GoodsName":"","Specifications":"6.5cm*10cm","Conversion":"10cm","Unit":"","Vender":"云南白药集团无锡药业有限公司","Type":"1","RetailPrice":"30.00","DeKaiPrice":"22.00","Image":"http://app.yibaotongapp.com//pic/1548_P_1455663883205.png","YFYL":"贴患处。","BLFY":"敏性体质患者可能有胶布过敏反应或药物接触性瘙痒反应","SYZ":"本品的功效是活血散瘀，消肿止痛，祛风除湿。用于跌打损伤，瘀血肿痛，风湿疼痛。","ReturnMoney":"5"},{"ID":"61","DKID":"61","Drug_ID":"0","Name":"关节止痛膏","GoodsName":"","Specifications":"10cm*7cm","Conversion":"7cm","Unit":"","Vender":"河南羚锐制药股份有限公司","Type":"1","RetailPrice":"6.60","DeKaiPrice":"5.50","Image":"http://app.yibaotongapp.com//pic/1365_P_1454377046021.png","YFYL":"贴患处","BLFY":"尚不明确","SYZ":"活血散瘀、温经镇痛，寒湿淤阻经络所致的风湿关节痛及关节扭伤。","ReturnMoney":"2"},{"ID":"7","DKID":"7","Drug_ID":"0","Name":"鸿茅药酒","GoodsName":"","Specifications":"250ml*3","Conversion":"3","Unit":"","Vender":"内蒙古鸿茅药业有限责任公司","Type":"1","RetailPrice":"518.40","DeKaiPrice":"432.00","Image":"http://app.yibaotongapp.com//pic/6273_P_1478823143593.png","YFYL":"口服，一次15毫升，一日2次。 1瓶约服用半个月","BLFY":"尚不明确","SYZ":"祛风除湿，补气通络，舒筋活血，健脾温肾。用于风寒湿痹，筋骨疼痛，脾胃虚寒，肾亏腰酸以及妇女气虚血亏。","ReturnMoney":"87"},{"ID":"60","DKID":"60","Drug_ID":"0","Name":"颈复康颗粒","GoodsName":"","Specifications":"5g*10袋","Conversion":"10袋","Unit":"","Vender":"河北承德颈复康药业集团有限公司","Type":"1","RetailPrice":"28.00","DeKaiPrice":"21.00","Image":"http://app.yibaotongapp.com//pic/1931_P_1455671344264.png","YFYL":"开水冲服，一次1～2袋，一日2次，饭后服用。","BLFY":"尚不明确。","SYZ":"活血通络、散风止痛。用于风湿淤阻所致的颈椎病，症见头晕、颈项僵硬、肩背酸痛、手臂麻木。","ReturnMoney":"5"},{"ID":"63","DKID":"63","Drug_ID":"0","Name":"奇正消痛贴膏","GoodsName":"","Specifications":"1.2g*5贴","Conversion":"5贴","Unit":"","Vender":"甘肃奇正藏药有限公司","Type":"1","RetailPrice":"82.80","DeKaiPrice":"62.50","Image":"http://app.yibaotongapp.com//pic/1839_P_1454525817611.png","YFYL":"外用。将小袋内稀释剂均匀涂在药垫表面，润湿后直接敷于患处或穴位。每贴敷24小时。","BLFY":"尚不明确。","SYZ":"活血化瘀，消肿止痛。用于急慢性扭挫伤、跌打瘀痛、骨质增生、风湿及类风湿疼痛。亦适用于落枕、肩周炎、腰肌劳损和陈旧性伤痛等。","ReturnMoney":"15"},{"ID":"40","DKID":"40","Drug_ID":"0","Name":"万通筋骨贴","GoodsName":"","Specifications":"","Conversion":"","Unit":"","Vender":"通化万通药业股份有限公司","Type":"1","RetailPrice":"47.76","DeKaiPrice":"39.80","Image":"http://app.yibaotongapp.com//pic/5735_P_1468352936633.png","YFYL":"将皮肤清洗后，揭去下方离型纸，将膏面贴于患处的皮肤上，每24小时更换一次。7天为一疗程。","BLFY":"详见说明","SYZ":"适用于肩周炎、颈椎病、腰椎间盘突出、类风湿关节炎、膝关节炎引起的颈、肩、腰、髓及关节、肌肉疼痛的人群。","ReturnMoney":"8"},{"ID":"46","DKID":"46","Drug_ID":"0","Name":"壮骨麝香止痛膏","GoodsName":"","Specifications":"10贴","Conversion":"","Unit":"","Vender":"河南羚锐制药股份有限公司","Type":"1","RetailPrice":"21.60","DeKaiPrice":"6.00","Image":"http://app.yibaotongapp.com//pic/1816_P_1454523774587.png","YFYL":"外用，贴于患处","BLFY":"少数患者出现皮疹、皮肤瘙痒、用药局部水肿、皮肤溃烂。","SYZ":"祛风湿，活血止痛。用于风湿关节、肌肉痛，扭伤。","ReturnMoney":"2"}]
     * count : 7
     */

    private int count;
    private List<GoodsListBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<GoodsListBean> getList() {
        return list;
    }

    public void setList(List<GoodsListBean> list) {
        this.list = list;
    }


}

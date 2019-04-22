package com.lzyc.ybtappcal.bean;

/**
 * Created by yang on 2017/05/04.
 */
public class MerchantInfo extends BaseBean{
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getFreightMsg() {
        return FreightMsg;
    }

    public void setFreightMsg(String freightMsg) {
        FreightMsg = freightMsg;
    }

    public String getFreight() {
        return Freight;
    }

    public void setFreight(String freight) {
        Freight = freight;
    }

    public String getFreeShipp() {
        return FreeShipp;
    }

    public void setFreeShipp(String freeShipp) {
        FreeShipp = freeShipp;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    /**
     * ID : 1
     * Name : 德开大药房
     * Logo : http://new.yibaotongapp.com//uploads/logo/logo_dekai.png
     * Detail : 德开大药房是北京康复之家集团全资子公司，专注药品及医疗器械商品销售，2015年销售额超过4.2亿人民币，2016年销售额超过5.5亿人民币，是2015年与2016年中国医药电商十强企业。集团是中国家用医疗器械第一零售商，在全国30余个城市拥有超过150家药品及医疗器械销售门店。累计会员好评超过50万条，累计会员数量超过500万人。德开大药房是您身边的健康管理专家。
     * FreightMsg : 医保通全部药品包邮
     * Freight : 10.00
     * FreeShipp : 300.00
     * Phone : 18928798081
     */
    private String ID;
    private String Name;
    private String Logo;
    private String Detail;
    private String FreightMsg;
    private String Freight;
    private String FreeShipp;
    private String Phone;


}

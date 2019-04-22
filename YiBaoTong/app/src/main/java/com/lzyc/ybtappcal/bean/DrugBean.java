package com.lzyc.ybtappcal.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2017/3/10.
 * 药品
 */
public class DrugBean extends BaseBean {


    private static final long serialVersionUID = 43L;
    private String id;
    private String HostopShortName;
    private String AmountMonryForPay;
    private String number10;
    private String number20;
    private String number50;
    private String city;
    private String Yyid;
    private String HosNum;
    private String ComNum;
    /*新接口，新加字段 start*/
//    private int type;
    private int recDrug;
    private int rekedu;//TODO
    private int Adopt;
    /*新接口，新加字段 end*/
    private String DrugType;   //如果等于 "内部制剂"    就显示内部制剂的标记
    private List<String> hospitalImages;
    private String drugImage;
    private String venderCountry;
    private String Sell;
    private String drug_id;
    private String renkedu;
    private boolean isSeleted;
    private String MedicineChest;
    /**
     * Type : 0
     * Image : http://app.yibaotongapp.com//pic/31314.png
     * DrugID : 14376
     */

    private int Type;
    private String Image;
    private String DrugID;


    public String getDrugImage() {
        return drugImage;
    }

    public void setDrugImage(String drugImage) {
        this.drugImage = drugImage;
    }

    public String getSell() {
        return Sell;
    }

    public void setSell(String sell) {
        Sell = sell;
    }

    public void setRenkedu(String renkedu) {
        this.renkedu = renkedu;
    }

    public String getDrugType() {
        return DrugType;
    }

    public void setDrugType(String drugType) {
        DrugType = drugType;
    }

    public List<String> getHospitalImages() {
        return hospitalImages;
    }

    public void setHospitalImages(List<String> hospitalImages) {
        this.hospitalImages = hospitalImages;
    }

    public int getReim() {
        return Reim;
    }

    public void setReim(int reim) {
        Reim = reim;
    }

    public String getZifei() {
        return zifei;
    }

    public void setZifei(String zifei) {
        this.zifei = zifei;
    }

    public String getShequ() {
        return shequ;
    }

    public void setShequ(String shequ) {
        this.shequ = shequ;
    }

    public String getYiyuan() {
        return yiyuan;
    }

    public void setYiyuan(String yiyuan) {
        this.yiyuan = yiyuan;
    }

    private String lng;
    private String lat;
    private int Reim;        //1为报销0为不报销
    private String zifei;    //自费价格      不报销的药,社区或者医院价格更自费的价格是一样的
    private String shequ;    //社区价格   none说明社区没有此药
    private String yiyuan;   //医院价格
    //名字
    private String Name;
    private String rn;
    //药品id
    private String DrugNameID;
    private String SpecificationsID;
    private String Vender;
    private String VenderID;
    private String ScanCode6;
    private String code;
    private String Images;
    private String ScanName;
    private String Specifications;
    private String ScanCode;
    private String GoodsName;
    private String Price;
    private String rnName;
    private String rnGoodsName;
    private String zy;
    private String yType;
    private ArrayList<String> conditions;
    private String Other;
    private String baoxiao;
    private String Unit;
    private String Conversion;
    private String Date;
    private String shareUrl;
    private String newPrice;
    private String Juli;
    private String Level;
    private String Jibie;
    private String average;
    private String commentCount;

    private PercentageBean ZZ00;
    private PercentageBean ZZ01;
    private PercentageBean ZZ02;
    private PercentageBean ZZ03;
    private PercentageBean ZZ04;

    public DrugBean() {
    }

    public DrugBean(String id, String hostopShortName, String amountMonryForPay, String number10, String number20, String number50, String city, String yyid, String lng, String lat, String name, String rn, String drugNameID, String specificationsID, String vender, String venderID, String scanCode6, String code, String images, String specifications, String scanCode, String goodsName, String price, String rnName, String rnGoodsName, String zy, String yType, ArrayList<String> conditions, String other, String baoxiao, String unit, String conversion, String date, String shareUrl, PercentageBean ZZ00, PercentageBean ZZ01, PercentageBean ZZ02, PercentageBean ZZ03, PercentageBean ZZ04) {
        this.id = id;
        HostopShortName = hostopShortName;
        AmountMonryForPay = amountMonryForPay;
        this.number10 = number10;
        this.number20 = number20;
        this.number50 = number50;
        this.city = city;
        Yyid = yyid;
        this.lng = lng;
        this.lat = lat;
        Name = name;
        this.rn = rn;
        DrugNameID = drugNameID;
        SpecificationsID = specificationsID;
        Vender = vender;
        VenderID = venderID;
        ScanCode6 = scanCode6;
        this.code = code;
        Images = images;
        Specifications = specifications;
        ScanCode = scanCode;
        GoodsName = goodsName;
        Price = price;
        this.rnName = rnName;
        this.rnGoodsName = rnGoodsName;
        this.zy = zy;
        this.yType = yType;
        this.conditions = conditions;
        Other = other;
        this.baoxiao = baoxiao;
        Unit = unit;
        Conversion = conversion;
        Date = date;
        this.shareUrl = shareUrl;
        this.ZZ00 = ZZ00;
        this.ZZ01 = ZZ01;
        this.ZZ02 = ZZ02;
        this.ZZ03 = ZZ03;
        this.ZZ04 = ZZ04;
    }

    public String getJuli() {
        return Juli;
    }

    public void setJuli(String juli) {
        Juli = juli;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getJibie() {
        return Jibie;
    }

    public void setJibie(String jibie) {
        Jibie = jibie;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostopShortName() {
        return HostopShortName;
    }

    public void setHostopShortName(String hostopShortName) {
        HostopShortName = hostopShortName;
    }

    public String getAmountMonryForPay() {
        return AmountMonryForPay;
    }

    public void setAmountMonryForPay(String amountMonryForPay) {
        AmountMonryForPay = amountMonryForPay;
    }

    public String getNumber10() {
        return number10;
    }

    public void setNumber10(String number10) {
        this.number10 = number10;
    }

    public String getNumber20() {
        return number20;
    }

    public void setNumber20(String number20) {
        this.number20 = number20;
    }

    public String getNumber50() {
        return number50;
    }

    public void setNumber50(String number50) {
        this.number50 = number50;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getYyid() {
        return Yyid;
    }

    public void setYyid(String yyid) {
        Yyid = yyid;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public String getDrugNameID() {
        return DrugNameID;
    }

    public void setDrugNameID(String drugNameID) {
        DrugNameID = drugNameID;
    }

    public String getSpecificationsID() {
        return SpecificationsID;
    }

    public void setSpecificationsID(String specificationsID) {
        SpecificationsID = specificationsID;
    }

    public String getVender() {
        return Vender;
    }

    public void setVender(String vender) {
        Vender = vender;
    }

    public String getVenderID() {
        return VenderID;
    }

    public void setVenderID(String venderID) {
        VenderID = venderID;
    }

    public String getScanCode6() {
        return ScanCode6;
    }

    public void setScanCode6(String scanCode6) {
        ScanCode6 = scanCode6;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }

    public String getScanName() {
        return ScanName;
    }

    public void setScanName(String scanName) {
        ScanName = scanName;
    }

    public String getSpecifications() {
        return Specifications;
    }

    public void setSpecifications(String specifications) {
        Specifications = specifications;
    }

    public String getScanCode() {
        return ScanCode;
    }

    public void setScanCode(String scanCode) {
        ScanCode = scanCode;
    }

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getRnName() {
        return rnName;
    }

    public void setRnName(String rnName) {
        this.rnName = rnName;
    }

    public String getRnGoodsName() {
        return rnGoodsName;
    }

    public void setRnGoodsName(String rnGoodsName) {
        this.rnGoodsName = rnGoodsName;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    public String getyType() {
        return yType;
    }

    public void setyType(String yType) {
        this.yType = yType;
    }

    public ArrayList<String> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<String> conditions) {
        this.conditions = conditions;
    }

    public String getOther() {
        return Other;
    }

    public void setOther(String other) {
        Other = other;
    }

    public String getBaoxiao() {
        return baoxiao;
    }

    public void setBaoxiao(String baoxiao) {
        this.baoxiao = baoxiao;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getConversion() {
        return Conversion;
    }

    public void setConversion(String conversion) {
        Conversion = conversion;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public PercentageBean getZZ00() {
        return ZZ00;
    }

    public void setZZ00(PercentageBean ZZ00) {
        this.ZZ00 = ZZ00;
    }

    public PercentageBean getZZ01() {
        return ZZ01;
    }

    public void setZZ01(PercentageBean ZZ01) {
        this.ZZ01 = ZZ01;
    }

    public PercentageBean getZZ02() {
        return ZZ02;
    }

    public void setZZ02(PercentageBean ZZ02) {
        this.ZZ02 = ZZ02;
    }

    public PercentageBean getZZ03() {
        return ZZ03;
    }

    public void setZZ03(PercentageBean ZZ03) {
        this.ZZ03 = ZZ03;
    }

    public PercentageBean getZZ04() {
        return ZZ04;
    }

    public void setZZ04(PercentageBean ZZ04) {
        this.ZZ04 = ZZ04;
    }

    public String getHosNum() {
        return HosNum;
    }

    public void setHosNum(String hosNum) {
        HosNum = hosNum;
    }

    public String getComNum() {
        return ComNum;
    }

    public void setComNum(String comNum) {
        ComNum = comNum;
    }

    public int getRecDrug() {
        return recDrug;
    }

    public void setRecDrug(int recDrug) {
        this.recDrug = recDrug;
    }

    public int getRekedu() {
        return rekedu;
    }

    public void setRekedu(int rekedu) {
        this.rekedu = rekedu;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getRenkedu() {
        return renkedu;
    }

    public String getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(String drug_id) {
        this.drug_id = drug_id;
    }

    public int getAdopt() {
        return Adopt;
    }

    public void setAdopt(int adopt) {
        Adopt = adopt;
    }

    public String getVenderCountry() {
        return venderCountry;
    }

    public void setVenderCountry(String venderCountry) {
        this.venderCountry = venderCountry;
    }

    public boolean isSeleted() {
        return isSeleted;
    }

    public void setSeleted(boolean seleted) {
        isSeleted = seleted;
    }

    public String getMedicineChest() {
        return MedicineChest;
    }

    public void setMedicineChest(String medicineChest) {
        MedicineChest = medicineChest;
    }

    @Override
    public String toString() {
        return "DrugBean{" +
                "id='" + id + '\'' +
                ", HostopShortName='" + HostopShortName + '\'' +
                ", AmountMonryForPay='" + AmountMonryForPay + '\'' +
                ", number10='" + number10 + '\'' +
                ", number20='" + number20 + '\'' +
                ", number50='" + number50 + '\'' +
                ", city='" + city + '\'' +
                ", Yyid='" + Yyid + '\'' +
                ", HosNum='" + HosNum + '\'' +
                ", ComNum='" + ComNum + '\'' +
                ", recDrug=" + recDrug +
                ", rekedu=" + rekedu +
                ", Adopt=" + Adopt +
                ", DrugType='" + DrugType + '\'' +
                ", hospitalImages=" + hospitalImages +
                ", drugImage='" + drugImage + '\'' +
                ", venderCountry='" + venderCountry + '\'' +
                ", Sell='" + Sell + '\'' +
                ", drug_id='" + drug_id + '\'' +
                ", renkedu='" + renkedu + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", Reim=" + Reim +
                ", zifei='" + zifei + '\'' +
                ", shequ='" + shequ + '\'' +
                ", yiyuan='" + yiyuan + '\'' +
                ", Name='" + Name + '\'' +
                ", rn='" + rn + '\'' +
                ", DrugNameID='" + DrugNameID + '\'' +
                ", SpecificationsID='" + SpecificationsID + '\'' +
                ", Vender='" + Vender + '\'' +
                ", VenderID='" + VenderID + '\'' +
                ", ScanCode6='" + ScanCode6 + '\'' +
                ", code='" + code + '\'' +
                ", Images='" + Images + '\'' +
                ", ScanName='" + ScanName + '\'' +
                ", Specifications='" + Specifications + '\'' +
                ", ScanCode='" + ScanCode + '\'' +
                ", GoodsName='" + GoodsName + '\'' +
                ", Price='" + Price + '\'' +
                ", rnName='" + rnName + '\'' +
                ", rnGoodsName='" + rnGoodsName + '\'' +
                ", zy='" + zy + '\'' +
                ", yType='" + yType + '\'' +
                ", conditions=" + conditions +
                ", Other='" + Other + '\'' +
                ", baoxiao='" + baoxiao + '\'' +
                ", Unit='" + Unit + '\'' +
                ", Conversion='" + Conversion + '\'' +
                ", Date='" + Date + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", newPrice='" + newPrice + '\'' +
                ", Juli='" + Juli + '\'' +
                ", Level='" + Level + '\'' +
                ", Jibie='" + Jibie + '\'' +
                ", average='" + average + '\'' +
                ", commentCount='" + commentCount + '\'' +
                ", ZZ00=" + ZZ00 +
                ", ZZ01=" + ZZ01 +
                ", ZZ02=" + ZZ02 +
                ", ZZ03=" + ZZ03 +
                ", ZZ04=" + ZZ04 +
                '}';
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getDrugID() {
        return DrugID;
    }

    public void setDrugID(String DrugID) {
        this.DrugID = DrugID;
    }
}

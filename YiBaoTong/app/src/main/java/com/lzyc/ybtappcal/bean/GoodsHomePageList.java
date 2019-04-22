package com.lzyc.ybtappcal.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lxx on 2017/3/24.
 */
public class GoodsHomePageList implements Serializable{

    /**
     * goodsList : [{"ID":"1","DKID":"2445","Drug_ID":"411","Name":"加味保和丸","GoodsName":"同仁堂 加味保和丸 6g*12袋","Specifications":"6g*12袋/盒","Vender":"北京同仁堂制药有限公司","Type":"OTC","RetailPrice":"13.20","DeKaiPrice":"0.01","Image":"http://www.dkyao.com/images/201602/source_img/2445_G_1456168775527.jpg","ReturnMoney":"0"},{"ID":"2","DKID":"1162","Drug_ID":"442","Name":"加味逍遥丸","GoodsName":"同仁堂 加味逍遥丸 10*1盒","Specifications":"6g*10袋","Vender":"北京同仁堂科技发展股份有限公司制药厂","Type":"OTC","RetailPrice":"15.60","DeKaiPrice":"13.00","Image":"http://www.dkyao.com/images/201602/source_img/1162_G_1454536517365.jpg","ReturnMoney":"0"},{"ID":"3","DKID":"1167","Drug_ID":"446","Name":"加味逍遥丸","GoodsName":"千金 加味逍遥丸（单盒）","Specifications":"6g*10袋","Vender":"株洲千金药业股份有限公司","Type":"OTC","RetailPrice":"14.40","DeKaiPrice":"12.00","Image":"http://www.dkyao.com/images/201602/source_img/1167_G_1454621251709.jpg","ReturnMoney":"0"},{"ID":"4","DKID":"5393","Drug_ID":"458","Name":"甲氨蝶呤片","GoodsName":"茂祥 甲氨蝶呤片 2.5mg*16片","Specifications":"2.5mg*16片","Vender":"通化茂祥制药有限公司","Type":"RX","RetailPrice":"54.00","DeKaiPrice":"0.01","Image":"http://www.dkyao.com/images/201606/source_img/5393_G_1466719491527.jpg","ReturnMoney":"0"},{"ID":"5","DKID":"1020","Drug_ID":"514","Name":"甲钴胺片","GoodsName":"奇信 甲钴胺胶囊 0.5mg*50粒/盒","Specifications":"0.5mg*50片/盒","Vender":"扬子江药业集团南京海陵药业有限公司","Type":"RX","RetailPrice":"59.00","DeKaiPrice":"49.90","Image":"http://www.dkyao.com/images/201602/source_img/1020_G_1456250903516.jpg","ReturnMoney":"0"},{"ID":"6","DKID":"3032","Drug_ID":"522","Name":"甲钴胺片","GoodsName":"兆敏欣 甲钴胺片 0.5mg*24片","Specifications":"0.5mg*24片/盒","Vender":"江西青峰药业有限公司","Type":"RX","RetailPrice":"37.68","DeKaiPrice":"31.40","Image":"http://www.dkyao.com/images/201602/source_img/3032_G_1456106788445.jpg","ReturnMoney":"0"},{"ID":"7","DKID":"1404","Drug_ID":"526","Name":"甲钴胺片","GoodsName":"奇信 甲钴胺片 0.5mg*48片/盒","Specifications":"0.5mg*48片/盒","Vender":"扬子江药业集团南京海陵药业有限公司","Type":"RX","RetailPrice":"74.00","DeKaiPrice":"62.00","Image":"http://www.dkyao.com/images/201602/source_img/1404_G_1456251144439.jpg","ReturnMoney":"0"},{"ID":"8","DKID":"2846","Drug_ID":"528","Name":"甲钴胺片","GoodsName":"迪赛诺 甲钴胺片 0.5mg*20片","Specifications":"0.5mg*20片","Vender":"江苏迪赛诺制药有限公司","Type":"RX","RetailPrice":"15.83","DeKaiPrice":"13.20","Image":"http://www.dkyao.com/images/201602/source_img/2846_G_1456209695149.jpg","ReturnMoney":"0"},{"ID":"9","DKID":"3291","Drug_ID":"529","Name":"甲钴胺片","GoodsName":"星佳定 甲钴胺片 0.5mg*20片","Specifications":"0.5mg*20片","Vender":"北京星昊医药股份有限公司","Type":"RX","RetailPrice":"11.88","DeKaiPrice":"9.90","Image":"http://www.dkyao.com/images/201602/source_img/3291_G_1456269231955.jpg","ReturnMoney":"0"},{"ID":"10","DKID":"2915","Drug_ID":"530","Name":"甲钴胺片","GoodsName":"怡神保 甲钴胺片 0.5mg*30片","Specifications":"0.5mg*30片","Vender":"华北制药股份有限公司","Type":"RX","RetailPrice":"33.60","DeKaiPrice":"28.00","Image":"http://www.dkyao.com/images/201602/source_img/2915_G_1456106744842.jpg","ReturnMoney":"0"}]
     * goodsCount : 859
     */

    private int goodsCount;
    /**
     * ID : 1
     * DKID : 2445
     * Drug_ID : 411
     * Name : 加味保和丸
     * GoodsName : 同仁堂 加味保和丸 6g*12袋
     * Specifications : 6g*12袋/盒
     * Vender : 北京同仁堂制药有限公司
     * Type : OTC
     * RetailPrice : 13.20
     * DeKaiPrice : 0.01
     * Image : http://www.dkyao.com/images/201602/source_img/2445_G_1456168775527.jpg
     * ReturnMoney : 0
     */

    private List<GoodsListBean> goodsList;
    private List<ImagesBean> images;

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public List<GoodsListBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsListBean> goodsList) {
        this.goodsList = goodsList;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public static class GoodsListBean implements Serializable{
        private String ID;
        private String DKID;
        private String Drug_ID;
        private String Name;
        private String GoodsName;
        private String Specifications;
        private String Vender;
        private String Type;
        private String RetailPrice;
        private String DeKaiPrice;
        private String Image;
        private String ReturnMoney;

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

        public String getReturnMoney() {
            return ReturnMoney;
        }

        public void setReturnMoney(String ReturnMoney) {
            this.ReturnMoney = ReturnMoney;
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
                    ", Vender='" + Vender + '\'' +
                    ", Type='" + Type + '\'' +
                    ", RetailPrice='" + RetailPrice + '\'' +
                    ", DeKaiPrice='" + DeKaiPrice + '\'' +
                    ", Image='" + Image + '\'' +
                    ", ReturnMoney='" + ReturnMoney + '\'' +
                    '}';
        }

    }


    public static class ImagesBean {
        /**
         * web_url : http://www.baidu.com
         * img_url : http://new.yibaotongapp.com//uploads/page_style/image_banner@2x.png
         */

        private String web_url;
        private String img_url;

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
    }
}

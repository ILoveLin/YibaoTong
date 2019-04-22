package com.lzyc.ybtappcal.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by abc on 2017/6/21.
 */

public class BYDrugDetailBean implements Serializable {


    /**
     * status : 0
     * msg :
     * data : {"Detail":{"BaoxiaoDetail":[{"Type":"医院","Price":"27.28","Ratio":"0.63","BxPrice":"10.09"},{"Type":"社区","Price":"27.28","Ratio":"0.81","BxPrice":"5.18"}],"BaoxiaoType":"报销","Conditions":["无"],"DrugID":"31205","GoodsName":"","Image":"http://app.yibaotongapp.com//pic/12395.png","Name":"感冒疏风胶囊","Price":"27.28","Specification":"","Specifications":"0.3g*36粒/盒","Vender":"吉林龙鑫药业有限公司","VenderDetailUrl":"http://new.yibaotongapp.com/","YyType":"乙类"}}
     */

    private int status;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * Detail : {"BaoxiaoDetail":[{"Type":"医院","Price":"27.28","Ratio":"0.63","BxPrice":"10.09"},{"Type":"社区","Price":"27.28","Ratio":"0.81","BxPrice":"5.18"}],"BaoxiaoType":"报销","Conditions":["无"],"DrugID":"31205","GoodsName":"","Image":"http://app.yibaotongapp.com//pic/12395.png","Name":"感冒疏风胶囊","Price":"27.28","Specification":"","Specifications":"0.3g*36粒/盒","Vender":"吉林龙鑫药业有限公司","VenderDetailUrl":"http://new.yibaotongapp.com/","YyType":"乙类"}
         */

        private DetailBean Detail;

        public DetailBean getDetail() {
            return Detail;
        }

        public void setDetail(DetailBean Detail) {
            this.Detail = Detail;
        }

        public static class DetailBean implements Serializable {
            /**
             * BaoxiaoDetail : [{"Type":"医院","Price":"27.28","Ratio":"0.63","BxPrice":"10.09"},{"Type":"社区","Price":"27.28","Ratio":"0.81","BxPrice":"5.18"}]
             * BaoxiaoType : 报销
             * Conditions : ["无"]
             * DrugID : 31205
             * GoodsName :
             * Image : http://app.yibaotongapp.com//pic/12395.png
             * Name : 感冒疏风胶囊
             * Price : 27.28
             * Specification :
             * Specifications : 0.3g*36粒/盒
             * Vender : 吉林龙鑫药业有限公司
             * VenderDetailUrl : http://new.yibaotongapp.com/
             * YyType : 乙类
             */

            private String BaoxiaoType;
            private String DrugID;
            private String GoodsName;
            private String Image;
            private String Name;
            private String Price;
            private String Specification;
            private String Specifications;
            private String Vender;
            private String VenderDetailUrl;
            private String VenderID;

            public String getVenderID() {
                return VenderID;
            }

            public void setVenderID(String venderID) {
                VenderID = venderID;
            }

            private String YyType;
            private List<BaoxiaoDetailBean> BaoxiaoDetail;
            private List<String> Conditions;

            public String getBaoxiaoType() {
                return BaoxiaoType;
            }

            public void setBaoxiaoType(String BaoxiaoType) {
                this.BaoxiaoType = BaoxiaoType;
            }

            public String getDrugID() {
                return DrugID;
            }

            public void setDrugID(String DrugID) {
                this.DrugID = DrugID;
            }

            public String getGoodsName() {
                return GoodsName;
            }

            public void setGoodsName(String GoodsName) {
                this.GoodsName = GoodsName;
            }

            public String getImage() {
                return Image;
            }

            public void setImage(String Image) {
                this.Image = Image;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public String getPrice() {
                return Price;
            }

            public void setPrice(String Price) {
                this.Price = Price;
            }

            public String getSpecification() {
                return Specification;
            }

            public void setSpecification(String Specification) {
                this.Specification = Specification;
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

            public String getVenderDetailUrl() {
                return VenderDetailUrl;
            }

            public void setVenderDetailUrl(String VenderDetailUrl) {
                this.VenderDetailUrl = VenderDetailUrl;
            }

            public String getYyType() {
                return YyType;
            }

            public void setYyType(String YyType) {
                this.YyType = YyType;
            }

            public List<BaoxiaoDetailBean> getBaoxiaoDetail() {
                return BaoxiaoDetail;
            }

            public void setBaoxiaoDetail(List<BaoxiaoDetailBean> BaoxiaoDetail) {
                this.BaoxiaoDetail = BaoxiaoDetail;
            }

            public List<String> getConditions() {
                return Conditions;
            }

            public void setConditions(List<String> Conditions) {
                this.Conditions = Conditions;
            }

            public static class BaoxiaoDetailBean implements Serializable {
                /**
                 * Type : 医院
                 * Price : 27.28
                 * Ratio : 0.63
                 * BxPrice : 10.09
                 */

                private String Type;
                private String Price;
                private String Ratio;
                private String BxPrice;

                public String getType() {
                    return Type;
                }

                public void setType(String Type) {
                    this.Type = Type;
                }

                public String getPrice() {
                    return Price;
                }

                public void setPrice(String Price) {
                    this.Price = Price;
                }

                public String getRatio() {
                    return Ratio;
                }

                public void setRatio(String Ratio) {
                    this.Ratio = Ratio;
                }

                public String getBxPrice() {
                    return BxPrice;
                }

                public void setBxPrice(String BxPrice) {
                    this.BxPrice = BxPrice;
                }
            }
        }
    }
}

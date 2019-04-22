package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by yang on 2017/03/24.
 */

public class ResultsDrugInfo extends BaseBean2{


    /**
     * data : {"areaAndStreet":[],"drugDetail":{"venderCountry":"Test vender country","SpecificationsID":"040","Adopt":1,"renkedu":1,"Specifications":"10mg*100/瓶","GoodsName":"","Name":"维生素B4片","Indication":"用于防治各种原因引起的白细胞减少症，急性粒细胞减少症，尤其是对肿瘤化学和放射治疗以及苯中毒等引起的白细胞减少症。","drugImage":"https://ybt.yibaotongapp.com//pic/31314.png","drugUsage":"口服。<|>成人，每次10～20mg（1-2片），一日3次。小儿，每次5～10mg（0.5-1片），每日2次。","DrugNameID":"XL03AXW040A001","Sell":"0","Price":"6.32","Vender":"天津力生制药股份有限公司","drug_id":"14376","VenderID":"0022019"},"other":{"Message":"测试提示语文本(弹窗外)","HospitalCount":0,"hosList":["都是大医院用啊，很多家医院用啊"]}}
     */

    private DataBean data;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * areaAndStreet : []
         * drugDetail : {"venderCountry":"Test vender country","SpecificationsID":"040","Adopt":1,"renkedu":1,"Specifications":"10mg*100/瓶","GoodsName":"","Name":"维生素B4片","Indication":"用于防治各种原因引起的白细胞减少症，急性粒细胞减少症，尤其是对肿瘤化学和放射治疗以及苯中毒等引起的白细胞减少症。","drugImage":"https://ybt.yibaotongapp.com//pic/31314.png","drugUsage":"口服。<|>成人，每次10～20mg（1-2片），一日3次。小儿，每次5～10mg（0.5-1片），每日2次。","DrugNameID":"XL03AXW040A001","Sell":"0","Price":"6.32","Vender":"天津力生制药股份有限公司","drug_id":"14376","VenderID":"0022019"}
         * other : {"Message":"测试提示语文本(弹窗外)","HospitalCount":0,"hosList":["都是大医院用啊，很多家医院用啊"]}
         */

        private DrugDetailBean drugDetail;
        private OtherBean other;
        private List<?> areaAndStreet;

        public DrugDetailBean getDrugDetail() {
            return drugDetail;
        }

        public void setDrugDetail(DrugDetailBean drugDetail) {
            this.drugDetail = drugDetail;
        }

        public OtherBean getOther() {
            return other;
        }

        public void setOther(OtherBean other) {
            this.other = other;
        }

        public List<?> getAreaAndStreet() {
            return areaAndStreet;
        }

        public void setAreaAndStreet(List<?> areaAndStreet) {
            this.areaAndStreet = areaAndStreet;
        }


        public static class OtherBean {
            /**
             * Message : 测试提示语文本(弹窗外)
             * HospitalCount : 0
             * hosList : ["都是大医院用啊，很多家医院用啊"]
             */

            private String Message;
            private int HospitalCount;
            private List<String> hosList;

            public String getMessage() {
                return Message;
            }

            public void setMessage(String Message) {
                this.Message = Message;
            }

            public int getHospitalCount() {
                return HospitalCount;
            }

            public void setHospitalCount(int HospitalCount) {
                this.HospitalCount = HospitalCount;
            }

            public List<String> getHosList() {
                return hosList;
            }

            public void setHosList(List<String> hosList) {
                this.hosList = hosList;
            }
        }
    }
}

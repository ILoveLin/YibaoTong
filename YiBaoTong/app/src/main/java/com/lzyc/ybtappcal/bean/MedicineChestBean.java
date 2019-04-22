package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lxx on 2017/6/4.
 */

public class MedicineChestBean {


    /**
     * list : [{"ID":"128","DrugID":"31118","AddTime":"2017-06-02 17:26:50","Name":"感冒滴丸","GoodsName":"","Vender":"贵州健兴药业有限公司","Specifications":"每袋装2.5g(相当于总生药3.6g)*12/盒","Image":"http://app.yibaotongapp.com//pic/mr.png"},{"ID":"127","DrugID":"28221","AddTime":"2017-06-02 17:26:28","Name":"阿托伐他汀钙片(薄膜衣)","GoodsName":"立普妥","Vender":"辉瑞制药有限公司","Specifications":"10mg*7/盒","Image":"http://app.yibaotongapp.com//pic/16119.png"}]
     * count : 2
     */

    private int count;
    private List<ListBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * ID : 128
         * DrugID : 31118
         * AddTime : 2017-06-02 17:26:50
         * Name : 感冒滴丸
         * GoodsName :
         * Vender : 贵州健兴药业有限公司
         * Specifications : 每袋装2.5g(相当于总生药3.6g)*12/盒
         * Image : http://app.yibaotongapp.com//pic/mr.png
         */

        private String ID;
        private String DrugID;
        private String AddTime;
        private String Name;
        private String GoodsName;
        private String Vender;
        private String Specifications;
        private String Image;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getDrugID() {
            return DrugID;
        }

        public void setDrugID(String DrugID) {
            this.DrugID = DrugID;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String AddTime) {
            this.AddTime = AddTime;
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

        public String getVender() {
            return Vender;
        }

        public void setVender(String Vender) {
            this.Vender = Vender;
        }

        public String getSpecifications() {
            return Specifications;
        }

        public void setSpecifications(String Specifications) {
            this.Specifications = Specifications;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "ID='" + ID + '\'' +
                    ", DrugID='" + DrugID + '\'' +
                    ", AddTime='" + AddTime + '\'' +
                    ", Name='" + Name + '\'' +
                    ", GoodsName='" + GoodsName + '\'' +
                    ", Vender='" + Vender + '\'' +
                    ", Specifications='" + Specifications + '\'' +
                    ", Image='" + Image + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MedicineChestBean{" +
                "count=" + count +
                ", list=" + list +
                '}';
    }
}

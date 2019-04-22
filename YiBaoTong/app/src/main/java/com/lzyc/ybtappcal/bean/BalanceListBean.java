package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lxx on 2017/6/5.
 */

public class BalanceListBean {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * ID : 1533
         * Type : 1
         * Money : 0.99
         * Time : 2017-06-05 15:59:21
         * Info : 取消订单
         */

        private String ID;
        private String Type;
        private String Money;
        private String Time;
        private String Info;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public String getMoney() {
            return Money;
        }

        public void setMoney(String Money) {
            this.Money = Money;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        public String getInfo() {
            return Info;
        }

        public void setInfo(String Info) {
            this.Info = Info;
        }
    }
}

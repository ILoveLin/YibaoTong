package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by Lxx on 2017/4/13.
 */
public class BalanceDetail {

    /**
     * total : 27.00
     * list : [{"OrderID":"1490964011524875","State":"3","Delete":"0","ReturnMoney":"9.00","Time":"0000-00-00","Title":"购药返现"},{"OrderID":"1490964023360383","State":"2","Delete":"0","ReturnMoney":"9.00","Time":"0000-00-00","Title":"购药返现"},{"OrderID":"1490964030198305","State":"4","Delete":"0","ReturnMoney":"9.00","Time":"0000-00-00","Title":"购药返现"}]
     */

    private String total;
    /**
     * OrderID : 1490964011524875
     * State : 3
     * Delete : 0
     * ReturnMoney : 9.00
     * Time : 0000-00-00
     * Title : 购药返现
     */

    private List<ListBean> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String OrderID;
        private String State;
        private String Delete;
        private String ReturnMoney;
        private String Time;
        private String Title;

        public String getOrderID() {
            return OrderID;
        }

        public void setOrderID(String OrderID) {
            this.OrderID = OrderID;
        }

        public String getState() {
            return State;
        }

        public void setState(String State) {
            this.State = State;
        }

        public String getDelete() {
            return Delete;
        }

        public void setDelete(String Delete) {
            this.Delete = Delete;
        }

        public String getReturnMoney() {
            return ReturnMoney;
        }

        public void setReturnMoney(String ReturnMoney) {
            this.ReturnMoney = ReturnMoney;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }
    }
}

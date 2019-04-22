package com.lzyc.ybtappcal.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lxx on 2017/4/11.
 */
public class PersonWithdrawList implements Serializable{

    /**
     * list : [{"Alipay":"316467887","AliName":"提提提体检","Balance":"245458.00","State":"2","CreateTime":"2017-03-31 23:08:24","Msg":"提现成功"},{"Alipay":"5487878875","AliName":"普通其中","Balance":"5.00","State":"1","CreateTime":"2017-03-31 23:08:04","Msg":"处理中"}]
     * count : 2
     */

    private int count;
    /**
     * Alipay : 316467887
     * AliName : 提提提体检
     * Balance : 245458.00
     * State : 2
     * CreateTime : 2017-03-31 23:08:24
     * Msg : 提现成功
     */

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

    public static class ListBean implements Serializable {
        private String Alipay;
        private String AliName;
        private String Balance;
        private String State;
        private String CreateTime;
        private String Msg;

        public String getAlipay() {
            return Alipay;
        }

        public void setAlipay(String Alipay) {
            this.Alipay = Alipay;
        }

        public String getAliName() {
            return AliName;
        }

        public void setAliName(String AliName) {
            this.AliName = AliName;
        }

        public String getBalance() {
            return Balance;
        }

        public void setBalance(String Balance) {
            this.Balance = Balance;
        }

        public String getState() {
            return State;
        }

        public void setState(String State) {
            this.State = State;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public String getMsg() {
            return Msg;
        }

        public void setMsg(String Msg) {
            this.Msg = Msg;
        }
    }
}

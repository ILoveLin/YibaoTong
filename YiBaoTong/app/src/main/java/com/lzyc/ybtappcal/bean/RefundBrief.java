package com.lzyc.ybtappcal.bean;

/**
 * Created by yang on 2017/04/13.
 */

public class RefundBrief extends BaseBean {
    private String returnMoneyTotal;
    private String balance;
    private int withdrawCount;
    private String returnMoneyWait;

    public String getReturnMoneyTotal() {
        return returnMoneyTotal;
    }

    public void setReturnMoneyTotal(String returnMoneyTotal) {
        this.returnMoneyTotal = returnMoneyTotal;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getWithdrawCount() {
        return withdrawCount;
    }

    public void setWithdrawCount(int withdrawCount) {
        this.withdrawCount = withdrawCount;
    }

    public String getReturnMoneyWait() {
        return returnMoneyWait;
    }

    public void setReturnMoneyWait(String returnMoneyWait) {
        this.returnMoneyWait = returnMoneyWait;
    }

}

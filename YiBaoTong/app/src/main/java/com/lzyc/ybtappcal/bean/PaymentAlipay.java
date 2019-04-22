package com.lzyc.ybtappcal.bean;

import com.lzyc.ybtappcal.activity.payment.PayResult;

import java.io.Serializable;

/**
 * 支付宝支付回调
 * Created by yang on 2017/03/30.
 */
public class PaymentAlipay  implements Serializable{

    private String resultStatus;
    private String result;
    private String memo;

    public PaymentAlipay(PayResult payResult){
        this.resultStatus=payResult.getResultStatus();
        this.result=payResult.getResult();
        this.memo=payResult.getMemo();
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "PaymentAlipay{" +
                "resultStatus='" + resultStatus + '\'' +
                ", result='" + result + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
}

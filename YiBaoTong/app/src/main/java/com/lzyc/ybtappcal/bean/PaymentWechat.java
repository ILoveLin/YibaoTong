package com.lzyc.ybtappcal.bean;

import java.io.Serializable;

/**
 * 微信支付回调
 * Created by yang on 2017/03/29.
 */

public class PaymentWechat implements Serializable{
    public String transaction;
    public String openId;
    public int errCode;
    public String errStr;
    private int type;
    private boolean checkArgs;
    private boolean isWXAppInstalled;//是否安装了微信
    private boolean isWXAppSupportAPI;//版本是否过低


    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrStr() {
        return errStr;
    }

    public void setErrStr(String errStr) {
        this.errStr = errStr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Boolean isCheckArgs() {
        return checkArgs;
    }

    public void setCheckArgs(boolean checkArgs) {
        this.checkArgs = checkArgs;
    }

    public boolean isWXAppInstalled() {
        return isWXAppInstalled;
    }

    public void setWXAppInstalled(boolean WXAppInstalled) {
        isWXAppInstalled = WXAppInstalled;
    }

    public boolean isWXAppSupportAPI() {
        return isWXAppSupportAPI;
    }

    public void setWXAppSupportAPI(boolean WXAppSupportAPI) {
        isWXAppSupportAPI = WXAppSupportAPI;
    }

    @Override
    public String toString() {
        return "PaymentWechat{" +
                "transaction='" + transaction + '\'' +
                ", openId='" + openId + '\'' +
                ", errCode=" + errCode +
                ", errStr='" + errStr + '\'' +
                ", type=" + type +
                ", checkArgs=" + checkArgs +
                ", isWXAppInstalled=" + isWXAppInstalled +
                ", isWXAppSupportAPI=" + isWXAppSupportAPI +
                '}';
    }
}

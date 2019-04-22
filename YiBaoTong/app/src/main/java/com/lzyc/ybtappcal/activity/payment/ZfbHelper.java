package com.lzyc.ybtappcal.activity.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.lzyc.ybtappcal.bean.PaymentAlipay;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnPaymentCallbackListener;
import com.lzyc.ybtappcal.util.LogUtil;

import java.util.Map;

/**
 * 支付宝支付
 * Created by Lxx on 2017/3/25.
 */
public class ZfbHelper{
    private static final String TAG=ZfbHelper.class.getSimpleName();
    private static final int SDK_PAY_FLAG = 1;
    private Activity mContext;
    private boolean isRSA = false;//(HttpConstant.ZHIFUBAO_RSA_PRIVATE.length() > 0);
    private String orderID;
    /**
     * 支付宝成功（即resultStatus是9000的时候）做二次验证
     * 再根据后台返回的state做是否成功的判断，才能决定是否是真正的成功
     * 支付失败不做任何操作
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult  = new PayResult((Map<String, String>) msg.obj);
                    PaymentAlipay paymentAlipay=new PaymentAlipay(payResult) ;
                    String resultStatus = paymentAlipay.getResultStatus();
                    sendPaymentAlipayBroadcast(paymentAlipay);
                    break;
                }
                default:
                    break;
            }
        }

    };

    private void sendPaymentAlipayBroadcast(PaymentAlipay paymentAlipay) {
        Intent intent = new Intent();
        intent.setAction(Contants.ACTION_NAME_PAYMENT_ALIPY);
        intent.putExtra(Contants.KEY_OBJ_PAYMENT_ALIPY,paymentAlipay);
        mContext.sendBroadcast(intent);
        mContext=null;
    }

    /**
     * 调用支付，监听支付结果
     * @param context
     * @param bundle
     */
    public  void sendPayment(Activity context, Bundle bundle) {
        this.mContext=context;
        String orderId=bundle.getString(Contants.KEY_ORDER_ID);
        String price=bundle.getString(Contants.KEY_PAYEMNT_PRICE);
        String name=bundle.getString(Contants.KEY_PAYEMNT_NAME);
        if(TextUtils.isEmpty(orderId)){
            orderId="1";
        }
        this.orderID=orderId;
        if(TextUtils.isEmpty(price)){
            price="0.00";
        }
        if(TextUtils.isEmpty(name)){
            name="error:empty";
        }
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(HttpConstant.ZHIFUBAO_APPID, isRSA, orderID, price, name);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String privateKey = isRSA ? HttpConstant.ZHIFUBAO_RSA_PRIVATE : HttpConstant.ZHIFUBAO_RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, isRSA);
        final String orderInfo = orderParam + "&" + sign;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(mContext);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void sendPayReq(final Activity act, final String orderID, String payPrice, String name) {
        this.mContext = act;
        this.orderID = orderID;
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(HttpConstant.ZHIFUBAO_APPID, isRSA, orderID, payPrice, name);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String privateKey = isRSA ? HttpConstant.ZHIFUBAO_RSA_PRIVATE : HttpConstant.ZHIFUBAO_RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, isRSA);
        final String orderInfo = orderParam + "&" + sign;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(act);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


}

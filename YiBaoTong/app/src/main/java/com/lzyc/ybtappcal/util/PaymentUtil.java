package com.lzyc.ybtappcal.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.lzyc.ybtappcal.activity.payment.ZfbHelper;
import com.lzyc.ybtappcal.activity.payment.order.OrderMineActivity;
import com.lzyc.ybtappcal.app.ActivityCollector;
import com.lzyc.ybtappcal.bean.Event;
import com.lzyc.ybtappcal.bean.PaymentInfo;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnPaymentCallbackListener;
import com.lzyc.ybtappcal.volley.BaseService;
import com.lzyc.ybtappcal.volley.ServiceResponseCallback;
import com.lzyc.ybtappcal.wxapi.util.WxHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 第三方支付工具类
 * Created by yang on 2017/03/29.
 */
public class PaymentUtil  implements ServiceResponseCallback {
    private static final String TAG = PaymentUtil.class.getSimpleName();
    private Bundle mBundle;
    public BaseService httpRequest;
    private int typePage=0;
    private int statusInt;
    private Context mContext;
    private String orderId;
    private String returnMoney;

    public PaymentUtil() {
        if(mBundle==null){
            mBundle = new Bundle();
        }
        if (httpRequest == null) {
            httpRequest = new BaseService();
        }
    }
    /**
     * 微信支付
     */
    public void paymentWeChat(Context context, PaymentInfo paymentInfo) {

        String orderId = paymentInfo.getOrderId();
        if (TextUtils.isEmpty(orderId)) {
            LogUtil.e(TAG, "orderId不能为空");
            return;
        }
        Double priceDouble = Double.parseDouble(paymentInfo.getPrice());
        double priceDouble2=priceDouble * 100;
        DecimalFormat df = new DecimalFormat("######0");
        String price = df.format(priceDouble2);
        if (TextUtils.isEmpty(orderId)) {
            LogUtil.e(TAG, "price不能为空");
            return;
        }
        String orderName;
        String name = paymentInfo.getName();
        String goosdName = paymentInfo.getGoodsName();

        if(TextUtils.isEmpty(goosdName)){
            orderName = name;
        } else {
            orderName = goosdName;
        }

        new WxHelper().sendPayment(context, getBundle(orderId, orderName, price));
    }

    /**
     * 支付宝支付
     * @param context
     * @param paymentInfo
     */
    public void paymentAlipay(Activity context, PaymentInfo paymentInfo){

        String orderId = paymentInfo.getOrderId();
        if (TextUtils.isEmpty(orderId)) {
            LogUtil.e(TAG, "orderId不能为空");
            return;
        }
        Double priceDouble = Double.parseDouble(Util.getFloatDotStr(paymentInfo.getPrice()));
        String price = String.valueOf(priceDouble);
        if (TextUtils.isEmpty(orderId)) {
            LogUtil.e(TAG, "price不能为空");
            return;
        }
        String orderName;
        String name = paymentInfo.getName();
        String goosdName = paymentInfo.getGoodsName();
//        if (TextUtils.isEmpty(name)) {
//            LogUtil.y(TAG, "name是空");
//        }else{
//            goosdName=name;
//        }
//        if (TextUtils.isEmpty(goosdName)) {
//            LogUtil.y(TAG, "goodsname是空");
//            orderName=name;
//        }else{
//            orderName=goosdName;
//        }

        if(TextUtils.isEmpty(goosdName)){
            orderName = name;
        } else {
            orderName = goosdName;
        }

        new ZfbHelper().sendPayment(context, getBundle(orderId, orderName, price));
    }

    /**
     * @param out_trade_no orderId
     * @param body
     * @param totalFee     price
     * @return
     */
    private Bundle getBundle(String out_trade_no, String body, String totalFee) {
        mBundle.putString(Contants.KEY_ORDER_ID, out_trade_no);
        mBundle.putString(Contants.KEY_PAYEMNT_NAME, body);
        mBundle.putString(Contants.KEY_PAYEMNT_PRICE, totalFee);
        return mBundle;
    }

    /**
     * 每次订单支付成功都需要执行
     * 订单二次验证
     */
    public void requestGoodsValidateOrder(Context context,String orderId,String returnMoney) {
        this.mContext=context;
        this.orderId=orderId;
        this.returnMoney = returnMoney;
        typePage= (int) SharePreferenceUtil.get(context,Contants.KEY_PAGE_PAYMENT,0);
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_SUBMIT_SECOND_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_SUBMIT_SECOND_SIGN);
        params.put(HttpConstant.SHOP_SUBMIT_SECOND_ORDER_ID, orderId);
        LogUtil.e(TAG,"###二次验证#params##"+params.toString());
        httpRequest.postMap(HttpConstant.APP_URL, this, params);

    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        LogUtil.e(TAG,"#msg>>"+msg+"#what>>"+what+"#response>>"+response);
        try {
            JSONObject data = response.getJSONObject(HttpConstant.DATA);
            SharePreferenceUtil.put(mContext,Contants.KEY_BOL_CART_REFRSH,true);
            LogUtil.e(TAG, data.toString());
            statusInt = response.optInt("status");
            LogUtil.e(TAG,"##typePage##"+typePage);
//            if(typePage==Contants.VAL_PAGE_PAYMENT_PURCHASE||typePage==Contants.VAL_PAGE_PAYMENT_MINE){//如果走的是购买相关界面
//
//            }else{
//                resetSharePreferencePage();
//            }
            resetSharePreferencePage();
            onResutlt();
//            EventBus.getDefault().post(new Event("z004"));
        } catch (JSONException e) {
            e.printStackTrace();
//            EventBus.getDefault().post(new Event("z005"));
        }
    }

    /**
     * 重置存储的page缓存
     */
    private void resetSharePreferencePage() {
        SharePreferenceUtil.put(mContext,Contants.KEY_PAGE_PAYMENT,Contants.VAL_PAGE);
    }

    /**
     *返回订单未支付状态不做其他操作
     * @param errorMsg
     */
    @Override
    public void error(String errorMsg) {
        LogUtil.e(TAG,"##错误##"+errorMsg);
        switchActivity (errorMsg);

    }

    /**
     * 切换界面
     */
    private void switchActivity(String errorMsg) {
        LogUtil.y(orderId+"########typePage###############################"+typePage);
        if(typePage==Contants.VAL_PAGE_PAYMENT_PURCHASE) {
            if(errorMsg.equals("订单处于未支付状态")){
//                EventBus.getDefault().post(new Event("z005"));
                SharePreferenceUtil.put(mContext,Contants.KEY_BOL_CART_REFRSH,true);
                Intent intent=new Intent(mContext,OrderMineActivity.class);
                mContext.startActivity(intent);
                ActivityCollector.removeOrderAll();
            }
        }else{
            sendPaymentErrorBroadcast();
            LogUtil.e(TAG,errorMsg);
        }
    }

    private void onResutlt(){
        if(Contants.INT_0 == statusInt){
            Bundle bundle=new Bundle();
            bundle.putString("OTC","1");
            bundle.putString(Contants.KEY_ORDER_ID, orderId);
            bundle.putString("return_money", returnMoney);
            LogUtil.d("lxx", "224-->"+returnMoney);
            mContext.startActivity(Intents.openOrderSuccessActivity(mContext, bundle));
//            sendPaymentSuccessBroadcast();
        } else {
            LogUtil.e(TAG, "##############购买失败");
            if(typePage==Contants.VAL_PAGE_PAYMENT_PURCHASE||typePage==Contants.VAL_PAGE_PAYMENT_MINE){
//                Intent intent = new Intent(mContext, OrderMineActivity.class);
//                intent.putExtra("orderId", orderId);
//                mContext.startActivity(intent);
//                Bundle bundle = new Bundle();
//                bundle.putString("orderId",orderId);
//                Intents.openOrderDetailActivity(mContext,bundle);
                ActivityCollector.removeOrderAll();
            }else{
                sendPaymentErrorBroadcast();
            }

        }
    }

    /**
     * 购买成功
     */
    private void sendPaymentSuccessBroadcast() {
        Intent intent = new Intent();
        intent.setAction(Contants.ACTION_NAME_PAYMENT_SUCCESS);
        mContext.sendBroadcast(intent);
    }
    /**
     * 购买失败
     */
    private void sendPaymentErrorBroadcast() {
        Intent intent = new Intent();
        intent.setAction(Contants.ACTION_NAME_PAYMENT_ERROR);
        mContext.sendBroadcast(intent);
    }
}

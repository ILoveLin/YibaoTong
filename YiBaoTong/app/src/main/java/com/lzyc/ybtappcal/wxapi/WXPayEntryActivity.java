package com.lzyc.ybtappcal.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.PaymentWechat;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.wxapi.util.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付回调
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "lxx";
    private IWXAPI api;
	private PaymentWechat paymentWechat;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
		LogUtil.e(TAG, "##############onCreate##########");
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		LogUtil.e(TAG, "##############onNewIntent##########");
		setIntent(intent);
		api.handleIntent(intent, this);
		LogUtil.e(TAG, "##############api.handleIntent(intent, this);##########");
	}

	/**
	 *
	 *0	成功	展示成功页面
	 *-1签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
	 *-2无需处理。发生场景：用户不支付了，点击取消，返回APP。
	 * @param req
     */
	@Override
	public void onReq(BaseReq req) {
		if(paymentWechat==null){
			paymentWechat=new PaymentWechat();
		}
		paymentWechat.setOpenId(req.openId);
		paymentWechat.setTransaction(req.transaction);
		paymentWechat.setType(req.getType());
		paymentWechat.setCheckArgs(req.checkArgs());
		LogUtil.e(TAG,"onReq#"+paymentWechat.toString());
	}



	@Override
	public void onResp(BaseResp resp) {
		LogUtil.e(TAG, "##############onResp##########" + resp.errCode);
		if(paymentWechat==null){
			paymentWechat=new PaymentWechat();
		}
		paymentWechat.setOpenId(resp.openId);
		paymentWechat.setTransaction(resp.transaction);
		paymentWechat.setType(resp.getType());
		paymentWechat.setCheckArgs(resp.checkArgs());
		paymentWechat.setErrCode(resp.errCode);
		paymentWechat.setErrStr(resp.errStr);
		paymentWechat.setWXAppInstalled(api.isWXAppInstalled());
		paymentWechat.setWXAppSupportAPI(api.isWXAppSupportAPI());
		sendPaymentWeChatBroadcast(paymentWechat);
		finish();
	}

	/**
	 *
	 * @param paymentWechat
     */
	private void sendPaymentWeChatBroadcast(PaymentWechat paymentWechat) {
		Intent intent = new Intent();
		intent.setAction(Contants.ACTION_NAME_PAYMENT_WECHAT);
		intent.putExtra(Contants.KEY_OBJ_PAYMENT_WECHAT,paymentWechat);
		sendBroadcast(intent);
	}

}

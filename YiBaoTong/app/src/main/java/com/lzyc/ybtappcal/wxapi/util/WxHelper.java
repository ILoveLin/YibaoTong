package com.lzyc.ybtappcal.wxapi.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Xml;
import android.widget.Toast;

import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.LogUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 微信支付
 */
public class WxHelper {

    private static final String TAG ="WxHelper";
    private Map<String, String> resultunifiedorder;
    private PayReq req;
    private IWXAPI msgApi;
    private Context mContext;
    private PayAsyncTask payAsyncTask;
    public WxHelper() {
        req = new PayReq();
        payAsyncTask = new PayAsyncTask();
    }

    /**
     * 调用支付
     * @param context
     * @param bundle
     */
    public void sendPayment(Context context, Bundle bundle){
        this.mContext=context;
        msgApi = WXAPIFactory.createWXAPI(mContext, null);
        String out_trade_no=bundle.getString(Contants.KEY_ORDER_ID);
        String totalFee=bundle.getString(Contants.KEY_PAYEMNT_PRICE);
        String body=bundle.getString(Contants.KEY_PAYEMNT_NAME);
        if(TextUtils.isEmpty(out_trade_no)){
            out_trade_no="1";
        }
        if(TextUtils.isEmpty(totalFee)){
            totalFee="0.00";
        }
        if(TextUtils.isEmpty(body)){
            totalFee="error:empty";
        }
        LogUtil.d(TAG,"#info#urlWeixin>>"+HttpConstant.WEXIN_PAYMENT_REQUEST +"#out_trade_no>>"+out_trade_no+"#totalFee>>"+totalFee+"#>>"+body);
        payAsyncTask.execute(HttpConstant.WEXIN_PAYMENT_REQUEST,out_trade_no, totalFee, body);
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /***
     * 生成签名参数
     */
    private void genPayReq() {
        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        if (resultunifiedorder != null) {
            req.prepayId = resultunifiedorder.get("prepay_id");
            req.packageValue = "prepay_id=" + resultunifiedorder.get("prepay_id");
        } else {
            LogUtil.d(TAG, "prepayid为空");
        }
        req.nonceStr = getNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());
        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genAppSign(signParams);
        LogUtil.d(TAG, "----" + signParams.toString());
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        LogUtil.e(TAG, "----" + appSign);
        return appSign;
    }

    private class PayAsyncTask extends AsyncTask<String, Void, Map<String, String>> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(mContext, "提示", "正在提交订单");
        }

        @Override
        protected Map<String, String> doInBackground(String... params) {
            String url = String.format(String.format(params[0]));
            String entity = getProductArgs(String.format(params[1]), String.format(params[2]), String.format(params[3]));
            LogUtil.d(TAG, ">>>>entity>>" + entity);
            byte[] buf = Util.httpPost(url, entity);
            String content = new String(buf);
            if(content.isEmpty()){
                LogUtil.e(TAG,"error:微信支付content是空");
            }
            Map<String, String> xml = decodeXml(content);
            LogUtil.d(TAG, "--content--" + content);
            return xml;
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            super.onPostExecute(result);
            if (dialog != null) {
                dialog.dismiss();
            }
            resultunifiedorder = result;
            genPayReq();//生成签名参数
            msgApi.registerApp(Constants.APP_ID);
            msgApi.sendReq(req);
        }
    }

    /**
     * 回调信息
     * 解析xml
     * @param content
     * @return
     */
    private Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("xml".equals(nodeName) == false) {
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.CDSECT:
                        break;
                }
                event = parser.next();
            }
            return xml;
        } catch (Exception e) {
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
            LogUtil.e(TAG, "--decodeXml--" + e.toString(),e);
        }
        return null;
    }

    /**
     * 获取支付相关参数
     * @param out_trade_no
     * @param total_fee
     * @param body
     * @return
     */
    private String getProductArgs(String out_trade_no, String total_fee, String body) {
        StringBuffer xml = new StringBuffer();
        try {
            String nonceStr = getNonceStr();
            xml.append("<xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", body));
            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", HttpConstant.WX_CALL_BACK));//回调地址
            packageParams.add(new BasicNameValuePair("out_trade_no", out_trade_no));
            packageParams.add(new BasicNameValuePair("total_fee", total_fee));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
            String sign = getPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));
            String xmlString = toXml(packageParams);
            LogUtil.d(TAG, "###xmlString##" + xmlString);
            return xmlString;
        } catch (Exception e) {
            LogUtil.e(TAG, "--getProductArgs--" + e.toString(),e);
            return null;
        }
    }

    /*
     * 生成随机号，防重发
     */
    private String getNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /*
     * 生成签名
     */
    private String getPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return packageSign;
    }

    /*
     * 转换成xml
     */
    private String toXml(List<NameValuePair> params) {
        String xml = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><xml>");
            for (int i = 0; i < params.size(); i++) {
                sb.append("<" + params.get(i).getName() + ">");
                sb.append(params.get(i).getValue());
                sb.append("</" + params.get(i).getName() + ">");
            }
            sb.append("</xml>");
            xml = new String(sb.toString().getBytes(), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LogUtil.d(TAG, ">>xml>>" + xml);
        return xml;
    }

}

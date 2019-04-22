package com.lzyc.ybtappcal.activity.payment.order;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.OrderCheckAdapter;
import com.lzyc.ybtappcal.adapter.OrderCheckPaymentAdapter;
import com.lzyc.ybtappcal.app.ActivityCollector;
import com.lzyc.ybtappcal.bean.AddressInfo;
import com.lzyc.ybtappcal.bean.GoodsBean;
import com.lzyc.ybtappcal.bean.PaymentAlipay;
import com.lzyc.ybtappcal.bean.PaymentInfo;
import com.lzyc.ybtappcal.bean.PaymentWechat;
import com.lzyc.ybtappcal.bean.ShopingCart;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.PaymentUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.Util;
import com.lzyc.ybtappcal.view.CustomLinearLayoutManager;
import com.lzyc.ybtappcal.view.SwitchButtom;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 检查订单
 * Created by lxx on 2017/4/26.
 */
public class OrderCheckActivity extends BaseActivity{
    private static final int REQUEST_CODE_ADDRESS_MANAGER =100;//地址信息相关逻辑，处理地址管理界面回调
    private static final int REQUEST_CODE_ADDRESS_BUILD =101;//地址信息相关逻辑，处理地址创建界面回调
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_return_money)
    TextView tvReturnMoney;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_submit_progress)
    ProgressBar tvSubmitProgress;
    @BindView(R.id.lin_bottom)
    LinearLayout linBottom;
    @BindView(R.id.tv_bottom)
    TextView tvBottom;
    @BindView(R.id.v_shade)
    View vShade;

    //弹窗
    RecyclerView recPayOnline;
    RecyclerView recPayOnmoney;
    ImageView imgPayOnline;
    ImageView imgPayOnmoney;
    SwitchButtom balanceCheck;
    LinearLayout linWeixin;
    LinearLayout linAliPay;
    TextView tvSubmitDialog;
    ProgressBar tvSubmitProgressDialog;
    LinearLayout layoutSubmit;
    LinearLayout linPayOnline;
    LinearLayout linPayOnmoney;
    LinearLayout linTopFloat;
    LinearLayout linTopFloatOnMoney;
    LinearLayout linTopFloatOnLine;
    View vDialog;
    ImageView imgCancel;
    View vShadeDialog;

    OrderCheckPaymentAdapter mAdapterOnline;
    OrderCheckPaymentAdapter mAdapterOnmoney;

    View vOnlineHeight;
    View vOnLine;
    View vOnMoney;
    View vOnmoneyBottom;
    View vOnmoneyBottomLine;

    boolean imgFlagOnmoney = true;
    boolean imgFlagOnline = true;

    Dialog dialog;
    View dialogView;
    boolean onMoney = false;

    List<ShopingCart> mDataShop = new ArrayList<>();

    TextView tvPayOnline;//弹窗  在线支付
    TextView tvPayOnmoney;//弹窗  线下支付
    TextView tvPaymentBalance;//弹窗  账户余额

    Double mBalancePrice; // 账户余额
    Double mTotalPrice; // 订单总金额
    Double payPrice; // 支付金额

    Double balance = 0d;

    boolean mBalanceSelect = false;

    int payType = 3; // 1. 微信 2. 支付宝 3.余额

    boolean payAli = true;

    //界面
    Context mContext;
    OrderCheckAdapter mAdapter;

    AddressInfo addressInfoData;

    Bundle bundle;
    final String OTC = "OTC";

    ArrayList<GoodsBean> otcData = new ArrayList<>();
    ArrayList<GoodsBean> rxData = new ArrayList<>();

    List<ShopingCart> mData = new ArrayList<>();

    String orderId="";

    PaymentBroadcastReceiver receiver=new PaymentBroadcastReceiver();

    double payAliWeixin = 0;

    String total;//总价

    String returnMoney;//返现金额

    double rxFreight = 0;//线下支付运费
    double otcFreight = 0;//线上支付运费
    double freight = 0;//运费
    double freeShip = 0;//包邮额度
    double rxPrice = 0;//线上支付价格
    double otcPrice = 0;//线下支付价格

    boolean flagRx;//处方药  线下支付

    String bal = "";//请求接口时  余额支付金额

    int payPage = 1;//是否从购物车页面进入购买

    String submitStr;

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_check;
    }

    @Override
    protected void init() {

        setPageStateView();
        showLoading();

        setTitleName(Contants.STR_TITLE_ORDER_CHECK);
        mContext = this;
        registerPaymentBroadcastReceiver();

        ActivityCollector.getInstance().addActivityOrder(this);
        ActivityCollector.getInstance().activityOrderAbout(this);

        mAdapter = new OrderCheckAdapter(mContext);

        initBundle();
        requestAddress();
        initRecyclerView();



    }

    /**
     * 获取前一界面信息
     */
    private void initBundle(){
        bundle = getIntent().getExtras();

        payPage = bundle.getInt(Contants.KEY_BUY_FROM_ORDERBY, 1);

        //返现
        returnMoney = (String) bundle.getSerializable(Contants.KEY_GOODS_PRICE_RETURN);
        tvReturnMoney.setText(returnMoney);

        //按药房分类-->按处方/非处方分类
        mDataShop = (List<ShopingCart>) bundle.getSerializable(Contants.KEY_GOODS_LIST);

        //总价
        total = (String) bundle.getSerializable(Contants.KEY_GOODS_PRICE_ALL);
        mTotalPrice = Double.parseDouble(total);

        for(ShopingCart bean : mDataShop){

//            LogUtil.d("TAG", "174-->"+bean.toString());

            try{
                freight = Double.parseDouble(bean.getFreight());
                freeShip = Double.parseDouble(bean.getFreeShipp());

            } catch (Exception e){
                e.printStackTrace();
            }

        }

        Map<String,List<ShopingCart>> mShopsData = new HashMap<>();

        for(ShopingCart shopingCart : mDataShop){

            String shopName = shopingCart.getName();

            List<ShopingCart> mShopSingData = mShopsData.get(shopName);

            if(null == mShopSingData){
                mShopSingData = new ArrayList<>();
                mShopsData.put(shopName,mShopSingData);
            }

            mShopSingData.add(shopingCart);
        }

        mData = new ArrayList<>();
        double dkOtcPrice = 0;

        for(String name : mShopsData.keySet()){ // 药房

            List<ShopingCart> list = mShopsData.get(name);

            for(ShopingCart cart : list){ // 所选中商品

                List<GoodsBean> itemList = cart.getGoodsList();

                if("德开大药房".equals(cart.getName())){

                    for(GoodsBean b : itemList){

                        if("1".equals(b.getOnlinePay())){
                            dkOtcPrice += (Double.parseDouble(b.getDeKaiPrice()) * b.getGoodsCount());
                        }
                    }
                }

                otcData = new ArrayList<>();

                rxData = new ArrayList<>();

                for(GoodsBean b : itemList){

                    if("0".equals(b.getOnlinePay())){
                        rxPrice += (Double.parseDouble(b.getDeKaiPrice()) * b.getGoodsCount());
                    }

                    if("1".equals(b.getOnlinePay())){
                        otcPrice += (Double.parseDouble(b.getDeKaiPrice()) * b.getGoodsCount());
                    }
                }

                if(freeShip > rxPrice && 0 != rxPrice){//线下支付  付运费
                    rxFreight = freight;
                }

                Double oPrice = otcPrice-dkOtcPrice;
                if(freeShip > oPrice && 0 != oPrice){//线上支付  付运费
                    otcFreight = freight;
                }

                otcPrice += otcFreight;
                rxPrice += rxFreight;
                mTotalPrice += otcFreight;

                total = String.valueOf(mTotalPrice + rxFreight);

                for(GoodsBean goodsBean: itemList){ // 单个商品

                    if("1".equals(goodsBean.getOnlinePay())){
                        otcData.add(goodsBean);
                    }else{
                        rxData.add(goodsBean);
                    }
                }

                cart.getGoodsList().clear();

                ShopingCart otcShoping = cart.clone();
                ShopingCart rxShoping = cart.clone();
                otcShoping.setGoodsList(otcData);
                rxShoping.setGoodsList(rxData);

                if(!otcData.isEmpty()){
                    mData.add(otcShoping);
                }
                if(!rxData.isEmpty()){
                    mData.add(rxShoping);
                    onMoney = true;
                }

                if(!rxData.isEmpty() && otcData.isEmpty()){
                    tvSubmit.setText("提交预订");
                } else {
                    tvSubmit.setText("提交订单");
                }

            }
        }

        tvTotal.setText(Util.getFloatDotStr(String.valueOf(rxPrice+otcPrice)));

        mAdapter.updata(mData);
    }

    /**
     * 初始化列表
     */
    private void initRecyclerView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setCheckOrderListener(new OrderCheckAdapter.CheckOrderListener() {
            @Override
            public void onAddress() {
                checkAddress();
            }
        });
    }

    @OnClick({R.id.ib_left, R.id.rel_submit, R.id.v_shade})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.ib_left:
                finish();
                break;
            case R.id.rel_submit:

                requestEventCode("z002");

                if(null ==addressInfoData || "-1".equals(addressInfoData.getDefault())){
                    showToast(Contants.STR_ADDRESS_CHOOSE);
                    return;
                }

                if (!NetworkUtil.CheckConnection(this)) {
                    showToast(getResources().getString(R.string.error_net_toast));
                    return;
                }

                if("提交中".equals(tvSubmit.getText().toString())) return;

                if("提交预订".equals(tvSubmit.getText().toString())){
                    initPaymentDatas();
                    checkSubmit();
                    payType = 3;
                    flagRx = true;
                    vShade.setVisibility(View.VISIBLE);
                    setPayType();

                    requestSubmit();
                } else {
                    animationBottom();
                }
                break;
            case R.id.v_shade:
                break;
        }
    }

    /**
     * 底部弹窗切换
     */
    private void animationBottom(){
        TranslateAnimation animationBottom = new TranslateAnimation(0, 0, 0, linBottom.getHeight());
        animationBottom.setDuration(200);
        animationBottom.setFillAfter(true);
        linBottom.startAnimation(animationBottom);
        tvBottom.startAnimation(animationBottom);
        animationBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showPayment();
                initPaymentDatas();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void animationTop() {
        try{
            TranslateAnimation animationBottom = new TranslateAnimation(0, 0, linBottom.getHeight(), 0);
            animationBottom.setDuration(300);
            animationBottom.setFillAfter(true);
            linBottom.startAnimation(animationBottom);
            tvBottom.startAnimation(animationBottom);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 选择地址
     */
    private void checkAddress(){
        if(null != addressInfoData){
            Intent intent = new Intent(mContext, AddressManagerActivity.class);
            intent.putExtra(Contants.KEY_PAGE_ADDRESS_MANAGER,Contants.VAL_PAGE_PAYMENT_CHECK);
            intent.putExtra(Contants.KEY_ADDRESS_ID,addressInfoData.getID());
            startActivityForResult(intent, REQUEST_CODE_ADDRESS_MANAGER);
        } else {
            Intent intent = new Intent(mContext, AddressBuildActivity.class);
            bundle.putInt("size", 0);
            bundle.putBoolean("order", true);
            bundle.putInt(Contants.KEY_PAGE_ADDRESS_BUILD,Contants.VAL_PAGE_PAYMENT_CHECK);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE_ADDRESS_BUILD);
        }
    }

    private void checkSubmit(){
        double balance = 0d;
        if (mBalanceSelect) {
            if (mBalancePrice >= mTotalPrice) {
                payType = 3;
                balance = mTotalPrice;
                payPrice = 0d;
            } else {
                payType = 0;
                balance = mBalancePrice;
                payPrice = mTotalPrice - balance;
            }
        } else {
            payType = 0;
            payPrice = mTotalPrice;
        }

        if(!"提交预订".equals(tvSubmit.getText().toString())){
            if (0 == payType) {
                if (linAliPay.isSelected()){
                    payType = 2;
                }
                if (linWeixin.isSelected()){
                    payType = 1;
                }
            }
        }

        if(payType==1){
            if(!isWeixinAvilible(mContext)){
                showToast("微信未安装或版本过低");
                return;
            }
        }

        payPrice = Double.valueOf(Util.getFloatDotStr(String.valueOf(Double.valueOf(payPrice))));

        tvSubmitProgress.setVisibility(View.VISIBLE);
        submitStr = tvSubmit.getText().toString();
        tvSubmit.setText("提交中");
        tvSubmit.setClickable(false);
        tvSubmit.setEnabled(false);

    }

    private void setPayType(){

        if(null != balanceCheck && balanceCheck.isOpened()){
            bal = String.valueOf(balance);
            if("0.00".equals(tvPayOnline.getText().toString())){
                payType = 3;
            }
        } else {
            bal = "0";
        }

        if(flagRx){
            payType = 0;
        }
    }

    @Override
    protected void onClickRetry() {
        showLoading();
        requestAddress();
    }

    /**
     * 接口-->地址
     */
    private void requestAddress(){
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_ORDER_CHECK_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_ORDER_CHECK_SIGN);
        params.put(HttpConstant.SHOP_ORDER_PARAM_CHECK_UID, SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString());

        if (!NetworkUtil.CheckConnection(this)) {
            showError();
            return;
        }

        request(params, HttpConstant.BUY_CHECK);
    }

    /**
     * 提交订单
     */
    private void requestSubmit(){

        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_SUBMIT_SHOP_CARD_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_SUBMIT_SHOP_CARD_SIGN);
        params.put(HttpConstant.APP_UID, SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString());
        params.put(HttpConstant.SHOP_SUBMIT_PARAM_ADDRESS_ID, addressInfoData.getID());
        params.put(HttpConstant.SHOP_SUBMIT_PARAM_PAY_TYPE, String.valueOf(payType));//支付方式
        params.put(HttpConstant.SHOP_SUBMIT_SHOP_CARD_GOODS_INFO, getGoodsInfoStr());//商品ID数量等信息
        params.put(HttpConstant.SHOP_SUBMIT_PARAM_BALANCE, bal);//余额支付金额
        params.put(HttpConstant.SHOP_SUBMIT_SHOP_CARD_TOTAL_PRICE, Util.getFloatDotStr(total));//订单总价
        params.put(HttpConstant.SHOP_SUBMIT_PARAM_RETURN_MONEY, tvReturnMoney.getText().toString());//返现金额
        params.put(HttpConstant.SHOP_SUBMIT_PARAM_SHOPINGCART, String.valueOf(payPage));

        request(params, HttpConstant.BUY_SUBMIT_SHOP_CARD);

    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what){
            case HttpConstant.BUY_CHECK:
                try {
                    addressInfoData = new Gson().fromJson(response.getJSONObject(
                            HttpConstant.DATA).optString("defaultAddress"),AddressInfo.class);

                    mBalancePrice = response.getJSONObject(HttpConstant.DATA)
                            .getJSONObject("Other").optDouble("Balance");

                } catch (Exception e) {
                    e.printStackTrace();
                    addressInfoData = null;
                }
                mAdapter.updataAddress(addressInfoData);

                showContent();
                break;

            case HttpConstant.BUY_SUBMIT_SHOP_CARD:

                try{

                    GoodsBean bean = mData.get(0).getGoodsList().get(0);

                    StringBuilder type = new StringBuilder();
                    for(ShopingCart shop : mData){
                        for(GoodsBean b : shop.getGoodsList()){
                            type.append(b.getType());
                        }
                    }
//--------------------------------------------
//                    String t = type.toString();
//                    if(t.contains("2")){
//                        payPrice -= payAliWeixin;
//                    }
// --------------------------------------------

                    orderId = response.getJSONObject(HttpConstant.DATA).optString("OrderID");

                    PaymentInfo paymentInfo = new PaymentInfo();
                    String name = bean.getName();
                    String goodsName = bean.getGoodsName();

                    if(mData.size() > 1 || otcData.size() > 1){
                        if(!TextUtils.isEmpty(goodsName)){
                            goodsName += "等";
                        } else {
                            name += "等";
                        }

                    }
                    paymentInfo.setName(name);
                    paymentInfo.setGoodsName(goodsName);
                    //--------------------------------------------
//                    if(0 != otcFreight && 0 != rxFreight){
//                        payPrice += otcFreight;
//                    }
                    //--------------------------------------------

                    String priceStr = "";
                    try{
                        priceStr = tvPayOnline.getText().toString();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    paymentInfo.setPrice(priceStr);
                    paymentInfo.setOrderId(orderId);
                    // 线上
                    if (Contants.STR_1.equals(bean.getOnlinePay())) {

                        SharePreferenceUtil.put(OrderCheckActivity.this, Contants.KEY_PAGE_PAYMENT,Contants.VAL_PAGE_PAYMENT_PURCHASE);

                        if(Contants.INT_1 == payType){
                            new PaymentUtil().paymentWeChat(OrderCheckActivity.this, paymentInfo);
                            dismissDialog();
                        }

                        if(Contants.INT_2 == payType){
                            new PaymentUtil().paymentAlipay(OrderCheckActivity.this, paymentInfo);
                            dismissDialog();
                        }

                        if(Contants.INT_3 == payType){
                            int status = response.getInt(HttpConstant.STATUS);
                            if (0 == status) {
                                requestEventCode("z004");
                                bundle.putString(OTC, bean.getOnlinePay());
                                bundle.putString(Contants.KEY_ORDER_ID, orderId);
                                bundle.putString("return_money", tvReturnMoney.getText().toString());
                                openActivity(OrderSuccessActivity.class, bundle);
                                dismissDialog();
                            }
                        }

                    } else {

                        requestEventCode("z004");
                        bundle.putString(OTC, bean.getOnlinePay());
                        bundle.putString(Contants.KEY_ORDER_ID, orderId);
                        bundle.putString("return_money", tvReturnMoney.getText().toString());
                        openActivity(OrderSuccessActivity.class ,bundle);
                        dismissDialog();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void registerPaymentBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contants.ACTION_NAME_PAYMENT_WECHAT);
        intentFilter.addAction(Contants.ACTION_NAME_PAYMENT_ALIPY);
        intentFilter.addAction(Contants.ACTION_NAME_PAYMENT_SUCCESS);
        registerReceiver(receiver, intentFilter);
    }

    private class PaymentBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String actionName=intent.getAction();
            if(actionName.equals(Contants.ACTION_NAME_PAYMENT_WECHAT)){
                PaymentWechat paymentWechat= (PaymentWechat) intent.getSerializableExtra(Contants.KEY_OBJ_PAYMENT_WECHAT);
                paymentWechatCallback(paymentWechat,orderId,""+payType, returnMoney);
            }else if(actionName.equals(Contants.ACTION_NAME_PAYMENT_ALIPY)){
                PaymentAlipay paymentAlipay= (PaymentAlipay) intent.getSerializableExtra(Contants.KEY_OBJ_PAYMENT_ALIPY);
                new  PaymentUtil().requestGoodsValidateOrder(mContext,orderId, returnMoney);
            }else if(actionName.equals(Contants.ACTION_NAME_PAYMENT_SUCCESS)){
                new PopChufangyao(recyclerView,OrderCheckActivity.this,1);
            }else{
                LogUtil.y("error:异常广播"+actionName);
            }
        }
    }

    /**
     * 微信回调
     * @param paymentWechat
     */
    public void paymentWechatCallback(PaymentWechat paymentWechat,String orderId,String payType, String returnMoney) {
        int errCode=paymentWechat.getErrCode();
        int type=paymentWechat.getType();
        if(type== ConstantsAPI.COMMAND_PAY_BY_WX){
            switch (errCode){
                case Contants.INT_MINUS2://用户没有操作取消付款
                case Contants.INT_0:
                    if(paymentWechat.isWXAppInstalled()){//如果微信安装了情况下走二次验证
                       new  PaymentUtil().requestGoodsValidateOrder(mContext,orderId,returnMoney);
                    }else{
                        showToast("您还没有安装微信");
                    }
                    break;
            }
        }

    }
    public  boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    //******************************弹窗************************************************

    /**
     * 支付弹窗
     */
    private void showPayment(){

        initDialog();

        initDialogWidgets(dialogView);

        initDialogLinsteners();

        initDialogRecyclers();

//        //默认支付宝支付
        linAliPay.setSelected(true);

        //余额
        tvPaymentBalance.setText(Util.getFloatDotStr(String.valueOf(mBalancePrice)));

        //余额按钮
        balanceCheck.setColor(getResources().getColor(R.color.toggle_color_slelected), getResources().getColor(R.color.color_ffffff));

        balanceCheck.setOnStateChangedListener(new SwitchButtom.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchButtom view) {
                useBalance();
            }

            @Override
            public void toggleToOff(SwitchButtom view) {
                unUseBalance();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animationTop();
                    }
                },500);
            }
        });

        dialog.show();

    }

    /**
     * 使用余额支付
     */
    private void useBalance(){
        if (0.0 == mBalancePrice) {//账户余额为0
            balanceCheck.toggleSwitch(false);
            return;
        }

        if (mBalancePrice >= otcPrice) {
            payType = 3;
            balance = otcPrice;
            payPrice = 0d;
            setPayBtnSelected(false, false);
            setPayBtnClickable(false, false);

            tvPaymentBalance.setText(Util.getFloatDotStr(String.valueOf(mBalancePrice - otcPrice)));
        } else {
            payType = 0;
            balance = mBalancePrice;
            payPrice = otcPrice - balance;
            setPayBtnClickable(true, true);
            tvPaymentBalance.setText("0.00");
        }

        tvPayOnline.setText(Util.getFloatDotStr(String.valueOf(payPrice)));

        balanceCheck.toggleSwitch(true);
    }

    /**
     * 不使用余额
     */
    private void unUseBalance(){
        payType = 0;
        payPrice = otcPrice;
        setPayBtnClickable(true, true);
        if(payAli){
            linAliPay.setSelected(true);
        } else {
            linWeixin.setSelected(true);
        }
        tvPaymentBalance.setText(Util.getFloatDotStr(String.valueOf(mBalancePrice)));

        tvPayOnline.setText(Util.getFloatDotStr(String.valueOf(payPrice)));
        balanceCheck.toggleSwitch(false);
    }

    private void initDialog(){
        dialog = new Dialog(mContext, R.style.Theme_Light_Dialog);
        dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_order_check_pay,null);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_show_from_bottom);
        window.getDecorView().setPadding(0, 0, 0, 0);
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setContentView(dialogView);
    }

    private void initDialogWidgets(View dialogView){
        vShadeDialog = dialogView.findViewById(R.id.v_shade_dialog);
        imgCancel = (ImageView) dialogView.findViewById(R.id.img_cancel);
        tvPayOnline = (TextView) dialogView.findViewById(R.id.tv_pay_online);
        tvPayOnmoney = (TextView) dialogView.findViewById(R.id.tv_pay_onmoney);
        tvPaymentBalance = (TextView) dialogView.findViewById(R.id.payment_balance);
        linAliPay = (LinearLayout) dialogView.findViewById(R.id.lin_aliPay);
        linWeixin = (LinearLayout) dialogView.findViewById(R.id.lin_weixin);
        linPayOnline = (LinearLayout) dialogView.findViewById(R.id.lin_pay_online);
        linPayOnmoney = (LinearLayout) dialogView.findViewById(R.id.lin_pay_onmoney);
        linTopFloat = (LinearLayout) dialogView.findViewById(R.id.lin_top_float);
        linTopFloatOnMoney = (LinearLayout) dialogView.findViewById(R.id.lin_top_float_onmoney);
        linTopFloatOnLine = (LinearLayout) dialogView.findViewById(R.id.lin_top_float_online);
        imgPayOnline = (ImageView) dialogView.findViewById(R.id.img_pay_online);
        imgPayOnmoney = (ImageView) dialogView.findViewById(R.id.img_pay_onmoney);
        balanceCheck = (SwitchButtom) dialogView.findViewById(R.id.payment_balance_check);
        tvSubmitDialog = (TextView) dialogView.findViewById(R.id.tv_pay_submit);
        tvSubmitProgressDialog = (ProgressBar) dialogView.findViewById(R.id.tv_submit_progress_dialog);
        layoutSubmit = (LinearLayout) dialogView.findViewById(R.id.layout_submit);
        vOnMoney = dialogView.findViewById(R.id.v_pay_money);
        vOnLine = dialogView.findViewById(R.id.v_pay_line);
        vOnmoneyBottom = dialogView.findViewById(R.id.v_onmoney_bottom);
        vOnmoneyBottomLine = dialogView.findViewById(R.id.v_onmoney_bottom_line);
        vOnlineHeight = dialogView.findViewById(R.id.v_pay_line_height);

        recPayOnline = (RecyclerView) dialogView.findViewById(R.id.recycler_pay_online);
        recPayOnmoney = (RecyclerView) dialogView.findViewById(R.id.recycler_pay_onmoney);

        vDialog = dialogView.findViewById(R.id.v_dialog);

//        recPayOnline.setNestedScrollingEnabled(false);
//        recPayOnmoney.setNestedScrollingEnabled(false);

//        TopFloatScrollView tfScrollview = (TopFloatScrollView) dialogView.findViewById(R.id.tfScrollview);
//        tfScrollview.setOnScrollListener(this);
    }

    private void initDialogLinsteners(){
        imgCancel.setOnClickListener(dialogListener);
        linAliPay.setOnClickListener(dialogListener);
        linWeixin.setOnClickListener(dialogListener);
        linPayOnline.setOnClickListener(dialogListener);
        linPayOnmoney.setOnClickListener(dialogListener);
        layoutSubmit.setOnClickListener(dialogListener);
        vDialog.setOnClickListener(dialogListener);
        vShadeDialog.setOnClickListener(dialogListener);
    }

    private void initDialogRecyclers(){

        if(onMoney){
            linPayOnmoney.setVisibility(View.VISIBLE);
            vOnMoney.setVisibility(View.VISIBLE);
        } else {
            linPayOnmoney.setVisibility(View.GONE);
            vOnMoney.setVisibility(View.GONE);
        }

        //在线支付
        CustomLinearLayoutManager linOnline = new CustomLinearLayoutManager(mContext);
        mAdapterOnline = new OrderCheckPaymentAdapter(mContext, 1);
        recPayOnline.setLayoutManager(linOnline);
        recPayOnline.setAdapter(mAdapterOnline);
        mAdapterOnline.updata(mData);

        //货到付款
        CustomLinearLayoutManager linOnmoney = new CustomLinearLayoutManager(mContext);
        mAdapterOnmoney = new OrderCheckPaymentAdapter(mContext, 0);
        recPayOnmoney.setLayoutManager(linOnmoney);
        recPayOnmoney.setAdapter(mAdapterOnmoney);
        mAdapterOnmoney.updata(mData);
    }

    private void initPaymentDatas(){

        payAliWeixin = rxPrice;

        if(null == dialog) return;

        tvPayOnline.setText(Util.getFloatDotStr(String.valueOf(otcPrice)));
        tvPayOnmoney.setText(Util.getFloatDotStr(String.valueOf(rxPrice)));
    }

    View.OnClickListener dialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                //取消支付
                case R.id.img_cancel:
                    dismissDialog();

                    break;

                //支付宝支付
                case R.id.lin_aliPay:
                    setPayBtnSelected(true, false);
                    payAli = true;
                    break;

                //微信支付
                case R.id.lin_weixin:
                    setPayBtnSelected(false, true);
                    payAli = false;
                    break;

                //在线支付
                case R.id.lin_pay_online:
                    setPayOnlineType();
                    break;
                //线下支付
                case R.id.lin_pay_onmoney:
                    setPayOnmoneyType();
                    break;
                //确认支付
                case R.id.layout_submit:

                    requestEventCode("z003");

                    if (!NetworkUtil.CheckConnection(mContext)) {
                        showToast(getResources().getString(R.string.error_net_toast));

                        return;
                    }

                    if("提交中".equals(tvSubmitDialog.getText().toString())){
                        return;
                    }

                    checkSubmit();

                    if(payType==1){
                        if(!isWeixinAvilible(mContext)){
                            showToast("微信未安装或版本过低");
                            return;
                        }
                    }
                    vShade.setVisibility(View.VISIBLE);
                    vShadeDialog.setVisibility(View.VISIBLE);
                    tvSubmitDialog.setText("提交中");
                    tvSubmitProgressDialog.setVisibility(View.VISIBLE);
                    setPayType();

                    requestSubmit();
                    break;

                case R.id.v_dialog:
                    dismissDialog();

                    break;

                case R.id.v_shade_dialog:
                    break;
            }
        }
    };

    private void setPayOnlineType(){

        recPayOnline.setVisibility(imgFlagOnline ? View.VISIBLE : View.GONE);
        vOnLine.setVisibility(imgFlagOnline ? View.GONE : View.VISIBLE);

        imgPayOnline.setImageResource(imgFlagOnline ? R.mipmap.icon_forword_up_little : R.mipmap.icon_forword_down_little);

        if(View.GONE == linPayOnmoney.getVisibility()){
            vOnmoneyBottom.setVisibility(imgFlagOnline ? View.GONE : View.VISIBLE);
            vOnmoneyBottomLine.setVisibility(imgFlagOnline ? View.GONE : View.VISIBLE);
        }

        imgFlagOnline = !imgFlagOnline;
    }

    private void setPayOnmoneyType() {

        recPayOnmoney.setVisibility(imgFlagOnmoney ? View.VISIBLE : View.GONE);
        vOnMoney.setVisibility(imgFlagOnmoney ? View.GONE : View.VISIBLE);
        vOnmoneyBottom.setVisibility(imgFlagOnmoney ? View.GONE : View.VISIBLE);
        vOnmoneyBottomLine.setVisibility(imgFlagOnmoney ? View.GONE : View.VISIBLE);

        imgPayOnmoney.setImageResource(imgFlagOnmoney ? R.mipmap.icon_forword_up_little : R.mipmap.icon_forword_down_little);
        imgFlagOnmoney = !imgFlagOnmoney;
    }

    private String getGoodsInfoStr(){

        StringBuilder GoodsInfo = new StringBuilder();

        List<GoodsBean> dataRx = new ArrayList<>();
        List<GoodsBean> dataOtc = new ArrayList<>();
        for(ShopingCart cart : mData){
            for(GoodsBean bean : cart.getGoodsList()){
                if("1".equals(bean.getOnlinePay())){
                    dataOtc.add(bean);
                } else {
                    dataRx.add(bean);
                }
            }
        }

        for(int i = 0; i < dataRx.size(); i++){

            GoodsInfo.append("{");

            StringBuilder id = new StringBuilder();
            StringBuilder count = new StringBuilder();

            id.append("\"ID\":\""+dataRx.get(i).getDKID()+"\",");

            count.append("\"Count\":\""+String.valueOf(dataRx.get(i).getGoodsCount())+"\"");

            GoodsInfo.append(id);
            GoodsInfo.append(count);
            GoodsInfo.append("},");
        }

        for(int i = 0; i < dataOtc.size(); i++){

            GoodsInfo.append("{");

            StringBuilder id = new StringBuilder();
            StringBuilder count = new StringBuilder();

            id.append("\"ID\":\""+dataOtc.get(i).getDKID()+"\",");

            count.append("\"Count\":\""+String.valueOf(dataOtc.get(i).getGoodsCount())+"\"");

            GoodsInfo.append(id);
            GoodsInfo.append(count);
            GoodsInfo.append("},");
        }

        return "["+GoodsInfo.substring(0, GoodsInfo.length()-1)+"]";
    }

    private void setPayBtnClickable(boolean flagAli, boolean flagWX){
        linAliPay.setClickable(flagAli);
        linWeixin.setClickable(flagWX);
    }

    private void setPayBtnSelected(boolean flagAli, boolean flagWX){
        linAliPay.setSelected(flagAli);
        linWeixin.setSelected(flagWX);
    }

    private void dismissDialog(){
        if(null != dialog && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    //界面跳转回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            //地址信息的处理
            switch (requestCode){
                case REQUEST_CODE_ADDRESS_MANAGER://地址管理界面的回调
                    int emptyNum= data.getIntExtra(Contants.KEY_ADDRESS_EMPTY,Contants.INT_MINUS1);
                    if(emptyNum==1){//需要刷新数据
                        int flagAction=data.getIntExtra(Contants.KEY_ADDRESS_ACTION,Contants.INT_MINUS1);
                        onActivityResultAddressManager(flagAction,data);
                    }else{//需要清空数据
                        addressInfoData = null;
                        mAdapter.updataAddress(null);
                    }
                    break;
                case REQUEST_CODE_ADDRESS_BUILD://地址创建界面的回调
                    int buildStatus=data.getIntExtra(Contants.KEY_ADDRESS_ACTION,Contants.INT_MINUS1);
                    if(buildStatus==AddressBuildActivity.ADDRESS_BUILD_SUCCESS){//地址创建成功了，刷新数据吧
                        requestAddress();
                    }else{//地址创建失败，清空地址信息吧
                        mAdapter.updataAddress(null);
                    }
                    break;
            }
        }
    }

    /**
     * 地址管理界面动作
     * @param flagAction
     */
    private void onActivityResultAddressManager(int flagAction,Intent data) {
        switch (flagAction){
            case AddressManagerActivity.ACTION_NONE://没有操作
                break;
            case AddressManagerActivity.ACTION_CHOOSE://手动选择了地址
                addressInfoData= (AddressInfo) data.getSerializableExtra(Contants.KEY_ADDRESS_INFO);
                mAdapter.updataAddress(addressInfoData);
                break;
            case AddressManagerActivity.ACTION_DELETE://删除回掉
                String addresId = data.getStringExtra(Contants.KEY_ADDRESS_DELETE_ID);
                if(addressInfoData.getID().equals(addresId)){//删除了已有地址
                    requestAddress();
                }
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        try{
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
            switch (errorResponse.getWhat()) {
                case HttpConstant.BUY_CHECK:
                    if(errorResponse.isNetError()) {
                        showError();
                    } else {
                        showToast(errorResponse.getMsg());
                    }
                    break;
                case HttpConstant.BUY_SUBMIT_SHOP_CARD:
                    if(errorResponse.isNetError()) {
                        showToast(getResources().getString(R.string.error_net_toast));
                    } else {
                        vShade.setVisibility(View.GONE);
                        tvSubmitProgress.setVisibility(View.GONE);
                        tvSubmit.setText(submitStr);
//                        tvSubmit.setClickable(true);
//                        tvSubmit.setEnabled(true);
                        dismissDialog();
                        showToast(errorResponse.getMsg());
                    }
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        ActivityCollector.getInstance().removeActivityOrderAbout(this);
        ActivityCollector.getInstance().removeActivityOrder(this);
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }

    }

    private static final String TAG = OrderCheckActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

}

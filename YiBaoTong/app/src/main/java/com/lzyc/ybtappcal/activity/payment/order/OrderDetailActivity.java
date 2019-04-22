package com.lzyc.ybtappcal.activity.payment.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.OrderDetailAdapter;
import com.lzyc.ybtappcal.bean.AddressInfo;
import com.lzyc.ybtappcal.bean.GoodsBean;
import com.lzyc.ybtappcal.bean.MerchantInfo;
import com.lzyc.ybtappcal.bean.OrderInfo;
import com.lzyc.ybtappcal.bean.PaymentAlipay;
import com.lzyc.ybtappcal.bean.PaymentInfo;
import com.lzyc.ybtappcal.bean.PaymentWechat;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.Intents;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.PaymentUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.StringUtils;
import com.lzyc.ybtappcal.util.TimeUtil;
import com.lzyc.ybtappcal.view.NoShadowListView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.popupwindow.BasePopupWindow;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单详情等待付款，发货，收货
 * Created by yang on 2017/03/16.
 */
public class OrderDetailActivity extends BaseActivity {
    private static String TAG = OrderDetailActivity.class.getSimpleName();
    @BindView(R.id.listview)
    NoShadowListView mListView;
    @BindView(R.id.tv_kong)
    TextView tvKong;
    @BindView(R.id.tv_shi)
    TextView tvShi;
    @BindView(R.id.linear_operation)
    LinearLayout linearOperation;
    @BindView(R.id.tv_shadow)
    TextView tvShadow;
    @BindView(R.id.scroll_huitan)
    TwinklingRefreshLayout scrollHuitan;
    @BindView(R.id.lin_state)
    LinearLayout linState;
    @BindView(R.id.tv_state_wait)
    TextView tvStateWait;
    @BindView(R.id.iv_state_wait)
    ImageView imgStateWait;
    @BindView(R.id.iv_state)
    ImageView imgState;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.lin_return)
    LinearLayout linReturn;
    @BindView(R.id.lin_pay_real)
    LinearLayout linPayReal;
    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.tv_refund_time)
    TextView tvRefundTime;
    @BindView(R.id.tv_refund_txt)
    TextView tvRefundTxt;
    @BindView(R.id.tv_pay_money)
    TextView tvPayMoney;
    @BindView(R.id.tv_return_money)
    TextView tvReturnMoney;
    @BindView(R.id.tv_pay_type)
    TextView tvPayType;
    @BindView(R.id.tv_remaining_time)
    TextView tvRemainingTime;
    @BindString(R.string.prompt_confirm_goods)
    String promptConfirmGoods;

    private TextView headerAddressAccount, headerAddressPhone, headerAddressContent;//地址相关
    private TextView headerResName, headerResContectKefu;
    private ImageView headerResLogo;
    private TextView footerPayType;//支付方式
    private TextView footerPriceJinqian, footerPriceYunfei, footerPriceYuQian, footerPriceJiaQian, footerOrderId;
    private TextView footerPriceJiaQianDesc;
    private TextView footerCreateTime;
    private LinearLayout footerLinearYuqian;
    private String orderId;
    private BasePopupWindow popfukuan;
    private OrderInfo orderInfo;
    private AddressInfo addressInfo;
    //    private OrderDrugDetail orderDrugDetail;
    private PaymentBroadcastReceiver receiver = new PaymentBroadcastReceiver();
    private Intent intent;
    private int typePage;
    private Bundle mBundle;
    private String payType;
    private String returnMoney;
    private List<GoodsBean> listGoods;
    private OrderDetailAdapter mAdapter;
    private MerchantInfo merchantInfo;

    private static final int WHAT = 101;
    private Timer mTimer;
    private TimerTask mTimerTask;

    private long MAX_TIME = 86400000;
    private long curTime = 0;

    private boolean isRefresh;

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void init() {
        setTitleName(Contants.STR_TITLE_ORDER_DETAIL);
        setPageStateView();
        showLoading();
        scrollHuitan.setPureScrollModeOn();
        intData();
        registerPaymentBroadcastReceiver();
        listGoods = new ArrayList<>();
        View headerView = View.inflate(this, R.layout.header_order_detail, null);
        initHeaderView(headerView);
        mListView.addHeaderView(headerView, null, false);
        View footerView = View.inflate(this, R.layout.footer_order_detail, null);
        initFooterView(footerView);
        mListView.addFooterView(footerView, null, false);
        mAdapter = new OrderDetailAdapter(this, R.layout.item_order_detail, listGoods);
        mListView.setAdapter(mAdapter);
        requestOrderDetail();
    }

    private void initFooterView(View footerView) {
        footerPayType = (TextView) footerView.findViewById(R.id.order_detail_paytype);
        footerPriceJinqian = (TextView) footerView.findViewById(R.id.order_detail_price_balance);
        footerPriceYunfei = (TextView) footerView.findViewById(R.id.order_detail_price_yunfei);
        footerPriceYuQian = (TextView) footerView.findViewById(R.id.order_detail_yuqian);
        footerPriceJiaQian = (TextView) footerView.findViewById(R.id.order_detail_price_zongjia);
        footerOrderId = (TextView) footerView.findViewById(R.id.order_detail_id);
        footerPriceJiaQianDesc = (TextView) footerView.findViewById(R.id.order_detail_price_desc);
        footerLinearYuqian = (LinearLayout) footerView.findViewById(R.id.order_detail_linear_yuqian);
        footerCreateTime = (TextView) footerView.findViewById(R.id.order_detail_createtime);
    }

    @OnClick({R.id.ib_left, R.id.tv_kong, R.id.tv_shi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_left:
                closeActivity();
                break;
            case R.id.tv_kong:
                clickEventActionKong();
                break;
            case R.id.tv_shi:
                clickEventActionShi();
                break;
        }
    }

    /**
     * @param headerView
     */
    private void initHeaderView(View headerView) {
        headerAddressAccount = (TextView) headerView.findViewById(R.id.order_detail_account);
        headerAddressPhone = (TextView) headerView.findViewById(R.id.order_detail_phone);
        headerAddressContent = (TextView) headerView.findViewById(R.id.order_detail_address);
        headerResLogo = (ImageView) headerView.findViewById(R.id.res_merchant_logo);
        headerResName = (TextView) headerView.findViewById(R.id.res_merchant_name);
        headerResContectKefu = (TextView) headerView.findViewById(R.id.contact_kefu);
        headerResContectKefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestEventCode("a-3001");//联系客服
                popupCall();
            }
        });
    }

    /**
     * 联系客服
     */
    private void popupCall() {
        String phone = StringUtils.getSubsectionPhone(merchantInfo.getPhone());
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTv_content().setText(phone);
        twoButton.getTvOk().setText("呼叫");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + merchantInfo.getPhone());
                intent.setData(data);
                startActivity(intent);
                twoButton.dismiss();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(mListView, Gravity.CENTER);
    }

    private void intData() {
        try {
            intent = getIntent();
            mBundle = intent.getExtras();
            orderInfo = (OrderInfo) mBundle.getSerializable(Contants.KEY_OBJ_ORDERINFO);
            if (orderInfo != null) {
                orderId = orderInfo.getOrderID();
            } else {
                orderId = mBundle.getString(Contants.KEY_ORDER_ID);
                String operation = mBundle.getString("operation");
                if (Contants.STR_1.equals(operation)) {
                    linearOperation.setVisibility(View.GONE);
                    tvShadow.setVisibility(View.GONE);
                } else {
                    linearOperation.setVisibility(View.VISIBLE);
                    tvShadow.setVisibility(View.VISIBLE);
                }
            }
            typePage = mBundle.getInt(Contants.KEY_PAGE_PAYMENT);
        } catch (Exception e) {
            LogUtil.e(TAG, "error:" + e.getMessage());
            LogUtil.e(TAG, e.getMessage(), e);
        }
    }

    private void closeActivity() {
        LogUtil.y("##detail#####state#######"+orderInfo.getState());
        if(isRefresh){
            setResult(RESULT_OK,intent);
        }
        OrderDetailActivity.this.finish();
}

    /**
     * 实心按钮点击事件
     */
    private void clickEventActionShi() {
        int state = Integer.parseInt(orderInfo.getState());
        if (state == 4) {//确认收货
            requestEventCode("a-3007");
            Bundle bundle = new Bundle();
            bundle.putString("money", orderInfo.getReturnMoney());
            requestOrderQueRenShouHuo(orderId);
        } else if (state == 3) {
            showPrompt(tvShi, promptConfirmGoods);
        } else {//立即付款
            requestEventCode("a-3003");
            popoupwindowFukuan(orderInfo);
        }
    }

    /**
     * 空心按钮点击事件
     */
    private void clickEventActionKong() {
        int state = Integer.parseInt(orderInfo.getState());
        String type = orderInfo.getType();
        Bundle mBundle = new Bundle();
        switch (state) {
            case 1://取消订单
                if (!type.equals(Contants.DRUG_TYPE_RX)) {
                    popWindowOderCancel(orderId);
                    requestEventCode("a-3004");
                } else {
                    showToast("已提醒商家发货");
                    requestEventCode("a-3002"); //提醒发货
                }
                break;
            case 2://提醒发货
                showToast("已提醒商家发货");
                requestEventCode("a-3002");//提醒发货
                break;
            case 3://查看物流
//                orderInfo.setLogistics();
//                orderInfo.setLogisticsCom();
//                orderInfo.setLogisticsCode();
                requestEventCode("a-3005");
                mBundle.putSerializable(Contants.KEY_OBJ_ORDERINFO, orderInfo);
                Intents.openLogisticsActivity(OrderDetailActivity.this, mBundle);
//                popoupwindowLogistics();
                break;
            case 4://查看物流
                requestEventCode("a-3005");
                mBundle.putSerializable(Contants.KEY_OBJ_ORDERINFO, orderInfo);
                Intents.openLogisticsActivity(OrderDetailActivity.this, mBundle);
//                popoupwindowLogistics();
                break;
            default:
                requestEventCode("a-3008");
                popWindowOderDelete(orderId);
                break;
        }

    }

    /**
     * 删除订单
     */
    private void requestOrderDelete(String orderId) {
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_GOODS_ORDER_DELETE_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_GOODS_ORDER_DELETE_SIGN);
        params.put(HttpConstant.PARAM_KEY_ORDERID, orderId);
        request(params, HttpConstant.ORDER_MINE_DELETE);
    }


//关键字弹窗
//
//    /**
//     * 物流信息弹窗
//     */
    /*private void popoupwindowLogistics() {
        int w = ScreenUtils.getScreenWidth() - DensityUtils.dp2px(80);
        final BasePopupWindow popupLogistics = new BasePopupWindow(this, R.layout.popup_logistics, w, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView pop_logistics_copy = (TextView) popupLogistics.getConentView().findViewById(R.id.pop_logistics_copy);
        TextView pop_wuliu_company = (TextView) popupLogistics.getConentView().findViewById(R.id.pop_wuliu_company);
        final TextView pop_logistics_num = (TextView) popupLogistics.getConentView().findViewById(R.id.pop_logistics_num);
        pop_wuliu_company.setText("" + orderInfo.getLogisticsCom());
        pop_logistics_num.setText("" + orderInfo.getLogistics());
        pop_logistics_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.copy(OrderDetailActivity.this, pop_logistics_num.getText().toString());
                showToast("复制成功");
                popupLogistics.dismiss();
            }
        });

        View viewout1 = popupLogistics.getConentView().findViewById(R.id.viewout1);
        View viewout2 = popupLogistics.getConentView().findViewById(R.id.viewout2);
        viewout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLogistics.dismiss();
            }
        });

        viewout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLogistics.dismiss();
            }
        });
        popupLogistics.showPopupWindow(order_detail_linear, Gravity.CENTER);
    }*/

    /**
     * 支付弹窗
     */
    private void popoupwindowFukuan(final OrderInfo orderInfo) {
        popfukuan = new BasePopupWindow(this, R.layout.pop_payment_method, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View contentView = popfukuan.getContentView();
        final LinearLayout payment_check_weixin = (LinearLayout) contentView.findViewById(R.id.pop_payment_weixin);
        final LinearLayout payment_check_zhifubao = (LinearLayout) contentView.findViewById(R.id.pop_payment_zhifubao);
        final TextView pop_payment_tv_weixin = (TextView) contentView.findViewById(R.id.pop_payment_tv_wechat);
        final TextView pop_payment_tv_zhifunbao = (TextView) contentView.findViewById(R.id.pop_payment_tv_zhifubao);
        TextView pop_payment_price = (TextView) contentView.findViewById(R.id.pop_payment_price);
        pop_payment_price.setText(orderInfo.getPayPrice());
        orderId = orderInfo.getOrderID();
        payType = orderInfo.getPayType();
        returnMoney = orderInfo.getReturnMoney();
        if (payType.equals(Contants.STR_1)) {
            pop_payment_tv_weixin.setSelected(true);
            pop_payment_tv_zhifunbao.setSelected(false);
        } else {
            pop_payment_tv_weixin.setSelected(false);
            pop_payment_tv_zhifunbao.setSelected(true);
        }
        final PaymentInfo paymentInfo = getPaymentInfo(orderInfo);
        TextView pop_payment_fukuan = (TextView) contentView.findViewById(R.id.pop_payment_fukuan);
        payment_check_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_payment_tv_weixin.setSelected(true);
                pop_payment_tv_zhifunbao.setSelected(false);
            }
        });

        payment_check_zhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_payment_tv_weixin.setSelected(false);
                pop_payment_tv_zhifunbao.setSelected(true);

            }
        });
        popfukuan.getConentView().findViewById(R.id.pop_payment_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popfukuan.dismiss();
                popfukuan = null;
            }
        });
        popfukuan.getConentView().findViewById(R.id.view_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popfukuan.dismiss();
                popfukuan = null;
            }
        });
        pop_payment_fukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePreferenceUtil.put(OrderDetailActivity.this, Contants.KEY_PAGE_PAYMENT, Contants.VAL_PAGE_PAYMENT_DETAIL);
                int payType;
                LogUtil.e(TAG, new Gson().toJson(orderInfo));
                if (pop_payment_tv_weixin.isSelected()) {
                    if (!isWeixinAvilible(OrderDetailActivity.this)) {
                        showToast(Contants.STR_WECHAT_INSTALLED);
                        return;
                    }
                    new  PaymentUtil().paymentWeChat(OrderDetailActivity.this, paymentInfo);
                    payType = 1;
                    if (curTime != 0) {
                        curTime = 0;
                        mTimer.cancel();
                    }
                } else {
                    new  PaymentUtil().paymentAlipay(OrderDetailActivity.this, paymentInfo);
                    payType = 2;
                    if (curTime != 0) {
                        curTime = 0;
                        mTimer.cancel();
                    }
                }
                popfukuan.dismiss();
                popfukuan = null;
                requestFukuanFangshi(payType, orderId);
            }
        });
        popfukuan.showPopupWindowTop(mListView, Gravity.BOTTOM);
    }

//    /**
//     * 新加的几个字段数据，
//     */
//    private void orderInfoIncrease() {
//        orderInfo.setGoodsName(listGoods.get(0).getGoodsName());
//        orderInfo.setName(listGoods.get(0).getName());
//        orderInfo.setCount(listGoods.get(0).getCount());
//        orderInfo.setImage(listGoods.get(0).getImage());
//        orderInfo.setOrderID(orderId);
//    }

    public boolean isWeixinAvilible(Context context) {
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


    /**
     * 立即付款
     * 支付方式修改
     *
     * @param payType
     * @param orderID
     */
    private void requestFukuanFangshi(int payType, String orderID) {
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_GOODS_ORDER_UPDATE_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_GOODS_ORDER_UPDATE_SIGN);
        params.put(HttpConstant.PARAM_KEY_ORDERID, "" + orderID);
        params.put(HttpConstant.PARAM_KEY_PAYTYPE, "" + payType);
        if (!NetworkUtil.CheckConnection(mContext)) {
            showToast("网络不给力");
            return;
        }
        request(params, HttpConstant.ORDER_PAYMENT_UPDATE);
    }

//网络请求

    /**
     * 确认收货
     */
    private void requestOrderQueRenShouHuo(String orderId) {
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_GOODS_ORDER_COMPLETE_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_GOODS_ORDER_COMPLETE_SIGN);
        params.put(HttpConstant.PARAM_KEY_ORDERID, orderId);
        request(params, HttpConstant.ORDER_PAYMENT_QUERENSHOUHUO);
    }

    /**
     * 获取订单详情
     */
    private void requestOrderDetail() {
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_GOODS_ORDER_DETAIL_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_GOODS_ORDER_DETAIL_SIGN);
        params.put(HttpConstant.PARAM_KEY_ORDERID, orderId);
        request(params, HttpConstant.ORDER_MINE_DETAIL);
    }

    /**
     * 取消订单
     */
    private void requestOrderCancel(String orderId) {
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_GOODS_ORDER_CANCEL_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_GOODS_ORDER_CANCEL_SIGN);
        params.put(HttpConstant.PARAM_KEY_ORDERID, orderId);
        request(params, HttpConstant.ORDER_MINE_CANCEL);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.ORDER_MINE_DETAIL:
                try {
                    JSONObject dataJo = response.getJSONObject(HttpConstant.DATA);
                    JSONObject orderInfoJo = dataJo.getJSONObject(HttpConstant.ORDER_INFO);
                    JSONObject addressInfoJo = dataJo.getJSONObject(HttpConstant.ADDRESS_INFO);
                    JSONObject merchantInfoJo = dataJo.getJSONObject(HttpConstant.MERCHANT_INFO);
//                    JSONObject arderDetailJo = dataJo.getJSONObject("OrderDetail");
                    orderInfo = JsonUtil.getModle(orderInfoJo.toString(), OrderInfo.class);
                    addressInfo = JsonUtil.getModle(addressInfoJo.toString(), AddressInfo.class);
                    merchantInfo = JsonUtil.getModle(merchantInfoJo.toString(), MerchantInfo.class);
//                    orderDrugDetail = JsonUtil.getModle(arderDetailJo.toString(), OrderDrugDetail.class);
                    if (orderInfo == null || addressInfo == null) {
                        showEmpty(errorServer, R.mipmap.empty_error_net_server);
                    }
                    showContent();
                    Type type = new TypeToken<ArrayList<GoodsBean>>() {
                    }.getType();
                    listGoods = JsonUtil.getListModle(dataJo.toString(), HttpConstant.GOODSLIST, type);
                    mAdapter.setList(listGoods);
                    orderInfo.setAddressRegion(addressInfo.getAddressRegion());//不能删！！！  物流界面要用这俩地址！！
                    orderInfo.setAddressDetail(addressInfo.getAddressDetail());//不能注释！！！ lxx
                    fullContent();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showEmpty(errorServer, R.mipmap.empty_error_net_server);
                } catch (Exception e) {
                    showEmpty(errorServer, R.mipmap.empty_error_net_server);
                }
                break;
            case HttpConstant.ORDER_MINE_CANCEL:
                requestOrderDetail();
                onCancelOrder();
                linPayReal.setVisibility(View.GONE);
                linReturn.setVisibility(View.GONE);
                break;
            case HttpConstant.ORDER_MINE_DELETE:
                isRefresh = true;
                closeActivity();
                break;
            case HttpConstant.ORDER_PAYMENT_QUERENSHOUHUO:
                requestOrderDetail();
                isRefresh = true;
                break;
            case HttpConstant.BUY_SUBMIT_SECOND:
                LogUtil.d("tag", "##json##" + response.toString());
                break;
            default:
                break;

        }
    }

    private void fullContent() {
        headerAddressContent.setText(addressInfo.getAddressRegion() + addressInfo.getAddressDetail());
        headerAddressAccount.setText(addressInfo.getName());
        headerAddressPhone.setText(addressInfo.getPhone());
        footerOrderId.setText("订单编号：" + orderInfo.getOrderID());
        footerCreateTime.setText("下单时间：" + orderInfo.getCreateTime());
        payType = orderInfo.getPayType();
        if (payType.equals("1")) {
            footerPayType.setText("支付方式：微信支付");
        } else if (payType.equals("2")) {
            footerPayType.setText("支付方式：支付宝支付");
        } else if (payType.equals("3")) {
            footerPayType.setText("支付方式：余额支付");
        }
        double zongjia = Double.parseDouble(orderInfo.getTotalPrice()) + Double.parseDouble(orderInfo.getFreight());
        footerPriceYunfei.setText("+" + orderInfo.getFreight());
//        StringUtils.getReturnMoney()
        footerPriceJinqian.setText(orderInfo.getTotalPrice());
        tvReturnMoney.setText(orderInfo.getReturnMoney());
        final int state = Integer.parseInt(orderInfo.getState());
        String type = orderInfo.getType();
        if (type.equals("2")) {
//            footerPayType.setVisibility(View.GONE);
//            footerLinearYuqian.setVisibility(View.GONE);
        } else {
            footerPriceYuQian.setText("-" + orderInfo.getBalance());
//            footerLinearYuqian.setVisibility(View.VISIBLE);
//            footerPayType.setVisibility(View.VISIBLE);
        }
        switch (state) {
            case 1:
                if (type.equals("1")) {
                    Picasso.with(mContext).load(R.mipmap.icon_order_detail_wait_pay).placeholder(R.mipmap.image_empty_minimum).error(R.mipmap.image_empty_minimum).into(imgState);
                    tvState.setText("等待付款");
                    tvKong.setText("取消订单");
                    tvPayType.setText("需付款 ");
                    footerPriceJiaQianDesc.setText(tvPayType.getText().toString());
                    tvPayMoney.setText("" + orderInfo.getPayPrice());
                    footerPriceJiaQian.setText("" + orderInfo.getPayPrice());
                    footerPayType.setVisibility(View.GONE);

                    linState.setVisibility(View.GONE);
                    tvStateWait.setVisibility(View.VISIBLE);
                    imgStateWait.setVisibility(View.VISIBLE);
                    tvRemainingTime.setVisibility(View.VISIBLE);

                    MAX_TIME -= initDate(orderInfo.getCreateTime(), TimeUtil.getCurrentTimeInString());
                    initTimer();

                    mTimer.schedule(mTimerTask, 0, 1000);
                } else {
                    Picasso.with(mContext).load(R.mipmap.icon_order_detail_wait_fahuo).placeholder(R.mipmap.image_empty_minimum).error(R.mipmap.image_empty_minimum).into(imgState);
                    tvState.setText("等待发货");
                    tvShi.setVisibility(View.GONE);
                    tvKong.setText("提醒发货");
                    tvPayType.setText("实付款 ");
                    footerPriceJiaQianDesc.setText(tvPayType.getText().toString());
                    linState.setVisibility(View.VISIBLE);
                    tvStateWait.setVisibility(View.GONE);
                    imgStateWait.setVisibility(View.GONE);
                    tvRemainingTime.setVisibility(View.GONE);
                    footerPayType.setVisibility(View.VISIBLE);
                    tvPayMoney.setText("" + new DecimalFormat("0.00").format(zongjia));
                    footerPriceJiaQian.setText("" + new DecimalFormat("0.00").format(zongjia));
                }
                break;
            case 2:
                if (type.equals("2")) {
                    tvPayMoney.setText("" + new DecimalFormat("0.00").format(zongjia));
                    footerPriceJiaQian.setText("" + new DecimalFormat("0.00").format(zongjia));
                } else {
                    tvPayMoney.setText("" + orderInfo.getPayPrice());
                    footerPriceJiaQian.setText("" + orderInfo.getPayPrice());
                }
                Picasso.with(mContext).load(R.mipmap.icon_order_detail_wait_fahuo).placeholder(R.mipmap.image_empty_minimum).error(R.mipmap.image_empty_minimum).into(imgState);
                tvState.setText("等待发货");
                tvShi.setVisibility(View.GONE);
                tvKong.setText("提醒发货");
                tvPayType.setText("实付款 ");
                footerPriceJiaQianDesc.setText(tvPayType.getText().toString());
                linState.setVisibility(View.VISIBLE);
                tvStateWait.setVisibility(View.GONE);
                imgStateWait.setVisibility(View.GONE);
                tvRemainingTime.setVisibility(View.GONE);
                footerPayType.setVisibility(View.VISIBLE);
                break;
            case 3:
                if (type.equals("2")) {
                    tvPayMoney.setText("" + new DecimalFormat("0.00").format(zongjia));
                    footerPriceJiaQian.setText("" + new DecimalFormat("0.00").format(zongjia));
                } else {
                    tvPayMoney.setText("" + orderInfo.getPayPrice());
                    footerPriceJiaQian.setText("" + orderInfo.getPayPrice());
                }
                Picasso.with(mContext).load(R.mipmap.icon_order_detail_wait_fahuo).placeholder(R.mipmap.image_empty_minimum).error(R.mipmap.image_empty_minimum).into(imgState);
                tvState.setText("等待收货");
                tvKong.setText("查看物流");
                tvShi.setText("确认收货");
                tvPayType.setText("实付款 ");
                footerPriceJiaQianDesc.setText(tvPayType.getText().toString());
                tvShi.setBackgroundResource(R.drawable.shape_order_shouhuo);
                break;
            case 4:
                if (type.equals("2")) {
                    tvPayMoney.setText("" + new DecimalFormat("0.00").format(zongjia));
                    footerPriceJiaQian.setText("" + new DecimalFormat("0.00").format(zongjia));
                } else {
                    tvPayMoney.setText("" + orderInfo.getPayPrice());
                    footerPriceJiaQian.setText("" + orderInfo.getPayPrice());
                }
                Picasso.with(mContext).load(R.mipmap.icon_order_detail_wait_fahuo).placeholder(R.mipmap.image_empty_minimum).error(R.mipmap.image_empty_minimum).into(imgState);
                tvState.setText("等待收货");
                tvKong.setText("查看物流");
                tvShi.setText("确认收货");
                tvPayType.setText("实付款 ");
                footerPriceJiaQianDesc.setText(tvPayType.getText().toString());
                tvShi.setBackgroundResource(R.drawable.shape_order_zhifu);
                break;
            case 5:
                if (type.equals("2")) {
                    tvPayMoney.setText("" + new DecimalFormat("0.00").format(zongjia));
                    footerPriceJiaQian.setText("" + new DecimalFormat("0.00").format(zongjia));
                } else {
                    tvPayMoney.setText("" + orderInfo.getPayPrice());
                    footerPriceJiaQian.setText("" + orderInfo.getPayPrice());
                }
                Picasso.with(mContext).load(R.mipmap.icon_order_detail_finished).placeholder(R.mipmap.image_empty_minimum).error(R.mipmap.image_empty_minimum).into(imgState);
                tvState.setText("已完成");
                tvKong.setText("删除订单");
                tvPayType.setText("实付款 ");
                footerPriceJiaQianDesc.setText(tvPayType.getText().toString());
                tvShi.setVisibility(View.GONE);
                break;
            case 6:
                if (type.equals("2")) {
                    tvPayMoney.setText("" + new DecimalFormat("0.00").format(zongjia));
                    footerPriceJiaQian.setText("" + new DecimalFormat("0.00").format(zongjia));
                } else {
                    tvPayMoney.setText("" + orderInfo.getPayPrice());
                    footerPriceJiaQian.setText("" + orderInfo.getPayPrice());
                }
                Picasso.with(mContext).load(R.mipmap.icon_order_detail_return_money).placeholder(R.mipmap.image_empty_minimum).error(R.mipmap.image_empty_minimum).into(imgState);
                tvState.setText("退款完成");
                tvKong.setText("删除订单");
                tvShi.setVisibility(View.GONE);

                linPayReal.setVisibility(View.GONE);
                linReturn.setVisibility(View.GONE);
                tvRefundTxt.setVisibility(View.VISIBLE);
                tvRefundTime.setVisibility(View.VISIBLE);
                tvRefundTime.setText(orderInfo.getRefundTime());
                break;
            default:
                if (type.equals("2")) {
                    tvPayMoney.setText("" + new DecimalFormat("0.00").format(zongjia));
                    footerPriceJiaQian.setText("" + new DecimalFormat("0.00").format(zongjia));
                } else {
                    tvPayMoney.setText("" + orderInfo.getPayPrice());
                    footerPriceJiaQian.setText("" + orderInfo.getPayPrice());
                }
                Picasso.with(mContext).load(R.mipmap.icon_order_detail_closed).placeholder(R.mipmap.image_empty_minimum).error(R.mipmap.image_empty_minimum).into(imgState);
                tvState.setText("交易关闭");
                tvKong.setText("删除订单");
                tvShi.setVisibility(View.GONE);
                linPayReal.setVisibility(View.GONE);
                linReturn.setVisibility(View.GONE);
                tvClose.setVisibility(View.VISIBLE);
                tvClose.setText(orderInfo.getCancelMsg());
                break;
        }
        Picasso.with(mContext).load(merchantInfo.getLogo()).error(R.mipmap.empty_logo).placeholder(R.mipmap.empty_logo).into(headerResLogo);
        headerResName.setText(merchantInfo.getName());
    }

    private void registerPaymentBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contants.ACTION_NAME_PAYMENT_WECHAT);
        intentFilter.addAction(Contants.ACTION_NAME_PAYMENT_ALIPY);
        intentFilter.addAction(Contants.ACTION_NAME_PAYMENT_ERROR);
        registerReceiver(receiver, intentFilter);
    }

    private class PaymentBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String actionName = intent.getAction();
            if (actionName.equals(Contants.ACTION_NAME_PAYMENT_WECHAT)) {
                PaymentWechat paymentWechat = (PaymentWechat) intent.getSerializableExtra(Contants.KEY_OBJ_PAYMENT_WECHAT);
                paymentWechatCallback(paymentWechat, orderId, payType, returnMoney);
//                LogUtil.y(TAG, "微信回调成功" + paymentWechat.toString());
                requestOrderDetail();
                isRefresh = true;

            } else if (actionName.equals(Contants.ACTION_NAME_PAYMENT_ALIPY)) {
                PaymentAlipay paymentAlipay = (PaymentAlipay) intent.getSerializableExtra(Contants.KEY_OBJ_PAYMENT_ALIPY);
//                PaymentUtil.getInstance().requestGoodsValidateOrder(OrderDetailActivity.this, orderId, payType);
//                LogUtil.e(TAG, "支付宝支付回调" + paymentAlipay.toString());
                requestOrderDetail();
                isRefresh = true;
            } else if (actionName.equals(Contants.ACTION_NAME_PAYMENT_ERROR)) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }


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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        destroyTimer();
        if (mHandler != null) {
            mHandler.removeMessages(WHAT);
            mHandler = null;
        }

        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    /**
     * 是否取消的弹窗
     */
    private void popWindowOderCancel(final String orderId) {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText("确认取消订单？");
        twoButton.getTvOk().setText("确认");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOrderCancel(orderId);
                twoButton.dismiss();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(mListView, Gravity.CENTER);
    }

    /**
     * 是否删除的弹窗
     */
    private void popWindowOderDelete(final String orderId) {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText("确认删除订单？");
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTvOk().setText("确认");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOrderDelete(orderId);
                twoButton.dismiss();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(mListView, Gravity.CENTER);
    }

    /**
     * 微信回调
     *
     * @param paymentWechat
     */
    public void paymentWechatCallback(PaymentWechat paymentWechat, String orderId, String payType, String returnMoney) {
        int errCode = paymentWechat.getErrCode();
        int type = paymentWechat.getType();
        if (type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (errCode) {
                case Contants.INT_MINUS2://用户没有操作取消付款
                case Contants.INT_0:
                    if (paymentWechat.isWXAppInstalled()) {//如果微信安装了情况下走二次验证
                        new  PaymentUtil().requestGoodsValidateOrder(OrderDetailActivity.this, orderId, returnMoney);
                    } else {
                        showToast("您还没有安装微信");
                    }
                    break;
            }
        }
    }

    private long initDate(String startTime, String endTime) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long between = 0;
        try {
            java.util.Date begin = dfs.parse(startTime);
            java.util.Date end = dfs.parse(endTime);
            between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
            return between;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * 初始化Timer
     */
    public void initTimer() {
        try{
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (curTime == 0) {
                        curTime = MAX_TIME;
                    } else {
                        curTime -= 1000;
                    }
                    Message message = new Message();
                    message.what = WHAT;
                    message.obj = curTime;
                    mHandler.sendMessage(message);
                }
            };
            mTimer = new Timer();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * destory上次使用的
     */
    public void destroyTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT:
                    long sRecLen = (long) msg.obj;
                    long millisecond = sRecLen - TimeZone.getDefault().getRawOffset();
                    Date date = new Date(millisecond);
                    SimpleDateFormat format = new SimpleDateFormat("HH时mm分ss秒");

                    tvRemainingTime.setText("剩余："+format.format(date));

                    if (sRecLen <= 0) {
                        mTimer.cancel();
                        curTime = 0;
                        requestOrderCancel(orderId);
                    }
                    break;
            }
        }
    };

    private void onCancelOrder(){
        Picasso.with(mContext).load(R.mipmap.icon_order_detail_closed).placeholder(R.mipmap.image_empty_minimum).error(R.mipmap.image_empty_minimum).into(imgState);
        tvStateWait.setVisibility(View.GONE);
        imgStateWait.setVisibility(View.GONE);
        tvRemainingTime.setVisibility(View.GONE);
        linState.setVisibility(View.VISIBLE);
        tvState.setText("交易关闭");
        tvKong.setText("删除订单");
        footerPriceJiaQianDesc.setText("实付款 ");
        tvShi.setVisibility(View.GONE);
        tvClose.setVisibility(View.VISIBLE);
        tvClose.setText(orderInfo.getCancelMsg());
        isRefresh = true;
    }

    /**
     * 获取支付相关的信息
     *
     * @return
     */
    public PaymentInfo getPaymentInfo(OrderInfo orderInfo) {
        PaymentInfo paymentInfo = new PaymentInfo();
        String name;
        String goodsName;

        if (listGoods.size() == 1) {
            name = listGoods.get(0).getName();
            goodsName = listGoods.get(0).getGoodsName();
        } else {
            name = listGoods.get(0).getName() + "等";
            goodsName = "";
        }
        paymentInfo.setName(name);
        paymentInfo.setGoodsName(goodsName);
        paymentInfo.setPrice(orderInfo.getPayPrice());
        paymentInfo.setOrderId(orderInfo.getOrderID());
        return paymentInfo;
    }

    @Override
    public void error(String errorMsg) {
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
            if (errorResponse.isNetError()) {
                showError();
            }
        } catch (Exception e) {
            showEmpty(errorServer, R.mipmap.empty_error_net_server);
        }
    }

}

package com.lzyc.ybtappcal.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseFragment;
import com.lzyc.ybtappcal.activity.payment.order.OrderDetailActivity;
import com.lzyc.ybtappcal.adapter.OrderMineAdapter;
import com.lzyc.ybtappcal.bean.OrderInfo;
import com.lzyc.ybtappcal.bean.PaymentInfo;
import com.lzyc.ybtappcal.bean.PaymentWechat;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.Intents;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.PaymentUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.popupwindow.BasePopupWindow;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;

/**
 * 我的订单
 * Created by yang on 2017/04/27.
 */
public class OrderMineFragment extends BaseFragment implements OrderMineAdapter.OnChildClikListener {
    private static final String TAG = OrderMineFragment.class.getSimpleName();
    @BindView(R.id.listview)
    XYbtRefreshListView mListView;
    @BindString(R.string.empty_ordermine)
    String emptyOrderMine;
    @BindString(R.string.prompt_confirm_goods)
    String promptConfirmGoods;
    private String orderId;
    private int tabValue = 0;
    private int pageIndex = 0;
    private List<OrderInfo> listOrder;
    private OrderMineAdapter mAdapter;
    private View footerView;
    private LinearLayout footer_ybtlv_linear;
    private BasePopupWindow popfukuan;
    private String payType;
    private String returnMoney;
    private PaymentBroadcastReceiver receiver = new PaymentBroadcastReceiver();

    public static OrderMineFragment getInstance(String orderid) {
        OrderMineFragment orderMineFragment = new OrderMineFragment();
        orderMineFragment.orderId = orderid;
        return orderMineFragment;
    }

    public static OrderMineFragment getInstance(String orderid, int tabValue) {
        OrderMineFragment orderMineFragment = new OrderMineFragment();
        orderMineFragment.orderId = orderid;
        orderMineFragment.tabValue = tabValue;
        return orderMineFragment;
    }

    public void onRefresh() {
        mAdapter = new OrderMineAdapter(mContext, R.layout.item_order_mine, listOrder);
        mAdapter.setOnChildClikListener(this);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAdapter(mAdapter);
        footer_ybtlv_linear.setVisibility(View.GONE);
        pageIndex = 0;
        requestOrderList();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_order_mine;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG + tabValue);
//        onRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG + tabValue);
    }

    @Override
    protected void init() {
        registerPaymentBroadcastReceiver();
        setPageStateView();
        showLoading();
        listOrder = new ArrayList<>();
        mAdapter = new OrderMineAdapter(mContext, R.layout.item_order_mine, listOrder);
        mAdapter.setOnChildClikListener(this);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAdapter(mAdapter);
        footerView = View.inflate(mContext, R.layout.ybt_listview_footer_complete, null);
        footer_ybtlv_linear = (LinearLayout) footerView.findViewById(R.id.footer_ybtlv_linear);
        mListView.addFooterView(footerView, null, false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                OrderInfo orderInfo = (OrderInfo) adapterView.getItemAtPosition(position);
                if (orderInfo != null) {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    Bundle mBundle= new Bundle();
                    mBundle.putSerializable(Contants.KEY_OBJ_ORDERINFO, orderInfo);
                    mBundle.putInt(Contants.KEY_PAGE_PAYMENT, Contants.VAL_PAGE_PAYMENT_MINE);
                    mBundle.putInt(Contants.KEY_POSITION, position-1);
                    intent.putExtras(mBundle);
                    startActivityForResult(intent, 1);
                }

            }
        });
        mListView.setOnRefreshListener(new OnYbtRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                pageIndex = 0;
                requestOrderList();
            }

            @Override
            public void onLoadingMore() {
                pageIndex++;
                requestOrderList();
            }
        });
        onRefresh();
    }

    /**
     * 获取订单列表
     */
    private void requestOrderList() {
        String uid = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "");
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_GOODS_ORDER_LIST_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_GOODS_ORDER_SIGN);
        params.put(HttpConstant.APP_UID, "" + uid);
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX, "" + pageIndex);
        if (!NetworkUtil.CheckConnection(mContext) && pageIndex == 0) {
            showError();
            return;
        }
        params.put(HttpConstant.PARAM_KEY_TYPE2, "" + tabValue);
        requestEventCode("a-200" + (tabValue + 1));//事件统计
        request(params, HttpConstant.ORDER_MINE_LIST);

    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.ORDER_MINE_LIST:
                showContent();
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    Type type = new TypeToken<ArrayList<OrderInfo>>() {
                    }.getType();
                    listOrder = JsonUtil.getListModle(data.toString(), "OrderList", type);
                    if (listOrder.isEmpty()) {//数据为空
                        if (pageIndex == 0) {//如果是刷新
                            showEmpty(emptyOrderMine, R.mipmap.empty_order_mine);
                        } else {//如果是加载
                            pageIndex--;
                            footer_ybtlv_linear.setVisibility(View.VISIBLE);//到底了
                            mListView.setPullLoadEnable(false);
                            mListView.setPullRefreshEnable(true);
                            mListView.setAdapter(mAdapter);
                            if (mAdapter.getCount() < 3) {//
                                footer_ybtlv_linear.setVisibility(View.GONE);
                            }
                            if (mAdapter.getCount() < 10 && mAdapter.getCount() >= 3) {
                                footer_ybtlv_linear.setVisibility(View.VISIBLE);
                                mListView.setPullLoadEnable(false);
                                mListView.setPullRefreshEnable(true);
                                mListView.setAdapter(mAdapter);
                            }
                        }
                    } else {//数据不为空
                        if (pageIndex == 0) {//如果是刷新
                            mAdapter.setList(listOrder);
                        } else {//如果是加载
                            mAdapter.addList(listOrder);
                            if (listOrder.size() < 10) {
                                footer_ybtlv_linear.setVisibility(View.VISIBLE);
                                mListView.setPullLoadEnable(false);
                                mListView.setPullRefreshEnable(true);
                                mListView.setAdapter(mAdapter);
                            }
                        }
                        if (mAdapter.getCount() < 3) {//
                            footer_ybtlv_linear.setVisibility(View.GONE);
                        }
                        if (mAdapter.getCount() < 10 && mAdapter.getCount() >= 3) {
                            footer_ybtlv_linear.setVisibility(View.VISIBLE);
                            mListView.setPullLoadEnable(false);
                            mListView.setPullRefreshEnable(true);
                            mListView.setAdapter(mAdapter);
                        }
                    }
                    mListView.onRefreshComplete();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showEmpty(errorServer, R.mipmap.empty_error_net_server);
                } catch (Exception e) {

                }
                break;
            case HttpConstant.ORDER_MINE_CANCEL:
                requestOrderList();
                break;
            case HttpConstant.ORDER_MINE_DELETE:
                mAdapter.removeItem(position);
                if (mAdapter.getCount() == 0) {
                    showEmpty(emptyOrderMine, R.mipmap.empty_order_mine);
                }
                break;
            case HttpConstant.ORDER_PAYMENT_QUERENSHOUHUO:
                requestOrderList();
                break;
            case HttpConstant.ORDER_PAYMENT_UPDATE:
                break;
            case HttpConstant.BUY_SUBMIT_SECOND:
                break;

            default:
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
            switch (errorResponse.getWhat()) {
                case HttpConstant.ORDER_MINE_LIST:
                    if (pageIndex == 0) {
                        if (errorResponse.isNetError()) {
                            showError();
                        } else {
                            showEmpty(errorServer, R.mipmap.empty_error_net_server);
                        }
                    } else {
                        showToast("网络不给力");
                        mListView.onRefreshComplete();
                    }
                    break;
                default:
                    showToast("" + errorResponse.getMsg());
                    break;
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onChildClickOrderCancelListener(int position, String orderId) {
        popWindowOderCancel(position, orderId);
    }

    @Override
    public void onChildClickOrderDeleteListener(int position, String orderId) {
        popWindowOderDelete(position, orderId);
    }

    @Override
    public void onChildClickOrderPaymentListener(OrderInfo orderInfo) {
        if (popfukuan == null) {
            popoupwindowFukuan(orderInfo);
        } else {
            popfukuan.dismiss();
            popfukuan = null;
        }
    }

    @Override
    public void onChildClickOrderPromptShipmentListener() {
        showToast("已提醒商家发货");
        requestEventCode("a-2005");
    }

    @Override
    public void onChildClickOrderLogisticsInfoListener(OrderInfo orderInfo) {
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(Contants.KEY_OBJ_ORDERINFO, orderInfo);
        Intents.openLogisticsActivity(mContext, mBundle);
    }

    @Override
    public void onChildClickOrderQueRenShouHuoListener(int position, OrderInfo orderInfo) {
        requestEventCode("");
        int state = Integer.parseInt(orderInfo.getState());
        if (state == 3) {
            requestEventCode("2009");
            showPrompt(mListView, promptConfirmGoods);
        } else {
            requestEventCode("2010");
            Bundle bundle = new Bundle();
            bundle.putString("money", orderInfo.getReturnMoney());
            requestOrderQueRenShouHuo(orderInfo.getOrderID());
        }
    }

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

    int position;

    /**
     * 是否删除的弹窗
     */
    private void popWindowOderDelete(final int position, final String orderId) {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(getActivity());
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText("确认删除订单？");
        twoButton.getTvOk().setText("确认");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOrderDelete(orderId);
                OrderMineFragment.this.position = position;
//                mAdapter.removeItem(position);
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
     * 是否取消的弹窗
     */
    private void popWindowOderCancel(final int position, final String orderId) {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(getActivity());
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText("确认取消订单？");
        twoButton.getTvOk().setText("确认");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOrderCancel(orderId);
//                mAdapter.updateItemState(position, "0");
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

    /**
     * 支付弹窗
     */
    private void popoupwindowFukuan(final OrderInfo orderInfo) {
        popfukuan = new BasePopupWindow(getActivity(), R.layout.pop_payment_method, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View contentView = popfukuan.getContentView();
        final LinearLayout payment_check_weixin = (LinearLayout) contentView.findViewById(R.id.pop_payment_weixin);
        final LinearLayout payment_check_zhifubao = (LinearLayout) contentView.findViewById(R.id.pop_payment_zhifubao);
        final TextView pop_payment_tv_wechat = (TextView) contentView.findViewById(R.id.pop_payment_tv_wechat);
        final TextView pop_payment_tv_zhifunbao = (TextView) contentView.findViewById(R.id.pop_payment_tv_zhifubao);
        TextView pop_payment_price = (TextView) contentView.findViewById(R.id.pop_payment_price);
        pop_payment_price.setText(orderInfo.getPayPrice());
        payType = orderInfo.getPayType();
        orderId = orderInfo.getOrderID();
        returnMoney = orderInfo.getReturnMoney();
        if (payType.equals(Contants.STR_1)) {
            pop_payment_tv_wechat.setSelected(true);
            pop_payment_tv_zhifunbao.setSelected(false);
        } else {
            pop_payment_tv_wechat.setSelected(false);
            pop_payment_tv_zhifunbao.setSelected(true);
        }
        TextView pop_payment_fukuan = (TextView) contentView.findViewById(R.id.pop_payment_fukuan);
        payment_check_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_payment_tv_wechat.setSelected(true);
                pop_payment_tv_zhifunbao.setSelected(false);
            }
        });

        payment_check_zhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_payment_tv_wechat.setSelected(false);
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
        final PaymentInfo paymentInfo = getPaymentInfo(orderInfo);
        pop_payment_fukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePreferenceUtil.put(mContext, Contants.KEY_PAGE_PAYMENT, Contants.VAL_PAGE_PAYMENT_MINE);
                int payType = 0;
                if (pop_payment_tv_wechat.isSelected()) {
                    if (!isWeixinAvilible(mContext)) {
                        showToast(Contants.STR_WECHAT_INSTALLED);
                        return;
                    }
                    new  PaymentUtil().paymentWeChat(mContext, paymentInfo);
                    payType = 1;
                } else {
                    new  PaymentUtil().paymentAlipay(getActivity(), paymentInfo);
                    payType = 2;
                }
                popfukuan.dismiss();
                popfukuan = null;
                requestFukuanFangshi(payType, orderInfo.getOrderID());
            }
        });

        popfukuan.showPopupWindowTop(mListView, Gravity.BOTTOM);
    }


    private void registerPaymentBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contants.ACTION_NAME_PAYMENT_WECHAT);
        intentFilter.addAction(Contants.ACTION_NAME_PAYMENT_ALIPY);
        intentFilter.addAction(Contants.ACTION_NAME_PAYMENT_SUCCESS);
        mContext.registerReceiver(receiver, intentFilter);
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
        if (orderInfo.getGoodsList().size() == 1) {
            name = orderInfo.getGoodsList().get(0).getName();
            goodsName = orderInfo.getGoodsList().get(0).getGoodsName();
        } else {
            name = orderInfo.getGoodsList().get(0).getName() + "等";
            goodsName = "";
        }
        paymentInfo.setName(name);
        paymentInfo.setGoodsName(goodsName);
        paymentInfo.setPrice(orderInfo.getPayPrice());
        paymentInfo.setOrderId(orderInfo.getOrderID());
        return paymentInfo;
    }

    private class PaymentBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String actionName = intent.getAction();
            if (actionName.equals(Contants.ACTION_NAME_PAYMENT_WECHAT)) {
                PaymentWechat paymentWechat = (PaymentWechat) intent.getSerializableExtra(Contants.KEY_OBJ_PAYMENT_WECHAT);
                paymentWechatCallback(paymentWechat, orderId, payType, returnMoney);
            } else if (actionName.equals(Contants.ACTION_NAME_PAYMENT_ALIPY)) {
//                PaymentUtil.getInstance().requestGoodsValidateOrder(mContext, orderId, payType);
            } else if (actionName.equals(Contants.ACTION_NAME_PAYMENT_SUCCESS)) {
                pageIndex = 0;
                requestOrderList();
            }
        }
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
                        new  PaymentUtil().requestGoodsValidateOrder(mContext, orderId, returnMoney);
                    } else {
                        showToast("您还没有安装微信");
                    }
                    break;
            }
        }
    }

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

    public void onClickRetry() {
        onRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
//            bundle.putSerializable(Contants.KEY_OBJ_ORDERINFO, orderInfo);
//            bundle.putInt(Contants.KEY_PAGE_PAYMENT, Contants.VAL_PAGE_PAYMENT_MINE);
//            bundle.putInt(Contants.KEY_POSITION, position);
//            Bundle mBundle = data.getExtras();
//            OrderInfo orderInfo = (OrderInfo) mBundle.getSerializable(Contants.KEY_OBJ_ORDERINFO);
//            int position=mBundle.getInt(Contants.KEY_POSITION);
//            int state = Integer.parseInt(orderInfo.getState());
//            LogUtil.y(position+"#position#####################orderInfo.getState()##"+state);
//            int detailState=mBundle.getInt("detailState",0);
//            LogUtil.y("##detailState##"+detailState);
////            position=position-1;
//            if(detailState==-1){
//                mAdapter.removeItem(position);
//            }else {
//                mAdapter.updateItemOrderInfo(position, orderInfo);
//            }
            requestOrderList();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
        }
    }
}

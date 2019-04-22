package com.lzyc.ybtappcal.activity.mine.withdraw;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.payment.order.OrderDetailActivity;
import com.lzyc.ybtappcal.adapter.MineWithdrawReturnAdapter;
import com.lzyc.ybtappcal.bean.BalanceDetail;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowOneButton;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 返现记录/待返现
 * Created by Lxx on 2017/4/12.
 */
public class MineWithdrawReturnActivity extends BaseActivity {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_return_balance_rise)
    TextView tvReturnBalanceRise;
    @BindView(R.id.tv_return_balance)
    TextView tvReturnBalance;
    @BindView(R.id.tv_return_notice)
    TextView tvReturnNotice;
    @BindView(R.id.layout)
    RelativeLayout relTop;
    private MineWithdrawReturnAdapter mAdapter;

    private List<BalanceDetail.ListBean> mData = new ArrayList<>();

    private int type = 1;

    private int redPacket;
    private int typePage;
    private boolean isHongBaoFirstEnter = false;
    private String fanxianMoney;//返现的缓存金额
    private String returnMoneyString;//前一个界面传递过来的返现金额

    private Context mContext;

    String totalMoney;
    @Override
    public int getContentViewId() {
        return R.layout.activity_mine_withdraw_return;
    }

    @Override
    public void init() {

        setPageStateView();
        showLoading();

        mContext = this;

        returnMoneyString = getIntent().getStringExtra("returnMoney");
        type = getIntent().getIntExtra("type", 0);
        redPacket = getIntent().getIntExtra("red_packet", 0);
        typePage = getIntent().getExtras().getInt(Contants.KEY_PAGE);
        if (typePage == Contants.VAL_PAGE_HOONBAO) {
            isHongBaoFirstEnter = true;
            requestReturnList();
        }
        if (1 == type) {
            setTitleName("累计返现");
            tvType.setText("累计返现(元)");
            tvReturnNotice.setVisibility(View.GONE);
        } else {
            setTitleName("待返现");
            tvType.setText("待返现(元)");
            tvReturnNotice.setVisibility(View.VISIBLE);
        }
        if (0 == redPacket) {
            tvReturnBalanceRise.setVisibility(View.GONE);
            tvReturnBalance.setVisibility(View.VISIBLE);
        } else {
            tvReturnBalanceRise.setVisibility(View.VISIBLE);
            tvReturnBalance.setVisibility(View.GONE);
        }
        initRecycler();

        relTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        requestReturnList();
        if (TextUtils.isEmpty(returnMoneyString)) {
            tvReturnBalanceRise.setVisibility(View.GONE);
            tvReturnBalance.setVisibility(View.VISIBLE);
        }

    }

    private void initRecycler() {
        mAdapter = new MineWithdrawReturnAdapter(mContext);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setAdapter(mAdapter);

        mAdapter.update(mData);

        mAdapter.setOnWithdrawReturnListener(new MineWithdrawReturnAdapter.WithdrawReturnListener() {
            @Override
            public void onItem(BalanceDetail.ListBean bean) {

                returnMoneyString = "";

                if ("1".equals(bean.getDelete())) {
                    popupDetail();
                    return;
                }

                String operation;
                if (1 == type) {
                    operation = "1";
                } else {
                    operation = "";
                }

                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra("operation", operation);
                intent.putExtra(Contants.KEY_ORDER_ID, bean.getOrderID());
                startActivityForResult(intent, type);
            }
        });
    }

    public void popupDetail() {
        final PopupWindowOneButton oneButton = new PopupWindowOneButton(this);
        oneButton.getTv_content().setText("该订单已被删除");
        oneButton.getTv_ok().setText("关闭");
        oneButton.getTv_ok().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneButton.dismiss();
            }
        });
        oneButton.showPopupWindow(recyclerview, Gravity.CENTER);
    }

    private void requestReturnList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_UCENTER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.PERSON_WITHDRAW_RETURN_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.PERSON_WITHDRAW_RETURN_SIGN);
        params.put(HttpConstant.PERSON_WITHDRAW_RETURN_PARAM_TYPE, String.valueOf(type));
        params.put(HttpConstant.APP_UID, SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString());

        if (!NetworkUtil.CheckConnection(this)) {
            showError();
            return;
        }

        request(params, HttpConstant.WHTHDRAW_RETURN);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        try {
            JSONObject data = response.getJSONObject(HttpConstant.DATA);
            Gson gson = new Gson();
            BalanceDetail balanceDetail = gson.fromJson(data.toString(), BalanceDetail.class);
            totalMoney=balanceDetail.getTotal();
            tvReturnBalance.setText(totalMoney);

            mData = balanceDetail.getList();
            mAdapter.update(mData);
            if (mData == null || mData.isEmpty()) {

                if (type == 1) {
                    showEmpty("您还没有获得返现哦!", R.mipmap.icon_fanxian_empty);
                } else {
                    showEmpty("您没有未到账返现哦!", R.mipmap.icon_fanxian_empty);
                }
                recyclerview.setVisibility(View.GONE);
                return;
            }
            relTop.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showContent();
                    if (!TextUtils.isEmpty(returnMoneyString)) {
                        float floatTotalMoney=Float.parseFloat(totalMoney);
                        float floatReturnMoney=Float.parseFloat(returnMoneyString);
                        final float floatMoney =new BigDecimal(floatTotalMoney- floatReturnMoney).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
                        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, floatReturnMoney);
                        valueAnimator.setDuration(500);
                        valueAnimator.start();
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                try{
                                    float count=floatMoney + (float)animation.getAnimatedValue();
                                    float floatAnimatedValue2= new BigDecimal(count).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
                                    tvReturnBalanceRise.setText(new DecimalFormat("0.00").format(floatAnimatedValue2));
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            },1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onClickRetry() {
        showLoading();
        requestReturnList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        redPacket = 0;
    }

    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);
        try{
            ErrorResponse errorResponse = new ErrorResponse();
            if(errorResponse.isNetError()){
                showError();
            } else {
                showToast(errorResponse.getMsg());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static final String TAG = MineWithdrawReturnActivity.class.getSimpleName();

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            requestReturnList();
            if (TextUtils.isEmpty(returnMoneyString)) {
                tvReturnBalanceRise.setVisibility(View.GONE);
                tvReturnBalance.setVisibility(View.VISIBLE);
            }
        }

    }
}

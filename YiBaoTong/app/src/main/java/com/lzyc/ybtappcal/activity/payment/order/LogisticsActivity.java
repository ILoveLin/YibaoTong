package com.lzyc.ybtappcal.activity.payment.order;

import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.LogisticsAdapter;
import com.lzyc.ybtappcal.bean.LogisticsBean;
import com.lzyc.ybtappcal.bean.OrderInfo;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.KdniaoTrackQueryAPI;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.Util;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * 物流详情
 * Created by lxx on 2017/4/21.
 */
public class LogisticsActivity extends BaseActivity {


    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_copy_number)
    TextView tvCopyNumber;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.rvTrace)
    RecyclerView rvTrace;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    private LogisticsAdapter adapter;

    private List<LogisticsBean.TracesBean> mData = new ArrayList<>();

    private OrderInfo orderInfo;

    private Context mContext;

    @Override
    public int getContentViewId() {
        return R.layout.activity_logistics;
    }

    @Override
    public void init() {

        mContext = this;

        setTitleName("物流详情");

        setPageStateView();
        showLoading();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        orderInfo = (OrderInfo) getIntent().getExtras().getSerializable(Contants.KEY_OBJ_ORDERINFO);
        if(orderInfo==null){
            orderInfo=new OrderInfo();
        }
//        LogUtil.d("lxx", "90-->" + orderInfo.toString());

        initData();

        checkNet();

        initRecycler();

        requestLogistics();

        tvCopyNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.copy(mContext, orderInfo.getLogistics());
                showToast("复制成功");
            }
        });
    }

    private void initData() {
        String logistics= orderInfo.getLogistics();
        String logisticsCom= orderInfo.getLogisticsCom();
        String logoStr=orderInfo.getLogo();
        if (!TextUtils.isEmpty(logistics)) {
            tvNumber.setVisibility(View.VISIBLE);
            tvNumber.setText(logisticsCom + ":" + logistics);
            tvCopyNumber.setVisibility(View.VISIBLE);
        } else {
            tvNumber.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(logisticsCom)) {
            tvState.setText(logisticsCom);
        } else {
            tvState.setText("暂无物流信息");
        }
        if (!TextUtils.isEmpty(logoStr)) {
            Picasso.with(mContext).load(logoStr)
                    .error(R.mipmap.image_empty)
                    .placeholder(R.mipmap.image_empty)
                    .into(imgIcon);
        }
    }

    private void initRecycler() {
        adapter = new LogisticsAdapter(mContext);
        rvTrace.setLayoutManager(new LinearLayoutManager(this));
        rvTrace.setAdapter(adapter);
    }

    /**
     * 模拟接口请求
     */
    private void requestLogistics(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                KdniaoTrackQueryAPI api = new KdniaoTrackQueryAPI();

                try {
                    String result = api.getOrderTracesByJson(orderInfo.getLogisticsCode(), orderInfo.getLogistics());

                    if(!result.endsWith("}")){
                        result = result + "}";
                    }

                    done(result);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showContent();
                        }
                    }, 1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 模拟接口返回
     */
    private void done(String result){
        LogisticsBean trace = new Gson().fromJson(result.toString(), LogisticsBean.class);

        if (null == trace.getTraces() || trace.getTraces().isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvTrace.setVisibility(View.GONE);

            return;
        }

        LogisticsBean.TracesBean bean = new LogisticsBean.TracesBean();

        bean.setAcceptStation("[收件地址]" + orderInfo.getAddressRegion() + orderInfo.getAddressDetail());
        bean.setAcceptTime("");
        mData = trace.getTraces();
        mData.add(bean);
        Collections.reverse(mData);
        if (mData != null && !mData.isEmpty()) {
            adapter.update(mData, trace.getState());
        }

        if ("4".equals(trace.getState()) || "3".equals(trace.getState())) {
            tvCopyNumber.setVisibility(View.GONE);
            tvState.setText("已签收");
            tvState.setTextColor(getResources().getColor(R.color.color_ff8431));
        } else {
            tvCopyNumber.setVisibility(View.VISIBLE);
            tvState.setText("运输中");
            tvState.setTextColor(getResources().getColor(R.color.color_76b7fa));
        }

    }

    /**
     * 检查网络
     */
    private void checkNet() {
        if (!NetworkUtil.CheckConnection(mContext)) {
            showError();
            return;
        }
    }

    @Override
    protected void onClickRetry() {
        showLoading();
        initRecycler();
    }

    private static final String TAG = LogisticsActivity.class.getSimpleName();

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

package com.lzyc.ybtappcal.activity.mine.withdraw;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.MineBalanceListAdapter;
import com.lzyc.ybtappcal.bean.BalanceListBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 余额明细
 * Created by lxx on 2017/6/2.
 */

public class MineBalanceListActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.scroll)
    NestedScrollView scroll;

    MineBalanceListAdapter mAdapter;

    List<BalanceListBean.ListBean> mData = new ArrayList<>();

    final String DATA_EMPTY = "您还没有余额明细哦!";

    @Override
    public int getContentViewId() {
        return R.layout.activity_mine_withdraw_list;
    }

    @Override
    protected void init() {

        setPageStateView();

        showLoading();

        setTitleName(Contants.STR_TITLE_BALANCE_DETAILS);

        setTitleRightTvbtnName("提现");

        initRecycler();

        requestBalanceList();
    }

    @Override
    public void onClickTitleRightTvBtn(View v) {
        super.onClickTitleRightTvBtn(v);

        requestEventCode("c010");

        Intent intent = getIntent();
        intent.setClass(mContext, MineWithdrawActivity.class);

        startActivityForResult(intent, Contants.INT_3);
    }

    private void initRecycler(){

        mAdapter = new MineBalanceListAdapter(mContext);

        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));

        recyclerview.setNestedScrollingEnabled(false);

        recyclerview.setAdapter(mAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(RESULT_OK == resultCode){
            showLoading();
            mData.clear();
            requestBalanceList();
        }
    }

    private void requestBalanceList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_UCENTER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.PERSON_WITHDRAW_RETURN_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.PERSON_WITHDRAW_RETURN_SIGN);
        params.put(HttpConstant.PERSON_WITHDRAW_RETURN_PARAM_TYPE, "3");
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

            BalanceListBean bean = new Gson().fromJson(data.toString(), BalanceListBean.class);

            mData.addAll(bean.getList());

            if(mData.isEmpty()){
                showEmpty(DATA_EMPTY, R.mipmap.icon_balance_empty);
                return;
            }

            mAdapter.updata(mData);

            scroll.scrollTo(0, 0);

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

    @Override
    protected void onClickRetry() {
        super.onClickRetry();
        showLoading();
        requestBalanceList();
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

    private static final String TAG = MineBalanceListActivity.class.getSimpleName();

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

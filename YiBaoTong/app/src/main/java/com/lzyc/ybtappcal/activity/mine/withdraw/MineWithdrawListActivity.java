package com.lzyc.ybtappcal.activity.mine.withdraw;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.MineWithdrawDetailAdapter;
import com.lzyc.ybtappcal.bean.PersonWithdrawList;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.Intents;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 提现记录
 * Created by Lxx on 2017/4/11.
 */
public class MineWithdrawListActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private MineWithdrawDetailAdapter mAdapter;
    private List<PersonWithdrawList.ListBean> mData = new ArrayList<>();

    private Context mContext;

    @Override
    public int getContentViewId() {
        return R.layout.activity_mine_withdraw_list;
    }

    @Override
    public void init() {
        setTitleName("提现记录");

        mContext = this;

        setPageStateView();
        showLoading();

        initRecycler();
        mAdapter.setOnDetailListener(new MineWithdrawDetailAdapter.DetailListener() {
            @Override
            public void onItem(PersonWithdrawList.ListBean bean) {
                requestEventCode("c015");
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(Intents.DRAW_DETAIL, bean);
                Intents.openMineWithdrawDetailActivity(mContext, mBundle);
            }
        });

        requestEventCode("c014");

        requestWithdrawList();
    }

    private void initRecycler() {
        mAdapter = new MineWithdrawDetailAdapter(mContext);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setAdapter(mAdapter);
        mAdapter.update(mData);
    }

    private void requestWithdrawList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.PERSON_WITHDRAW_LIST_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.PERSON_WITHDRAW_LIST_SIGN);
        params.put(HttpConstant.APP_UID, SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString());

        if (!NetworkUtil.CheckConnection(mContext)) {
            showError();
            return;
        }

        request(params, HttpConstant.WHTHDRAW_LIST);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        try {
            JSONObject data = response.getJSONObject(HttpConstant.DATA);
            PersonWithdrawList personWithdrawList = new Gson().fromJson(data.toString(), PersonWithdrawList.class);
            mData = personWithdrawList.getList();
            if (mData.isEmpty()) {
                showEmpty("您还没有申请过提现哦!", R.mipmap.icon_withdraw_empty);
            } else {
                showContent();
            }
            mAdapter.update(mData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onClickRetry() {
        showLoading();
        requestWithdrawList();
    }

    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);

            if(errorResponse.isNetError()) {
                showError();
            } else {
                showToast(errorResponse.getMsg());
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static final String TAG = MineWithdrawListActivity.class.getSimpleName();

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

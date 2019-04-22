package com.lzyc.ybtappcal.activity.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.mock.MockApplication;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.GoodsSearchActivity;
import com.lzyc.ybtappcal.activity.ShoppingCartActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.HomeTypeAllAdapter;
import com.lzyc.ybtappcal.bean.GoodsGetCategoryBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.view.BadgeView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 药品分类目录
 * Created by lxx on 2017/5/1.
 */

public class TypeActivity extends BaseActivity {
    private static final String TAG=TypeActivity.class.getSimpleName();
    @BindView(R.id.recycler_types_all)
    RecyclerView recyclerViewAll;
    @BindView(R.id.rel_loading)
    RelativeLayout relLoading;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.tv_car_num)
    TextView tvCarNum;
    @BindView(R.id.badgeview_car_num)
    BadgeView badgeViewCarNum;

    HomeTypeAllAdapter mAdapter;

    Context mContext;

    List<GoodsGetCategoryBean.CategoryBean> mData = new ArrayList<>();

    @Override
    public int getContentViewId() {
        return R.layout.activity_type;
    }

    @Override
    protected void init() {
        mContext = this;

        setPageStateView();
        setPageStateView(relLoading);
        showLoading();

        setTitleBarVisibility(View.GONE);

        initRecyclerTypesAll();

        requestTypesAll();

        String search = getIntent().getStringExtra("search");
        tvSearch.setText(search);

    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        requestShopCardNum();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    private void initRecyclerTypesAll() {
        mAdapter = new HomeTypeAllAdapter(mContext);
        recyclerViewAll.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewAll.setAdapter(mAdapter);

        mAdapter.updata(mData);
    }

    @OnClick({R.id.img_back, R.id.img_shop_card, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.img_back:
                finish();
                break;

            //跳转到购物车
            case R.id.img_shop_card:
                requestEventCode("k011");
                if (!popupLogin()) return;
                openActivity(ShoppingCartActivity.class);
                break;

            //跳转到搜索
            case R.id.tv_search:
                requestEventCode("k012");
                Bundle mBundle = new Bundle();
                mBundle.putString(Contants.KEY_HINT_SEARCH, tvSearch.getText().toString());
                openActivity(GoodsSearchActivity.class, mBundle);
                break;
        }
    }


    @Override
    protected void onClickRetry() {
        showLoading();
        requestShopCardNum();
        requestTypesAll();
    }

    /**
     * 购物车数量
     */
    public void requestShopCardNum() {
        if (TextUtils.isEmpty(SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString()))
            return;

        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_CARD_NUM_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_CARD_NUM_SIGN);
        params.put(HttpConstant.APP_UID, SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString());

        if (!NetworkUtil.CheckConnection(mContext)) {
            showToast("网络不给力");
            return;
        }

        request(params, HttpConstant.BUY_ORDERBY_SHOP_CARD_NUM);
    }

    /**
     * 全部分类
     */
    private void requestTypesAll() {
        Map<String, String> params = new HashMap<>();

        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_HOME_CATEGORY_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_HOME_CATEGORY_SIGN);

        if (!NetworkUtil.CheckConnection(mContext)) {
            showError();
            return;
        }

        request(params, HttpConstant.HOME_TYPE_ALL);

    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);

        switch (what) {
            case HttpConstant.BUY_ORDERBY_SHOP_CARD_NUM:
                try {
                    int count = response.getJSONObject(HttpConstant.DATA).optInt("Count");

                    if (0 == count) {
                        badgeViewCarNum.setVisibility(View.INVISIBLE);
                        badgeViewCarNum.setText("0");
                    }

                    String countStr = String.valueOf(count);

                    if (0 < count) {
                        if (99 < count) {
                            countStr = "99+";
                        }

                        badgeViewCarNum.setText(countStr);
                        badgeViewCarNum.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {

                }
                break;
            case HttpConstant.HOME_TYPE_ALL:
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);

                    Gson gson = new Gson();
                    GoodsGetCategoryBean bean = gson.fromJson(data.toString(), GoodsGetCategoryBean.class);

                    mData = bean.getCategory();

                    mAdapter.updata(mData);
                    showContent();
                } catch (Exception e) {
                }
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);

            if (errorResponse.isNetError()) {
                showError();
            } else {
                showToast(errorResponse.getMsg());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

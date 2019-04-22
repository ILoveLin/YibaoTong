package com.lzyc.ybtappcal.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.GoodsSearchActivity;
import com.lzyc.ybtappcal.activity.MainActivity;
import com.lzyc.ybtappcal.activity.ShoppingCartActivity;
import com.lzyc.ybtappcal.activity.base.BaseFragment;
import com.lzyc.ybtappcal.activity.home.HotActivity;
import com.lzyc.ybtappcal.activity.top.HomeWebActivity;
import com.lzyc.ybtappcal.adapter.HomeAdapter;
import com.lzyc.ybtappcal.bean.AttributeBean;
import com.lzyc.ybtappcal.bean.BannerBean;
import com.lzyc.ybtappcal.bean.CategoryBean;
import com.lzyc.ybtappcal.bean.GoodsDrugListBean;
import com.lzyc.ybtappcal.bean.GoodsHomePageList2;
import com.lzyc.ybtappcal.bean.GoodsListBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.view.BadgeView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.IRecyclerView;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.lzyc.ybtappcal.widget.xrecycler.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 * Created by Lxx on 2017/3/21.
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.badgeview_car_num)
    BadgeView badgeViewCarNum;
    @BindView(R.id.tv_car_num)
    TextView tvCarNum;
    @BindView(R.id.recyclerview)
    IRecyclerView recyclerview;
    @BindView(R.id.tv_to_top)
    TextView tvToTop;
    @BindView(R.id.lin_search)
    LinearLayout linSearch;
    @BindView(R.id.tv_search_title)
    TextView tvSearchTitle;
    @BindView(R.id.net_error)
    View vNetError;
    static final int START_ALPHA = 0;
    static final int END_ALPHA = 255;
    int fadingHeight = 100;   //当ScrollView滑动到什么位置时渐变消失（根据需要进行调整）
    Drawable drawable;        //顶部渐变布局需设置的Drawable

    HomeAdapter mAdapter;

    LinearLayoutManager layoutManager;

    int pageIndex = -1;

    String idStr = "0";

    List<BannerBean.ImagesBean> mBannerList = new ArrayList<>();

    List<GoodsHomePageList2> mData = new ArrayList<>();

    List<GoodsHomePageList2> mDataCache = new ArrayList<>();

    List<CategoryBean> mCategoryData = new ArrayList<>();

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {

        setPageStateView();

        initRecycler();
        showCacheData();

        initSearch();

        requestBanner();
        requestGoodsHomePageList();
    }

    private void initRecycler() {
        layoutManager = new LinearLayoutManager(mContext);
        mAdapter = new HomeAdapter(mContext);
        mAdapter.updataActivity((MainActivity) getActivity());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(mAdapter);

        recyclerview.setPullRefreshEnabled(false);

        recyclerview.setLoadingMoreEnabled(false);

        initFooter();

        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if (isSlideToBottom(recyclerview)) {
                    pageIndex += 1;
                    requestMore();
                } else {
                    recyclerview.loadMoreComplete();
                }
            }
        });

        recyclerview.setOnScrollListener(new IRecyclerView.ScrollListener() {
            @Override
            public void onScrollStateChanged(IRecyclerView recyclerView, int newState) {

                if(null != recyclerView.getChildAt(3)){
                    tvToTop.setVisibility(View.GONE);
                } else {
                    tvToTop.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onScrolled(IRecyclerView recyclerView, int dx, int dy, int totalOffsetDy) {
                totalOffsetDy = Math.abs(totalOffsetDy);
                if (totalOffsetDy > fadingHeight) {
                    totalOffsetDy = fadingHeight;   //当滑动到指定位置之后设置颜色为纯色，之前的话要渐变---实现下面的公式即可
                }
                drawable.setAlpha(totalOffsetDy * (END_ALPHA - START_ALPHA) / fadingHeight + START_ALPHA);

            }
        });

        mAdapter.setOnHomeListener(new HomeAdapter.HomeListener() {
            @Override
            public void OnBannerClickListener(String url) {

                requestEventCode("k003");

                if (TextUtils.isEmpty(url)) return;
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                openActivity(HomeWebActivity.class, bundle);
            }

            @Override
            public void onHotClickListener(String title, String type, String idStr) {
                requestEventCode("k009");

                Bundle bundle = new Bundle();
                bundle.putString(Contants.KEY_HOME_ID, idStr);
                bundle.putString(Contants.KEY_HOME_TITLE, title);
                bundle.putString(Contants.KEY_HOME_TYPE, type);
                openActivity(HotActivity.class, bundle);
            }

        });

        tvToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
                recyclerview.smoothScrollToPosition(0);
                //computeVerticalScrollOffset
                drawable.setAlpha(START_ALPHA);
                linSearch.setBackgroundDrawable(drawable);
            }
        });
        startUPAndUnderAnimation(tvToTop);
    }

    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    private void initSearch() {
        drawable = linSearch.getBackground();
        drawable.setAlpha(START_ALPHA);
        linSearch.setBackgroundDrawable(drawable);
    }

    @OnClick({R.id.rel_shop_card, R.id.lin_search, R.id.include_net_setting_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rel_shop_card:

                requestEventCode("k004");
                if (!popupLogin()) return;
                openActivity(ShoppingCartActivity.class);
                break;
            case R.id.lin_search:

                requestEventCode("k002");
                Bundle mBundle = new Bundle();
                mBundle.putString(Contants.KEY_HINT_SEARCH, tvSearchTitle.getText().toString());
                openActivity(GoodsSearchActivity.class, mBundle);
                break;
            case R.id.include_net_setting_tv:
                if (!NetworkUtil.CheckConnection(mContext)) {
                    showNetDialog();
                    return;
                }

                showLoading();

                requestBanner();
                requestGoodsHomePageList();
                requestShopCardNum();
                break;
        }
    }

    /**
     * 返回顶部按钮动画
     */
    private void startUPAndUnderAnimation(final View v) {
        float redPackedY = v.getTranslationY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationY", redPackedY, redPackedY - DensityUtils.dp2px(5), redPackedY);
        AnimatorSet set = new AnimatorSet();
        set.play(animator);
        set.setDuration(3000);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startUPAndUnderAnimation(v);
            }
        });
    }

    private void showCacheData() {

        try {
            String json = SharePreferenceUtil.getString(getActivity(), SharePreferenceUtil.KEY_CACHE_DATA, "");

            String categoryStr = SharePreferenceUtil.getString(getActivity(), SharePreferenceUtil.KEY_CACHE_DATA_CATEGORY, "");
            List<CategoryBean> categoryData = new Gson().fromJson(categoryStr, new TypeToken<List<CategoryBean>>() {
            }.getType());
            mAdapter.updataCategory(categoryData);

            mDataCache = new Gson().fromJson(json, new TypeToken<List<GoodsHomePageList2>>() {
            }.getType());

            mAdapter.updateData(mDataCache);

            String bannerData = SharePreferenceUtil.getString(getActivity(), SharePreferenceUtil.KEY_CACHE_BANNER, "");
            List<BannerBean.ImagesBean> bnnerData = new Gson().fromJson(bannerData, new TypeToken<List<BannerBean.ImagesBean>>() {
            }.getType());
            mAdapter.updateBannerList(bnnerData);

            String title = SharePreferenceUtil.getString(getActivity(), SharePreferenceUtil.KEY_CACHE_TITLE, "");
            tvSearchTitle.setText(title);
            mAdapter.updataSearch(title);

            if (0 == mDataCache.get(0).getAttribute().get(mDataCache.get(0).getAttribute().size() - 1).getShowMore()) {
                idStr = mDataCache.get(0).getAttribute().get(mDataCache.get(0).getAttribute().size() - 1).getID();
            }

        } catch (Exception e) {
            e.printStackTrace();

            if (!NetworkUtil.CheckConnection(mContext)) {
                vNetError.setVisibility(View.VISIBLE);
                recyclerview.setVisibility(View.GONE);
                return;
            }
            showLoading();
        }

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
            showToast(getResources().getString(R.string.error_net_toast));
            return;
        }

        request(params, HttpConstant.BUY_ORDERBY_SHOP_CARD_NUM);
    }

    /**
     * 获取banner
     */
    public void requestBanner() {
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_HOME_BANNER_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_HOME_BANNER_SIGN);
        params.put(HttpConstant.SHOP_HOME_PARAM_OS, HttpConstant.SHOP_HOME_PARAM_PAGE_ANDROID);
        params.put(HttpConstant.SHOP_HOME_PARAM_NAME, HttpConstant.SHOP_HOME_PARAM_PAGE_HOME_BANNER);

        request(params, HttpConstant.HOME_PAGE_BANNER);
    }

    /**
     * 商品列表／分类列表
     */
    private void requestGoodsHomePageList() {

        if (!NetworkUtil.CheckConnection(mContext)) {
            if (null == mData || mData.isEmpty())
                mData = new ArrayList<>();
            if (null != mDataCache && !mDataCache.isEmpty())
                mData.addAll(mDataCache);
        }

        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_HOME_CLZ2);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_HOME_SIGN2);
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX, "0");

        request(params, HttpConstant.HOME_PAGE_LIST2);
    }

    /**
     * 加载更多
     */
    private void requestMore() {
        Map<String, String> params = new HashMap<>();

        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_HOME_GOODS_DRUG_LIST);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_HOME_GOODS_DRUG_LIST_SIGN);
        params.put(HttpConstant.PARAM_KEY_TYPE, "attribute");
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX, String.valueOf(pageIndex));
//        params.put(HttpConstant.PARAM_KEY_PAGEINDEX, "0");
        params.put(HttpConstant.ID_SMALL, idStr);

        if (!NetworkUtil.CheckConnection(mContext)) {
            showToast(getResources().getString(R.string.error_net_toast));
            pageIndex -= 1;
            recyclerview.loadMoreComplete();
            return;
        }

        request(params, HttpConstant.HOME_TYPE);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);

        recyclerview.setLoadingMoreEnabled(true);

        try {
            recyclerview.setVisibility(View.VISIBLE);
            vNetError.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                    e.printStackTrace();
                }
                break;

            case HttpConstant.HOME_PAGE_BANNER:
                try {
                    JSONObject dataBanner = response.getJSONObject(HttpConstant.DATA);

                    Gson gson = new Gson();
                    BannerBean bannerBean = gson.fromJson(dataBanner.toString(), BannerBean.class);

                    mBannerList.addAll(bannerBean.getImages());

                    mAdapter.updateBannerList(mBannerList);

                    if (!mBannerList.isEmpty()) {
                        SharePreferenceUtil.putString(getActivity(), SharePreferenceUtil.KEY_CACHE_BANNER, gson.toJson(mBannerList));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case HttpConstant.HOME_PAGE_LIST2:
                try {

                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    Log.e("chun", "data = " + data.toString());
                    Gson gson = new Gson();
                    GoodsHomePageList2 goodsHomePageList = gson.fromJson(data.toString(), GoodsHomePageList2.class);

                    if (null != mData && !mData.isEmpty()) {
                        mData.clear();
                    } else {
                        mData = new ArrayList<>();
                    }
                    mData.add(goodsHomePageList);

                    if (null != mCategoryData && !mCategoryData.isEmpty()) {
                        mCategoryData.clear();
                    } else {
                        mCategoryData = new ArrayList<>();
                    }

                    for (GoodsHomePageList2 bean : mData) {
                        mCategoryData.addAll(bean.getCategory());
                    }

                    List<GoodsHomePageList2> listTemp = new ArrayList<>();
                    listTemp.addAll(mData);
                    if (!listTemp.isEmpty()) {
                        SharePreferenceUtil.putString(getActivity(), SharePreferenceUtil.KEY_CACHE_DATA, gson.toJson(listTemp));
                    }

                    List<CategoryBean> categoryTtemp = new ArrayList<>();
                    categoryTtemp.addAll(mCategoryData);
                    if (!categoryTtemp.isEmpty()) {
                        SharePreferenceUtil.putString(getActivity(), SharePreferenceUtil.KEY_CACHE_DATA_CATEGORY, gson.toJson(categoryTtemp));
                    }

                    if (!TextUtils.isEmpty(goodsHomePageList.getSearchTiele())) {
                        SharePreferenceUtil.putString(getActivity(), SharePreferenceUtil.KEY_CACHE_TITLE, goodsHomePageList.getSearchTiele());
                    }

                    mAdapter.updataCategory(mCategoryData);

                    mAdapter.updataSearch(goodsHomePageList.getSearchTiele());

                    mAdapter.updateData(mData);

                    tvSearchTitle.setText(goodsHomePageList.getSearchTiele());

                    if (0 == mData.get(0).getAttribute().get(mData.get(0).getAttribute().size() - 1).getShowMore()) {
                        idStr = mData.get(0).getAttribute().get(mData.get(0).getAttribute().size() - 1).getID();
                    }

                    pageIndex += 1;
                    requestMore();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case HttpConstant.HOME_TYPE:
                recyclerview.loadMoreComplete();
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    Log.e("yanlc", "more = " + data.toString());
                    GoodsDrugListBean dataBean = new Gson().fromJson(data.toString(), GoodsDrugListBean.class);

                    if (null == dataBean || dataBean.getList().size() <= 0) {
                        showToast("没有更多数据了");
                        return;
                    }

                    List<AttributeBean> attrlist = mData.get(0).getAttribute();
                    List<GoodsListBean> list = attrlist.get(attrlist.size() - 1).getGoodsList();
                    if (pageIndex == 0 && 0 == attrlist.get(attrlist.size() - 1).getShowMore()) {
                        list.clear();
                    }
                    list.addAll(dataBean.getList());

                    mAdapter.updateData(mData);
                    mAdapter.notifyDataSetChangedChild();

                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(getResources().getString(R.string.error_net_toast));
                }

                break;
        }
        showContent();

    }

    public void showNetDialog() {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(getActivity());
        twoButton.getTvTitle().setText("提示");
        twoButton.getTv_content().setText("当前网络连接不可用，是否设置网络？");
        twoButton.getTvOk().setText("确认");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    NetworkUtil.openNetWorkSetting(mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(getActivity().getWindow().getDecorView(), Gravity.CENTER);
    }

    @Override
    public void error(String errorMsg) {
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
            switch (errorResponse.getWhat()) {
                case HttpConstant.HOME_TYPE:
                    if (errorResponse.isNetError()) {
                        pageIndex -= 1;
                        recyclerview.loadMoreComplete();

                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * loading
     */
    private void initFooter() {

        LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.home_footer, null);

        moreView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ImageView imageView = (ImageView) moreView
                .findViewById(R.id.loadding_listview_image);
        imageView.setBackgroundResource(R.drawable.ybtlistview_load);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                .getBackground();
        animationDrawable.start();

        recyclerview.setFootView(moreView);
    }

    private static final String TAG = HomeFragment.class.getSimpleName();

    @Override
    public void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        requestShopCardNum();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

}

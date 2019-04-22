package com.lzyc.ybtappcal.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.ScanActivity;
import com.lzyc.ybtappcal.activity.SearchActivity;
import com.lzyc.ybtappcal.activity.base.BaseFragment;
import com.lzyc.ybtappcal.activity.top.CitylistSearchActivity;
import com.lzyc.ybtappcal.activity.top.DrugNoDetailActivity;
import com.lzyc.ybtappcal.activity.top.ResultsActivity;
import com.lzyc.ybtappcal.adapter.BiyaoHistorysAdapter;
import com.lzyc.ybtappcal.adapter.BiyaoStatePagerAdapter;
import com.lzyc.ybtappcal.app.App;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.bean.HistoryCache;
import com.lzyc.ybtappcal.bean.TopBanner;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.handler.FunnelContants;
import com.lzyc.ybtappcal.handler.OnClickListenerHandler;
import com.lzyc.ybtappcal.service.LocationService;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.PermissionUtil;
import com.lzyc.ybtappcal.util.ScreenUtils;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.StringUtils;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.util.cache.SPCacheList;
import com.lzyc.ybtappcal.view.NoShadowListView;
import com.lzyc.ybtappcal.view.viewpager.DecoratorViewPager;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import ybt.library.indicator.indicator.PageIndicatorView;

/**
 * 比药
 * 药品查询
 *
 * @author yang
 */
public class TopFragment extends BaseFragment implements Animator.AnimatorListener {

    protected static final String TAG = TopFragment.class.getSimpleName();

    @BindView(R.id.tv_start_anim)
    ImageView tvStartAnim;
    @BindView(R.id.tv_dingwei)
    TextView tvDingwei;
    @BindView(R.id.tv_search)
    LinearLayout tvSearch;
    @BindView(R.id.rel_anim)
    RelativeLayout relAnim;
    @BindView(R.id.rel_bottom)
    RelativeLayout relBottom;
    @BindView(R.id.tv_anim_under)
    TextView tvAnimUnder;
    @BindView(R.id.linear_hidden)
    LinearLayout linearHidden;
    @BindView(R.id.imageview)
    ImageView imageView;
    @BindView(R.id.tv_anim_bg)
    ImageView tvAnimBg;
    @BindView(R.id.linear_all)
    LinearLayout linearAll;
    @BindView(R.id.no_showdow_listview)
    NoShadowListView noShowdowListView;
    @BindView(R.id.linear_history)
    LinearLayout linearHistory;
    @BindView(R.id.iv_history_clear)
    ImageView ivHistoryClear;
    @BindView(R.id.scroll_pull)
    TwinklingRefreshLayout scrollPull;

    private String currentChooseCity ;//选择的城市
    private boolean startAnim = false;
    private float mDensity;
    private int mHiddenViewMeasureHeight;
    protected boolean toLeft = false;
    public static Bitmap bitmap;
    public static boolean isCaptureBack;
    protected int w, h;
    private static final int MSG_SWITCH_CAPTURE = 300;
    private LocationService locService;
    private int i = 1;
    private boolean isDingweiCancel = false;
    public static boolean isCreate = false;
    private String currentChooseProvince;
    private String dingweiProvince;
    private boolean isFirstLunch;
    private String dingweiCity;
    private List<TopBanner.DataBean.ImagesBean> listImageUrl;
    private View headerViewBanner;
    private View headerViewHistoryBar;
    private BiyaoHistorysAdapter mAdapter;
    private ArrayList<HistoryCache> listHistorySearch;
    private ImageView goodsSearchClear;
    private DecoratorViewPager headerViewpager;
    private PageIndicatorView headerIndicator;
    private BiyaoStatePagerAdapter advertisementAdapter;//广告轮播
    private HistoryCache mHistoryCache;
    private String scanCode;//扫码历史药品码
    private static final int REQUEST_CODE_LOCATION = 2;//位置信息

    private MyLocationListener mListener = new MyLocationListener();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Contants.FLAG_START_HANDLER_ANIM:
                    tvStartAnim.setClickable(false);
                    animateOpen(linearHidden);
                    animationIvRotateOpen();
                    mHandler.sendEmptyMessageDelayed(Contants.FLAG_END_HANDLER_ANIM, 2000);
                    break;
                case Contants.FLAG_END_HANDLER_ANIM:
                    animateClose(linearHidden);
                    animationIvRoateClose();
                    break;
                case MSG_SWITCH_CAPTURE:
                    boolean isTemp= requestCODE_CAMERA();
                    if(!isTemp){
                       return;
                    }
                    Bundle mBundle = new Bundle();
                    mBundle.putInt(Contants.KEY_PAGE_SCAN, Contants.VAL_PAGE_SEARCH_DURUG);
                    mBundle.putInt(Contants.KEY_PAGE_SEARCH, Contants.VAL_PAGE_SEARCH_DURUG);
                    mBundle.putInt(Contants.KEY_POSITION_CUT, getHeight());
                    openActivityNoAnim(ScanActivity.class, mBundle);
                    getActivity().overridePendingTransition(0, 0);
                    bitmap = ScreenUtils.snapShotWithoutStatusBar(getActivity());
                    break;
                default:
                    break;
            }
        }
    };

    public int getHeight() {
        int location[] = new int[2];
        imageView.getLocationOnScreen(location);
        int height = imageView.getHeight() / 2 + location[1] - ScreenUtils.getStatusHeight();
        return height;
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_top;
    }

    @Override
    protected void init() {
        locService = App.getInstance().locationService;
        locService.registerListener(mListener);
        if (!isCreate) {
            requestPageStyle();
        }
        currentChooseCity = "";//置空已选择的城市
        isDingweiCancel = false;
        onLayoutParams();
        startHandlerAnim();
        handlerOnClickListener();
        scalingAnim();
        scrollPull.setPureScrollModeOn();
        tvStartAnim.setOnClickListener(headerClickListener);
        tvAnimUnder.setOnClickListener(headerClickListener);
        listHistorySearch = new ArrayList<>();
        listHistorySearch = SPCacheList.getInstance().readDrugSearchHistory();
        mAdapter = new BiyaoHistorysAdapter(mContext, R.layout.item_drugs_history_biyao, listHistorySearch);
        headerViewBanner = View.inflate(mContext, R.layout.header_biyao_banner, null);
        headerViewpager = (DecoratorViewPager) headerViewBanner.findViewById(R.id.header_viewpager);
        headerIndicator = (PageIndicatorView) headerViewBanner.findViewById(R.id.detail_vp_indicator);
        headerViewHistoryBar = View.inflate(mContext, R.layout.header_biyao_history, null);
        goodsSearchClear = (ImageView) headerViewHistoryBar.findViewById(R.id.iv_clear);
        noShowdowListView.addHeaderView(headerViewBanner, null, false);
        noShowdowListView.addHeaderView(headerViewHistoryBar, null, false);
        noShowdowListView.setAdapter(mAdapter);
        goodsSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupClearHistory();
            }
        });
        noShowdowListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 1) {
                    linearHistory.setVisibility(View.VISIBLE);
                } else {
                    linearHistory.setVisibility(View.GONE);
                }
            }
        });

        noShowdowListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                requestEventCode("d003");//点击历史记录条目
                if (tvDingwei.getText().equals("未定位")) {
                    showWeiDingwei();
                    return;
                }
//                if (isDingweiCancel) {
//                    showWeiDingwei();
//                    return;
//                }
                mHistoryCache = (HistoryCache) adapterView.getItemAtPosition(position);
                if (mHistoryCache != null && !mHistoryCache.isEmptyView()) {
                    Bundle mBundle = new Bundle();
                    String goodsname = mHistoryCache.getGoodsName();
                    String keyword;
                    if (!TextUtils.isEmpty(goodsname)) {
                        keyword = goodsname + " " + mHistoryCache.getName();
                    } else {
                        keyword = mHistoryCache.getName();
                    }
                    int type = mHistoryCache.getCacheType();
                    if (type == HistoryCache.TYPE_CACHE_SCAN) {
                        scanCode = mHistoryCache.getCode();
                        requestDrugList(mHistoryCache.getCode());
                    } else {
                        mBundle.putString(Contants.KEY_STR_KEYWORD, keyword);
                        mBundle.putInt(Contants.KEY_PAGE_SEARCH, Contants.VAL_PAGE_SEARCH_HISTORY);
                        SPCacheList.getInstance().writeDrugSearchHistory(mHistoryCache);
                        openActivity(SearchActivity.class, mBundle);
                    }
                }
            }
        });
    }


    private void clearHistory() {
        SPCacheList.getInstance().clearHistory(SharePreferenceUtil.SPACE_HISTORY_DRUG_SEARCH);
        mAdapter.clear();
        mAdapter.setEmptyView();
        listHistorySearch = SPCacheList.getInstance().readDrugSearchHistory();
        if (listHistorySearch.size() == 0) {
            goodsSearchClear.setVisibility(View.GONE);
            mAdapter.setEmptyView();
        } else {
            goodsSearchClear.setVisibility(View.VISIBLE);
            mAdapter.setList(listHistorySearch);
        }
    }

    private void requestPageStyle() {
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_PAGE_STYLE_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_PAGE_STYLE_SIGN);
        params.put(HttpConstant.PARAM_KEY_OS, "android");
        params.put("name", "biyao_banner");
        request(params, HttpConstant.BIYAO_BANNER);
    }

    private View.OnClickListener headerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_start_anim: //hidden动画
                    requestEventCode("a009");
                    tvStartAnim.setClickable(false);
                    if (linearHidden.getVisibility() == View.GONE) {   //隐藏
                        animateOpen(linearHidden);
                        animationIvRotateOpen();
                    } else {
                        animateClose(linearHidden);
                        animationIvRoateClose();
                    }
                    break;
                case R.id.tv_anim_under://hidden动画点击效果不好的bug的解决方法
                    tvStartAnim.setClickable(false);
                    if (linearHidden.getVisibility() == View.GONE) {   //隐藏
                        animateOpen(linearHidden);
                        animationIvRotateOpen();
                    } else {
                        animateClose(linearHidden);
                        animationIvRoateClose();
                    }
                    break;
            }
        }
    };

    private void onLayoutParams() {
        String phoneType = Build.MODEL;
        if (phoneType.equals(Contants.FLAG_PHONE_TYPE_XIAOMINOTE)) {
            DensityUtils.setLinearMWTopMargin(-1, linearHidden);
            DensityUtils.setLinearMWTopMargin(-25, relBottom);
        } else if (phoneType.equals(Contants.FLAG_PHONE_TYPE_LETVX501)) {
            DensityUtils.setLinearMWTopMargin(-1, linearHidden);
            DensityUtils.setLinearMWTopMargin(-25, relBottom);
        } else if (DensityUtils.getPhoneWidth(getActivity().getWindowManager()) == Contants.FLAG_PHONE_WIDTH && DensityUtils.getPhoneHeight(getActivity().getWindowManager()) == Contants.FLAG_PHONE_HEIGHT) {//分辨率是  1440 * 2560
            LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) relAnim.getLayoutParams();
            layoutParams1.topMargin = DensityUtils.dp2px(-4);
            relAnim.setLayoutParams(layoutParams1);
            DensityUtils.setLinearMWTopMargin(-2, linearHidden);
        }
    }

    private void scalingAnim() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        w = metric.widthPixels;
        h = metric.heightPixels;
        int densityDpi = metric.densityDpi;  // 像素密度DPI（120 / 160 / 240/320
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvStartAnim.getLayoutParams();
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) relAnim.getLayoutParams();
        if (densityDpi == 480) {
            layoutParams.width = 111;
            layoutParams.height = 111;
            layoutParams.rightMargin = 71;
            layoutParams.bottomMargin = -120;
            layoutParams1.height = 135;
            relAnim.setLayoutParams(layoutParams1);
            tvStartAnim.setLayoutParams(layoutParams);
            tvAnimBg.setLayoutParams(layoutParams);
        }
    }

    private void startHandlerAnim() {//这个是隐藏动画获取高度的
        tvStartAnim.setClickable(false);
        mDensity = getResources().getDisplayMetrics().density;
        mHiddenViewMeasureHeight = (int) (mDensity * 124);
        if (!isCaptureBack) {
            mHandler.sendEmptyMessageDelayed(Contants.FLAG_START_HANDLER_ANIM, 1000);
        } else {
            isCaptureBack = false;
        }
    }

    //打开隐藏部分
    private void animateOpen(LinearLayout linear_hidden) {
        tvStartAnim.setClickable(false);
        linear_hidden.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(linear_hidden, 0, mHiddenViewMeasureHeight);
        animator.setDuration(400);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tvStartAnim.setClickable(true);
                tvAnimUnder.setClickable(false);
            }
        });
        animator.start();
    }

    private void animateClose(final View view) {
        tvStartAnim.setClickable(false);
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                tvStartAnim.setClickable(true);
                tvAnimUnder.setClickable(true);
            }
        });
        animator.setDuration(400);
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                Integer animatedValue = (Integer) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = animatedValue;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


    /**
     * 圆圈的展开动画
     */
    private void animationIvRotateOpen() {
        tvStartAnim.setClickable(false);
        android.animation.ObjectAnimator animatior1 = android.animation.ObjectAnimator.ofFloat(tvAnimBg, "alpha", 0.6f, 1f);
        android.animation.ObjectAnimator animatior2 = android.animation.ObjectAnimator.ofFloat(tvStartAnim, "alpha", 0.6f, 1f);
        android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofFloat(tvStartAnim, "rotation", 360f, 180f);
        AnimatorSet set = new AnimatorSet();
        set.play(animator).with(animatior1).with(animatior2);
        set.setDuration(400);
        set.start();
        set.addListener(this);
    }

    /**
     * 圆圈的关闭动画
     */
    private void animationIvRoateClose() {
        tvStartAnim.setClickable(false);
        android.animation.ObjectAnimator animatior1 = android.animation.ObjectAnimator.ofFloat(tvAnimBg, "alpha", 1f, 0.6f);
        android.animation.ObjectAnimator animatior2 = android.animation.ObjectAnimator.ofFloat(tvStartAnim, "alpha", 1f, 0.6f);
        android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofFloat(tvStartAnim, "rotation", 180f, 360f);
        AnimatorSet set = new AnimatorSet();
        set.play(animator).with(animatior1).with(animatior2);
        set.setDuration(400);
        set.start();
        set.addListener(this);
    }

    private void handlerOnClickListener() {
        //进入扫码
        imageView.setOnClickListener(new OnClickListenerHandler(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkUtil.CheckConnection(mContext)) {
                    showNetDialog();
                    return;
                }
                if (tvDingwei.getText().equals("未定位")) {
                    showWeiDingwei();
                    return;
                }
                requestEventCode("a002");
//                if (isDingweiCancel) {
//                    showWeiDingwei();
//                    return;
//                }
                mHandler.sendEmptyMessage(MSG_SWITCH_CAPTURE);
            }
        }, getActivity(), FunnelContants.FUNNEL2_CAMERA_CLICKED).create());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.y("#######################onResume##################");
        MobclickAgent.onPageStart(TAG);
        boolean isDingwei = (boolean) SharePreferenceUtil.get(mContext, SharePreferenceUtil.KEY_DW_STATUS, false);
        //选择的城市
        currentChooseCity = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.CITY_TOP_CHOOSE, "");
        currentChooseProvince = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "");
        LogUtil.y( "###currentChooseProvince###" + currentChooseProvince + "#####currentChooseCity#####" + currentChooseCity);
        isFirstLunch = (boolean) SharePreferenceUtil.get(mContext, SharePreferenceUtil.IS_LAUNCH, false);
        if (!isDingwei) {
            if (isFirstLunch) {
                LogUtil.y(TAG + "#未定位##首次进入################");
                tvDingwei.setText("未定位");
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.IS_LAUNCH, false);
                if(!isDingweiCancel){
                    showWeiDingwei();
                }
                if (!TextUtils.isEmpty(currentChooseCity)) {
                    dingwei();
                }
            } else {
                LogUtil.y(TAG + "#未定位##二次进入################");
                if (!TextUtils.isEmpty(currentChooseCity)) {
                    showCityText();
                } else {
                    dingwei();
                }
            }
        } else {
            if (isFirstLunch) {
                LogUtil.y(TAG + "#已定位##首次进入################");
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.IS_LAUNCH, false);
                if (!TextUtils.isEmpty(currentChooseCity)) {
                    showCityText();
                }
                dingwei();
            } else {
                boolean isdingwei = (boolean) SharePreferenceUtil.get(mContext, SharePreferenceUtil.IS_DINGWEI, false);
                LogUtil.y(TAG + "#已定位##二次进入################isdingwei" + isdingwei);
                if (isdingwei) {
                    SharePreferenceUtil.put(mContext, SharePreferenceUtil.IS_DINGWEI, false);
                    dingwei();
                } else {
                    showCityText();
                }
            }
        }
        listHistorySearch = SPCacheList.getInstance().readDrugSearchHistory();
        if (listHistorySearch.size() == 0) {
            goodsSearchClear.setVisibility(View.GONE);
            mAdapter.setEmptyView();
        } else {
            goodsSearchClear.setVisibility(View.VISIBLE);
            mAdapter.setList(listHistorySearch);
        }
        if (listImageUrl == null) {
            listImageUrl = SPCacheList.getInstance().readLisImageBean();
        }
        if (listImageUrl.size() > 0) {
            advertisementAdapter = new BiyaoStatePagerAdapter(mContext, getActivity().getSupportFragmentManager(), listImageUrl);
            headerViewpager.setAdapter(advertisementAdapter);
            headerIndicator.setCount(listImageUrl.size());
            headerIndicator.setViewPager(headerViewpager);
            headerIndicator.setAutoScroll();
            if (listImageUrl.size() == 1) {
                headerIndicator.setVisibility(View.GONE);
            } else {
                headerIndicator.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 选择的城市
     */
    private void showCityText() {
        currentChooseProvince = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "");
        currentChooseCity = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.CITY_TOP_CHOOSE, "");
        if (TextUtils.isEmpty(currentChooseCity)) {
            dingwei();
        }
        tvDingwei.setText(StringUtils.getCity(mContext, currentChooseCity));
    }

    /**
     * 如果没有选择城市，则认为定位的城市是已选择的城市
     */
    private void showCityTextDingwei() {
        LogUtil.y("没有选择城市认为当前定位城市就是已选择的城市");
        //定位的省份
        dingweiProvince = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.PROVINCE, "");
        //定位的城市
        dingweiCity = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.CITY, "");
        SharePreferenceUtil.put(mContext, SharePreferenceUtil.PROVICE_TOP_CHOOSE, dingweiProvince);
        SharePreferenceUtil.put(mContext, SharePreferenceUtil.CITY_TOP_CHOOSE, dingweiCity);
        if (TextUtils.isEmpty(dingweiCity)) {
//            showDingweiDialog();
            showWeiDingwei();
            dingweiCity = "未定位";
            LogUtil.y(TAG + "没有选择城市认为当前定位城市就是已选择的城市");

        }
        tvDingwei.setText(StringUtils.getCity(mContext, dingweiCity));
    }

    /**
     * 搜索药品
     */
    private void requestDrugList(String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_SEARCH_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_SEARCH_SIGN);
        params.put(HttpConstant.PARAM_KEY_PROVINCE2, currentChooseCity);
        params.put(HttpConstant.PARAM_KEY_CODE2, code);
        params.put(HttpConstant.PARAM_KEY_KEYWORD2, "");
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX2, "0");
        request(params, HttpConstant.SEARCH_DRUG_LIST);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.BIYAO_BANNER:
                try {
                    TopBanner topBanner = JsonUtil.getModle(response.toString(), TopBanner.class);
                    TopBanner.DataBean dataBean = topBanner.getData();
                    if (dataBean != null) {
                        List<TopBanner.DataBean.ImagesBean> listImage = dataBean.getImages();
                        SPCacheList.getInstance().writeListImageBean(listImage);
                        advertisementAdapter = new BiyaoStatePagerAdapter(mContext, getActivity().getSupportFragmentManager(), listImage);
                        headerViewpager.setAdapter(advertisementAdapter);
                        headerIndicator.setCount(listImage.size());
                        headerIndicator.setViewPager(headerViewpager);
                        headerIndicator.setAutoScroll();
                        if (listImage.size() == 1) {
                            headerIndicator.setVisibility(View.GONE);
                        } else {
                            headerIndicator.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {

                }
                break;
            case HttpConstant.SEARCH_DRUG_LIST:
                JSONObject data = null;
                try {
                    data = response.getJSONObject(HttpConstant.DATA);
                    int count = data.getInt(HttpConstant.COUNT);
                    Type type = new TypeToken<ArrayList<DrugBean>>() {
                    }.getType();

                    List<DrugBean> list = JsonUtil.getListModle(data.toString(), HttpConstant.INFO, type);
                    if (count == 1) {
                        DrugBean drugBean = list.get(0);
                        int type1 = drugBean.getType();
                        if (type1 == 0) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, list.get(0));
                            bundle.putInt(Contants.KEY_PAGE, Contants.VAL_PAGE_SEARCH_DURUG);
                            bundle.putString(Contants.KEY_STR_KEYWORD_CODE, list.get(0).getCode());
                            bundle.putInt(Contants.KEY_PAGE_SEARCH, Contants.VAL_PAGE_SEARCH_SCAN);
                            openActivity(ResultsActivity.class, bundle);
                        } else if (type1 == 1) {
                            switchDrugNoDetailActivity(drugBean);
                        } else {
                            LogUtil.e(TAG, "error,default switchResultActivity");
                        }
                    } else {
                        if (mHistoryCache != null) {
                            Bundle mBundle = new Bundle();
                            int typed = mHistoryCache.getCacheType();
                            if (typed == HistoryCache.TYPE_CACHE_SCAN) {
                                mBundle.putString(Contants.KEY_STR_KEYWORD_CODE, mHistoryCache.getCode());
                                String keyword;
                                String goodsname = mHistoryCache.getGoodsName();
                                if (!TextUtils.isEmpty(goodsname)) {
                                    keyword = goodsname + " " + mHistoryCache.getName();
                                } else {
                                    keyword = mHistoryCache.getName();
                                }
                                mBundle.putString(Contants.KEY_STR_KEYWORD, keyword);
                                mBundle.putInt(Contants.KEY_PAGE_SEARCH, Contants.VAL_PAGE_SEARCH_SCAN);
                                openActivity(SearchActivity.class, mBundle);
                            }
                        }
                    }
                    mHistoryCache.setCacheType(HistoryCache.TYPE_CACHE_SCAN);
                    mHistoryCache.setName(list.get(0).getName());
                    mHistoryCache.setGoodsName(list.get(0).getGoodsName());
                    mHistoryCache.setCode(scanCode);
                    SPCacheList.getInstance().writeDrugScanHistory(mHistoryCache);
                } catch (JSONException e) {
                } catch (Exception e) {

                }
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
            if (errorResponse.isNetError()) {
                ToastUtil.showToastCenter(mContext, "网络不给力");
            } else {
                ToastUtil.showToastCenter(mContext, errorResponse.getMsg());
            }

        } catch (Exception e) {

        }
    }

    private void dingwei() {
        if (!NetworkUtil.CheckConnection(mContext)) {
            showNetDialog();
            return;
        }
        locService.start();
    }


    /**
     * 是否切换弹窗
     */
    public void showDingweiDialogQieHuan(final String province, String city, final String longitude, final String latitude, final String addrStr) {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(getActivity());
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText("定位到您所在的城市是" + StringUtils.getCity(mContext, city) + ",是否切换?");
        twoButton.getTvOk().setText("是");
        twoButton.getTvCancel().setText("否");
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        final String finalCity = city;
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharePreferenceUtil.put(mContext, SharePreferenceUtil.LONGITUDE, longitude + "");
                    SharePreferenceUtil.put(mContext, SharePreferenceUtil.LATITUDE, latitude + "");
                    SharePreferenceUtil.put(mContext, SharePreferenceUtil.LONGITUDE, longitude + "");
                    SharePreferenceUtil.put(mContext, "positionlat", latitude + "");
                    SharePreferenceUtil.put(mContext, "positionlng", longitude + "");
                    if (province != null) {
                        SharePreferenceUtil.put(mContext, SharePreferenceUtil.PROVINCE, "" + StringUtils.getProvice(province));
                    }
                    if (finalCity != null) {
                        SharePreferenceUtil.put(mContext, SharePreferenceUtil.CITY, "" + finalCity);
                    }
                    if (addrStr != null) {
                        SharePreferenceUtil.put(mContext, SharePreferenceUtil.ADDRESS, "" + addrStr);
                        SharePreferenceUtil.put(mContext, "positionaddress", "" + addrStr);
                    }
                    showCityTextDingwei();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(linearAll, Gravity.CENTER);
    }

    //百度地图监听
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            String city = location.getCity();
            String addrStr = location.getAddrStr();
            String province = location.getProvince();
            SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_DW_STATUS, true);
            if (longitude != 4.9E-324 && latitude != 4.9E-324) {
                isDingweiCancel=false;
                locService.unregisterListener(mListener);
                locService.stop();
                if (!StringUtils.getCity(mContext, currentChooseCity).equals(StringUtils.getCity(mContext, city)) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(currentChooseCity)) {
                    showDingweiDialogQieHuan(province, city, "" + longitude, "" + latitude, addrStr);
                } else {
                    if (province != null) {
                        SharePreferenceUtil.put(mContext, SharePreferenceUtil.PROVINCE, "" + StringUtils.getProvice(province));
                    }
                    if (city != null) {
                        SharePreferenceUtil.put(mContext, SharePreferenceUtil.CITY, "" + city);
                    }
                    SharePreferenceUtil.put(mContext, SharePreferenceUtil.LONGITUDE, longitude + "");
                    SharePreferenceUtil.put(mContext, SharePreferenceUtil.LATITUDE, latitude + "");
                    showCityTextDingwei();
                }
            } else {
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.PROVINCE, "");
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.CITY, "");
                SharePreferenceUtil.put(mContext, "positionaddress", "定位失败，请重新定位");
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_DW_STATUS, false);
                if (i == 1) {
                    tvDingwei.setText("未定位");
                    showWeiDingwei();
                    LogUtil.y(TAG + "未定位呢");
                    locService.unregisterListener(mListener);
                    locService.stop();
                }
                i++;
            }
        }
    }

    private void showWeiDingwei() {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean isTemp=requestCODE_LOCATION();
            if(isTemp){
                final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(getActivity());
                twoButton.getTvTitle().setVisibility(View.INVISIBLE);
                twoButton.getTv_content().setText("定位未开启，是否开启定位？");
                twoButton.getTvOk().setText("去设置");
                twoButton.getTvCancel().setText("取消");
                twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isDingweiCancel = true;
                        twoButton.dismiss();
                    }
                });
                twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            PermissionUtil.getInstance().showDingwei();
                            SharePreferenceUtil.put(mContext, SharePreferenceUtil.IS_DINGWEI, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        twoButton.dismiss();
                    }
                });
                twoButton.showPopupWindow(linearAll, Gravity.CENTER);
            }
        } else {
            final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(getActivity());
            twoButton.getTvTitle().setVisibility(View.GONE);
            twoButton.getTv_content().setText("定位未开启，是否开启定位？");
            twoButton.getTvOk().setText("去设置");
            twoButton.getTvCancel().setText("取消");
            twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isDingweiCancel = true;
                    twoButton.dismiss();
                }
            });
            twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        PermissionUtil.getInstance().showDingwei();
                        SharePreferenceUtil.put(mContext, SharePreferenceUtil.IS_DINGWEI, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    twoButton.dismiss();
                }
            });
            twoButton.showPopupWindow(linearAll, Gravity.CENTER);
        }

    }

    @Override
    public void onStop() {
        locService.unregisterListener(mListener); //注销掉监听
        locService.stop(); //停止定位服务
        super.onStop();
    }

    //是否清除历史记录
    private void popupClearHistory() {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(getActivity());
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText("确认删除查询历史?");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvOk().setText("确认");
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHistory();
                twoButton.dismiss();
                requestEventCode("d004");

            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(linearAll, Gravity.CENTER);
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        tvStartAnim.setClickable(true);
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    private void switchDrugNoDetailActivity(DrugBean drugBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        openActivity(DrugNoDetailActivity.class, bundle);
    }

    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
    public boolean requestCODE_LOCATION() {//位置信息
        String perms[] = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        boolean isTemp=EasyPermissions.hasPermissions(mContext, perms);
        if (!isTemp) {
            EasyPermissions.requestPermissions(this, "定位权限已禁止，确认设置授权？",REQUEST_CODE_LOCATION, perms);
        }
        return isTemp;
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        SharePreferenceUtil.put(mContext, SharePreferenceUtil.IS_DINGWEI, true);
        LogUtil.y("定位权限被授予");
        SharePreferenceUtil.put(mContext, SharePreferenceUtil.IS_LAUNCH, false);
        isDingweiCancel = false;
    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        LogUtil.y("定位权限被拒绝");
        tvDingwei.setText("未定位");
        SharePreferenceUtil.put(mContext, SharePreferenceUtil.IS_LAUNCH, true);
        isDingweiCancel = true;
    }


    @OnClick({R.id.iv_history_clear, R.id.tv_search, R.id.tv_dingwei, R.id.linear_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_history_clear:
                popupClearHistory();
                break;
            case R.id.tv_search:
                requestEventCode("a001");
                if (startAnim == false) {
                    toLeft = true;
                }
                if (tvDingwei.getText().equals("未定位")) {
                    showWeiDingwei();
                    return;
                }
                Bundle mBundle = new Bundle();
                mBundle.putInt(Contants.KEY_PAGE_SEARCH, Contants.VAL_PAGE_SEARCH_DURUG);
                openActivity(SearchActivity.class, mBundle);
                break;
            case R.id.tv_dingwei://定位
                requestEventCode("a004");
                if (!NetworkUtil.CheckConnection(mContext)) {
                    showNetDialog();
                    return;
                }
                Intent intent = new Intent(mContext, CitylistSearchActivity.class);
                intent.putExtra("dingwei", tvDingwei.getText().toString());
                startActivity(intent);
                break;
            case R.id.linear_all:
                break;
        }
    }

    @Override
    public void onDestroy() {
        locService.unregisterListener(mListener);
        locService.stop();
        locService=null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }


    @AfterPermissionGranted(Contants.REQUEST_PERMISSION_CAMERA)
    public boolean  requestCODE_CAMERA(){//相机
        String perms[] ={Manifest.permission.CAMERA,Manifest.permission.VIBRATE,Manifest.permission.FLASHLIGHT} ;
        boolean isTemp=EasyPermissions.hasPermissions(mContext, perms);
        if (!isTemp) {
            EasyPermissions.requestPermissions(this,"医保通申请相机权限",Contants.REQUEST_PERMISSION_CAMERA, perms);
        }
        return isTemp;
    }

}

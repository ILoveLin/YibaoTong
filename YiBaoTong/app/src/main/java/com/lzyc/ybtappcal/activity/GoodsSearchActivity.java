package com.lzyc.ybtappcal.activity;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.payment.order.OrderbyActivity;
import com.lzyc.ybtappcal.adapter.HomeGoodsSearchAdapter;
import com.lzyc.ybtappcal.adapter.SearchDimGoodsAdapter;
import com.lzyc.ybtappcal.adapter.SearchHistoryGoodsAdapter;
import com.lzyc.ybtappcal.adapter.SearchHotGoodsAdapter;
import com.lzyc.ybtappcal.bean.DimGoods;
import com.lzyc.ybtappcal.bean.GoodsSearch;
import com.lzyc.ybtappcal.bean.HistoryCache;
import com.lzyc.ybtappcal.bean.HotGoods;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.KeyBoardUtils;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.cache.SPCacheList;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.ClearEditTextView;
import com.lzyc.ybtappcal.widget.CustomGridView;
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

/**
 * 商品的搜索
 * Created by yang on 2017/05/02.
 */
public class GoodsSearchActivity extends BaseActivity {
    private static final String TAG=GoodsSearchActivity.class.getSimpleName();
    @BindView(R.id.linear_titlebar)
    LinearLayout linearTitleBar;
    @BindView(R.id.linear_edit)
    LinearLayout linearEdit;//可编辑的标题栏
    @BindView(R.id.linear_edit_none)
    LinearLayout linearEditNone;//不可编辑的标题栏
    @BindView(R.id.et_input)
    ClearEditTextView etInput;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.gv_hot)
    CustomGridView goodsGridviewHot;//热门搜索标签
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.gv_history)
    CustomGridView gvHistory;//历史记录列表
    @BindView(R.id.nested_search_scroll)
    NestedScrollView nestedSearchScroll;//热门搜索标签，历史记录整体布局
    @BindView(R.id.rel_history_bar)
    RelativeLayout relHistoryBar;
    @BindView(R.id.rel_history_bar_top)
    RelativeLayout relHistoryBarTop;
    @BindView(R.id.rel_hot_his)
    RelativeLayout relHotHis;//热门搜索和历史记录
    @BindView(R.id.rel_drug_list)
    RelativeLayout relDrugList;//商品列表
    @BindView(R.id.lv_dim)
    ListView lvvDim;//联想搜索列表
    @BindView(R.id.rel_bottom)
    RelativeLayout relBottom;
    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.tv_input)
    TextView tvInput;
    @BindView(R.id.lv_druglist)
    ListView lvDrugList;//药品列表
    @BindView(R.id.rel_search)
    RelativeLayout relSearch;

    @BindView(R.id.scroll_list)
    TwinklingRefreshLayout scrollList;
    @BindView(R.id.scroll_search)
    TwinklingRefreshLayout scrollSearch;
    private ArrayList<HistoryCache> historySearches;
    private SearchHistoryGoodsAdapter historyGoodsAdapter;
    private List<DimGoods> listDim;
    private SearchDimGoodsAdapter dimAdapter;//联想搜索
    private PopupWindowTwoButton twoButton;
    private boolean isItemClicked = false;
    private HomeGoodsSearchAdapter goodsSearchAdapter;//商品列表
    private boolean isInit = false;
    private String keywold;//记住搜索关键字
    private List<HotGoods> listHot;
    private SearchHotGoodsAdapter hotAdapter;//热门搜索
    private int what;//网络回调，用来做没网状态，点击重试按钮请求对应的网络请求
    private List<GoodsSearch> goodsSearchList;
    private String hintText;

    @Override
    public int getContentViewId() {
        isInit = true;
        return R.layout.activity_search_goods;
    }

    @Override
    protected void init() {
        setTitleBarVisibility(View.GONE);
        setPageStateView(relBottom);
        scrollList.setPureScrollModeOn();
        scrollSearch.setPureScrollModeOn();
        hintText = getIntent().getExtras().getString(Contants.KEY_HINT_SEARCH);
        etInput.setHint(hintText);
//        OverScrollDecoratorHelper.setUpStaticOverScroll(nestedSearchScroll, ORIENTATION_VERTICAL);
        showLoading();
        listDim = new ArrayList<>();
        historySearches = new ArrayList<>();
        goodsSearchList = new ArrayList<>();
        listHot = new ArrayList<>();
        hotAdapter = new SearchHotGoodsAdapter(GoodsSearchActivity.this, R.layout.item_search_hot, listHot);
        goodsGridviewHot.setAdapter(hotAdapter);
        historyGoodsAdapter = new SearchHistoryGoodsAdapter(GoodsSearchActivity.this, R.layout.item_search_hot, historySearches);
        gvHistory.setAdapter(historyGoodsAdapter);
        goodsGridviewHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isItemClicked = true;
                HotGoods hotGoods = (HotGoods) parent.getItemAtPosition(position);
                HistoryCache historySearch = new HistoryCache();
                historySearch.setName(hotGoods.getName());
                historySearch.setCacheType(HistoryCache.TYPE_CACHE_SEARCH);
                SPCacheList.getInstance().writeGoodsSearchHistory(historySearch);
                etInput.setText(hotGoods.getName());
                etInput.setSelection(hotGoods.getName().length());
            }
        });
        gvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isItemClicked = true;
                HistoryCache historySearch = (HistoryCache) parent.getItemAtPosition(position);
                historySearch.setCacheType(HistoryCache.TYPE_CACHE_SEARCH);
                SPCacheList.getInstance().writeGoodsSearchHistory(historySearch);
                if (!TextUtils.isEmpty(historySearch.getGoodsName())) {
                    etInput.setText(historySearch.getGoodsName());
                    etInput.setSelection(historySearch.getGoodsName().length());
                } else {
                    etInput.setText(historySearch.getName());
                    etInput.setSelection(historySearch.getName().length());
                }
            }
        });
        nestedSearchScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                etInput.clearFocus();
                KeyBoardUtils.closeKeybord(etInput, GoodsSearchActivity.this);
                if (scrollY > relHistoryBar.getTop()) {
                    relHistoryBarTop.setVisibility(View.VISIBLE);
                } else {
                    relHistoryBarTop.setVisibility(View.GONE);
                }
            }
        });
        dimAdapter = new SearchDimGoodsAdapter(this, R.layout.item_search_dim, listDim);
        lvvDim.setAdapter(dimAdapter);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyworld = etInput.getText().toString();
                    if (TextUtils.isEmpty(keyworld)) {
                        showToast("请输入搜索内容");
                        return true;
                    }
                    HistoryCache historySearch = new HistoryCache();
                    historySearch.setName(keyworld);
                    historySearch.setCacheType(HistoryCache.TYPE_CACHE_SEARCH);
                    SPCacheList.getInstance().writeGoodsSearchHistory(historySearch);
                    requestGoodsSearchList(keyworld);
                    return true;
                }
                return false;
            }
        });
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyworld = etInput.getText().toString();
                if (TextUtils.isEmpty(keyworld)) {
                    showHisANDHotView();
                } else {
                    if (isItemClicked) {
                        isItemClicked = false;
                        requestGoodsSearchList(keyworld);
                    } else {
                        requestGoodsDimSearchList(keyworld);
                    }
                }
            }
        });
        lvvDim.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DimGoods mDimGoods = (DimGoods) adapterView.getItemAtPosition(position);
                showGoodsListRelative();
                String keyword;
                String goodsName = mDimGoods.getGoodsName();
                String name = mDimGoods.getName();
                if (!TextUtils.isEmpty(goodsName)) {
                    keyword = goodsName+" "+name;
                } else {
                    keyword = mDimGoods.getName();
                }
                HistoryCache historyCache = new HistoryCache();
                historyCache.setCacheType(HistoryCache.TYPE_CACHE_SEARCH);
                historyCache.setName(mDimGoods.getName());
                historyCache.setGoodsName(mDimGoods.getGoodsName());
                SPCacheList.getInstance().writeGoodsSearchHistory(historyCache);
                requestGoodsSearchList(keyword);
            }
        });
        requestGoodsHotSearch();
        goodsSearchAdapter = new HomeGoodsSearchAdapter(this, R.layout.item_goods_search, goodsSearchList);
        lvDrugList.setAdapter(goodsSearchAdapter);
        lvDrugList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                GoodsSearch goodsSearch = (GoodsSearch) adapterView.getItemAtPosition(position);
                Bundle mBundle = new Bundle();
                mBundle.putInt(Contants.KEY_PAGE_PAYMENT, Contants.VAL_PAGE_PAYMENT_CART);
                mBundle.putString(HttpConstant.SHOP_GOODS_PARAM_DKID, goodsSearch.getDKID());
                openActivity(OrderbyActivity.class, mBundle);
            }
        });
    }

    /**
     * 历史记录是否显示空
     */
    private void showHistoryViewOrContent() {
        historySearches = SPCacheList.getInstance().readGoodsSearchHistory();
        historyGoodsAdapter.setList(historySearches);
        etInput.requestFocus();
        if (historySearches.size() == 0) {
            relHistoryBar.setVisibility(View.GONE);
            gvHistory.setVisibility(View.GONE);
        } else {
            relHistoryBar.setVisibility(View.VISIBLE);
            gvHistory.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.title_back, R.id.tv_input, R.id.tv_cancel, R.id.iv_clear, R.id.iv_history_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                GoodsSearchActivity.this.finish();
                break;
            case R.id.tv_cancel:
                if (isInit) {
                    etInput.clearFocus();
                    KeyBoardUtils.closeKeybord(etInput, GoodsSearchActivity.this);
                    GoodsSearchActivity.this.finish();
                    return;
                }
                showGoodsListRelative();
                break;
            case R.id.iv_clear:
            case R.id.iv_history_clear:
                historySearches = SPCacheList.getInstance().readGoodsSearchHistory();
                if (historySearches.size() == 0) {
                    KeyBoardUtils.closeKeybord(etInput, GoodsSearchActivity.this);
                    return;
                }
                if (twoButton == null) {
                    popupClearHistoryCreate();
                    popupClearHistoryShow();
                } else if (twoButton.isShowing()) {
                    twoButton.dismiss();
                } else {
                    popupClearHistoryShow();
                }
                break;
            case R.id.tv_input:
                String keyworld = tvInput.getText().toString();
                etInput.setText(keyworld);
                etInput.setSelection(keyworld.length());
                isItemClicked = false;
                showDimView();
                break;
        }
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        isItemClicked = false;
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    //显示，隐藏界面
    /*
     *显示历史和热门标签
     */
    private void showHisANDHotView() {
        showContent();
        showHistoryViewOrContent();
        etInput.requestFocus();
        KeyBoardUtils.openKeybord(etInput, this);
        relHotHis.setVisibility(View.VISIBLE);
        lvvDim.setVisibility(View.GONE);
        relDrugList.setVisibility(View.GONE);
        tvCancel.setVisibility(View.VISIBLE);
        linearEdit.setVisibility(View.VISIBLE);
        linearEditNone.setVisibility(View.GONE);
    }

    /*
     *显示联想搜索
     */
    public void showDimView() {
        titleBack.setVisibility(View.GONE);
        showContent();
        etInput.requestFocus();
        KeyBoardUtils.openKeybord(etInput, this);
        lvvDim.setVisibility(View.VISIBLE);
        tvCancel.setVisibility(View.VISIBLE);
        relHotHis.setVisibility(View.GONE);
        relDrugList.setVisibility(View.GONE);
        linearEdit.setVisibility(View.VISIBLE);
        linearEditNone.setVisibility(View.GONE);
    }

    /*
     *显示商品列表
     */
    public void showGoodsListRelative() {
        isInit = false;
        titleBack.setVisibility(View.VISIBLE);
        showContent();
        etInput.clearFocus();
        KeyBoardUtils.closeKeybord(etInput, this);
        lvvDim.setVisibility(View.GONE);
        relHotHis.setVisibility(View.GONE);
        tvCancel.setVisibility(View.GONE);
        relDrugList.setVisibility(View.VISIBLE);
        linearEdit.setVisibility(View.GONE);
        linearEditNone.setVisibility(View.VISIBLE);
        tvInput.setText(keywold);
    }
//网络

    /**
     * 获取商品热门搜索列表
     */
    private void requestGoodsHotSearch() {
        this.what = HttpConstant.GOODS_SERACH_HOT;
        if (!NetworkUtil.CheckConnection(this)) {
            showError();
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_SEARCH_GOODS_HOT_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_SEARCH_GOODS_HOT_SIGN);
        request(params, HttpConstant.GOODS_SERACH_HOT);

    }

    /**
     * 获取商品搜索结果列表
     */
    private void requestGoodsSearchList(String keyworld) {
        this.what = HttpConstant.GOODS_SERACH_LIST;
        keywold = keyworld;
        if (!NetworkUtil.CheckConnection(this)) {
            KeyBoardUtils.closeKeybord(etInput, this);
            showError();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_SEARCH_GOODS_LIST_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_SEARCH_GOODS_LIST_SIGN);
        params.put(HttpConstant.PARAM_KEY_KEYWORD, keyworld);
        request(params, HttpConstant.GOODS_SERACH_LIST);
    }

    /**
     * 获取商品联想搜索列表
     */
    private void requestGoodsDimSearchList(String keyworld) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_SEARCH_GOODS_DIM_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_SEARCH_GOODS_DIM_SIGN);
        params.put(HttpConstant.PARAM_KEY_KEYWORD, keyworld);
        request(params, HttpConstant.GOODS_SERACH_DIM);
    }


    @Override
    public void done(String msg, int what, JSONObject response) {
        switch (what) {
            case HttpConstant.GOODS_SERACH_DIM:
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
//                    int count = data.getInt("count");
//                    LogUtil.y("联想搜索的数据是" + count);
                    Type type = new TypeToken<ArrayList<DimGoods>>() {
                    }.getType();
                    List<DimGoods> list = JsonUtil.getListModle(data.toString(), "list", type);
                    dimAdapter.setList(list);
                    showDimView();
                } catch (JSONException e) {
                    e.printStackTrace();
//                    showToast(errorNetToast);
                } catch (Exception e) {
//                    showToast(errorNetToast);
                }
                break;
            case HttpConstant.GOODS_SERACH_LIST:
                try {
                    showGoodsListRelative();
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
//                    int count = data.getInt("count");
                    Type type = new TypeToken<ArrayList<GoodsSearch>>() {
                    }.getType();
                    List<GoodsSearch> list = JsonUtil.getListModle(data.toString(), "list", type);
                    goodsSearchAdapter.setList(list);
                    Type typeRecommend = new TypeToken<ArrayList<GoodsSearch>>() {
                    }.getType();
                    List<GoodsSearch> listRecommend = JsonUtil.getListModle(data.toString(), "recommend", typeRecommend);
                    goodsSearchAdapter.addListRecommend(listRecommend);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                }
                break;
            case HttpConstant.GOODS_SERACH_HOT:
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    Type type = new TypeToken<ArrayList<HotGoods>>() {
                    }.getType();
                    List<HotGoods> listHot = JsonUtil.getListModle(data.toString(), "list", type);
                    hotAdapter.setList(listHot);
                    showContent();
                    showHisANDHotView();
                } catch (Exception e) {
                    showEmpty(errorServer, R.mipmap.empty_error_net_server);
                }
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
            String msg = errorResponse.getMsg();
            if (!TextUtils.isEmpty(msg)) {
                showToast(msg);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        if (isInit) {
            GoodsSearchActivity.this.finish();
            return;
        }
        if (linearEditNone.getVisibility() == View.VISIBLE) {
            GoodsSearchActivity.this.finish();
            return;
        }
        showGoodsListRelative();
    }

    //是否清除历史记录
    private void popupClearHistoryCreate() {
        twoButton = new PopupWindowTwoButton(this);
        twoButton.getTvOk().setVisibility(View.VISIBLE);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText("确认清空历史记录?");
        twoButton.getTvCancel().setText(determine2);
        twoButton.getTvOk().setText(cancel);
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.setCursorVisible(true);
                twoButton.dismiss();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.setCursorVisible(true);
                twoButton.dismiss();
                SPCacheList.getInstance().clearHistory(SharePreferenceUtil.SPACE_HISTORY_GOODS);
                historySearches = SPCacheList.getInstance().readHistory(SharePreferenceUtil.SPACE_HISTORY_GOODS);
                historyGoodsAdapter.setList(historySearches);
                showHistoryViewOrContent();
            }
        });
    }

    public void popupClearHistoryShow() {
        etInput.setCursorVisible(false);
        KeyBoardUtils.closeKeybord(etInput, GoodsSearchActivity.this);
        etInput.clearFocus();
        twoButton.showPopupWindow(lvvDim, Gravity.CENTER);
    }

    /**
     * 点击了错误重试
     */
    protected void onClickRetry() {
        switch (what) {
            case HttpConstant.GOODS_SERACH_HOT:
                requestGoodsHotSearch();
                break;
            case HttpConstant.GOODS_SERACH_LIST:
                requestGoodsSearchList(keywold);
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isClickEt(view, event)) {
                etInput.clearFocus();
                KeyBoardUtils.closeKeybord(view, GoodsSearchActivity.this);
            }
            return super.dispatchTouchEvent(event);
        }
        if (getWindow().superDispatchTouchEvent(event)) {
            return true;
        }
        return onTouchEvent(event);
    }

    /**
     * 获取当前点击位置是否为et
     *
     * @param view  焦点所在View
     * @param event 触摸事件
     * @return
     */
    public boolean isClickEt(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] leftTop = {0, 0};
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else if (event.getY() < 200 || 980 < event.getY() && event.getY() < 1060) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}

package com.lzyc.ybtappcal.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.mine.medicine.MedicinePersonActivity;
import com.lzyc.ybtappcal.activity.top.DrugNoDetailActivity;
import com.lzyc.ybtappcal.activity.top.ResultsActivity;
import com.lzyc.ybtappcal.adapter.DrugDimAdapter;
import com.lzyc.ybtappcal.adapter.DrugsListAdapter;
import com.lzyc.ybtappcal.adapter.SearchHotAdapter;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.bean.HistoryCache;
import com.lzyc.ybtappcal.bean.HotDrug;
import com.lzyc.ybtappcal.bean.MedicineFamilyBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.KeyBoardUtils;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.util.cache.SPCacheList;
import com.lzyc.ybtappcal.view.YbtListView;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.ClearEditTextView;
import com.lzyc.ybtappcal.widget.CustomGridView;
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
import butterknife.OnClick;

/**
 * 药品搜索
 * Created by yang on 2017/05/02.
 */
public class SearchActivity extends BaseActivity implements DrugsListAdapter.OnItemClickListener {
    private static final String TAG=SearchActivity.class.getSimpleName();
    @BindView(R.id.ib_titlebar_left)
    ImageView titlebarLeft;
    @BindView(R.id.titlebar_right)
    TextView titlebarRight;
    @BindView(R.id.linear_edit)
    LinearLayout titlebarEdit;
    @BindView(R.id.search_linear_hot)
    LinearLayout searchLinearHot;
    @BindString(R.string.title_scan_results)
    String tiltleScanResults;
    @BindString(R.string.empty_drug_list)
    String emptyDrugList;
    @BindView(R.id.input_edit)
    ClearEditTextView inputEdit;//可编辑文本框
    @BindView(R.id.drug_contentview)
    RelativeLayout drugContentView;
    @BindView(R.id.content_view)
    LinearLayout contentView;
    @BindView(R.id.search_drug_num)
    TextView searchDrugNum;
    @BindView(R.id.search_scan_num)
    TextView searchDrugScanNum;
    @BindView(R.id.linear_drug_search)
    LinearLayout linearDrugSearch;
    @BindView(R.id.linear_search_scan)
    LinearLayout linearDrugScan;
    @BindView(R.id.search_drugbag)
    ImageView searchDrugbag;
    /*
     * 列表控件
     */
    @BindView(R.id.search_overscroll)
    TwinklingRefreshLayout searchOverscroll;//热门标签阻尼效果
    @BindView(R.id.search_hot_gv)
    CustomGridView searchHotGv;//热门标签
    @BindView(R.id.listview_dim)
    YbtListView dimListView;//联想词搜索
    @BindView(R.id.lv_search_druglist)
    XYbtRefreshListView mXYbtRefreshListView;//搜索药品结果列表
    /*
     *适配器
     */
    private SearchHotAdapter hotAdapter;//热门搜索
    private DrugDimAdapter dimAdapter;//联想搜索
    private DrugsListAdapter drugsListAdapter;//搜索药品结果
    /*
     *数据
     */
    private ArrayList<DrugBean> searchList;//联想搜索列表
    private ArrayList<DrugBean> drugBeanList;//药品结果列表
    private boolean isInit = false;
    private String keyword;
    private String lastKey;
    private int cPage = -1;
    private int typePage;
    private String keywordCode = "";//扫描编码
    private Bundle mBundle;
    private int pageIndexDim = 0;//联想词搜索分页
    private int pageIndexDurg = 0;//药品列表分页
    private boolean isItemClicked = false;
    private String cityTypeChoose;
    private String memberId;
    private MedicineFamilyBean.ListBean member;
    private int what;
    boolean isAdded;//添加用药-->是否刷新    lxx
    private ArrayList<String> mDrugIds = new ArrayList<>();

    @Override
    public int getContentViewId() {
        isInit = true;
        return R.layout.activity_search;
    }

    @Override
    protected void init() {
        mBundle = getIntent().getExtras();
        cityTypeChoose = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "北京");
        typePage = mBundle.getInt(Contants.KEY_PAGE_SEARCH, Contants.VAL_PAGE);
        setInitTitle();
//        if (searchDrugbag.getVisibility() == View.VISIBLE) {
//            startUPAndUnderAnimation(searchDrugbag);
//        }
    }


    /**
     * 设置标题
     */
    private void setInitTitle() {
        switch (typePage) {
            case Contants.VAL_PAGE_SEARCH_HISTORY://如果是搜索历史进来的
                searchOverscroll.setVisibility(View.GONE);
                drugContentView.setVisibility(View.VISIBLE);
                titlebarLeft.setVisibility(View.VISIBLE);
                setPageStateView(contentView);
                initEditContent(false);
                keyword = mBundle.getString(Contants.KEY_STR_KEYWORD);
                isItemClicked = true;
                inputEdit.setText(keyword);
                inputEdit.setSelection(keyword.length());
//                inputEdit.clearFocus();
                break;
            case Contants.VAL_PAGE_SEARCH_DURUG://如果是正常的搜索药品
                setPageStateView(contentView);
                initEditContent(true);
                break;
            case Contants.VAL_PAGE_SEARCH_SCAN://如果界面来自历史扫码
                showTtileBar();
                keywordCode = mBundle.getString(Contants.KEY_STR_KEYWORD_CODE);
                drugContentView.setVisibility(View.VISIBLE);
                setPageStateView(contentView);
                showLoading();
                initDrugListData();
                requestDrugList("");
                break;
            case Contants.VAL_PAGE_SEARCH_DRUG_ADD://如果界面来自添加用药
                searchLinearHot.setVisibility(View.INVISIBLE);
                searchDrugbag.setVisibility(View.VISIBLE);
                member = (MedicineFamilyBean.ListBean) getIntent().getExtras().getSerializable(Contants.KEY_MEDICINE_BEAN);
                memberId = member.getID();
                setPageStateView(contentView);
                showEditTtileBar();
                inputEdit.setHint("输入药品名称");
                searchOverscroll.setVisibility(View.VISIBLE);
                searchOverscroll.setPureScrollModeOn();
                inputEdit.requestFocus();
                KeyBoardUtils.openKeybord(inputEdit, this);
                initDimData();
                mXYbtRefreshListView.setPullRefreshEnable(false);
                initDrugListData();
                titlebarLeft.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 可编辑的
     */
    private void initEditContent(boolean isHot) {
        searchOverscroll.setVisibility(View.VISIBLE);
        searchOverscroll.setPureScrollModeOn();
        inputEdit.clearFocus();
        showEditTtileBar();
        initHotData();
        initDimData();
        //联想词搜索
        if(isHot){
            showLoading();
            requeHotSearchList();
        }
        initDrugListData();
    }


    private void initDrugListData() {
        drugBeanList = new ArrayList<>();
        drugsListAdapter = new DrugsListAdapter(this, R.layout.item_drugs_list_top, drugBeanList);
        drugsListAdapter.setOnItemClickListener(this);
        mXYbtRefreshListView.setAdapter(drugsListAdapter);
        mXYbtRefreshListView.setOnRefreshListener(new OnYbtRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                cPage = -1;
                pageIndexDurg = 0;
                if (typePage == Contants.VAL_PAGE_SEARCH_SCAN) {
                    requestDrugList("");
                } else {
                    String keyword = inputEdit.getText().toString();
                    if (TextUtils.isEmpty(keyword)) {
                        ToastUtil.showShort(SearchActivity.this, "关键字不能为空");
                        return;
                    }
                    requestDrugList(keyword);
                }
            }

            @Override
            public void onLoadingMore() {
                pageIndexDurg++;
                if (typePage == Contants.VAL_PAGE_SEARCH_SCAN) {
                    requestDrugList("");
                } else {
                    String key = inputEdit.getText().toString();
                    if (TextUtils.isEmpty(key)) {
                        ToastUtil.showShort(SearchActivity.this, "关键字不能为空");
                        return;
                    }
                    requestDrugList(key);
                }
            }
        });
//        mXYbtRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                DrugBean drugBean = (DrugBean) adapterView.getItemAtPosition(position);
//                if(typePage==Contants.VAL_PAGE_SEARCH_DRUG_ADD){
//
//                }else{
//                    switchResultActivity(drugBean);
//                }
//
//            }
//        });
    }

    /**
     * 热门搜索默认数据
     */
    private void initHotData() {
        List<HotDrug> listHot = new ArrayList<HotDrug>();
        hotAdapter = new SearchHotAdapter(this, R.layout.item_search_hot, listHot);
        searchHotGv.setAdapter(hotAdapter);
        searchHotGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HotDrug hotHistoryBean = (HotDrug) parent.getItemAtPosition(position);
                KeyBoardUtils.closeKeybord(inputEdit, SearchActivity.this);
                inputEdit.clearFocus();
                String key = hotHistoryBean.getDrug_name();
                HistoryCache historyCache = new HistoryCache();
                historyCache.setName("" + key);
                historyCache.setCacheType(HistoryCache.TYPE_CACHE_SEARCH);
                SPCacheList.getInstance().writeDrugSearchHistory(historyCache);
                requestEventCode("d002");
                pageIndexDurg = 0;
                isItemClicked = true;  //
                inputEdit.setText(key);
                inputEdit.setSelection(key.length());
            }
        });
    }

    private void initDimData() {
        searchList = new ArrayList<>();  //联想搜索列表
        dimAdapter = new DrugDimAdapter(this, R.layout.item_search_dim, searchList,typePage);
        dimListView.setAdapter(dimAdapter);
        inputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pageIndexDim = 0;
                pageIndexDurg = 0;
                String keyword = inputEdit.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) {//如果文本框最后文本没有，则显示历史记录相关页面内容
                    if(hotAdapter!=null&&hotAdapter.getCount()==0){
                        isInit=false;
                        requeHotSearchList();
                    }else{
                        showHotView();
                    }
                    isItemClicked = false;
                } else {//如果不是联想搜索，则直接执行药品列表搜索
                    if (isItemClicked) {
                        isItemClicked = false;//
                        showLoading();
                        drugsListAdapter.setKeyworldSpan(keyword);
                        requestDrugList(keyword);  //搜索药品
                    } else {
                        dimAdapter.setKeyworldSpan(keyword, true);
                        requestDimDrug(keyword);//联想搜索
                    }
                }
            }

        });
        dimListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrugBean drugBean = (DrugBean) parent.getItemAtPosition(position);
                String keyword;
                String goodsName = drugBean.getGoodsName();
                String name = drugBean.getName();
                if (!TextUtils.isEmpty(goodsName)) {
                    keyword = goodsName + " " + name;
                } else {
                    keyword = drugBean.getName();
                }
                HistoryCache historyCache = new HistoryCache();
                historyCache.setName(drugBean.getName());
                historyCache.setCacheType(HistoryCache.TYPE_CACHE_SEARCH);
                historyCache.setGoodsName(drugBean.getGoodsName());
                SPCacheList.getInstance().writeDrugSearchHistory(historyCache);
                isItemClicked = true;
                inputEdit.setText(keyword);
                inputEdit.setSelection(keyword.length());
            }
        });
        //滑动隐藏软键盘
        dimListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                KeyBoardUtils.closeKeybord(inputEdit, SearchActivity.this);
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (dimAdapter.getCount() == dimListView.getLastVisiblePosition()) {
                        pageIndexDim++;
                        String keyword = inputEdit.getText().toString().trim();
                        requestDimDrug(keyword);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String key = inputEdit.getText().toString();
                    if (key.isEmpty()) {
                        ToastUtil.showToastCenter(SearchActivity.this, "请输入搜索内容");
                        return true;
                    }
                    HistoryCache historyCache = new HistoryCache();
                    historyCache.setName(key);
                    historyCache.setCacheType(HistoryCache.TYPE_CACHE_SEARCH);
                    SPCacheList.getInstance().writeDrugSearchHistory(historyCache);
                    pageIndexDurg = 0;
                    requestEventCode("d001");
                    requestDrugList(key);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 显示可编辑状态的标题栏
     */
    private void showEditTtileBar() {
        setTitleBarVisibility(View.GONE);
        titlebarEdit.setVisibility(View.VISIBLE);
    }

    /**
     * 显示不可编辑状态的标题栏
     */
    private void showTtileBar() {
        setTitleBarVisibility(View.VISIBLE);
        titlebarEdit.setVisibility(View.GONE);
        setTitleName(tiltleScanResults);
    }

    @OnClick({R.id.ib_left, R.id.ib_titlebar_left, R.id.titlebar_right, R.id.search_drugbag})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_left:
            case R.id.ib_titlebar_left:
                this.finish();
                break;
            case R.id.titlebar_right:
                if (titlebarRight.getText().toString().equals("取消")) {
                    KeyBoardUtils.closeKeybord(inputEdit, SearchActivity.this);
                    this.finish();
                    return;
                }
                String key = inputEdit.getText().toString();
                if (key.isEmpty()) {
                    ToastUtil.showToastCenter(SearchActivity.this, "请输入搜索内容");
                    return;
                }
                HistoryCache historyCache = new HistoryCache();
                historyCache.setName(key);
                historyCache.setCacheType(HistoryCache.TYPE_CACHE_SEARCH);
                SPCacheList.getInstance().writeDrugSearchHistory(historyCache);
                pageIndexDurg = 0;
                requestEventCode("d001");
                requestDrugList(key);
                break;
            case R.id.search_drugbag://小药箱
                requestEventCode("a4008");
                isAdded = false;
                Bundle bundle = new Bundle();
                bundle.putSerializable(Contants.KEY_MEDICINE_BEAN, member);
                openActivity(MedicinePersonActivity.class, bundle);
                Intent intent1 = new Intent();
                Intent intent2 = new Intent();
                intent1.setAction(Contants.ACTION_NAME_DRUG_ADD_FINISH);
                intent2.setAction(Contants.ACTION_NAME_MEDICINEPERSION_FINISH);
                sendBroadcast(intent1);
                sendBroadcast(intent2);
                SearchActivity.this.finish();
                break;
        }
    }

    /**
     * 获取热门搜索标签列表
     */
    private void requeHotSearchList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_GET_DURG_HOT_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_GET_DURG_HOT_SIGN);
        what = HttpConstant.SEARCH_DRUG_HOT;
        request(params, HttpConstant.SEARCH_DRUG_HOT);
    }

    /**
     * 即时搜索药品(搜索联想词的药品名)
     */
    public void requestDimDrug(String keyword) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS,HttpConstant.HOME_SEARCH_DIM_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_SEARCH_DIM_SIGN);
        params.put(HttpConstant.PARAM_KEY_KEYWORD, keyword);
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX, pageIndexDurg + "");
        request(params, HttpConstant.SEARCH_DRUG_DIM);
    }

    /**
     * 搜索药品
     *
     * @param keyword
     */
    private void requestDrugList(String keyword) {
        inputEdit.clearFocus();
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_SEARCH_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_SEARCH_SIGN);
        params.put(HttpConstant.PARAM_KEY_PROVINCE2, cityTypeChoose);
        if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
            params.put(HttpConstant.MEMBERID, memberId);
        }
        params.put(HttpConstant.PARAM_KEY_CODE2, keywordCode);
        params.put(HttpConstant.PARAM_KEY_KEYWORD2, keyword);
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX2, pageIndexDurg + "");
        what=HttpConstant.SEARCH_DRUG_LIST;
        request(params, HttpConstant.SEARCH_DRUG_LIST);
    }

    /**
     * 添加用药
     */
    private void requestDrugAdd(String drugId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_DRUG_ADD_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_DRUG_ADD_SIGN);
        params.put(HttpConstant.MEMBERID, memberId);
        params.put(HttpConstant.DRUGID, drugId);
        request(params, HttpConstant.MINE_DURG_BAG_ADD);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        switch (what) {
            case HttpConstant.SEARCH_DRUG_HOT://热门历史搜索标签
                try {
                    Type type = new TypeToken<ArrayList<HotDrug>>() {
                    }.getType();
                    List<HotDrug> listHot = JsonUtil.getListModle(response.toString(), "data", type);
                    showContent();
                    fullHotData(listHot);
                } catch (Exception e) {
                    showError();
                }
                break;
            case HttpConstant.SEARCH_DRUG_DIM:
                try {
                    Type type = new TypeToken<ArrayList<DrugBean>>() {
                    }.getType();
                    JSONObject data = response.getJSONObject("data");
                    List<DrugBean> list = JsonUtil.getListModle(data.toString(), "info", type);
                    fullDimData(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {

                }
                break;
            case HttpConstant.SEARCH_DRUG_LIST:
                LogUtil.y(pageIndexDurg+"#############"+response.toString());
                JSONObject data = null;
                int count;
                try {
                    data = response.getJSONObject(HttpConstant.DATA);
                    count = data.getInt(HttpConstant.COUNT);
                } catch (JSONException e) {
                    showEmpty(emptyDrugList, R.mipmap.icon_search_nothing);
                    return;
                }
                try {
                    Type type = new TypeToken<ArrayList<DrugBean>>() {
                    }.getType();
                    List<DrugBean> list = JsonUtil.getListModle(data.toString(),HttpConstant.INFO, type);
                    if (count == 1 && typePage != Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
                        switchResultActivity(list.get(0));
                    }
                    fullDrugListData(list, count);
                    showContent();
                } catch (Exception e) {
                    e.printStackTrace();
//                    showEmpty(errorServer, R.mipmap.empty_error_net_server);
                    if(mXYbtRefreshListView!=null){
                        mXYbtRefreshListView.onRefreshComplete();
                    }
                }
                break;
            case HttpConstant.MINE_DURG_BAG_ADD:
                showToast("添加成功");
                isAdded = true;
                break;

        }
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

    private void fullDrugListData(List<DrugBean> list, int count) {
        if (list == null) {
            list = new ArrayList<DrugBean>();
        }
        String key = inputEdit.getText().toString();
        if (key.equals(lastKey)) {
            if (pageIndexDurg > cPage) {
                if (pageIndexDurg == 0) {
                    if (!drugBeanList.isEmpty()) {
                        drugBeanList.clear();
                    }
                }
                cPage = pageIndexDurg;
                drugBeanList.addAll(list);
            }
        } else {
            if (!drugBeanList.isEmpty()) {
                drugBeanList.clear();
            }
            if (!list.isEmpty()) {
                drugBeanList.addAll(list);
            }
        }
        if (list.size() == 0) {
            ToastUtil.showToastCenter(SearchActivity.this, "没有更多数据了");
            pageIndexDurg = cPage - 1;
        }
        drugsListAdapter.notifyDataSetChanged();
        mXYbtRefreshListView.onRefreshComplete();
        lastKey = key;
        searchDrugNum.setText("" + count);
        searchDrugScanNum.setText("" + count);
        showDrugListView();
    }

    /**
     * 显示列表
     */
    private void showDrugListView() {
        drugContentView.setVisibility(View.VISIBLE);
        searchOverscroll.setVisibility(View.GONE);
        searchLinearHot.setVisibility(View.GONE);
        dimListView.setVisibility(View.GONE);
        inputEdit.clearFocus();
        KeyBoardUtils.closeKeybord(inputEdit, this);
        if (typePage == Contants.VAL_PAGE_SEARCH_SCAN) {
            linearDrugSearch.setVisibility(View.GONE);
            linearDrugScan.setVisibility(View.VISIBLE);
        } else {
            linearDrugSearch.setVisibility(View.VISIBLE);
            linearDrugScan.setVisibility(View.GONE);
            titlebarLeft.setVisibility(View.VISIBLE);
            titlebarRight.setText("搜索");
        }
    }

    /**
     * 显示联想词搜索
     *
     * @param list
     */
    private void fullDimData(List<DrugBean> list) {
        if (!searchList.isEmpty()) {
            searchList.clear();
        }
        if (!list.isEmpty()) {
            searchList.addAll(list);
        }
        showContent();
        dimAdapter.notifyDataSetChanged();
        dimListView.setVisibility(View.VISIBLE);
        searchLinearHot.setVisibility(View.GONE);
        searchOverscroll.setVisibility(View.VISIBLE);
        drugContentView.setVisibility(View.GONE);
        titlebarLeft.setVisibility(View.GONE);
//        titlebarRight.setText("搜索");
    }

    /**
     * 填充热门搜索数据
     *
     * @param listHot
     */
    private void fullHotData(List<HotDrug> listHot) {
        if (listHot.isEmpty()) {
            showEmpty(errorServer, R.mipmap.empty_error_net_server);
            return;
        }
        hotAdapter.setList(listHot);
        showHotView();
    }

    /**
     * 显示热门
     */
    private void showHotView() {
        if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
            titlebarLeft.setVisibility(View.GONE);
            titlebarRight.setText("取消");
            searchOverscroll.setVisibility(View.GONE);
            searchLinearHot.setVisibility(View.GONE);
            dimListView.setVisibility(View.GONE);
            searchLinearHot.setVisibility(View.INVISIBLE);
            drugContentView.setVisibility(View.GONE);
            showContent();
            return;
        }
        if (isInit && typePage == Contants.VAL_PAGE_SEARCH_HISTORY || typePage == Contants.VAL_PAGE_SEARCH_SCAN) {
            isInit = false;
//            pageIndexDurg = 0;
//            drugsListAdapter.setKeyworldSpan(keyword);
//            showLoading();
//            requestDrugList(keyword);
//            showDrugListView();
        } else {
            showContent();
            searchOverscroll.setVisibility(View.VISIBLE);
            searchLinearHot.setVisibility(View.VISIBLE);
            drugContentView.setVisibility(View.GONE);
            inputEdit.requestFocus();
            dimListView.setVisibility(View.GONE);
            KeyBoardUtils.openKeybord(inputEdit, this);
        }
        titlebarLeft.setVisibility(View.GONE);
        titlebarRight.setText("取消");
    }

    @Override
    public void error(String errorJson) {
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorJson, ErrorResponse.class);
            int what = errorResponse.getWhat();
            boolean isNetError = errorResponse.isNetError();
            String msg = errorResponse.getMsg();
            switch (what) {
                case HttpConstant.SEARCH_DRUG_HOT:
                    if (isNetError) {
                        showError();
                    } else {
                        if (!TextUtils.isEmpty(msg)) {
                            showToast(msg);
                        }
                    }
                    break;
                case HttpConstant.SEARCH_DRUG_LIST:
                    if (isNetError) {
                        if(pageIndexDurg==0){
                            showError();
                        }else{
                            showToast("网络不给力");
                        }
                    } else {
                        if (!TextUtils.isEmpty(msg)) {
                            showToast(msg);
                        }
                        showEmpty(emptyDrugList, R.mipmap.icon_search_nothing);
                    }
                    break;
                case HttpConstant.MINE_DURG_BAG_ADD:
                    showToast("添加失败");
                    break;

            }
        } catch (Exception e) {
        }
    }

    /**
     * 切换到结果页
     *
     * @param drugBean
     */
    private void switchResultActivity(DrugBean drugBean) {
        int type = drugBean.getType();
        if (type == 0) {
            switchResultsActivity(drugBean);
        } else {
            switchDrugNoDetailActivity(drugBean);
        }
    }

    private void switchDrugNoDetailActivity(DrugBean drugBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        openActivity(DrugNoDetailActivity.class, bundle);
    }

    /**
     * 首页切换到搜索结果页
     *
     * @param drugBean
     */
    private void switchResultsActivity(DrugBean drugBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        bundle.putInt(Contants.KEY_PAGE_RESULTS, typePage);
        openActivity(ResultsActivity.class, bundle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isClickEt(view, event)) {
                KeyBoardUtils.closeKeybord(view, SearchActivity.this);
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

    //
//    /**
//     * 上下浮动的动画
//     *
//     * @param v
//     */
//    private void startUPAndUnderAnimation(final View v) {
//        float redPackedY = v.getTranslationY();
//        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.TRANSLATION_Y, redPackedY, redPackedY - DensityUtils.dp2px(10), redPackedY);
//        AnimatorSet set = new AnimatorSet();
//        set.play(animator);
//        set.setDuration(2000);
//        set.start();
//        set.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                startUPAndUnderAnimation(v);
//            }
//        });
//    }
    PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];
    boolean playLayout = false;//判断小药箱动画

    /**
     * 把药品添加到小药箱的抛物线动画
     *
     * @param itemDrugImageView
     */
    private void addDugBag(ImageView itemDrugImageView) {
        final ImageView imageDrug = new ImageView(this);
        Drawable drawable = itemDrugImageView.getDrawable();
        imageDrug.setImageDrawable(drawable);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drugContentView.addView(imageDrug, params);
        int[] parentLocation = new int[2];
        drugContentView.getLocationInWindow(parentLocation);
        int startLoc[] = new int[2];
        itemDrugImageView.getLocationInWindow(startLoc);
        int endLoc[] = new int[2];
        searchDrugbag.getLocationInWindow(endLoc);
        float startX = startLoc[0] - parentLocation[0];
        float startY = startLoc[1] - parentLocation[1];
        float toX = endLoc[0] - parentLocation[0] - (itemDrugImageView.getWidth() + DensityUtils.dp2px(20)) / 3 + DensityUtils.dp2px(10);
        float toY = endLoc[1] - parentLocation[1] - searchDrugbag.getHeight() / 4 - DensityUtils.dp2px(10);
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(startX, startY - DensityUtils.dp2px(100), toX, toY);
        mPathMeasure = new PathMeasure(path, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                imageDrug.setTranslationX(mCurrentPosition[0]);
                imageDrug.setTranslationY(mCurrentPosition[1]);
            }
        });
        AnimatorSet animSet = new AnimatorSet();
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageDrug.setVisibility(View.GONE);
                if (!playLayout) {
                    try {
                        playDrugBag();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        ObjectAnimator scalex = ObjectAnimator.ofFloat(imageDrug, View.SCALE_X, 1.0f, 0.1f);
        ObjectAnimator scaley = ObjectAnimator.ofFloat(imageDrug, View.SCALE_Y, 1.0f, 0.1f);
        animSet.playTogether(valueAnimator, scalex, scaley);
        animSet.setDuration(800);
        animSet.start();
    }


    /**
     * 小药箱动画
     */
    public void playDrugBag() throws Exception {
        float searchDrugbagY = searchDrugbag.getY();
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(searchDrugbag, "y", searchDrugbagY, searchDrugbagY + DensityUtils.dp2px(10));
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(searchDrugbag, "y", searchDrugbagY);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(searchDrugbag, "y", searchDrugbagY, searchDrugbagY - DensityUtils.dp2px(7));
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(searchDrugbag, "y", searchDrugbagY);
        ObjectAnimator anim5 = ObjectAnimator.ofFloat(searchDrugbag, "y", searchDrugbagY, searchDrugbagY + DensityUtils.dp2px(5));
        ObjectAnimator anim6 = ObjectAnimator.ofFloat(searchDrugbag, "y", searchDrugbagY);
        ObjectAnimator anim7 = ObjectAnimator.ofFloat(searchDrugbag, "y", searchDrugbagY, searchDrugbagY - DensityUtils.dp2px(2));
        ObjectAnimator anim8 = ObjectAnimator.ofFloat(searchDrugbag, "y", searchDrugbagY);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1);
        animSet.play(anim2).after(anim1);
        animSet.play(anim3).after(anim2);
        animSet.play(anim4).after(anim3);
        animSet.play(anim5).after(anim4);
        animSet.play(anim6).after(anim5);
        animSet.play(anim7).after(anim6);
        animSet.play(anim8).after(anim7);
        animSet.setDuration(150);
        animSet.start();
        playLayout = true;

        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                playLayout = false;
            }
        });
    }

    protected void onClickRetry() {
        switch (what) {
            case HttpConstant.SEARCH_DRUG_HOT:
                requeHotSearchList();
                break;
            case HttpConstant.SEARCH_DRUG_LIST:
                showLoading();
                requestDrugList(keyword);
                break;
        }
    }


    @Override
    public void onItemClickListener(ImageView imageView, int position, DrugBean drugBean) {
        if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
            mDrugIds.add(drugBean.getDrugID());
            drugBean.setSeleted(true);
            drugBean.setMedicineChest("1");
            drugsListAdapter.setItemSeleted(position, drugBean);
            requestEventCode("a-4003");
            requestDrugAdd(drugBean.getDrugID());
            addDugBag(imageView);

        } else {
            switchResultActivity(drugBean);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isAdded) {
            Intent intent = new Intent();
            intent.putExtra(Contants.KEY_DRUG_ID, mDrugIds);
            intent.setAction(Contants.ACTION_NAME_DRUG_ADD_REFRESH);

            Intent intent1 = new Intent();
            intent1.setAction(Contants.ACTION_NAME_MEDICINE_PERSON_REFRESH);
            sendBroadcast(intent);
            sendBroadcast(intent1);
        }
    }
}

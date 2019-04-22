package com.lzyc.ybtappcal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.reimbursement.ReimbursementDetailsActivity;
import com.lzyc.ybtappcal.activity.reimbursement.ReimbursementNoYbDetailsActivity;
import com.lzyc.ybtappcal.activity.reimbursement.ReimbursementYbDetailsActivity;
import com.lzyc.ybtappcal.activity.top.DrugNoDetailActivity;
import com.lzyc.ybtappcal.activity.top.ResultsActivity;
import com.lzyc.ybtappcal.adapter.ScanHistoryDrugsListAdapter;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.bean.HistoryCache;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.cache.SPCacheList;
import com.lzyc.ybtappcal.view.dialog.LoadingProgressBar;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
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

/**
 * 扫描记录
 * package_name com.lzyc.zxing.activity
 * Created by yang on 2016/6/27.
 */
public class CaptureHistoryActivity extends BaseActivity {

    protected static final String TAG = CaptureHistoryActivity.class.getSimpleName();
    @BindView(R.id.capture_history_listview)
    XYbtRefreshListView capture_history_listview;
    private int typePage;
    private List<HistoryCache> historySearchList;
    private ScanHistoryDrugsListAdapter adapter;
    private String cityYbChoose;
    private String cityOpenIs;
    private LoadingProgressBar mLoadingProgressBar;
    private String resultCode;
    private Handler handler = new Handler();
    private View footerView;
    private TextView tv_search_remove_history;
    private PopupWindowTwoButton twoButton = null;

    @Override
    public int getContentViewId() {
        return R.layout.activity_capture_history;
    }

    @Override
    public void init() {
        setTitleName("扫描历史");
        setPageStateView();
        initView();
        tv_search_remove_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (twoButton == null) {
                    popupClearHistory();
                }
            }
        });
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

    private void initView() {
        initData();
        adapter = new ScanHistoryDrugsListAdapter(this, R.layout.item_scan_history_druglist, historySearchList);
        initFooterView();
        capture_history_listview.addFooterView(footerView, null, false);
        capture_history_listview.setAdapter(adapter);
        capture_history_listview.setOnRefreshListener(new OnYbtRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        capture_history_listview.onRefreshComplete();
                    }
                }, 500);
            }

            @Override
            public void onLoadingMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        capture_history_listview.onRefreshComplete();
                    }
                }, 500);
            }
        });
        capture_history_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryCache historySearch = (HistoryCache) parent.getItemAtPosition(position);
                if (historySearch != null) {
                    requestEventCode("m001");
                    resultCode = historySearch.getCode();
                    requestDrug(resultCode);
                }
            }
        });
    }

    private void initFooterView() {
        footerView = View.inflate(this, R.layout.footer_search_history_clear, null);
        tv_search_remove_history = (TextView) footerView.findViewById(R.id.tv_search_remove_history);
        tv_search_remove_history.setVisibility(View.VISIBLE);
        tv_search_remove_history.setText("清除扫描历史");
        if (historySearchList.size() == 0) {
            tv_search_remove_history.setVisibility(View.GONE);
            showEmpty("您还没有扫描记录哦!",R.mipmap.empty_scan_history);
        } else {
            tv_search_remove_history.setVisibility(View.VISIBLE);
            showContent();

        }

    }

    //是否清除历史记录
    private void popupClearHistory() {
        twoButton = new PopupWindowTwoButton(this);
        twoButton.getTvOk().setVisibility(View.VISIBLE);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText("确认清空扫描历史记录?");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvCancel().setTextColor(Color.parseColor("#3E6398"));
        twoButton.getTvOk().setTextColor(Color.parseColor("#3E6398"));
        twoButton.getTvOk().setText("确认");
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
                twoButton = null;
                requestEventCode("m002");
                SPCacheList.getInstance().clearHistory(SharePreferenceUtil.SPACE_HISTORY_DRUG_SCAN);
                tv_search_remove_history.setVisibility(View.VISIBLE);
                historySearchList.removeAll(historySearchList);
                showEmpty("您还没有扫描记录哦!",R.mipmap.empty_scan_history);
                adapter.notifyDataSetChanged();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
                twoButton = null;
            }
        });
        twoButton.showPopupWindow(capture_history_listview, Gravity.CENTER);
    }


    private void initData() {
        mLoadingProgressBar = new LoadingProgressBar(this);
        typePage = getIntent().getExtras().getInt(Contants.KEY_PAGE_SEARCH);
        cityOpenIs = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.CITY_TOP_CHOOSE, " ");
        String locationCity = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.CITY, "");
        cityYbChoose = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.CITY_CANBAO, locationCity);
        if (historySearchList == null) {
            historySearchList = new ArrayList<>();
        }
        historySearchList = SPCacheList.getInstance().readDrugScanHistory();
    }

    @Override
    public void onClickTitleLeftBtn(View v) {
        super.onClickTitleLeftBtn(v);
    }

    //关键字：网络请求

    /**
     * 搜索药品
     */
    public void requestDrug(String code) {
        mLoadingProgressBar.show();
        switch (typePage) {
            case Contants.VAL_PAGE_SEARCH_DURUG:
            case Contants.VAL_PAGE_SEARCH_REIMBURSEMENT:
                requestLocalDrug(code);
                break;
            default:
                mLoadingProgressBar.dismiss();
                break;
        }
    }

    /**
     * 扫码
     *
     * @param code
     */
    private void requestLocalDrug(String code) {
        String currentProvince = "";
        switch (typePage) {
            case Contants.VAL_PAGE_SEARCH_DURUG:
                currentProvince = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "北京");
                break;
            case Contants.VAL_PAGE_SEARCH_REIMBURSEMENT:
                currentProvince = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PROVICE_JIUZHEN, "北京");
                break;
            default:
                mLoadingProgressBar.dismiss();
                break;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_SEARCH_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_SEARCH_SIGN);
        params.put(HttpConstant.PARAM_KEY_PROVINCE2, currentProvince);
        if (typePage == Contants.VAL_PAGE_SEARCH_DRUG_ADD) {
//            params.put(HttpConstant.MEMBERID, memberId);
        }
        params.put(HttpConstant.PARAM_KEY_CODE2, code);
        params.put(HttpConstant.PARAM_KEY_KEYWORD2, "");
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX2,   "0");
        request(params, HttpConstant.SEARCH_DRUG_LIST);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        mLoadingProgressBar.dismiss();
        HistoryCache historyCache = new HistoryCache();
        switch (what) {
            case HttpConstant.SEARCH_DRUG_LIST:
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    int count = data.getInt(HttpConstant.COUNT);
                    Type typeD = new TypeToken<ArrayList<DrugBean>>() {
                    }.getType();
                    List<DrugBean> sList = JsonUtil.getListModle(data.toString(),HttpConstant.INFO, typeD);
                    DrugBean drugBean = sList.get(0);
                    switch (count) {
                        case 0://这里不存入历史
                            switchScancodeErrorActivity();
                            break;
                        case 1:
                            historyCache.setCode(resultCode);
                            historyCache.setImageUrl(drugBean.getImage());
                            historyCache.setName(drugBean.getName());
                            historyCache.setGoodsName(drugBean.getGoodsName());
                            historyCache.setId(drugBean.getDrugID());
                            switchActivity(drugBean);
                            break;
                        default:
                            if (drugBean != null) {
                                historyCache.setCode(resultCode);
                                historyCache.setImageUrl(drugBean.getImage());
                                historyCache.setName(drugBean.getName());
                                historyCache.setId(drugBean.getDrugID());
                                historyCache.setGoodsName(drugBean.getGoodsName());
                            }
                            switchSearchActivity();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                historyCache.setCacheType(HistoryCache.TYPE_CACHE_SCAN);
                SPCacheList.getInstance().writeDrugScanHistory(historyCache);
                break;
            default:
                break;
        }
    }


    private void switchActivity(DrugBean drugBean) {
        if (drugBean == null) {
            drugBean = new DrugBean();
        }
        int type = drugBean.getType();
        if (type == 0) {
            requestEventCode("a011");//扫码后出推荐
            switch (typePage) {
                case Contants.VAL_PAGE_SEARCH_DURUG:
                    switchResultsActivity(drugBean);
                    break;
                case Contants.VAL_PAGE_SEARCH_REIMBURSEMENT: //从异地报销跳转到搜索页面
                    switchReimbursementDetailsActivity(drugBean);
                    break;
                default:
                    break;
            }
        } else if (type == 1) {
            switchDrugNoDetailActivity(drugBean);
        } else {
            LogUtil.e(TAG, "error,default switchResultActivity");
        }
    }

    private void switchDrugNoDetailActivity(DrugBean drugBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        openActivity(DrugNoDetailActivity.class, bundle);
    }


    /**
     * 报销查询结果界面
     *
     * @param drugBean
     */
    private void switchReimbursementDetailsActivity(DrugBean drugBean) {
        Intent intent = new Intent(this, ReimbursementDetailsActivity.class);
        intent.putExtra(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        intent.putExtra(Contants.KEY_PAGE_SEARCH, typePage);
        intent.putExtra(Contants.KEY_PAGE, Contants.VAL_PAGE_SAOMA);
        startActivity(intent);
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



    private void switchSearchActivity() {
        requestEventCode("a003");
        Bundle mBundle = new Bundle();
        mBundle.putString(Contants.KEY_STR_KEYWORD_CODE, resultCode);
        mBundle.putInt(Contants.KEY_PAGE_SEARCH, Contants.VAL_PAGE_SEARCH_SCAN);
        openActivity(SearchActivity.class, mBundle);
    }
    @Override
    public void error(String errorMsg) {
        mLoadingProgressBar.dismiss();
        switchScancodeErrorActivity();
    }

    private void switchScancodeErrorActivity() {
        HistoryCache scanHistory = new HistoryCache();
        scanHistory.setName("提示");
        scanHistory.setImageUrl("null");
        scanHistory.setCode(resultCode);
        scanHistory.setCacheType(HistoryCache.TYPE_CACHE_SCAN);
        SPCacheList.getInstance().writeDrugScanHistory(scanHistory);
        Bundle bundle = new Bundle();
        bundle.putString("resultcode", resultCode);
        bundle.putInt(Contants.KEY_PAGE_SEARCH, typePage);
        openActivity(ScancodeErrorActivity.class, bundle);
        finish();
    }

    private void switchScanResultActivity(DrugBean drugBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        bundle.putInt(Contants.KEY_PAGE_SEARCH, 0);
        bundle.putInt(Contants.KEY_PAGE, Contants.VAL_PAGE_SAOMA);
        openActivity(ResultsActivity.class, bundle);
        finish();
    }

    /**
     * 异地报销切换到搜索结果页
     *
     * @param drugBean
     */
    private void switchReimbursementActivity(DrugBean drugBean) {
        String scanName = "";
        if (drugBean == null) {
            switchScancodeErrorActivity();
            return;
        }
        HistoryCache scanHistory = new HistoryCache();
        scanHistory.setCode(resultCode);
        String name = drugBean.getName();
//        if (goodname != null && !goodname.isEmpty()) {
//            scanName = goodname;
//        } else {
//            scanName = drugBean.getName();
//        }
        scanHistory.setName(name);
        scanHistory.setImageUrl(drugBean.getImage());
//        writeHistory(resultCode, new Gson().toJson(tvScanHistory).toString());

        Bundle bundle = new Bundle();
        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
        bundle.putInt(Contants.KEY_PAGE_DRUG, Contants.VAL_PAGE_SEARCH_REIMBURSEMENT);
        if (cityYbChoose.equals("北京市")) {
            openActivity(ReimbursementYbDetailsActivity.class, bundle);
        } else {
            openActivity(ReimbursementNoYbDetailsActivity.class, bundle);
        }
        finish();
    }


}

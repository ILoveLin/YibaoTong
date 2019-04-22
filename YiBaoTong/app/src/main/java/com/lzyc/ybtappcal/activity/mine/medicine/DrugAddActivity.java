package com.lzyc.ybtappcal.activity.mine.medicine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.ScanActivity;
import com.lzyc.ybtappcal.activity.SearchActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.DrugAddAdapter;
import com.lzyc.ybtappcal.bean.BrowseBean;
import com.lzyc.ybtappcal.bean.MedicineFamilyBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.fragment.TopFragment;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.ScreenUtils;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.zbar.activity.CaptureActivity;
import com.umeng.analytics.MobclickAgent;

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
 * 添加用药
 */
public class DrugAddActivity extends BaseActivity implements OnYbtRefreshListener {

    @BindString(R.string.tilte_name_drug_add)
    String titleName;
    @BindString(R.string.complete)
    String complete;
    @BindView(R.id.drug_add_search)
    TextView drugAddSearch;
    @BindView(R.id.durg_add_scan)
    TextView durgAddScan;

    @BindView(R.id.rel_loading)
    RelativeLayout relLoading;
    @BindView(R.id.x_listview)
    XYbtRefreshListView xListView;

    private String memberId;
    private MedicineFamilyBean.ListBean bean;
    private int pageIndex;
    private DrugAddAdapter mAdapter;

    StringBuilder ids = new StringBuilder();

    List<BrowseBean> mData = new ArrayList<>();

    List<BrowseBean> listTemp = new ArrayList<>();

    @Override
    public int getContentViewId() {
        return R.layout.activity_drug_add;
    }

    @Override
    protected void init() {

        setTitleName(titleName);

        setPageStateView(relLoading);

        showLoading();

        bean = (MedicineFamilyBean.ListBean) getIntent().getExtras().getSerializable(Contants.KEY_MEDICINE_BEAN);

        memberId = bean.getID();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contants.ACTION_NAME_DRUG_ADD_FINISH);
        intentFilter.addAction(Contants.ACTION_NAME_DRUG_ADD_REFRESH);
        registerReceiver(receiver, intentFilter);

        initListView();

        requestBrowseHistoryList();
    }

    private void initListView() {

        mAdapter = new DrugAddAdapter(mContext, R.layout.item_drug_add, mData);

        mAdapter.updataActivity(this);

        xListView.setAdapter(mAdapter);

        xListView.setPullLoadEnable(false);

        xListView.setPullRefreshEnable(false);

        xListView.setOnRefreshListener(this);
    }

    @OnClick({R.id.tv_right, R.id.drug_add_search, R.id.durg_add_scan})
    public void onViewClicked(View view) {
        Bundle mBundle = new Bundle();
        mBundle.putInt(Contants.KEY_PAGE_SEARCH, Contants.VAL_PAGE_SEARCH_DRUG_ADD);
        switch (view.getId()) {
            case R.id.tv_right:
                requestEventCode("a-4007");

                if (null == mAdapter.getmSelectedIDS() || mAdapter.getmSelectedIDS().isEmpty()) {
                    showToast("请选择需要加入药箱的药品");
                    return;
                }

                for (String id : mAdapter.getmSelectedIDS()) {
                    ids.append(id);
                }

                requestAdd(ids.toString().substring(0, ids.length() - 1));

                break;
            case R.id.drug_add_search:
                requestEventCode("a-4005");
                mBundle.putSerializable(Contants.KEY_MEDICINE_BEAN, bean);
                openActivity(SearchActivity.class, mBundle);
                break;
            case R.id.durg_add_scan:
                requestEventCode("a-4006");
                mBundle.putInt(Contants.KEY_PAGE_SCAN, Contants.VAL_PAGE_SEARCH_DRUG_ADD);
                mBundle.putInt(Contants.KEY_POSITION_CUT, getHeight());
                mBundle.putSerializable(Contants.KEY_MEDICINE_BEAN, bean);
                openActivityNoAnim(ScanActivity.class, mBundle);
                TopFragment.bitmap = ScreenUtils.snapShotWithoutStatusBar(DrugAddActivity.this);
                break;
        }
    }

    public int getHeight() {
        int location[] = new int[2];
        durgAddScan.getLocationOnScreen(location);
        int height = durgAddScan.getHeight() / 2 + location[1] - ScreenUtils.getStatusHeight();
        return height;
    }

    /**
     * 获取记录浏览列表
     */
    public void requestBrowseHistoryList() {

        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_BROWSE_HISTORYLIST_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_BROWSE_HISTORYLIST_SIGN);
        params.put(HttpConstant.APP_UID, String.valueOf(SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "")));
        params.put(HttpConstant.MEMBERID, memberId);
        params.put(HttpConstant.PARAM_KEY_TYPE2, Contants.STR_2);
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX2, String.valueOf(pageIndex));

        request(params, HttpConstant.MINE_BROWSE_HISTORYLIST);
    }

    /**
     * 添加到药箱
     */
    private void requestAdd(String ids) {
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_DRUG_ADD_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_DRUG_ADD_SIGN);
        params.put(HttpConstant.MEMBERID, memberId);
        params.put(HttpConstant.DRUGID, ids);
        request(params, HttpConstant.MINE_DURG_BAG_ADD);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.MINE_BROWSE_HISTORYLIST:
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);

                    xListView.setPullLoadEnable(true);

                    Type type = new TypeToken<ArrayList<BrowseBean>>() {
                    }.getType();

                    listTemp = JsonUtil.getListModle(data.toString(), "list", type);

                    mData = JsonUtil.getListModle(data.toString(), "list", type);

                    xListView.stopLoadMore();

                    if (mData.isEmpty()) {

                        if (0 == pageIndex) {
                            showEmpty("您还没有浏览记录哦!", R.mipmap.empty_browser_hisotry);
                            xListView.setVisibility(View.GONE);
                            return;
                        } else {
                            showToast(getResources().getString(R.string.no_more_data));
                        }
                    }

                    for (int i = 0; i < listTemp.size(); i++) {
                        if ("1".equals(listTemp.get(i).getMedicineChest())) {
                            mData.get(i).setAdded(true);
                        }
                    }

                    setTitleRightTvbtnName(complete);

                    xListView.setVisibility(View.VISIBLE);

                    if (pageIndex == 0) {
                        mAdapter.setList(mData);
                        xListView.stopRefresh();
                    } else {
                        if (mData.size() == 0) {
                            ToastUtil.showToastCenter(mContext, "没有更多数据了");
                            pageIndex = pageIndex - 1;
                        } else {
                            mAdapter.addList(mData);
                        }
                        xListView.stopLoadMore();

                    }

                    showContent();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case HttpConstant.MINE_DURG_BAG_ADD:

                for (BrowseBean bean : mAdapter.getData()) {
                    if ("1".equals(bean.getMedicineChest())) {

                        bean.setAdded(true);

                        mAdapter.notifyDataSetChanged();
                    }
                }

                Intent intent = new Intent();
                intent.setAction(Contants.ACTION_NAME_MEDICINE_PERSON_REFRESH);
                sendBroadcast(intent);

                finish();
                break;
        }
    }

    @Override
    protected void onClickRetry() {
        super.onClickRetry();
        showLoading();
        requestBrowseHistoryList();
    }

    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);

        try {
            xListView.stopLoadMore();

            ErrorResponse errorResponse = new ErrorResponse();

            switch (errorResponse.getWhat()) {
                case HttpConstant.MINE_BROWSE_HISTORYLIST:
                    if (errorResponse.isNetError()) {
                        showError();
                    } else {
                        showToast(errorResponse.getMsg());
                    }
                    break;

                case HttpConstant.MINE_DURG_BAG_ADD:
                    if (errorResponse.isNetError()) {
                        showToast(getResources().getString(R.string.error_net_toast));
                    } else {
                        showToast(errorResponse.getMsg());
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (Contants.ACTION_NAME_DRUG_ADD_REFRESH == intent.getAction()) {

                List<BrowseBean> data = mAdapter.getData();

                if (null == data || data.isEmpty()) return;

                ArrayList<String> mDrugIds = (ArrayList<String>) intent.getSerializableExtra(Contants.KEY_DRUG_ID);

                if(null == mDrugIds || mDrugIds.isEmpty()) return;

                for(String drugID : mDrugIds){

                    for (BrowseBean bean : data) {

                        if (drugID.equals(bean.getDrugID())) {

                            bean.setMedicineChest("1");

                            bean.setAdded(true);

                            if (ids.toString().contains(bean.getDKID())) {
                                mAdapter.removeSelectedID(bean.getDrugID() + ",");
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            if (intent.getAction().equals(Contants.ACTION_NAME_DRUG_ADD_FINISH)) {
                DrugAddActivity.this.finish();
                overridePendingTransition(0, 0);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (null == receiver) return;
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDownPullRefresh() {

    }

    @Override
    public void onLoadingMore() {
        pageIndex++;
        requestBrowseHistoryList();
    }

    private static final String TAG = DrugAddActivity.class.getSimpleName();

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

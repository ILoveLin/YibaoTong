package com.lzyc.ybtappcal.activity.top;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.DrugsListRecommentAdapter;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;

/**
 * 扫描为空时，推荐药品
 * package_name com.lzyc.ybtappcal.activity.top
 * Created by yang on 2016/6/3.
 */
public class RecommentIFEmptyActivity extends BaseActivity {
    private static final String TAG = RecommentIFEmptyActivity.class.getSimpleName();

    @BindView(R.id.ptlv_recomment_druglist)
    XYbtRefreshListView ptlv_recomment_druglist;
    @BindString(R.string.title_recommed)
    String titleName;
    private String keyword;
    private DrugsListRecommentAdapter drugsListAdapter;//药品列表适配器
    private List<DrugBean> drugBeanList;
    private int page;
    private int cPage = -1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_recmment_empty;
    }

    @Override
    public void init() {
        setTitleName(titleName);
        Bundle bundle = getIntent().getExtras();
        keyword = bundle.getString(HttpConstant.PARAM_KEY_KEYWORD);
        page = 0;
        drugBeanList = new ArrayList<DrugBean>();
        drugsListAdapter = new DrugsListRecommentAdapter(this, R.layout.item_drugs_list, drugBeanList);
        ptlv_recomment_druglist.setAdapter(drugsListAdapter);
        requestDrug();
        drugsListAdapter.setKeyworldSpan(keyword);
        ptlv_recomment_druglist.setOnRefreshListener(new OnYbtRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                page = 0;
                requestDrug();
            }

            @Override
            public void onLoadingMore() {
                page++;
                requestDrug();
            }
        });
        ptlv_recomment_druglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrugBean drugBean = (DrugBean) parent.getItemAtPosition(position);
                if (drugBean != null) {
                    switchScanResultActivity(drugBean);
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

    private void requestDrug() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, "RecDrugs");
        String sign = MD5ChangeUtil.Md5_32("HomeRecDrugs" + HttpConstant.APP_SECRET);
        params.put(HttpConstant.PARAM_KEY_SIGN, sign);
        params.put(HttpConstant.PARAM_KEY_KEYWORD, keyword);
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX, page + "");
        request(params, HttpConstant.RECOMMEND_LIST_EMPTY);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.RECOMMEND_LIST_EMPTY://即时搜索药品
                Type type = new TypeToken<ArrayList<DrugBean>>() {
                }.getType();
                List<DrugBean> list = JsonUtil.getListModle(response.toString(), "data", type);
                if (list.isEmpty()) {
                    ToastUtil.showShort(this, "暂无数据");
                    page = cPage - 1;
                }
                if (page > cPage) {
                    if (page == 0) {
                        if (!drugBeanList.isEmpty()) {
                            drugBeanList.clear();
                        }
                    }
                    cPage = page;
                    drugBeanList.addAll(list);
                }
                drugsListAdapter.notifyDataSetChanged();
                ptlv_recomment_druglist.onRefreshComplete();
                break;
        }

    }

    /**
     * 首页切换到搜索结果页
     *
     * @param drugBean
     */
    private void switchScanResultActivity(DrugBean drugBean) {
//        Intent intent = new Intent(RecommentIFEmptyActivity.this, ScanResultActivity.class);
//        intent.putExtra("drug_id", drugBean.getDrug_id());
//        intent.putExtra("drugNameID", drugBean.getDrugNameID());
//        intent.putExtra("venderID", drugBean.getVenderID());
//        intent.putExtra("specificationsID", drugBean.getSpecificationsID());
//        startActivity(intent);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
//        bundle.putInt(Contants.KEY_PAGE_SEARCH, 0);
//        bundle.putInt(Contants.KEY_PAGE, 0);
//        openActivity(ResultsAdoptActivity.class, bundle);
        Bundle mBundle = new Bundle();
        mBundle.putString("drugNameID", drugBean.getDrugNameID());
        mBundle.putString("venderID", drugBean.getVenderID());
        mBundle.putString("specificationsID", drugBean.getSpecificationsID());
        mBundle.putString("drug_id", drugBean.getDrug_id());
        mBundle.putInt(Contants.KEY_PAGE, Contants.VAL_PAGE_SEARCH_DURUG);
        openActivity(ScanResultActivity.class, mBundle);
    }
}

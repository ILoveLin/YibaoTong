package com.lzyc.ybtappcal.activity.top;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.HispitalAdapter;
import com.lzyc.ybtappcal.bean.HospitalListBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
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
 * 医院列表
 */
public class HispitalListActivity extends BaseActivity {
    private static final String TAG=HispitalListActivity.class.getSimpleName();

    @BindView(R.id.listview)
    XYbtRefreshListView mListView;
    @BindString(R.string.title_hispital_list)
    String titleName;

    private int page;
    private int cPage=-1;
    private ArrayList<HospitalListBean> mList;
    private HispitalAdapter adapter;
    private String drugId;
    private String drugNameID;



    @Override
    public int getContentViewId() {
        return R.layout.activity_hospital_list;
    }

    @Override
    public void init() {
        setTitleName(titleName);
        setPageStateView();
        showLoading();
        initLv();
    }


    private void initLv() {
        mList = new ArrayList<>();
//        adapter = new HispitalAdapter(this, mList, R.layout.item_hispital);
        adapter = new HispitalAdapter(this, R.layout.item_hospital_by, mList);
        mListView.setOnRefreshListener(new OnYbtRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                requestHospital();
            }

            @Override
            public void onLoadingMore() {
                page++;
                requestHospital();
            }
        });
        mListView.setAdapter(adapter);
        //医院详情
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HospitalListBean hlb=(HospitalListBean) parent.getItemAtPosition(position);
                if(hlb!=null){
                    Intent intent = new Intent(HispitalListActivity.this, HispitalDetailActivity.class);
                    intent.putExtra("url",hlb.getUrl());
                    intent.putExtra("yyId",hlb.getYyid());
                    startActivity(intent);
                }else{
                    showToast("暂时没有数据！");
                }
            }
        });
    }


    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        drugId = getIntent().getStringExtra(Contants.KEY_DRUG_ID2);
        drugNameID = getIntent().getStringExtra(Contants.KEY_DRUG_NAME_ID);
        requestHospital();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    /**
     * 医院列表
     */
    public void requestHospital() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, "AdoptionHospital");
        String sign = MD5ChangeUtil.Md5_32("HomeAdoptionHospital" + HttpConstant.APP_SECRET);
        params.put(HttpConstant.PARAM_KEY_SIGN, sign);
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX, page+"");
        params.put(HttpConstant.PARAM_KEY_ID_DRUG,""+drugId);
        params.put(HttpConstant.PARAM_KEY_ID_DRUGNAME,""+drugNameID);
        params.put(HttpConstant.PARAM_KEY_TYPE,"all");
        request(params, HttpConstant.TOP_HOSPITAL);

    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.TOP_HOSPITAL:
                Type type = new TypeToken<ArrayList<HospitalListBean>>() {}.getType();
                List<HospitalListBean> list = JsonUtil.getListModle(response.toString(), "data", type);

                if(0 != page && (null == list || list.isEmpty())){
                    showToast("没有更多数据了");
                }

                reload(list);

                showContent();

                break;
            default:
                break;
        }
    }

    private void reload(List<HospitalListBean> list) {
        if (list.isEmpty()){
            showEmpty("暂时还没有医院列表哦", R.mipmap.icon_empty_hospitorlist);
        }
        if (page > cPage) {
            if (page==0){
                if (!mList.isEmpty()){
                    mList.clear();
                }
            }
            cPage = page;
            mList.addAll(list);
            adapter.notifyDataSetChanged();
        }
        mListView.onRefreshComplete();

    }

    @Override
    protected void onClickRetry() {
        super.onClickRetry();

        showLoading();
        requestHospital();
    }

    @Override
    public void error(String errorMsg) {
        try{
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);

            if(errorResponse.isNetError()) {

                if(null == mList || mList.isEmpty()){
                    showError();
                    mListView.stopLoadMore();
                } else {
                    showToast(getResources().getString(R.string.error_net_toast));
                    mListView.onRefreshComplete();
                }

            } else {
                showToast(errorResponse.getMsg());
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}

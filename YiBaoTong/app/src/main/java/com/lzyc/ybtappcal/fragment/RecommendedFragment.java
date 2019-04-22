package com.lzyc.ybtappcal.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseFragment;
import com.lzyc.ybtappcal.activity.top.RecommendActivity;
import com.lzyc.ybtappcal.adapter.RecommendedAdapter;
import com.lzyc.ybtappcal.bean.RecommendBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 资讯
 * @author yang
 */
public class RecommendedFragment extends BaseFragment implements OnYbtRefreshListener {
    private static final String TAG = RecommendedFragment.class.getSimpleName();
    @BindView(R.id.listview)
    XYbtRefreshListView mListView;

    @Override
    public void onDownPullRefresh() {
        pageIndex = 0;
        requestData();
    }

    @Override
    public void onLoadingMore() {
        ++pageIndex;
        requestData();
    }

    private int pageIndex = 0;
    private RecommendedAdapter mAdapter;
    private List<RecommendBean> listData;


    @Override
    public int getContentViewId() {
        return R.layout.fragment_recommened;
    }

    @Override
    protected void init() {
        setPageStateView(mListView);
        showLoading();
        pageIndex = 0;
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setOnRefreshListener(this);
        listData = new ArrayList<>();
        mAdapter = new RecommendedAdapter(mContext, R.layout.item_frag_recomment, listData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecommendBean recommendBean = (RecommendBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), RecommendActivity.class);
                intent.putExtra(Contants.KEY_OBJ_RECOMMEND,recommendBean);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        onDownPullRefresh();
    }

    /**
     * 获取资讯数据
     */
    public void requestData() {
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP,HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_RECOMMEND_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_RECOMMEND_SIGN);
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX, pageIndex + "");
        request(params, HttpConstant.RECOMMEND_LIST);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.RECOMMEND_LIST:
                Type type = new TypeToken<ArrayList<RecommendBean>>() {
                }.getType();
                listData = JsonUtil.getListModle(response.toString(), "data", type);
                if (pageIndex == 0) {
                    mAdapter.setList(listData);
                    mListView.stopRefresh();
                } else {
                    if (listData.size() == 0) {
                        ToastUtil.showToastCenter(mContext, "没有更多数据了");
                        pageIndex = pageIndex - 1;
                    } else {
                        mAdapter.addList(listData);
                    }
                    mListView.stopLoadMore();
                }
                showContent();
                break;
            default:
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);
        if(mAdapter.getCount()==0){
            showError();
        }else{
            showToast("网络不给力");
            mListView.onRefreshComplete();
        }
    }

    @Override
    protected void onClickRetry() {
        showLoading();
        onDownPullRefresh();
    }
}

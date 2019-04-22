package com.lzyc.ybtappcal.activity.home;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.payment.order.OrderbyActivity;
import com.lzyc.ybtappcal.adapter.HotAdapter;
import com.lzyc.ybtappcal.bean.BannerBean;
import com.lzyc.ybtappcal.bean.GoodsDrugListBean;
import com.lzyc.ybtappcal.bean.GoodsListBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.TBanner;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


/**
 * 热门药品／分类列表
 * Created by lxx on 2017/5/1.
 */
public class HotActivity extends BaseActivity implements OnYbtRefreshListener {
    private static final String TAG=HotActivity.class.getSimpleName();
    @BindView(R.id.list_hot)
    XYbtRefreshListView listHot;

    HotAdapter mAdapter;
    Context mContext;

    String bannerName;//获取banner时传递的banner名称  category_banner_1 1是分类id   attribute_banner_1 1是属性id  下划线拼接

    List<GoodsListBean> mData = new ArrayList<>();

    String type;

    String idStr;

    int pageIndex;

    @Override
    public int getContentViewId() {
        return R.layout.activity_hot;
    }

    @Override
    protected void init() {

        mContext = this;

        setPageStateView();

        showLoading();

        initBundles();

        initListView();

        requestBanner();

        requestType();
    }

    private void initBundles() {
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString(Contants.KEY_HOME_TITLE);
        type = bundle.getString(Contants.KEY_HOME_TYPE);
        idStr = bundle.getString(Contants.KEY_HOME_ID);

        setTitleName(title);

        bannerName = type + "_banner_" + idStr;
    }
    TBanner tbanner;
    private void initListView(){
        mAdapter = new HotAdapter(mContext, R.layout.item_type_drug, mData);

        listHot.setPullLoadEnable(true);
        listHot.setPullRefreshEnable(false);
        View headerView=View.inflate(mContext,R.layout.item_home_banner,null);
        tbanner= (TBanner) headerView.findViewById(R.id.tbanner);
        tbanner.setVisibility(View.GONE);
        listHot.addHeaderView(headerView,null,false);
        listHot.setAdapter(mAdapter);
        listHot.setOnRefreshListener(this);

        listHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Bundle bundle = new Bundle();
                GoodsListBean bean= (GoodsListBean) adapterView.getItemAtPosition(position);
                bundle.putString(HttpConstant.SHOP_GOODS_PARAM_DKID, bean.getDKID());
                openActivity(OrderbyActivity.class, bundle);
            }
        });
        tbanner.setOnImgRollChanged(new TBanner.ImgRollChanged() {
            @Override
            public void onChanged(int p) {
//                showToast("####tbanner##setOnImgRollChanged##");
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

    @Override
    protected void onClickRetry() {
        showLoading();
        requestType();
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
        params.put(HttpConstant.SHOP_HOME_PARAM_NAME, bannerName);//bannerName     "category_banner_1"

        request(params, HttpConstant.HOME_PAGE_BANNER);
    }


    /**
     * 加载更多
     */
    private void requestType() {
        Map<String, String> params = new HashMap<>();

        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_SHOP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_HOME_GOODS_DRUG_LIST);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_HOME_GOODS_DRUG_LIST_SIGN);
        params.put(HttpConstant.PARAM_KEY_TYPE, type);
        params.put(HttpConstant.ID_SMALL, idStr);
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX, String.valueOf(pageIndex));

        request(params, HttpConstant.HOME_TYPE);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);

        switch (what){
            case HttpConstant.HOME_PAGE_BANNER:
                try {
                    JSONObject dataBanner = response.getJSONObject(HttpConstant.DATA);
                    BannerBean bannerBean = new Gson().fromJson(dataBanner.toString(), BannerBean.class);

                    List<String> bannerURl = new ArrayList<>();
                    for(BannerBean.ImagesBean bean : bannerBean.getImages()){
                        bannerURl.add(bean.getImg_url());
                    }

                    if(bannerURl.isEmpty()){
                        tbanner.setVisibility(View.GONE);
                        return;
                    }
                    tbanner.setVisibility(View.VISIBLE);
                    tbanner.update(bannerURl);

                } catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case HttpConstant.HOME_TYPE:

                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);

                    GoodsDrugListBean bean = new Gson().fromJson(data.toString(), GoodsDrugListBean.class);

                    mData = bean.getList();


                    if (pageIndex == 0) {
                        mAdapter.setList(mData);
                        listHot.stopRefresh();
                    } else {
                        if (mData.size() == 0) {
                            ToastUtil.showToastCenter(mContext, "没有更多数据了");
                            pageIndex = pageIndex - 1;
                        } else {
                            mAdapter.addList(mData);
                        }
                        listHot.stopLoadMore();

                    }
                    showContent();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void onDownPullRefresh() {

    }

    @Override
    public void onLoadingMore() {
        pageIndex += 1;
        requestType();
    }

    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);

        try{
            ErrorResponse errorResponse= JsonUtil.getModle(errorMsg,ErrorResponse.class);
            switch (errorResponse.getWhat()){
                case HttpConstant.HOME_TYPE:
                    if(null == mData || mData.isEmpty()){
                        if(errorResponse.isNetError()){
                            showError();
                        }else{
                            showEmpty(errorServer,R.mipmap.empty_error_net_server);
                        }
                    }else{
                        pageIndex -= 1;
//                        showToast("网络不给力");
                        showToast(errorResponse.getMsg());
                        listHot.stopLoadMore();
                    }
                    break;

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

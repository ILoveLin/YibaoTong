package com.lzyc.ybtappcal.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.bean.TopBanner;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 启动页
 * @author yang
 */
public class LaunchActivity extends BaseActivity {
    private static final String TAG=LaunchActivity.class.getSimpleName();
    private boolean isFirst;
    @BindView(R.id.iv_holder)
    ImageView ivHolder;

    @Override
    public int getContentViewId() {
        return R.layout.activity_launch;
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
    protected void init() {
        setTitleBarVisibility(View.GONE);
        isFirst = (Boolean) SharePreferenceUtil.get(this, SharePreferenceUtil.IS_FIRST, true);
        SharePreferenceUtil.put(LaunchActivity.this, SharePreferenceUtil.IS_LAUNCH, true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestPageStyle();
//                if(isFirst){
//                    openActivity(GuideActivity.class);
//                    finish();
//                }else {
//                    SharePreferenceUtil.put(LaunchActivity.this,SharePreferenceUtil.DYNAMIC_SWITCH_TAB, Contants.TAB_HOME);
//                    openActivity(MainActivity.class);
//                    finish();
//                }
            }
        }, 1500);
        IntentFilter filter = new IntentFilter("lauchActivity_finish");
        registerReceiver(mBroadcastReceiver,filter);
    }


    /**
     * 获取banner
     */
    public void requestPageStyle() {
        Map<String, String> params = new HashMap<>();
        if(!NetworkUtil.CheckConnection(this)){
            switchMainActivity();
            return;
        }
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.SHOP_HOME_BANNER_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.SHOP_HOME_BANNER_SIGN);
        params.put(HttpConstant.SHOP_HOME_PARAM_OS, HttpConstant.SHOP_HOME_PARAM_PAGE_ANDROID);
        params.put(HttpConstant.SHOP_HOME_PARAM_NAME, "home_advert");
        request(params, HttpConstant.ADVERTISEMENT);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        switch (what) {
            case HttpConstant.ADVERTISEMENT:
                try {
                    TopBanner topBanner = JsonUtil.getModle(response.toString(), TopBanner.class);
                    TopBanner.DataBean dataBean = topBanner.getData();
                    if (dataBean != null) {
                        ArrayList<TopBanner.DataBean.ImagesBean> listImage = dataBean.getImages();
                        if (!listImage.isEmpty()) {
                            switchAdvertisementActivity(listImage);
                        } else {
                            switchMainActivity();
                        }
                    }
                } catch (Exception e) {
                    switchMainActivity();
                }
                break;
        }
    }

    private void switchAdvertisementActivity(ArrayList<TopBanner.DataBean.ImagesBean> listImage) {
        try{
            TopBanner.DataBean.ImagesBean imagesBean=listImage.get(0);
            String url=imagesBean.getImg_url();
            if(!TextUtils.isEmpty(url)){
                Picasso.with(mContext).load(url).into(ivHolder);
            }
            Bundle mBundle = new Bundle();
            mBundle.putSerializable(Contants.KEY_IMAGE, imagesBean);
            openActivityNoAnim(AdvertisementActivity.class, mBundle);
            finish();
            overridePendingTransition(0,0);
            //这里暂时不要关闭该页面,因为该页面背景设置为了透明,如果在广告页面还没有完全启动就关闭了,会出现跳屏现象
        }catch (Exception e){
            switchMainActivity();
        }

    }

    @Override
    public void error(String errorMsg) {
        switchMainActivity();
    }

    /**
     * 跳转到主页
     */
    private void switchMainActivity() {
        SharePreferenceUtil.put(LaunchActivity.this, SharePreferenceUtil.DYNAMIC_SWITCH_TAB, Contants.TAB_HOME);
        openActivity(MainActivity.class);
        finish();
        overridePendingTransition(0,0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
              if(intent.getAction().equals("lauchActivity_finish")){
                  finish();
                  overridePendingTransition(0,0);
              }
        }
    };

}

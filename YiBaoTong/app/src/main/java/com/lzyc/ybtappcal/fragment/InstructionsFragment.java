package com.lzyc.ybtappcal.fragment;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseFragment;
import com.lzyc.ybtappcal.constant.Contants;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

/**
 * 药品说明书
 * Created by lxx on 2017/6/27.
 */

public class InstructionsFragment extends BaseFragment {
    private static final String TAG = InstructionsFragment.class.getSimpleName();

    @BindView(R.id.webview)
    WebView webview;

    @Override
    public int getContentViewId() {
        return R.layout.activity_home_web;
    }

    @Override
    protected void init() {
        initView();
    }

    private void initView(){

        Bundle bundle = getArguments();

        if(null == bundle) return;

        String url = bundle.getString(Contants.KEY_STR_URL);

        webview.loadUrl(url);

        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }


    int tabValue;


    @Override
    public void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

}

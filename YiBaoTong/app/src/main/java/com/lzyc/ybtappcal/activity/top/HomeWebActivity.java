package com.lzyc.ybtappcal.activity.top;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

/**
 * Created by lxx on 2017/4/24.
 */
public class HomeWebActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_home_web;
    }

    @Override
    public void init() {

        setTitleName("详情");

        showLoading();

        initData();
    }

    private void initData(){
        String url = getIntent().getExtras().getString("url");

        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);

        showContent();

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    private static final String TAG = HomeWebActivity.class.getSimpleName();

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

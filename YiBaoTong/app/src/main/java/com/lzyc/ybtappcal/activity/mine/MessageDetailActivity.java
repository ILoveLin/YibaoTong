package com.lzyc.ybtappcal.activity.mine;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.constant.Contants;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindString;
import butterknife.BindView;

/**
 * 消息详情
 * package_name com.lzyc.ybtappcal.activity.mine
 * Created by yang on 2016/5/10.
 */
public class MessageDetailActivity extends BaseActivity {
    private static final String TAG=MessageDetailActivity.class.getSimpleName();
    @BindView(R.id.webview)
    WebView mWebView;
    @BindString(R.string.title_message_detail)
    String titleName;

    private String url;

    @Override
    public int getContentViewId() {
        return R.layout.activity_message_detail;
    }

    @Override
    public void init() {
        setTitleName(titleName);
        url = getIntent().getExtras().getString(Contants.KEY_STR_URL);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
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
}
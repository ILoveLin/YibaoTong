package com.lzyc.ybtappcal.activity.account;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindString;
import butterknife.BindView;

/**
 * 使用条款
 * @author yang
 */
public class TermActivity extends BaseActivity {
    private static final String TAG=TermActivity.class.getSimpleName();
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindString(R.string.title_term)
    String titleName;
    @BindString(R.string.term_url)
    String URL;

    @Override
    public int getContentViewId() {
        return R.layout.activity_term;
    }

    @Override
    public void init() {
        setTitleName(titleName);
        setPageStateView();
        initdata();
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

    private void initdata() {
        if (!NetworkUtil.CheckConnection(this)) {
            showError();
            return;
        }
        showContent();
        webview.loadUrl(URL);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
    }

}

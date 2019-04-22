package com.lzyc.ybtappcal.activity.top;

import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 医院详情
 * @author yang
 */
public class HispitalDetailActivity extends BaseActivity {
    private static final String TAG=HispitalDetailActivity.class.getSimpleName();

    @BindView(R.id.wb_hospital_detail)
    WebView wb_hospital_detail;
    @BindView(R.id.progressbar)
    ProgressBar wb_pb;
    @BindString(R.string.title_hispital_detail)
    String titleName;
    @BindString(R.string.empty_hisptital_detail)
    String strEmpty;
    private String url;
    private String hUrl;
    private String yyId;
    private int num = 1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_hospital_detail;
    }

    @Override
    public void init() {
        setTitleName(titleName);
        setPageStateView();
        setTitleRightBtnResources(R.drawable.selector_titlebar_right_a);
        hUrl = getIntent().getStringExtra(Contants.KEY_STR_URL);
        yyId = getIntent().getStringExtra(Contants.KEY_STR_YYID);
        if (hUrl == null) {
            showEmpty(strEmpty,R.mipmap.icon_empty_hospitorlist);
        }
        WebSettings settings = wb_hospital_detail.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBuiltInZoomControls(false);//support zoom
        settings.setUseWideViewPort(false);// 这个很关键
        settings.setLoadWithOverviewMode(true);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }
        refresh();
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

    private void refresh() {
        wb_hospital_detail.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                wb_pb.setProgress(newProgress);
                if (newProgress == 100) {
                    wb_pb.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
        wb_hospital_detail.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        setType(num);
    }

    @OnClick({R.id.ib_left, R.id.ib_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_left:
                HispitalDetailActivity.this.finish();
                break;
            case R.id.ib_right:
                //切换字体
                if (num == 1) {
                    num = 2;
                } else {
                    num = 1;
                }
                setType(num);
                break;
            default:
                break;
        }
    }



    private void setType(int num) {
        switch (num) {
            case 1:
                url = hUrl + "HospitalDetailA?Yyid=" + yyId;
                break;
            case 2:
                url = hUrl + "HospitalDetailB?Yyid=" + yyId;
                break;
            default:
                break;
        }
        if (!NetworkUtil.CheckConnection(this)) {
            showError();
            return;
        }
        wb_hospital_detail.loadUrl(url);
        showContent();
    }

    @Override
    protected void onClickRetry() {
        refresh();
    }
}

package com.lzyc.ybtappcal.activity.top;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.bean.RecommendBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.Util;
import com.lzyc.ybtappcal.widget.popupwindow.BasePopupWindow;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 资讯详情
 *
 * @author yang
 */
public class RecommendActivity extends BaseActivity implements UMShareListener{
    private static final String TAG = RecommendActivity.class.getSimpleName();
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    private String url;
    private int num = 1;
    private String mUrl;
    private String id;
    private String title;
    private String img;
    private String time;
    private String intro;
    private UMImage image;
    private BasePopupWindow popup;

    @Override
    public int getContentViewId() {
        return R.layout.activity_recommend;
    }

    @Override
    public void init() {
        setTitleBarVisibility(View.GONE);
        setPageStateView(webView);
        RecommendBean mRecommendBean = (RecommendBean) getIntent().getSerializableExtra(Contants.KEY_OBJ_RECOMMEND);
        mUrl = mRecommendBean.getUrl();
        id = mRecommendBean.getId();
        title = mRecommendBean.getTitle();
        img = mRecommendBean.getImages();
        time = mRecommendBean.getCtime();
        intro = mRecommendBean.getIntro();
        WebSettings settings = webView.getSettings();
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

    private void refresh() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                }
                return true;   //注意点:返回true表示我们自己处理,false我们可以忽略
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        setType();
    }

    @OnClick({R.id.ib_recommend_back, R.id.ib_share, R.id.ib_font})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_recommend_back:
                finish();
                break;
            case R.id.ib_share:
                initPopupShare();
                break;
            case R.id.ib_font:
                //切换字体
                if (num == 1) {
                    num = 2;
                } else {
                    num = 1;
                }
                setType();
                break;
            default:
                super.onClickTitleLeftBtn(view);
                break;
        }
    }

    private void setType() {
        if (!NetworkUtil.CheckConnection(RecommendActivity.this)) {
            showError();
            return;
        }
        switch (num) {
            case 1:
                url = mUrl + "RecommendA/index?id=" + id;
                break;
            case 2:
                url = mUrl + "RecommendB/index?id=" + id;
                break;
            default:
                break;
        }
        webView.loadUrl(url);
        showContent();
    }

    /**
     * 分享
     */
    private void initPopupShare() {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
//        final UMImage image = new UMImage(this, bitmap);
        image = new UMImage(this, img);
        if (intro.isEmpty()) {
            intro = " ";
        }
        final String text = intro;
        if (img == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
            image = new UMImage(this, bitmap);
        } else {
            image = new UMImage(this, img);
        }
        popup = new BasePopupWindow(this, R.layout.popup_share, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View conentView = popup.getConentView();
        conentView.findViewById(R.id.tv_popup_share_cancel).setOnClickListener(onShareClick);
        conentView.findViewById(R.id.tv_popup_share_wb).setOnClickListener(onShareClick);
        conentView.findViewById(R.id.tv_popup_share_weixin).setOnClickListener(onShareClick);
        conentView.findViewById(R.id.tv_popup_share_pyq).setOnClickListener(onShareClick);
        conentView.findViewById(R.id.tv_popup_share_qq).setOnClickListener(onShareClick);
        conentView.findViewById(R.id.tv_popup_share_qzone).setOnClickListener(onShareClick);
        conentView.findViewById(R.id.tv_popup_share_copy).setOnClickListener(onShareClick);
        popup.showPopupWindowTop(webView, Gravity.BOTTOM);
    }

    private View.OnClickListener onShareClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_popup_share_wb:
                    share(SHARE_MEDIA.SINA);
                    break;

                case R.id.tv_popup_share_weixin:
                    share(SHARE_MEDIA.WEIXIN);
                    break;

                case R.id.tv_popup_share_pyq:
                    share(SHARE_MEDIA.WEIXIN_CIRCLE);
                    break;

                case R.id.tv_popup_share_qq:
                    share(SHARE_MEDIA.QQ);
                    break;

                case R.id.tv_popup_share_qzone:
                    share(SHARE_MEDIA.QZONE);
                    break;

                case R.id.tv_popup_share_copy:
                    Util.copy(mContext, url);
                    showToast("复制成功");
                    popup.dismiss();
                    break;

                case R.id.tv_popup_share_cancel:
                    popup.dismiss();
                    break;
            }
        }
    };

    private void share(SHARE_MEDIA var) {
//        new ShareAction(this).setPlatform(var).setCallback(umShareListener)
//                .withTitle(title)
//                .withText(intro)
//                .withMedia(image)
//                .withTargetUrl(url)
//                .share();
        shareURL(var, image, url, title, intro);
        popup.dismiss();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
    protected void onDestroy() {
        UMShareAPI.get(this).release();
        super.onDestroy();
    }

    @Override
    protected void onClickRetry() {
        refresh();
    }


    /**
     * 分享链接
     * @param platform
     * @param image
     * @param contentUrl
     * @param title
     * @param content
     */
    private  void shareURL(SHARE_MEDIA platform, UMImage image, String contentUrl, String title, String content){
        UMWeb web = new UMWeb(contentUrl);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(content);
        new ShareAction(RecommendActivity.this).setPlatform(platform).setCallback(this)
                .withMedia(web)
                .share();
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {

    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }
}

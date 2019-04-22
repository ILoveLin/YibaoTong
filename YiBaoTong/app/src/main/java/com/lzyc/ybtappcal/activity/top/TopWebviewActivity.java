package com.lzyc.ybtappcal.activity.top;

import android.text.TextUtils;
import android.view.View;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.MainActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.view.ProgressWebView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yang on 2017/04/24.
 */
public class TopWebviewActivity extends BaseActivity {
    private static final String TAG=TopWebviewActivity.class.getSimpleName();
    @BindView(R.id.webview)
    ProgressWebView webView;
    String titleName;
    private int typePage;

    @Override
    public int getContentViewId() {
        return R.layout.activity_webview_top;
    }

    @Override
    protected void init() {
        titleName=getIntent().getStringExtra(Contants.KEY_STR_TITLE);
        if(TextUtils.isEmpty(titleName)){
            setTitleName("详情");
        }else{
            setTitleName(titleName);
        }
        String url=getIntent().getStringExtra(Contants.KEY_STR_URL);
        typePage=getIntent().getIntExtra(Contants.KEY_PAGE,Contants.VAL_PAGE);
        webView.loadUrl(url);
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

    /**
     * title 左侧按钮点击事件
     *
     * @param v
     */
    @OnClick(R.id.ib_left)
    public void onClickTitleLeftBtn(View v) {
        if(typePage==Contants.VAL_PAGE_ADVERTIMENT){
            switchMainActivity();
        }
        this.finish();
    }
    /**
     * 跳转到主页
     */
    private void switchMainActivity() {
        SharePreferenceUtil.put(TopWebviewActivity.this, SharePreferenceUtil.DYNAMIC_SWITCH_TAB, Contants.TAB_HOME);
        openActivity(MainActivity.class);
    }

    @Override
    public void onBackPressed() {
        if(typePage==Contants.VAL_PAGE_ADVERTIMENT){
            switchMainActivity();
        }
        super.onBackPressed();
    }
}

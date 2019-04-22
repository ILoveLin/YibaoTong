package com.lzyc.ybtappcal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.constant.Contants;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫码错误
 * package_name com.lzyc.ybtappcal.activity
 * Created by yang on 2016/6/9.
 */
public class ScancodeErrorActivity extends BaseActivity {
    private static final String TAG=ScancodeErrorActivity.class.getSimpleName();

    @BindView(R.id.error_scan_tv_desc)
    TextView error_scan_tv_desc;
    private int typePage;

    @Override
    public int getContentViewId() {
        return R.layout.activity_error_scancode;
    }

    @Override
    public void init() {
        setTitleName("提示");
        typePage = getIntent().getExtras().getInt(Contants.KEY_PAGE_SEARCH);
        String desc = getIntent().getExtras().getString(Contants.KEY_RESULT_CODE);
        error_scan_tv_desc.setText("" + desc);
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

    @OnClick({R.id.error_scan_tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.error_scan_tv_search:
                requestEventCode("l001");
                //没药,搜索跳转
                Bundle mBundle = new Bundle();
                mBundle.putInt(Contants.KEY_PAGE_SEARCH, typePage);
                openActivity(SearchActivity.class, mBundle);
                break;
            default:
                super.onClickTitleLeftBtn(view);
                break;
        }
    }

}

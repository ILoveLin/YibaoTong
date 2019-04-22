package com.lzyc.ybtappcal.activity;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 投保
 * Created by yang on 17-6-15.
 */
public class InsuranceActivity extends BaseActivity{
    private static final String TAG=InsuranceActivity.class.getSimpleName();
    @Override
    public int getContentViewId() {
        return R.layout.activity_insurance;
    }

    @Override
    protected void init() {
        setTitleName("惠享e生医疗费用补偿保险");
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

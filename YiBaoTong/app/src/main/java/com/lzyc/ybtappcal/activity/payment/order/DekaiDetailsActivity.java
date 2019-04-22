package com.lzyc.ybtappcal.activity.payment.order;

import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.StringUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

/**
 * 德开大药房
 * Created by Lxx on 2017/3/16.
 */
public class DekaiDetailsActivity extends BaseActivity {
    @BindView(R.id.activity_dekai_details_text)
    TextView tvText;

    @Override
    public int getContentViewId() {
        return R.layout.activity_dekai_details;
    }

    @Override
    public void init() {

        String title = getIntent().getExtras().getString(Contants.KEY_PHARMACY_TITLE);
        String pharmacy = getIntent().getExtras().getString(Contants.KEY_PHARMACY);

        setTitleName(title);

        try {

            String text = pharmacy.substring(title.length(), pharmacy.length());

            tvText.setText(StringUtils.getSpannableText(title, text, 14));
            return;
        } catch (Exception e){
            e.printStackTrace();
        }

        tvText.setText(pharmacy);
    }

    private static final String TAG = DekaiDetailsActivity.class.getSimpleName();

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

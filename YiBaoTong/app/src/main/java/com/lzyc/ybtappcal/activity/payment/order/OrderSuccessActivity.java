package com.lzyc.ybtappcal.activity.payment.order;

import android.content.Context;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.MainActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.app.ActivityCollector;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.widget.redpacket.RedPacketGroup;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 购买成功
 * Created by Lxx on 2017/3/30.
 */
public class OrderSuccessActivity extends BaseActivity {

    private final String OTC = "OTC";
    private static final String NOTICE = "<font color='#333333'><big>温馨提示:</big></font><normal> <font color='#999999'>晚上10点以后提交的需求将于次日早上10点前回拨给您。</font></normal>";
    @BindView(R.id.tv_title_name)
    TextView tvTitle;
    @BindView(R.id.act_succ_success)
    TextView tvSucc;
    @BindView(R.id.act_succ_connect)
    TextView tvConnect;
    @BindView(R.id.act_succ_notice)
    TextView tvNotice;
    @BindView(R.id.red_packed)
    RedPacketGroup redPacketGroup;

    private Context mContext;

    @Override
    public int getContentViewId() {
        return R.layout.activity_success;
    }

    @Override
    public void init() {
        mContext = this;

        setTitleBarVisibility(View.GONE);

        ActivityCollector.getInstance().addActivityOrder(this);
        ActivityCollector.getInstance().activityOrderAbout(this);
        String type = getIntent().getExtras().getString(OTC);
        String returnMoney = getIntent().getExtras().getString("return_money");
        redPacketGroup.setReturnMoney(returnMoney);

        if (!"1".equals(type)) {
            tvTitle.setText("预订成功");
            tvNotice.setText(Html.fromHtml(NOTICE));
        } else {
            tvTitle.setText("支付成功");
            tvSucc.setText("恭喜您，支付成功");
            tvConnect.setVisibility(View.GONE);
            tvNotice.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.ibtn_left, R.id.act_succ_back_home, R.id.act_succ_goto_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_left:
                SharePreferenceUtil.put(mContext, Contants.KEY_BOL_CART_REFRSH, true);
                ActivityCollector.removeOrderAll();
                break;
            case R.id.act_succ_back_home:
                SharePreferenceUtil.put(this, SharePreferenceUtil.DYNAMIC_SWITCH_TAB, Contants.TAB_HOME);
                openActivityNoAnim(MainActivity.class);
                this.finish();
                ActivityCollector.removeAll();
                break;
            case R.id.act_succ_goto_order:
                openActivity(OrderMineActivity.class);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        SharePreferenceUtil.put(mContext, Contants.KEY_BOL_CART_REFRSH, true);
        ActivityCollector.removeOrderAll();
        return super.onKeyDown(keyCode, event);
    }

    private static final String TAG = OrderSuccessActivity.class.getSimpleName();

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
        ActivityCollector.getInstance().removeActivityOrderAbout(this);
        ActivityCollector.getInstance().removeActivityOrder(this);
        super.onDestroy();
    }
}

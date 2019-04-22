package com.lzyc.ybtappcal.activity.mine.withdraw;

import android.view.View;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.bean.PersonWithdrawList;
import com.lzyc.ybtappcal.util.Intents;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

/**
 * 提现详情
 * Created by Lxx on 2017/4/11.
 */
public class MineWithdrawDetailActivity extends BaseActivity {

    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.alipay)
    TextView alipay;
    @BindView(R.id.aliname)
    TextView aliname;
    @BindView(R.id.create_time)
    TextView createTime;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.msg_type)
    TextView msgType;
    @BindView(R.id.v_bottom)
    View vBottom;

    @Override
    public int getContentViewId() {
        return R.layout.activity_mine_withdraw_detail;
    }

    @Override
    public void init() {
        setTitleName("提现明细");
        initDatas();
    }

    private void initDatas() {
        PersonWithdrawList.ListBean bean = (PersonWithdrawList.ListBean) getIntent().getSerializableExtra(Intents.DRAW_DETAIL);
        if (null == bean) return;

        balance.setText(bean.getBalance());
        alipay.setText(bean.getAlipay());
        aliname.setText(bean.getAliName());
        tvMsg.setText(bean.getMsg());
        createTime.setText(bean.getCreateTime());

        switch (bean.getState()) {
            case "1":
                tvMsg.setTextColor(getResources().getColor(R.color.color_333333));
                break;
            case "2":
                tvMsg.setTextColor(getResources().getColor(R.color.color_43a047));
                break;
            case "-1":
                tvMsg.setTextColor(getResources().getColor(R.color.color_f55959));
                break;
            default:
                break;
        }
    }

    private static final String TAG = MineWithdrawDetailActivity.class.getSimpleName();

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

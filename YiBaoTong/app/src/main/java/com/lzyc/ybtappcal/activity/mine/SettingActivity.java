package com.lzyc.ybtappcal.activity.mine;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.MainActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.app.ActivityCollector;
import com.lzyc.ybtappcal.app.App;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.DataCleanManager;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.util.VersionUtil;
import com.lzyc.ybtappcal.util.cache.SPCache;
import com.lzyc.ybtappcal.view.SwitchButtom;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：xujm
 * 时间：2016/2/22
 * 备注：设置
 */
public class SettingActivity extends BaseActivity {
    private static final String TAG=SettingActivity.class.getSimpleName();
    @BindView(R.id.setting_linear)
    LinearLayout setting_linear;
    @BindView(R.id.setting_tv_chache_clear)
    TextView setting_tv_chache_clear;
    @BindView(R.id.setting_tv_logout)
    TextView setting_tv_logout;
    @BindView(R.id.setting_switch_message)
    SwitchButtom setting_switch_message;
    @BindView(R.id.setting_logout_line)
    View setting_logout_line;
    @BindView(R.id.setting_tv_logout_bottom)
    View setting_tv_logout_bottom;
    @BindString(R.string.title_setting)
    String titleName;
    private String phone;
    private Boolean isLogin;
    private boolean isFristClick = true;
    private long fristTime = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                String chacheSize = DataCleanManager.getTotalCacheSize(SettingActivity.this);
                setting_tv_chache_clear.setText("" + chacheSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
            showToastOnece();
        }
    };

    private void showToastOnece() {
        if (isFristClick) {
            isFristClick = false;
            ToastUtil.showToastCenter(SettingActivity.this, "清除成功");

        } else if ((System.currentTimeMillis() - fristTime) > 2000) {
            ToastUtil.showToastCenter(SettingActivity.this, "清除成功");
            fristTime = System.currentTimeMillis();
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    public void init() {
        setTitleName(titleName);
        String messageState = SPCache.getInstance().getIsmessage();
        isLogin = (Boolean) SharePreferenceUtil.get(this, SharePreferenceUtil.IS_LOGIN, false);
        if (isLogin) {
            phone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
        }
        if (messageState.equals(Contants.STR_1)) {
            setting_switch_message.setOpened(false);
        } else {
            setting_switch_message.setOpened(true);
        }
        try {
            String chacheSize = DataCleanManager.getTotalCacheSize(SettingActivity.this);
            setting_tv_chache_clear.setText(chacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setting_switch_message.setColor(getResources().getColor(R.color.toggle_color_slelected), getResources().getColor(R.color.color_ffffff));
        setting_switch_message.setOnStateChangedListener(new SwitchButtom.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchButtom view) {
                requestEventCode("r001");
                requestMessage(Contants.STR_0);
                setting_switch_message.toggleSwitch(true);
            }

            @Override
            public void toggleToOff(SwitchButtom view) {
                requestEventCode("r009");
                requestMessage(Contants.STR_1);
                setting_switch_message.toggleSwitch(false);
            }
        });
    }

    @OnClick({R.id.setting_linear_clear, R.id.setting_tv_about, R.id.setting_tv_feedback, R.id.setting_tv_update, R.id.setting_tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_linear_clear:
                requestEventCode("r002");
                popupClearCache();
                break;
            case R.id.setting_tv_about:
                requestEventCode("r004");
                openActivity(AboutActivity.class);
                break;
            case R.id.setting_tv_feedback:
                requestEventCode("r005");
                openActivity(FeedBackActivity.class);
                break;
            case R.id.setting_tv_update:
                boolean isNew = (Boolean) SharePreferenceUtil.get(SettingActivity.this, SharePreferenceUtil.KEY_BOOL_VERSION_ISNEW, false);
                if (isNew) {
                    initPopupUpdate();
                } else {
                    VersionUtil v=new VersionUtil();
                    v.setContent(SettingActivity.this);
                    v.checkVersion();
                }
                break;
            case R.id.setting_tv_logout:
                popupLogout();
                break;
        }
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        isLogin = (Boolean) SharePreferenceUtil.get(this, SharePreferenceUtil.IS_LOGIN, false);
        if (isLogin) {
            phone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
        }
        if (isLogin) {
            setting_tv_logout.setVisibility(View.VISIBLE);
            setting_logout_line.setVisibility(View.VISIBLE);
            setting_tv_logout_bottom.setVisibility(View.VISIBLE);
        } else {
            setting_tv_logout.setVisibility(View.GONE);
            setting_logout_line.setVisibility(View.GONE);
            setting_tv_logout_bottom.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    /**
     * 更新
     */
    public void initPopupUpdate() {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getLineTwo().setVisibility(View.GONE);
        twoButton.getTv_content().setText("已经是最新版本，无需更新");
        twoButton.getTvOk().setVisibility(View.GONE);
        twoButton.getTvCancel().setText("知道了");
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(setting_linear, Gravity.CENTER);
    }

    /**
     * 退出登录
     */
    public void popupLogout() {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText("确认退出当前账号？");
        twoButton.getTvOk().setText("确认");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferenceUtil.put(SettingActivity.this, SharePreferenceUtil.IS_LOGIN, false);
                //清空登录相关信息
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.UID, "");
                SharePreferenceUtil.put(SettingActivity.this, SharePreferenceUtil.USER_NAME, "");
                SharePreferenceUtil.put(SettingActivity.this, SharePreferenceUtil.NICKNAME, "");
                SharePreferenceUtil.put(SettingActivity.this, SharePreferenceUtil.PHONE, "");
                SharePreferenceUtil.put(SettingActivity.this, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, Contants.STR_TYPE_JOB);
                SharePreferenceUtil.put(SettingActivity.this, SharePreferenceUtil.KEY_BOOL_POP_JOB_ISCHEK, true);
                SharePreferenceUtil.put(SettingActivity.this, SharePreferenceUtil.PROVINCE_CANBAO, "北京");
                SharePreferenceUtil.put(SettingActivity.this, SharePreferenceUtil.CITY_CANBAO, "北京市");
                twoButton.dismiss();
                SPCache.getInstance().clearCache();
                SharePreferenceUtil.put(SettingActivity.this, SharePreferenceUtil.DYNAMIC_SWITCH_TAB, Contants.TAB_MINE);
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ActivityCollector.removeAll();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(setting_linear, Gravity.CENTER);
    }


    /**
     * 清除缓存
     */
    public void popupClearCache() {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTvTitle().setVisibility(View.GONE);
        twoButton.getTv_content().setText("确认清除缓存？");
        twoButton.getTvOk().setText("确认");
        twoButton.getTvCancel().setText("取消");
        twoButton.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String chacheSize = DataCleanManager.getTotalCacheSize(SettingActivity.this);
                    if (!chacheSize.equals("0B")) {
                        DataCleanManager.clearAllCache(SettingActivity.this);
                    }
                    requestEventCode("r003");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
                twoButton.dismiss();
            }
        });
        twoButton.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(setting_linear, Gravity.CENTER);
    }

    /**
     * 设置消息是否提醒
     */
    public void requestMessage(String state) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_UCENTER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.UCENTER_SETMESSAGE_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.UCENTER_SETMESSAGE_SIGN);
        if (!TextUtils.isEmpty(phone)) {
            params.put(HttpConstant.PARAM_KEY_PHONE, phone);
        }
        if (!TextUtils.isEmpty(App.getInstance().mPushAgent.getRegistrationId())) {
            params.put(HttpConstant.PARAM_KEY_TOKEN, App.getInstance().mPushAgent.getRegistrationId());
        }
        params.put(HttpConstant.IS_MESSAGE, state);
        request(params);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}

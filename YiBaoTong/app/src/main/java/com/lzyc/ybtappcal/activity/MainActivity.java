package com.lzyc.ybtappcal.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.app.ActivityCollector;
import com.lzyc.ybtappcal.app.App;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.fragment.HomeFragment;
import com.lzyc.ybtappcal.fragment.MineFragment;
import com.lzyc.ybtappcal.fragment.RecommendedFragment;
import com.lzyc.ybtappcal.fragment.TopFragment;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.VersionUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主界面
 * @author yang
 */
public class MainActivity extends BaseActivity {
    private FragmentManager fragmentManager;
    private TopFragment topFragment;
    private HomeFragment homeFragment;
    //    private ReimbursementFragment reimbursementFragment;
    private RecommendedFragment recommendedFragment;
    public static boolean isTabBiyao = false;
    private MineFragment mineFragment;
    @BindView(R.id.tv_tab_homepage)
    TextView tvTabHomePage;
    @BindView(R.id.tv_tab_drug_query)
    TextView tvTabDrugsQuery;
    @BindView(R.id.tv_tab_recomment)
    TextView tvTabRecomment;
    @BindView(R.id.tv_tab_mine)
    TextView tvTabMine;
    @BindView(R.id.rel_tab_mine)
    RelativeLayout relTabMine;
    @BindView(R.id.iv_tab_mine)
    ImageView ivTabMine;
    @BindView(R.id.rel_tab_biyao)
    RelativeLayout relTabBiYao;
    @BindView(R.id.iv_tab_biyao)
    ImageView ivTabBiYao;
    @BindView(R.id.linear_bottom)
    LinearLayout linearBottom;
    private Integer valTab;
    private MyBroadcastReceiver receiver = new MyBroadcastReceiver();
    private Handler mHandler = new Handler();
    private String phone;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        setTitleBarVisibility(View.GONE);
        phone = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.PHONE, "");
        linearBottom.setVisibility(View.VISIBLE);
//        requestREAD_PHONE_STATE();
        registerBroadCastReceiver();
        fragmentManager = getSupportFragmentManager();
        if (Contants.isFirstEnter) {//如果程序时第一次启动，那么做token的请求操作，和版本更新的请求
            VersionUtil v=new VersionUtil();
            v.setContent(MainActivity.this);
            v.checkVersion();
            requestDeviceToken();
            requestNotReadMessage();
            Contants.isFirstEnter = false;
        }
        relTabMine.setClickable(false);
        relTabMine.setEnabled(false);
        mHandler.postDelayed(new Runnable() {//处理点击跳过按钮触发主界面的事件，事件延迟授予
            @Override
            public void run() {
                relTabMine.setClickable(true);
                relTabMine.setEnabled(true);
            }
        }, 1500);
        valTab = (Integer) SharePreferenceUtil.get(this, SharePreferenceUtil.DYNAMIC_SWITCH_TAB, Contants.TAB_HOME);
        setChioceItem(valTab);
    }

    private void registerBroadCastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contants.ACTION_NAME_PUSH_READ_NO);
        registerReceiver(receiver, intentFilter);
    }

    /**
     * 访问未读消息
     */
    private void requestNotReadMessage() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_UCENTER);
        params.put(HttpConstant.PARAM_KEY_CLASS,HttpConstant.UCENTER_NEWMESSAGE_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.UCENTER_NEWMESSAGE_SIGN);
        if (phone.isEmpty()) {
            return;
        }
        params.put(HttpConstant.PARAM_KEY_PHONE, phone);
        if (!NetworkUtil.CheckConnection(this)) {
            return;
        }
        request(params, HttpConstant.MESSAGE_LIST_NOREAD);
    }

    public void setChioceItem(int index) {
        if (index < 0) {
            index = Contants.TAB_HOME;
        }
        SharePreferenceUtil.put(this, SharePreferenceUtil.DYNAMIC_SWITCH_TAB, index);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case Contants.TAB_HOME:
                requestEventCode("a008");
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.ll_content, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }

                tvTabHomePage.setSelected(true);
                tvTabDrugsQuery.setSelected(false);
                tvTabRecomment.setSelected(false);
                tvTabMine.setSelected(false);
                biyaoSelected(false);
                overAnim(tvTabHomePage);
                break;
            case Contants.TAB_DRUG_SEACH:
                requestEventCode("a013");
                if (topFragment == null) {
                    topFragment = new TopFragment();
                    transaction.add(R.id.ll_content, topFragment);
                } else {
                    transaction.show(topFragment);
                }
                biyaoSelected(true);

                tvTabHomePage.setSelected(false);
                tvTabDrugsQuery.setSelected(true);
                tvTabRecomment.setSelected(false);
                tvTabMine.setSelected(false);
                biyaoSelected(false);
                overAnim(tvTabDrugsQuery);

//                if (reimbursementFragment == null) {
//                    reimbursementFragment = new ReimbursementFragment();
//                    transaction.add(R.id.ll_content, reimbursementFragment);
//                } else {
//                    transaction.show(reimbursementFragment);
//                    reimbursementFragment.onResume();
//                }
//                tvTabHomePage.setSelected(false);
//                tvTabDrugsQuery.setSelected(true);
//                tvTabRecomment.setSelected(false);
//                tvTabMine.setSelected(false);
//                biyaoSelected(false);
//                overAnim(tvTabDrugsQuery);
//                requestEventCode("a005");
                break;
            case Contants.TAB_DRUGS_QUERY://药品查询
                requestEventCode("a014");
                if (topFragment == null) {
                    topFragment = new TopFragment();
                    transaction.add(R.id.ll_content, topFragment);
                } else {
                    transaction.show(topFragment);
                }
                biyaoSelected(true);
                break;

            case Contants.TAB_NEWS:
                requestEventCode("a006");
                if (recommendedFragment == null) {
                    recommendedFragment = new RecommendedFragment();
                    transaction.add(R.id.ll_content, recommendedFragment);
                } else {
                    transaction.show(recommendedFragment);
                }
                tvTabHomePage.setSelected(false);
                tvTabDrugsQuery.setSelected(false);
                tvTabRecomment.setSelected(true);
                tvTabMine.setSelected(false);
                biyaoSelected(false);
                overAnim(tvTabRecomment);
                break;

            case Contants.TAB_MINE:
                requestEventCode("a007");
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.ll_content, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
                tvTabHomePage.setSelected(false);
                tvTabDrugsQuery.setSelected(false);
                tvTabRecomment.setSelected(false);
                tvTabMine.setSelected(true);
                biyaoSelected(false);
                overAnim(relTabMine);
                break;
        }
        transaction.commit();
    }

    private void biyaoSelected(boolean b) {
        if (b) {
            ivTabBiYao.setVisibility(View.VISIBLE);
            relTabBiYao.setVisibility(View.GONE);
            tvTabHomePage.setSelected(false);
            tvTabDrugsQuery.setSelected(false);
            tvTabRecomment.setSelected(false);
            tvTabMine.setSelected(false);
            isTabBiyao = true;
        } else {
            ivTabBiYao.setVisibility(View.GONE);
            relTabBiYao.setVisibility(View.VISIBLE);
            isTabBiyao = false;
        }

    }


    @OnClick({R.id.tv_tab_homepage, R.id.tv_tab_drug_query, R.id.main_tab_biyao, R.id.tv_tab_recomment, R.id.rel_tab_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_tab_homepage:
                setChioceItem(Contants.TAB_HOME);
                break;
            case R.id.tv_tab_drug_query:
                setChioceItem(Contants.TAB_DRUG_SEACH);
                break;
            case R.id.main_tab_biyao:
//                if (!NetworkUtil.CheckConnection(mContext)) {
//                    showNetDialog();
//                    return;
//                }
//                EventBus.getDefault().post(new Event("a002"));
//                cityTypeChoose = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.CITY_TOP_CHOOSE, "北京市");
//                if (TextUtils.isEmpty(cityTypeChoose)) {
//                    cityTypeChoose = "北京市";
//                }
//                hanlder.sendEmptyMessage(MSG_SWITCH_CAPTURE);

                setChioceItem(Contants.TAB_DRUGS_QUERY);
                break;
            case R.id.tv_tab_recomment:
                setChioceItem(Contants.TAB_NEWS);
                break;
            case R.id.rel_tab_mine:
                setChioceItem(Contants.TAB_MINE);
                break;
            default:
        }
    }

    // 隐藏所有的Fragment,避免fragment混乱
    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
//        if (reimbursementFragment != null) {
//            transaction.hide(reimbursementFragment);
//        }
        if (topFragment != null) {
            transaction.hide(topFragment);
        }
        if (recommendedFragment != null) {
            transaction.hide(recommendedFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestNotReadMessage();
    }

    private void requestDeviceToken() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP,HttpConstant.APP_WAP);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.WAP_DEVICESTOKEN_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.WAP_DEVICESTOKEN_SIGN);
        params.put(HttpConstant.PARAM_KEY_TYPE,HttpConstant.OS_ANDROID);
        String currentLat = (String) SharePreferenceUtil.get(App.getInstance(), SharePreferenceUtil.LATITUDE, "");
        String currentLng = (String) SharePreferenceUtil.get(App.getInstance(), SharePreferenceUtil.LONGITUDE, "");
        if (!currentLat.isEmpty()) {
            params.put(HttpConstant.PARAM_KEY_LAT2, currentLat);
        }
        if (!currentLng.isEmpty()) {
            params.put(HttpConstant.PARAM_KEY_LNG2, currentLng);
        }
        params.put(HttpConstant.PARAM_KEY_TOKEN,  App.getInstance().mPushAgent.getRegistrationId());
        if (!NetworkUtil.CheckConnection(this)) {
            return;
        }
        LogUtil.y("###token##"+params.toString());
        httpRequest.postMap(HttpConstant.APP_URL, this, params);
    }


    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                exitTime = System.currentTimeMillis();
                showToast("再按一次退出程序");
            } else {
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                }
                MobclickAgent.onKillProcess(this);//进程杀死之前，保存统计数据
                ActivityCollector.removeAll();
                System.exit(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /*把某个控件往上拉伸一定的距离的动画*/
//       private void startAnim(View view){
//        final ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 0.8f);
//        final ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 0.8f);
//        final ObjectAnimator translationY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, -15);
//        AnimatorSet set = new AnimatorSet();
//        set.playTogether(scaleX, scaleY, translationY);
//        set.setDuration(200);
//        set.start();
//
//    }
    private void overAnim(View view) {
        final ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.8f, 1.0f);
        final ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.8f, 1.0f);
        final ObjectAnimator translationY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY, translationY);
        set.setDuration(200);
        set.start();
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        switch (what) {
            case HttpConstant.MESSAGE_LIST_NOREAD:
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    int messageStatus = data.getInt("messageStatus");
                    SharePreferenceUtil.put(MainActivity.this, SharePreferenceUtil.KEY_MESSAGE_STATUS, messageStatus);
                    if (messageStatus == 0) {
                        ivTabMine.setVisibility(View.GONE);
                    } else {
                        ivTabMine.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Contants.ACTION_NAME_PUSH_READ_NO)) {
                requestNotReadMessage();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.y("###############分享回调#######");
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

//    @AfterPermissionGranted(Contants.REQUEST_PERMISSION_IMEI)
//    public void requestREAD_PHONE_STATE() {//获取imei的权限，防止device_token获取失败
//        String perms = Manifest.permission.READ_PHONE_STATE;
//        if (!EasyPermissions.hasPermissions(this, perms)) {
//            EasyPermissions.requestPermissions(this, "申请设备状态权限", Contants.REQUEST_PERMISSION_IMEI, perms);
//        }
//    }

}

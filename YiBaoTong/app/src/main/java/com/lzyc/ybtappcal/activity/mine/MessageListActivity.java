package com.lzyc.ybtappcal.activity.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.account.LoginActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.bean.MessageCount;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.view.BadgeView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消息通知
 * package_name com.lzyc.ybtappcal.activity.mine
 * Created by yang on 2016/5/10.
 */
public class MessageListActivity extends BaseActivity {
    private static final String TAG=MessageListActivity.class.getSimpleName();
    @BindView(R.id.message_iv_dynamic)
    ImageView messageIvDynamic;
    @BindView(R.id.message_tv_dynamic_desc)
    TextView messageTvDynamicDesc;
    @BindView(R.id.message_tv_dynamic_badge)
    BadgeView messageTvDynamicBadge;
    @BindView(R.id.message_rel_dynamic)
    RelativeLayout messageRelDynamic;
    @BindView(R.id.message_iv_system)
    ImageView messageIvSystem;
    @BindView(R.id.message_tv_system_desc)
    TextView messageTvSystemDesc;
    @BindView(R.id.message_tv_system_badge)
    BadgeView messageTvSystemBadge;
    @BindView(R.id.message_rel_system)
    RelativeLayout messageRelSystem;
    private boolean isLogin;
    @BindString(R.string.title_message)
    String titleName;
    private MyBroadcastReceiver receiver = new MyBroadcastReceiver();
    private String phone;
    @Override
    public int getContentViewId() {
        return R.layout.activity_message_list;
    }

    @Override
    protected void init() {
        setTitleName(titleName);
        isLogin = (Boolean) SharePreferenceUtil.get(MessageListActivity.this, SharePreferenceUtil.IS_LOGIN, false);
        phone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
        setPageStateView();
        showLoading();
        registerBroadCastReceiver();
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        requestNotReadMessage();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
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
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.UCENTER_NEWMESSAGE_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.UCENTER_NEWMESSAGE_SIGN);
        if (phone.isEmpty()) {
            return;
        }
        params.put(HttpConstant.PARAM_KEY_PHONE, phone);
        if (!NetworkUtil.CheckConnection(this)) {
            showError();
            return;
        }
        request(params, HttpConstant.MESSAGE_LIST_NOREAD);
    }


    @Override
    public void done(String msg, int what, JSONObject response) {
        switch (what) {
            case HttpConstant.MESSAGE_LIST_NOREAD:
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    int messageStatus = data.getInt("messageStatus");
                    SharePreferenceUtil.put(MessageListActivity.this, SharePreferenceUtil.KEY_MESSAGE_STATUS, messageStatus);
                    JSONObject joMessageCount = data.getJSONObject("messageCount");
                    MessageCount messageCount = JsonUtil.getModle(joMessageCount.toString(), MessageCount.class);
                    int messageCountSYS = messageCount.getSystemMessageCount();
                    int messageCountDYN = messageCount.getCommentMessageCount();
                    messageTvDynamicBadge.setBadgeCount(messageCountDYN);
                    messageTvSystemBadge.setBadgeCount(messageCountSYS);
                    if (messageCountDYN == 0) {
                        messageTvDynamicBadge.setVisibility(View.GONE);
                    } else {
                        messageTvDynamicBadge.setVisibility(View.VISIBLE);
                    }
                    if (messageCountSYS == 0) {
                        messageTvSystemBadge.setVisibility(View.GONE);
                    } else {
                        messageTvSystemBadge.setVisibility(View.VISIBLE);
                    }
                    showContent();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showEmpty(errorServer,R.mipmap.empty_error_net_server);
                }catch (Exception e){
                    showEmpty(errorServer,R.mipmap.empty_error_net_server);
                }
                break;
        }
    }

    @OnClick({R.id.message_rel_dynamic, R.id.message_rel_system})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.message_rel_dynamic://评论动态
                if (isLogin) {
                    openActivity(DynMessageActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            case R.id.message_rel_system://系统消息
                requestEventCode("n002");
                if (isLogin) {
                    openActivity(SysMessageActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        try{
            ErrorResponse errorResponse=JsonUtil.getModle(errorMsg,ErrorResponse.class);
            if(errorResponse.getWhat()==HttpConstant.MESSAGE_LIST_NOREAD){
                if(errorResponse.isNetError()){
                    showError();
                }else{
                    showEmpty(errorServer,R.mipmap.empty_error_net_server);
                }
            }
        }catch (Exception e){
            showContent();
        }
    }

    /**
     * 点击了错误重试
     */
    protected void  onClickRetry(){
        showLoading();
        requestNotReadMessage();
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
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
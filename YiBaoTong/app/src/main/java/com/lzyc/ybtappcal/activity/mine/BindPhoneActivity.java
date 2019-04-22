package com.lzyc.ybtappcal.activity.mine;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.widget.TimerTextView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author yang
 */
public class BindPhoneActivity extends BaseActivity {
    private static final String TAG=BindPhoneActivity.class.getName();
    @BindView(R.id.ttv_getcode)
    TimerTextView ttvGetcode;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    String phone;
    @BindString(R.string.title_bind_phone)
    String titleName;
    @Override
    public int getContentViewId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void init() {
        setTitleName(titleName);
    }

    @OnClick({R.id.bt_bind_phone, R.id.ttv_getcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_bind_phone:
                phone = etPhone.getText().toString();
                String code = etCode.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showEditTextPrompt(etPhone, "手机号不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    showEditTextPrompt(etPhone, "验证码不能为空！");
                    return;
                }
                requestBind(phone, code);
                break;
            case R.id.ttv_getcode:
                requestEventCode("s010");
                phone = etPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showEditTextPrompt(etPhone, "手机号不能为空！");
                    return;
                }
                requestForgetPw(phone);
                break;
        }
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
     * 绑定新手机
     */
    public void requestBind(String newPhone, String code) {
        String oldphone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_UCENTER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.UCENTER_UPPHONE_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.UCENTER_UPPHONE_SIGN);
        params.put(HttpConstant.PARAM_KEY_PHONE, oldphone);
        params.put(HttpConstant.PARAM_KEY_PHONE_NEW, newPhone);
        params.put(HttpConstant.PARAM_KEY_CODE, code);
        request(params, HttpConstant.MY_BIND_PHONE);
    }


    /**
     * 获取验证码
     *
     * @param phone
     */
    public void requestForgetPw(String phone) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_UCENTER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.UCENTER_UPPHONE_SENDSMS_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.UCENTER_UPPHONE_SENDSMS_SIGN);
        params.put(HttpConstant.PARAM_KEY_PHONE, phone);
        request(params, HttpConstant.GET_CODE);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.MY_BIND_PHONE:
                requestEventCode("s011");
                ToastUtil.showToastCenter(BindPhoneActivity.this, "更新成功！");
                finish();
                SharePreferenceUtil.put(this, SharePreferenceUtil.PHONE, phone);
                break;
            case HttpConstant.GET_CODE:
                ttvGetcode.start();
                etPhone.clearFocus();
                etCode.requestFocus();
                break;
            default:
                break;
        }
    }


    @Override
    public void error(String errorMsg) {
        if (errorMsg.length() <= 20) {
            showEditTextPrompt(etPhone, errorMsg);
        }
    }
}
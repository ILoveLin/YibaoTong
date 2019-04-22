package com.lzyc.ybtappcal.activity.account;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.app.ActivityCollector;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.KeyBoardUtils;
import com.lzyc.ybtappcal.widget.TimerTextView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册
 * @author yang
 */
public class RegisterActivity extends BaseActivity {
    private static final String TAG=RegisterActivity.class.getSimpleName();
    protected static final int MSG_HAVE_SELECTED = 1;
    protected static final int MSG_HAVE_UNSELECTED = 2;

    @BindView(R.id.ttv_get_code)
    TimerTextView ttvGetCode;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.et_code_invite)
    EditText etCodeInvite;
    @BindView(R.id.cb_register)
    CheckBox cbRegister;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.v_title_line)
    View vTitleLine;
    private String phone;
    private String inviteCode;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HAVE_UNSELECTED:
                    tvNext.setPressed(false);
                    break;
                case MSG_HAVE_SELECTED:
                    tvNext.setPressed(true);
                    break;

            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_register;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void init() {
        ActivityCollector.getInstance().addActivityRegiser(this);
        setTitleName("注册", Color.parseColor("#70ffffff"));
        setTitleBackrounColor(R.color.color_377eb4);
        setTitleLeftBtnVisibility(View.GONE);
        setTitleRegisterLeftBtnVisibility(View.VISIBLE);
        responseListener();
//        setRightName("登录");

        vTitleLine.setVisibility(View.GONE);
    }

    private void responseListener() {
        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    requestEventCode("u001");
                }
            }
        });

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                final String phone_text = etPhone.getText().toString().trim();
                final String password_text = etCode.getText().toString().trim();
                if (TextUtils.isEmpty(phone_text) || TextUtils.isEmpty(password_text)) {
                    mHandler.sendEmptyMessage(MSG_HAVE_UNSELECTED);

                } else {
                    mHandler.sendEmptyMessage(MSG_HAVE_SELECTED);
                }

            }
        });
        etCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    requestEventCode("u003");
                }
            }
        });

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                final String phone_text = etPhone.getText().toString().trim();
                final String password_text = etCode.getText().toString().trim();
                if (TextUtils.isEmpty(phone_text) || TextUtils.isEmpty(password_text)) {
                    mHandler.sendEmptyMessage(MSG_HAVE_UNSELECTED);

                } else {
                    mHandler.sendEmptyMessage(MSG_HAVE_SELECTED);
                }

            }
        });


        etCodeInvite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    requestEventCode("u004");
                }
            }
        });
    }

    @Override
    public void onClickTitleRegisterLeftBtn(View v) {
        super.onClickTitleRegisterLeftBtn(v);
        this.finish();
    }

    @OnClick({R.id.tv_next, R.id.ttv_get_code, R.id.tv_register_term})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                requestEventCode("u005");
                checkCode();
                break;
            case R.id.ttv_get_code:
                requestEventCode("u002");
                checkPhone();
                break;
            case R.id.tv_register_term:
                KeyBoardUtils.closeKeybord(etPhone, this);
                openActivity(TermActivity.class);
                break;
        }
    }

    /**
     * 获取验证码
     *
     * @param phone
     */
    public void requestForgetPw(String phone) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_USER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.USER_REGISTER_SEND_SMS_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.USER_REGISTER_SEND_SMS_SIGN);
        params.put(HttpConstant.PARAM_KEY_PHONE, phone);
        request(params, HttpConstant.GET_CODE);
    }

    /**
     * 验证验证码是否正确
     *
     * @param phone
     * @param code
     */
    public void requestRegister(String phone, String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_USER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.USER_VERIFYCODE_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.USER_VERIFYCODE_SIGN);
        params.put(HttpConstant.PARAM_KEY_PHONE, phone);
        params.put(HttpConstant.PARAM_KEY_CODE, code);
        request(params, HttpConstant.REGISTER);
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

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.GET_CODE:
                ttvGetCode.start();
                etPhone.clearFocus();
                etCode.requestFocus();
                break;
            case HttpConstant.REGISTER:
                Intent intent = new Intent(this, SetPasswordActivity.class);
                intent.putExtra(Contants.KEY_PHONE, phone);
                intent.putExtra(Contants.KEY_CODE_INVITE, inviteCode);
                intent.putExtra(Contants.KEY_PAGE_PWD, Contants.VAL_PAGE_REGISTER);
                startActivity(intent);
                
                break;
            default:
                break;
        }
    }


    private void checkCode() {
        phone = etPhone.getText().toString();
        String code = etCode.getText().toString();
        inviteCode = etCodeInvite.getText().toString();
        if (phone.isEmpty()) {
//            showToast("手机号不能为空！");
            showEditTextPrompt(etPhone, "手机号不能为空！");
            
            return;
        }
//        if (!RegularExpressionUtil.checkMobile(phone)){
////            showToast("手机号不正确！");
//            showEditTextPrompt(etPhone,"手机号不正确！");
//            return;
//        }
        if (code.isEmpty()) {
//            showToast("验证码不能为空！");
            showEditTextPrompt(etPhone, "验证码不能为空！");
            
            return;
        }
        if (!cbRegister.isChecked()) {
//            showToast("未同意使用条款！");
            showEditTextPrompt(etPhone, "未同意使用条款！");
            
            return;
        }
        requestRegister(phone, code);
    }

    /**
     * 获取验证码
     */
    private void checkPhone() {
        phone = etPhone.getText().toString();
        if (phone.isEmpty()) {
            showEditTextPrompt(etPhone, "手机号不能为空！");
            return;
        }
        if (phone.length() < 11) {
            showEditTextPrompt(etPhone, "手机号不少于11位");
            return;
        }
//        if (!RegularExpressionUtil.checkMobile(phone)){
//            showEditTextPrompt(etPhone,"手机号不正确！");
//            return;
//        }
        requestForgetPw(phone);
    }

    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);
        showToast(errorMsg);
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.getInstance().removeActivityRegister(this);
        super.onDestroy();
    }
}

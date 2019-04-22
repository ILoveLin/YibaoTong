package com.lzyc.ybtappcal.activity.account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.app.ActivityCollector;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.RegularExpressionUtil;
import com.lzyc.ybtappcal.widget.TimerTextView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码
 * @author yang
 */
public class ForgetPasswordActivity extends BaseActivity {
    private static final String TAG = ForgetPasswordActivity.class.getSimpleName();
    protected static final int MSG_HAVE_SELECTED = 1;
    protected static final int MSG_HAVE_UNSELECTED = 2;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.ttv_get_code)
    TimerTextView ttvGetCode;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindString(R.string.title_forget_pwd)
    String titleName;
    private String phone;
    @BindView(R.id.v_title_line)
    View vTitleLine;
    @BindColor(R.color.color_ffffff_70)
    int titleRightColor;

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
        return R.layout.activity_forget_password;
    }

    @Override
    public void init() {
        designStyle();
        String input = getIntent().getExtras().getString(Contants.KEY_INTPUT);
        if (!TextUtils.isEmpty(input)) {
            etPhone.setText("" + input);
            etPhone.setSelection(input.length());
        }
        ActivityCollector.getInstance().addActivityRegiser(this);
        responseListener();
        vTitleLine.setVisibility(View.GONE);
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

    private void responseListener() {
        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    requestEventCode("v001");
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
                    requestEventCode("v002");
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

    }

    @Override
    public void onClickTitleRegisterLeftBtn(View v) {
        super.onClickTitleRegisterLeftBtn(v);
    }

    private void designStyle() {
        setTitleName(titleName, titleRightColor);
        setTitleBackrounColor(R.color.color_377eb4);
        setTitleLeftBtnVisibility(View.GONE);
        setTitleRegisterLeftBtnVisibility(View.VISIBLE);
    }

    @OnClick({R.id.tv_next, R.id.ttv_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                requestEventCode("v004");
                //验证是否符合手机号
                checkCode();
                break;
            case R.id.ttv_get_code:
                requestEventCode("v003");
                //获取验证码
                checkPhone();
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
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.USER_FORGOTPASSWD_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.USER_FORGOTPASSWD_SIGN);
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
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.GET_CODE:
                ttvGetCode.start();
                break;
            case HttpConstant.REGISTER:
                Intent intent = new Intent(this, SetPasswordActivity.class);
                intent.putExtra(Contants.KEY_PAGE_PWD, Contants.VAL_PAGE_FORGET);
                intent.putExtra(Contants.KEY_PHONE, phone);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    /**
     * 验证手机号和验证码
     */
    private void checkCode() {
        phone = etPhone.getText().toString();
        String code = etCode.getText().toString();
        if (phone.isEmpty()) {
            showEditTextPrompt(etPhone, "手机号不能为空！");
            return;
        }
//        if (!RegularExpressionUtil.checkMobile(phone)) {
//            showEditTextPrompt(etPhone, "手机号不正确！");
//            return;
//        }
        if (code.isEmpty()) {
            showEditTextPrompt(etPhone, "验证码不能为空！");
            return;
        }
//        if (!RegularExpressionUtil.checkPassWord(code)){
//            showToast("密码不符合规则！");
//            return;
//         }
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
//        if (!RegularExpressionUtil.checkMobile(phone)) {
//            showEditTextPrompt(etPhone, "手机号不正确！");
//            return;
//        }

        requestForgetPw(phone);
    }

    @Override
    public void error(String errorMsg) {
        showToast(errorMsg);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        ActivityCollector.getInstance().removeActivityRegister(this);
        super.onDestroy();
    }
}

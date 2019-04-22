package com.lzyc.ybtappcal.activity.account;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.app.App;
import com.lzyc.ybtappcal.bean.LoginBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.KeyBoardUtils;
import com.lzyc.ybtappcal.util.RegularExpressionUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：xujm
 * 时间：2016/2/17
 * 备注：登录页面
 */

public class LoginActivity extends BaseActivity {
    private static final String TAG=LoginActivity.class.getSimpleName();
    protected static final int MSG_HAVE_SELECTED = 1;
    protected static final int MSG_HAVE_UNSELECTED = 2;
    protected static final int MSG_START_ANIMA_FIRST = 3;
    protected static final int MSG_START_ANIMA_SECOND = 4;
    protected static final int MSG_START_ANIMA_THIRD = 5;
    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;
    @BindView(R.id.cb_set_yan)
    CheckBox cbSetYan;
    @BindView(R.id.bt_login_thisd_anim)
    Button btnLogin;
    @BindView(R.id.linea_login_first_anim)
    LinearLayout lineLoginFirstAnim;
    @BindView(R.id.linea_login_second_anim)
    LinearLayout lineLoginSecondAnim;
    @BindView(R.id.tv_login_forget)
    TextView tvLoginForget;
    @BindView(R.id.iv_login_logo)
    ImageView ivLoginLogo;
    @BindView(R.id.v_title_line)
    View vTitleLine;
    private int messureHeight;
    private boolean isFirstShow = true;
    private boolean isFirst = true;
    private boolean isNone;
    private boolean clickAgain = true;
    private boolean isSoftOpen=false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HAVE_UNSELECTED:
                    btnLogin.setPressed(false);
                    break;
                case MSG_HAVE_SELECTED:
                    btnLogin.setPressed(true);
                    break;
                case MSG_START_ANIMA_FIRST:
                    startMidAnimation();
                    mHandler.sendEmptyMessageDelayed(MSG_START_ANIMA_SECOND, 100);
                    break;
                case MSG_START_ANIMA_SECOND:
                    startThiredAnimation();
                    mHandler.sendEmptyMessageDelayed(MSG_START_ANIMA_THIRD, 100);
                    break;
                case MSG_START_ANIMA_THIRD:
                    startEndAnimation();
                    break;

            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        designStyle();
        responseListener();
        startLoginAnimation();

        vTitleLine.setVisibility(View.GONE);
    }

    private void designStyle() {
        setTitleName("登录", Color.parseColor("#70ffffff"));
        setTitleRightTvbtnName("注册", "#ffffff");
        setTitleBackrounColor(R.color.color_377eb4);
        setTitleLeftBtnVisibility(View.GONE);
        setTitleRegisterLeftBtnVisibility(View.VISIBLE);
    }

    private void startLoginAnimation() {
        startFirstAnimation();
    }

    private void startFirstAnimation() {
        lineLoginFirstAnim.setVisibility(View.VISIBLE);
        float currentTranslationY = lineLoginFirstAnim.getTranslationY();
        ObjectAnimator fristAnimator = ObjectAnimator.ofFloat(lineLoginFirstAnim, "TranslationY", currentTranslationY, currentTranslationY - DensityUtils.dp2px(20f));
        ObjectAnimator fristAnimator1 = ObjectAnimator.ofFloat(lineLoginFirstAnim, "alpha", 0.2f, 1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fristAnimator).with(fristAnimator1);
        animatorSet.setDuration(700);
        animatorSet.start();
        mHandler.sendEmptyMessageDelayed(MSG_START_ANIMA_FIRST, 100);

    }

    private void startMidAnimation() {
        lineLoginSecondAnim.setVisibility(View.VISIBLE);
        float currentTranslationY = lineLoginSecondAnim.getTranslationY();
        ObjectAnimator midAnimator = ObjectAnimator.ofFloat(lineLoginSecondAnim, "TranslationY", currentTranslationY, currentTranslationY - DensityUtils.dp2px(20f));
        ObjectAnimator midtAnimator1 = ObjectAnimator.ofFloat(lineLoginSecondAnim, "alpha", 0f, 1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(midAnimator).with(midtAnimator1);
        animatorSet.setDuration(700);
        animatorSet.start();

    }

    private void startThiredAnimation() {
        btnLogin.setVisibility(View.VISIBLE);
        float currentTranslationY = btnLogin.getTranslationY();
        ObjectAnimator endAnimator = ObjectAnimator.ofFloat(btnLogin, "TranslationY", currentTranslationY, currentTranslationY - DensityUtils.dp2px(20f));
        ObjectAnimator endAnimator1 = ObjectAnimator.ofFloat(btnLogin, "alpha", 0f, 1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(endAnimator).with(endAnimator1);
        animatorSet.setDuration(700);
        animatorSet.start();
    }

    private void startEndAnimation() {
        tvLoginForget.setVisibility(View.VISIBLE);
        float currentTranslationY = tvLoginForget.getTranslationY();
        ObjectAnimator endAnimator = ObjectAnimator.ofFloat(tvLoginForget, "TranslationY", currentTranslationY, currentTranslationY - DensityUtils.dp2px(20f));
        ObjectAnimator endAnimator1 = ObjectAnimator.ofFloat(tvLoginForget, "alpha", 0f, 1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(endAnimator).with(endAnimator1);
        animatorSet.setDuration(700);
        animatorSet.start();

        /**
         * 动画执行完  执行logo缩放动画
         */
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                try{
                    hiddenLogoAnimation(ivLoginLogo, true);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void showLogoAnimation(ImageView view) {
        view.setVisibility(View.VISIBLE);
        ValueAnimator dropAnimator = createDropAnimator(view, 0, messureHeight);
        dropAnimator.setDuration(500);
        dropAnimator.start();
        dropAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                etLoginPhone.clearFocus();
                KeyBoardUtils.closeKeybord(etLoginPhone, LoginActivity.this);
                isNone = true;
                clickAgain = true;

            }
        });

    }

    private void hiddenLogoAnimation(ImageView view, final boolean isOpen) {
        if (isFirst) {
            messureHeight = view.getHeight();
            isFirst = false;
        }
        view.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(view, messureHeight, 0);
        animator.setDuration(500);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isBlack) {
                    etLoginPhone.clearFocus();
                    etLoginPhone.requestFocus();//获取焦点
                    KeyBoardUtils.openKeybord(etLoginPhone, LoginActivity.this);
                    isSoftOpen=true;
                    isNone = false;
                }

            }
        });

    }

    private boolean isBlack;

    private ValueAnimator createDropAnimator(final ImageView view, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer animatedValue = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = animatedValue;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    /**
     * 获取当前点击位置是否为et
     *
     * @param view  焦点所在View
     * @param event 触摸事件
     * @return
     */
    public boolean isClickEt(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] leftTop = {0, 0};
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else if (event.getY() < 200 || 980 < event.getY() && event.getY() < 1060) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    /**
     * 點擊EditText以外的區域后鍵盤隱藏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if(!etLoginPassword.getText().toString().isEmpty()){
                mHandler.sendEmptyMessage(MSG_HAVE_SELECTED);
            }
            if (isClickEt(view, event)) {
                if (clickAgain) {
                    etLoginPhone.clearFocus();
                    KeyBoardUtils.closeKeybord(etLoginPhone, LoginActivity.this);
                    isNone = true;
                    clickAgain = true;
//                    showLogoAnimation(ivLoginLogo);
//                    clickAgain = false;
                }
            }
            return super.dispatchTouchEvent(event);
        }
        if (getWindow().superDispatchTouchEvent(event)) {
            return true;
        }
        return onTouchEvent(event);
    }


    private void responseListener() {


        etLoginPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    requestEventCode("t005");
                }
            }
        });

        etLoginPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        etLoginPassword.setClickable(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        etLoginPassword.setClickable(true);
                        break;
                }

                return false;
            }
        });

        etLoginPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                final String phone_text = etLoginPhone.getText().toString().trim();
                final String password_text = etLoginPassword.getText().toString().trim();
                if (TextUtils.isEmpty(phone_text) || TextUtils.isEmpty(password_text)) {
                    mHandler.sendEmptyMessage(MSG_HAVE_UNSELECTED);

                } else {
                    mHandler.sendEmptyMessage(MSG_HAVE_SELECTED);
                }

            }
        });


        etLoginPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
//                    showLogoAnimation(ivLoginLogo);
                    requestEventCode("t001");

                }
            }
        });
        etLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final String phone_text = etLoginPhone.getText().toString().trim();
                final String password_text = etLoginPassword.getText().toString().trim();
                if (TextUtils.isEmpty(phone_text) || TextUtils.isEmpty(password_text)) {
                    mHandler.sendEmptyMessage(MSG_HAVE_UNSELECTED);

                } else {
                    mHandler.sendEmptyMessage(MSG_HAVE_SELECTED);
                }
            }
        });

        cbSetYan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbSetYan.isChecked()) {
                    //显示密码
                    etLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    Editable etable = etLoginPassword.getText();
                    Selection.setSelection(etable, etable.length());
                } else {
                    etLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Editable etable = etLoginPassword.getText();
                    Selection.setSelection(etable, etable.length());
                }
            }
        });
    }

    @OnClick({R.id.bt_login_thisd_anim, R.id.tv_login_forget, R.id.ib_left_1})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.bt_login_thisd_anim:
                requestEventCode("t004");
                checkPhone();
                break;

            case R.id.tv_login_forget:
                requestEventCode("t003");
                Bundle mBundle=new Bundle();
                mBundle.putString("keyinput", etLoginPhone.getText().toString());
                openActivity(ForgetPasswordActivity.class,mBundle);
                break;

            case R.id.ib_left_1:
                if(!isSoftOpen){
                    KeyBoardUtils.closeKeybord(etLoginPhone,mContext);
                    isSoftOpen=true;
                    return;
                }
                finish();
                break;
        }
    }

    @Override
    public void onClickTitleRightTvBtn(View v) {
        super.onClickTitleRightTvBtn(v);
        requestEventCode("t002");
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void checkPhone() {
        String phone = etLoginPhone.getText().toString();
        String password = etLoginPassword.getText().toString();
        if (phone.isEmpty()) {
            mHandler.sendEmptyMessage(MSG_HAVE_SELECTED);
            showToast("手机号不能为空！");

            return;
        }
        if (phone.length() != 11) {
            mHandler.sendEmptyMessage(MSG_HAVE_SELECTED);
            showToast("手机号不正确！");

            return;
        }
        if (password.isEmpty()) {
            showToast("密码不能为空！");
            return;
        }
        if (!RegularExpressionUtil.checkPassWord(password)) {
            mHandler.sendEmptyMessage(MSG_HAVE_SELECTED);
            showToast("密码不符合规则！");
            return;
        }
        requestLogin(phone, password);
    }

    /**
     * 登录接口
     *
     * @param phone
     * @param password
     */
    public void requestLogin(String phone, String password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_USER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.USER_LOGIN_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.USER_LOGIN_SIGN);
        params.put(HttpConstant.PARAM_KEY_PHONE, phone);
        params.put(HttpConstant.PARAM_KEY_PWD, password);
        params.put(HttpConstant.PARAM_KEY_TOKEN,App.getInstance().mPushAgent.getRegistrationId());
        params.put(HttpConstant.PARAM_KEY_TYPE,HttpConstant.OS_ANDROID);
        request(params, HttpConstant.LOGIN);
    }


    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.LOGIN:
                try {
                    String data = response.getString(HttpConstant.DATA);
                    LoginBean loginBean = JsonUtil.getModle(data, LoginBean.class);
                    ToastUtil.showToastCenter(LoginActivity.this, "登录成功");
                    SharePreferenceUtil.put(this, SharePreferenceUtil.IS_LOGIN, true);
                    //登录成功保存相关信息
                    SharePreferenceUtil.put(this, SharePreferenceUtil.USER_NAME, loginBean.getUsername());
                    SharePreferenceUtil.put(this, SharePreferenceUtil.NICKNAME, loginBean.getNickname());
                    SharePreferenceUtil.put(this, SharePreferenceUtil.PHONE, loginBean.getPhone());
                    SharePreferenceUtil.put(this, SharePreferenceUtil.UID, loginBean.getUID());
                    String cityName = loginBean.getCityName();
                    if (cityName.equals("北京")) {
                        cityName = "北京市";
                    }
                    String proviceName = loginBean.getRegionName();
                    SharePreferenceUtil.put(this, SharePreferenceUtil.CITY_CANBAO, cityName);
                    SharePreferenceUtil.put(this, SharePreferenceUtil.PROVINCE_CANBAO, proviceName);
                    SharePreferenceUtil.put(this, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, loginBean.getYbtype());
                    sendBroadcastReceiver();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        isFirst = true;
        isFirstShow = true;
        clickAgain = true;
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
        isBlack = true;
    }

    @Override
    public void onBackPressed() {
        if(!isSoftOpen){
            KeyBoardUtils.closeKeybord(etLoginPhone,mContext);
            isSoftOpen=true;
            return;
        }
        mHandler.removeCallbacksAndMessages(null);
        super.onBackPressed();
    }

    @Override
    public void error(String errorMsg) {
        try{
            ErrorResponse errorResponse=JsonUtil.getModle(errorMsg,ErrorResponse.class);
            if(errorResponse.isNetError()){
                showToast("网络不给力");
            }else{
                showToast(""+errorResponse.getMsg());
            }
        }catch (Exception e){
//            showToast(errorMsg);
        }
    }

    public void sendBroadcastReceiver(){
        sendBroadcast(new Intent(Contants.ACTION_LOGION_SUCCESS));
    }



}

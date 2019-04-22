package com.lzyc.ybtappcal.activity.account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.app.ActivityCollector;
import com.lzyc.ybtappcal.bean.Event;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.KeyBoardUtils;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.ManifestUtil;
import com.lzyc.ybtappcal.util.RegularExpressionUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置密码
 * @author yang
 */
public class SetPasswordActivity extends BaseActivity {
    private static final String TAG=SetPasswordActivity.class.getSimpleName();
    protected static final int MSG_HAVE_SELECTED = 1;
    protected static final int MSG_HAVE_UNSELECTED = 2;

    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.cb_pwd)
    CheckBox cbPwd;
    @BindView(R.id.v_title_line)
    View vTitleLine;

    private String phone;
    private int typePage;
    private String inviteCode;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HAVE_UNSELECTED:
                    etPwd.setPressed(false);
                    break;
                case MSG_HAVE_SELECTED:
                    etPwd.setPressed(true);
                    break;

            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_set_password;
    }

    @Override
    public void init() {
        designStyle();
        ActivityCollector.getInstance().addActivityRegiser(this);
        phone = getIntent().getStringExtra(Contants.KEY_PHONE);
        inviteCode = getIntent().getStringExtra(Contants.KEY_CODE_INVITE);
        typePage = getIntent().getIntExtra(Contants.KEY_PAGE_PWD, Contants.VAL_PAGE_REGISTER);
        //切换明文密文
        etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        responseListener();

        vTitleLine.setVisibility(View.GONE);
    }

    private void responseListener() {

        cbPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbPwd.isChecked()) {
                    //显示密码
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    Editable etable = etPwd.getText();
                    Selection.setSelection(etable, etable.length());
                } else {
                    etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Editable etable = etPwd.getText();
                    Selection.setSelection(etable, etable.length());
                }
            }
        });
        etPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    requestEventCode("x001");
                }
            }
        });


        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                final String phone_text = etPwd.getText().toString().trim();
                if (TextUtils.isEmpty(phone_text)) {
                    mHandler.sendEmptyMessage(MSG_HAVE_UNSELECTED);

                } else {
                    mHandler.sendEmptyMessage(MSG_HAVE_SELECTED);
                }

            }
        });
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

    private void designStyle() {
        setTitleName("设置密码", Color.parseColor("#70ffffff"));
        setTitleBackrounColor(R.color.color_377eb4);
        setTitleLeftBtnVisibility(View.GONE);
        setTitleRegisterLeftBtnVisibility(View.VISIBLE);
    }

    @Override
    public void onClickTitleRegisterLeftBtn(View v) {
        super.onClickTitleRegisterLeftBtn(v);
        this.finish();
    }

    @OnClick(R.id.bt_set_pw)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_set_pw:
                String password = etPwd.getText().toString();
                if (password.isEmpty()) {
                    showEditTextPrompt(etPwd, "密码不能为空！");
                    return;
                }
                if (!RegularExpressionUtil.checkPassWord(password)) {
                    showEditTextPrompt(etPwd, "密码不符合规则！");
                    return;
                }
                requestSetPw(password);
                requestEventCode("x002");
                break;
        }
    }

    public void requestSetPw(String password) {
        String chanel = ManifestUtil.getMetaValue(this, "UMENG_CHANNEL");
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "User");
        params.put("class", "UpdatePasswd");
        String sing = MD5ChangeUtil.Md5_32("UserUpdatePasswd" + HttpConstant.APP_SECRET);
        params.put("sign", sing);
        params.put("phone", phone);
        params.put("passwd", password);
        if (inviteCode != null) {
            params.put("invitecode", inviteCode);
        }
        params.put("from", chanel);
        request(params, HttpConstant.SET_PW);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        LogUtil.e("tag", response.toString());
        switch (what) {
            case HttpConstant.SET_PW:
                SharePreferenceUtil.put(this, SharePreferenceUtil.IS_LOGIN, true);
                //登录成功保存相关信息
                SharePreferenceUtil.put(this, SharePreferenceUtil.PHONE, phone);
                String UID = "";
                try{
                    UID = response.getJSONObject(HttpConstant.DATA).getString(HttpConstant.APP_UID);
                } catch (Exception e){
                }
                SharePreferenceUtil.put(this, SharePreferenceUtil.UID, UID);
                if (typePage != Contants.VAL_PAGE_FORGET) {
                    KeyBoardUtils.closeKeybord(etPwd, SetPasswordActivity.this);
                    Intent intent = new Intent(this, FullInfoActivity.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                } else {
                    ToastUtil.showToastCenter(SetPasswordActivity.this, "找回成功，请重新登录");
                    ActivityCollector.removeRegisterAll();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
       showToast(errorMsg);
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.getInstance().removeActivityRegister(this);
        super.onDestroy();
    }
}
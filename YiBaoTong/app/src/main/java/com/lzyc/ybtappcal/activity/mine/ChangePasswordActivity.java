package com.lzyc.ybtappcal.activity.mine;

import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.account.LoginActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.RegularExpressionUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码
 * @author yang
 */
public class ChangePasswordActivity extends BaseActivity {
    private static final String TAG = ChangePasswordActivity.class.getSimpleName();
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.cb_pwd)
    CheckBox cbPwd;
    @BindView(R.id.et_pwd_new)
    EditText etPwdNew;
    @BindView(R.id.cb_pwd_new)
    CheckBox cbPwdNew;
    @BindString(R.string.title_update_pwd)
    String titleName;
    private String phone;

    @Override
    public int getContentViewId() {
        return R.layout.activity_change_password;
    }

    @Override
    public void init() {
        setTitleName(titleName);
        Boolean isLogin = (Boolean) SharePreferenceUtil.get(this, SharePreferenceUtil.IS_LOGIN, false);
        if (isLogin) {
            phone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
        } else {
            openActivity(LoginActivity.class);
        }
        //切换明文密文
        etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        cbPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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

        //切换明文密文
        etPwdNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        cbPwdNew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //显示密码
                    etPwdNew.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    Editable etable = etPwdNew.getText();
                    Selection.setSelection(etable, etable.length());
                } else {
                    etPwdNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Editable etable = etPwdNew.getText();
                    Selection.setSelection(etable, etable.length());
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

    @OnClick({R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                String pw = etPwd.getText().toString();
                String npw = etPwdNew.getText().toString();
                if (pw.isEmpty()) {
                    showEditTextPrompt(etPwd, "原密码不能为空！");
                    return;
                }
                if (npw.isEmpty()) {
                    showEditTextPrompt(etPwd, "新密码不能为空！");
                    return;
                }
                if (!RegularExpressionUtil.checkPassWord(npw)) {
                    showEditTextPrompt(etPwd, "新密码不符合规则！");
                    return;
                }
                requestEventCode("s008");
                request(pw, npw);
                break;
        }
    }

    /**
     * 修改密码
     */
    public void request(String pw, String npw) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_UCENTER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.UCENTER_UPDATE_PASSWD_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.UCENTER_UPDATE_PASSWD_SIGN);
        params.put(HttpConstant.PARAM_KEY_PHONE, phone);
        params.put(HttpConstant.PASSWD, npw);
        params.put(HttpConstant.PASSWD_OLD, pw);
        request(params, HttpConstant.MY_CHANGE_PASSWORD);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.MY_CHANGE_PASSWORD:
//                showToast("修改成功");
                ToastUtil.showToastCenter(ChangePasswordActivity.this, "修改成功");
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        showToast(errorMsg);
    }
}

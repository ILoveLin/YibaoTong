package com.lzyc.ybtappcal.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.view.ContainsEmojiEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改昵称
 * package_name com.lzyc.ybtappcal.activity
 * Created by yang on 2016/5/10.
 */
public class EditSetActivity extends BaseActivity {
    private static final String TAG=EditSetActivity.class.getSimpleName();
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.et_input)
    ContainsEmojiEditText etInput;
    private String textTitle;
    private String textContent;
    private Intent intent;
    public static final String KEY_OBJ_TITLE = "et_title";
    public static final String KEY_OBJ_CONTENT = "et_content";
    private String oldTextContent;
    private String beforeTextChangedTextContent;
    @BindString(R.string.edit_limit)
    public String mInputLimit;
    @BindString(R.string.empty_nickname)
    public String emptyNickname;

    @Override
    public int getContentViewId() {
        return R.layout.activity_edit_set;
    }

    @Override
    protected void init() {
        setTitleBarVisibility(View.GONE);
        intent = getIntent();
        textTitle = intent.getStringExtra(KEY_OBJ_TITLE);
        textContent = intent.getStringExtra(KEY_OBJ_CONTENT);
        oldTextContent = textContent;
        tvTitle.setText("" + textTitle);
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextChangedTextContent = etInput.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 8) {
                    showToast(mInputLimit);
                    etInput.setText(beforeTextChangedTextContent);
                    Selection.setSelection(etInput.getText(),etInput.getText().length());

                }
            }
        });
    }

    @Override
    protected void onClickRetry() {

    }

    @OnClick({R.id.iv_cancel, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cancel:
                EditSetActivity.this.finish();
                break;
            case R.id.tv_save:
                if (TextUtils.isEmpty(etInput.getText().toString())) {
                    showToast(emptyNickname);
                    return;
                }
                requestCheckNickname();
                break;
        }
    }

    private void requestCheckNickname() {
        textContent = etInput.getText().toString();
        if (oldTextContent.equals(textContent)) {
            this.finish();
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP,HttpConstant.APP_USER);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.USER_VALIDATE_NICKNAME_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.USER_VALIDATE_NICKNAME_SIGN);
        params.put(HttpConstant.USER_VALIDATE_PARAM_NICKNAME, textContent);
        request(params, HttpConstant.MESSAGE_NICKNAME);
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        etInput.setText("" + textContent);
        Selection.setSelection(etInput.getText(), etInput.getText().length());
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
            case HttpConstant.MESSAGE_NICKNAME:
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    String statusCode = data.getString(HttpConstant.STATUS_CODE);
                    String message = data.getString(HttpConstant.MESSAGE);
                    if (statusCode.equals(Contants.STR_0)) {
                        textContent = etInput.getText().toString();
                        intent.putExtra(KEY_OBJ_CONTENT, textContent);
                        setResult(RESULT_OK, intent);
                        EditSetActivity.this.finish();
                    } else {
                        showToast("" + message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast(errorNetToast);
                }
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        showToast(errorNetToast);
    }
}

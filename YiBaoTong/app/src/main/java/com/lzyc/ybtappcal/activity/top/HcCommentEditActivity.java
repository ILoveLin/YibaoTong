package com.lzyc.ybtappcal.activity.top;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.account.LoginActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.app.App;
import com.lzyc.ybtappcal.bean.Event;
import com.lzyc.ybtappcal.bean.HospitalBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.Util;
import com.lzyc.ybtappcal.view.RatingBar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 写评价
 * package_name com.lzyc.ybtappcal.activity.top
 * Created by yang on 2016/8/11.
 */
public class HcCommentEditActivity extends BaseActivity {
    private static final String TAG=HcCommentEditActivity.class.getSimpleName();
    @BindView(R.id.hc_fuwutaidu_comment_star_first)
    RatingBar hc_fuwutaidu_comment_star_first;
    @BindView(R.id.hc_yaopinqiquan_comment_star_second)
    RatingBar hc_yaopinqiquan_comment_star_second;
    @BindView(R.id.hc_nayaofangbian_comment_star_third)
    RatingBar hc_nayaofangbian_comment_star_third;
    @BindView(R.id.hc_fuwutaidu_tv_desc)
    TextView hc_fuwutaidu_tv_desc;
    @BindView(R.id.hc_yaopinqiquan_tv_desc)
    TextView hc_yaopinqiquan_tv_desc;
    @BindView(R.id.hc_nayaofangbian_tv_desc)
    TextView hc_nayaofangbian_tv_desc;
    @BindView(R.id.hc_comment_edit_content)
    EditText hc_comment_edit_content;
    @BindView(R.id.id_tv_count)
    TextView id_tv_count;
    private HospitalBean mHospitalBean;
    private boolean isLogin;
    private String phone;

    @Override
    public int getContentViewId() {
        return R.layout.activity_hc_comment_edit;
    }

    @Override
    public void init() {
        setTitleName("评价");
        initData();
        initListener();
    }
    private void initListener() {

        hc_fuwutaidu_comment_star_first.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(int var1) {
                hc_fuwutaidu_tv_desc.setText("" + var1 + ".0");

            }
        });
        hc_fuwutaidu_comment_star_first.getChildCount();

        hc_yaopinqiquan_comment_star_second.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(int var1) {
                hc_yaopinqiquan_tv_desc.setText("" + var1 + ".0");

            }
        });

        hc_nayaofangbian_comment_star_third.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(int var1) {
                hc_nayaofangbian_tv_desc.setText("" + var1 + ".0");
            }
        });


        hc_comment_edit_content.addTextChangedListener(Util.getUnSupportEmojiListen(this, hc_comment_edit_content));
        hc_comment_edit_content.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = 140 - s.length();
                id_tv_count.setText("" + (140 - number) + "/" + "140");
                selectionStart = hc_comment_edit_content.getSelectionStart();
                selectionEnd = hc_comment_edit_content.getSelectionEnd();
                if (temp.length() > 140) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    hc_comment_edit_content.setText(s);
                    hc_comment_edit_content.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

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
            //获取输入框当前的location位置
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            //此处根据输入框左上位置和宽高获得右下位置
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
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
            //获取当前获得当前焦点所在View
            View view = getCurrentFocus();
            if (isClickEt(view, event)) {
                //如果不是edittext，则隐藏键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    //隐藏键盘
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(event);
        }
        /**
         * 看源码可知superDispatchTouchEvent  是个抽象方法，用于自定义的Window
         * 此处目的是为了继续将事件由dispatchTouchEvent(MotionEvent event)传递到onTouchEvent(MotionEvent event)
         * 必不可少，否则所有组件都不能触发 onTouchEvent(MotionEvent event)
         */
        if (getWindow().superDispatchTouchEvent(event)) {
            return true;
        }
        return onTouchEvent(event);
    }


    private void initData() {
        Bundle bundle = getIntent().getExtras();
        mHospitalBean = (HospitalBean) bundle.getSerializable(Contants.KEY_OBJ_HOSPITALBEAN);
        hc_fuwutaidu_comment_star_first.setStar((float) 5);
        hc_yaopinqiquan_comment_star_second.setStar((float) 5);
        hc_nayaofangbian_comment_star_third.setStar((float) 5);
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        isLogin = (Boolean) SharePreferenceUtil.get(this, SharePreferenceUtil.IS_LOGIN, false);
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    private void requestSendComment() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, "Comment");
        params.put(HttpConstant.PARAM_KEY_CLASS, "SubmitComment");
        String sign = MD5ChangeUtil.Md5_32("CommentSubmitComment" + HttpConstant.APP_SECRET);
        params.put(HttpConstant.PARAM_KEY_SIGN, sign);
        String yyid = mHospitalBean.getYyid();
        if (yyid != null) {
            params.put("yyid", yyid);
        }
        params.put("content", "" + hc_comment_edit_content.getText().toString());
        params.put("phone", phone);
        params.put("token", PushAgent.getInstance(App.getInstance()).getRegistrationId());
        params.put("type", "1");
        params.put("serviceAttitude", hc_fuwutaidu_tv_desc.getText().toString());
        params.put("drugMany", hc_yaopinqiquan_tv_desc.getText().toString());
        params.put("getDrugConvenience", hc_nayaofangbian_tv_desc.getText().toString());
        params.put("parent_phone", "");
        params.put("parent_id", "");
        request(params, HttpConstant.TOP_COMMENT_HOSPITAL);
    }

    @OnClick({R.id.hc_comment_edit_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.hc_comment_edit_send:
                int length = hc_comment_edit_content.getText().toString().trim().length();
                if (isLogin) {
                    if (hc_comment_edit_content.getText().toString().trim().isEmpty()) {
                        showToast("评价内容不能为空");
                    } else {
                        String str = hc_comment_edit_content.getText().toString().trim();
                        if (!(str.length() > 9)) {
                            showToast("评价内容最少十个字");
                        } else {
                            phone = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, "");
                            if (length > 140) {
                                showToast("评价字数不能超过140个");
                            } else {
                               requestEventCode("y004");
                                requestSendComment();
                            }
                        }
                    }

                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            default:
                super.onClickTitleLeftBtn(view);
                break;
        }
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        this.finish();
    }


    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);
        this.finish();
    }
}

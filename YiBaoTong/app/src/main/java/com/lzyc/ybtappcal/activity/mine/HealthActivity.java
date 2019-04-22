package com.lzyc.ybtappcal.activity.mine;

import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.constant.YBConstant;
import com.lzyc.ybtappcal.util.KeyBoardUtils;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import ybt.library.dialog.wheel.DataPickerDialog;

/**
 * 作者：xujm
 * 时间：2016/2/22
 * 备注：医保计算器
 */
public class HealthActivity extends BaseActivity {
    private static final String TAG=HealthActivity.class.getSimpleName();
    @BindView(R.id.web_health)
    WebView web_health;
    @BindView(R.id.tv_health_title)
    TextView tv_health_title;
    @BindView(R.id.et_health)
    EditText et_health;
    @BindView(R.id.rb_health_info1)
    RadioButton rb_health_info1;
    @BindView(R.id.rb_health_info2)
    RadioButton rb_health_info2;
    @BindView(R.id.rb_health_info3)
    RadioButton rb_health_info3;
    @BindView(R.id.rb_health_info4)
    RadioButton rb_health_info4;
    @BindView(R.id.rb_health_info5)
    RadioButton rb_health_info5;
    @BindView(R.id.include_loading_linear)
    LinearLayout linear_loading;
    @BindView(R.id.include_loading_iv)
    ImageView loading_image_iv;
    @BindView(R.id.health_scrollview)
    ScrollView health_scrollview;
    @BindView(R.id.include_net_error_linear)
    LinearLayout net_error_linear;
    @BindView(R.id.include_net_setting_tv)
    TextView net_setting;
    String yyType;
    String ybType;
    ArrayList<String> listHealth;

    @Override
    public int getContentViewId() {
        return R.layout.activity_health;
    }

    @Override
    public void init() {

        setTitleBarVisibility(View.GONE);

        linear_loading.setVisibility(View.VISIBLE);
        health_scrollview.setVisibility(View.GONE);
        net_error_linear.setVisibility(View.GONE);
        AnimationDrawable animationDrawable = (AnimationDrawable) loading_image_iv.getBackground();
        animationDrawable.start();
        ybType = getIntent().getStringExtra("ybtype");
        listHealth = new ArrayList<String>();
        listHealth.addAll(Arrays.asList(YBConstant.YBNAME));
        for (int i = 0; i < YBConstant.YBTYPE.length; i++) {
            if (YBConstant.YBTYPE[i].equals(ybType)) {
                tv_health_title.setText("参保类型：" + listHealth.get(i));
            }
        }
        et_health.setText("10000");
        yyType = YBConstant.YYTYPE[3];
        setType(rb_health_info1);
        et_health.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                requestEventCode("q002");
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    load();
                    KeyBoardUtils.closeKeybord(et_health, HealthActivity.this);
                }
                return true;
            }
        });

        web_health.setOverScrollMode(View.OVER_SCROLL_NEVER);
        web_health.getSettings().setJavaScriptEnabled(true);
        web_health.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    private void load() {
        String money = et_health.getText().toString();
        if (TextUtils.isEmpty(money)) {
            showEditTextPrompt(et_health, "总花费不能为空！");
            return;
        }
        if (TextUtils.isEmpty(ybType)) {
            showEditTextPrompt(et_health, "医保类型不能为空！");
            return;
        }
        if (TextUtils.isEmpty(yyType)) {
            showEditTextPrompt(et_health, "医院类型不能为空！");
            return;
        }
        String url = HttpConstant.HEALTH_URL + "money=" + money + "&ybType=" + ybType + "&yyType=" + yyType;
        if (!NetworkUtil.CheckConnection(this)) {
            net_error_linear.setVisibility(View.VISIBLE);
            linear_loading.setVisibility(View.GONE);
            health_scrollview.setVisibility(View.GONE);
            net_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetworkUtil.CheckConnection(HealthActivity.this)) {
                        showNetDialog();
                        return;
                    }
                    load();
                }
            });
            return;
        }
        web_health.loadUrl(url);
        web_health.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    net_error_linear.setVisibility(View.GONE);
                    linear_loading.setVisibility(View.GONE);
                    health_scrollview.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
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


    @OnClick({R.id.ib_health_left, R.id.tv_health_title, R.id.rb_health_info1, R.id.rb_health_info2, R.id.rb_health_info3, R.id.rb_health_info4, R.id.rb_health_info5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_health_left:
                finish();
                break;
            case R.id.tv_health_title:
               requestEventCode("q001");
                showDialogYbType();
                break;
            case R.id.rb_health_info1:
               requestEventCode("q003");
                yyType = YBConstant.YYTYPE[3];
                setType(rb_health_info1);
                break;
            case R.id.rb_health_info2:
               requestEventCode("q003");
                yyType = YBConstant.YYTYPE[4];
                setType(rb_health_info2);
                break;
            case R.id.rb_health_info3:
                requestEventCode("q004");
                yyType = YBConstant.YYTYPE[0];
                setType(rb_health_info3);
                break;
            case R.id.rb_health_info4:
                requestEventCode("q004");
                yyType = YBConstant.YYTYPE[1];
                setType(rb_health_info4);
                break;
            case R.id.rb_health_info5:
                requestEventCode("q004");
                yyType = YBConstant.YYTYPE[2];
                setType(rb_health_info5);
                break;
        }
    }

    public void setType(RadioButton rb) {
        rb_health_info1.setChecked(false);
        rb_health_info2.setChecked(false);
        rb_health_info3.setChecked(false);
        rb_health_info4.setChecked(false);
        rb_health_info5.setChecked(false);
        rb.setChecked(true);
        load();
    }

    /**
     * 医保类型
     */
    private void showDialogYbType() {
        int position = 0;
        String titleContent = tv_health_title.getText().toString();
        String currentYbType = titleContent.substring("参保类型:".length(), titleContent.length());
        for (int i = 0; i < listHealth.size(); i++) {
            String ybType = listHealth.get(i);
            if (ybType.equals(currentYbType)) {
                position = i;
                break;
            }
        }
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        DataPickerDialog dialog = builder.setData(listHealth).setSelection(position)
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue, int position) {
                        ybType = YBConstant.YBTYPE[position];
                        tv_health_title.setText("参保类型:" + itemValue);
                        load();
                    }
                }).create();
        dialog.show();
    }

}

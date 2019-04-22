package com.lzyc.ybtappcal.activity.mine;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.util.Util;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindString;
import butterknife.BindView;

/**
 * @author yang
 */
public class AboutActivity extends BaseActivity {
    private static final String TAG = AboutActivity.class.getSimpleName();
    @BindView(R.id.tv_version_code)
    TextView tvVersionCode;
    @BindView(R.id.linear_email)
    LinearLayout linearEmail;
    @BindView(R.id.linear_wechat)
    LinearLayout linearWechat;
    @BindString(R.string.title_about_mine)
    String titleName;
    @BindString(R.string.email)
    String email;
    @BindString(R.string.email_prompt)
    String emailPrompt;
    @BindString(R.string.app_name)
    String appName;
    @BindString(R.string.wechat_public)
    String wechatPublic;
    @BindString(R.string.wechat_public_prompt)
    String wechatPublicPrompt;

    @Override
    public int getContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    public void init() {
        setTitleName(titleName);
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            tvVersionCode.setText("当前版本 " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        linearEmail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Util.copy(AboutActivity.this, email);
                showToast(emailPrompt);
                return true;
            }
        });
        linearWechat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Util.copy(AboutActivity.this, wechatPublic);
                showToast(wechatPublicPrompt);
                return true;
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
}

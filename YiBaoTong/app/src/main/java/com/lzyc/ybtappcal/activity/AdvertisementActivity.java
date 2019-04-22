package com.lzyc.ybtappcal.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.top.TopWebviewActivity;
import com.lzyc.ybtappcal.bean.TopBanner;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.ScreenUtils;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 广告
 * Created by yang on 2017/05/15.
 */
public class AdvertisementActivity extends BaseActivity {
    private static final String TAG=AdvertisementActivity.class.getSimpleName();
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.rel_content)
    RelativeLayout relContent;
    private int timeCount = 5;
    private TopBanner.DataBean.ImagesBean imagesBean;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0){
                tvTime.setText("" + timeCount);
                if (timeCount == 0) {
                    switchMainActivity();
                }else{
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            timeCount--;
                            tvTime.setText("" + timeCount);
                            mHandler.sendEmptyMessage(0);
                        }
                    }, 1000);
                }
            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_advertisement;
    }

    @Override
    protected void init() {
        setTitleBarVisibility(View.GONE);
        imagesBean= (TopBanner.DataBean.ImagesBean) getIntent().getExtras().getSerializable(Contants.KEY_IMAGE);
        String imageurl = imagesBean.getImg_url();
        if (!TextUtils.isEmpty(imageurl)) {
            ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(this).load(imageurl)
                    .placeholder(R.drawable.image_launch)
                    .resize(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight())
                    .into(ivImage, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
        mHandler.sendEmptyMessage(0);
    }

    /**
     * 跳转到主页
     */
    private void switchMainActivity() {
        SharePreferenceUtil.put(AdvertisementActivity.this, SharePreferenceUtil.DYNAMIC_SWITCH_TAB, Contants.TAB_HOME);
        openActivity(MainActivity.class);
        mHandler.removeCallbacksAndMessages(null);
        closeActivity();
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

    private void closeActivity() {
        sendBroadcast(new Intent("lauchActivity_finish"));
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }


    @OnClick({R.id.advertisement_skip, R.id.rel_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.advertisement_skip://跳过
                requestEventCode("a-1002");
                switchMainActivity();
                break;
            case R.id.rel_content:
                requestEventCode("a-1001");
                if(!TextUtils.isEmpty(imagesBean.getWeb_url())){
                    //关闭LaunchActivity
                    Intent intent=new Intent(AdvertisementActivity.this, TopWebviewActivity.class);
                    intent.putExtra("url",imagesBean.getWeb_url());
                    intent.putExtra(Contants.KEY_PAGE, Contants.VAL_PAGE_ADVERTIMENT);
                    startActivity(intent);
                    sendBroadcast(intent);
                    closeActivity();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }
}

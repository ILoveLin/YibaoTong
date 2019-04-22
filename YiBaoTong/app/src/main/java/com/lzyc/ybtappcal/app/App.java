package com.lzyc.ybtappcal.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.lzyc.ybtappcal.BuildConfig;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.service.LocationService;
import com.lzyc.ybtappcal.service.MyPushIntentService;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.StringUtils;
import com.lzyc.ybtappcal.util.notify.NotifyUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * 作者：xujm on 2015/12/24
 * 备注：com.lzyc.framwork.app
 */
public class App extends Application {
    private static App app;
    public static String AppFilePath = "app";
    public LocationService locationService;
    public PushAgent mPushAgent;
    private MyLocationListener mListener = new MyLocationListener();
    private int i = 1;
    public App(){
        app=this;
    }
    public static synchronized App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();

        Fresco.initialize(this, config);
        SDKInitializer.initialize(this);//百度地图
        NotifyUtil.init(this);//通知
        locationService = new LocationService(this);//百度地图定位
        locationService.registerListener(mListener);
        locationService.start();
        //友盟推送
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        //推送完全自定义消息
        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
        mPushAgent.setDebugMode(false);//关闭推送输出日志，注释这行解开输出日志
        //友盟统计
        MobclickAgent.setDebugMode(false);//是否打开调试模式
        MobclickAgent.setCatchUncaughtExceptions(false);//关闭错误统计，注释这行或者设置为true，错误统计上传。
        //友盟分享
        Config.DEBUG=true;
        UMShareAPI.get(this);
        PlatformConfig.setWeixin(Contants.STR_APPID_WECHAT, Contants.STR_SECRET_WECHAT);
        PlatformConfig.setSinaWeibo(Contants.STR_APPID_WEIBO, Contants.STR_SECRET_WEIBO, HttpConstant.SINA_SHARE_URL);
        PlatformConfig.setQQZone(Contants.STR_APPID_QQ, Contants.STR_SECRET_QQ);
        Contants.isFirstEnter = true;
        Picasso.Builder picassoBuilder = new Picasso.Builder(this);
        if(BuildConfig.DEBUG) { picassoBuilder.loggingEnabled(true); }
        if(isLowMemoryDevice()) { picassoBuilder.defaultBitmapConfig(Bitmap.Config.RGB_565); }
        Picasso.setSingletonInstance(picassoBuilder.build());
        //http://dev.umeng.com/analytics/android-doc/integration#2_5_2
        MobclickAgent.openActivityDurationTrack(false);//禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.enableEncrypt(true);//true,SDK按照加密的方式来传输日志
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

    }

    private boolean isLowMemoryDevice() {
        if(Build.VERSION.SDK_INT >= 19) {
            return ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).isLowRamDevice();
        } else {
            return false;
        }
    }

    //百度地图监听
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            String city = location.getCity();
            String addrStr = location.getAddrStr();
            String province = location.getProvince();
            if (province != null) {
                SharePreferenceUtil.put(app, SharePreferenceUtil.KEY_DW_STATUS, true);
                SharePreferenceUtil.put(app, SharePreferenceUtil.PROVINCE, StringUtils.getProvice(province));
            }
            if (city != null) {
                SharePreferenceUtil.put(app, SharePreferenceUtil.CITY, "" + city);
            }
            if (addrStr != null) {
                SharePreferenceUtil.put(app, SharePreferenceUtil.ADDRESS, "" + addrStr);
                SharePreferenceUtil.put(app, "positionaddress", "" + addrStr);
            }
            if (longitude != 4.9E-324 && latitude != 4.9E-324) {
                SharePreferenceUtil.put(app, SharePreferenceUtil.LONGITUDE, longitude + "");
                SharePreferenceUtil.put(app, SharePreferenceUtil.LATITUDE, latitude + "");
                SharePreferenceUtil.put(app, SharePreferenceUtil.LONGITUDE, longitude + "");
                SharePreferenceUtil.put(app, "positionlat", latitude + "");
                SharePreferenceUtil.put(app, "positionlng", longitude + "");
                locationService.unregisterListener(mListener);
                locationService.stop();
            } else {
                SharePreferenceUtil.put(app, SharePreferenceUtil.LONGITUDE, "116.390356");
                SharePreferenceUtil.put(app, SharePreferenceUtil.LATITUDE, "39.905206");
                SharePreferenceUtil.put(app, SharePreferenceUtil.PROVINCE, "");
                SharePreferenceUtil.put(app, SharePreferenceUtil.CITY, "");
                SharePreferenceUtil.put(app, "positionaddress", "定位失败，请重新定位");
                SharePreferenceUtil.put(app, SharePreferenceUtil.ADDRESS_CURRENT_CHOOSRE, "北京市天安门");
                if (i == 1) {
                    SharePreferenceUtil.put(app, SharePreferenceUtil.KEY_DW_STATUS, false);
                    locationService.unregisterListener(mListener);
                    locationService.stop();
                }
                i++;
            }
        }
    }

}

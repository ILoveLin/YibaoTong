package com.lzyc.ybtappcal.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.MainActivity;
import com.lzyc.ybtappcal.app.App;
import com.lzyc.ybtappcal.bean.Version;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.notify.NotifyUtil;
import com.lzyc.ybtappcal.util.notify.builder.ProgressBuilder;
import com.lzyc.ybtappcal.volley.BaseService;
import com.lzyc.ybtappcal.volley.ServiceResponseCallback;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowDownloadAPP;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowUpdate;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc 版本更新工具类
 * Created by yang on 2016/4/29.
 */
public class VersionUtil implements ServiceResponseCallback {
    public static String TAG = VersionUtil.class.getSimpleName();
//    private static VersionUtil instance = null;
    private Activity mContext;
    private String mVersionCode;
    public BaseService httpRequest;
    private static final int MSG_RESULT_CHECK = 100;
    private String localUrl;
    private MyHandler mHandler;
    private ProgressBuilder mBuilder;
    private int NOTIFICATION_ID = 10000;
    //    private int forced;//是否强制更新，0自动更新，1强制更新
    private PopupWindowDownloadAPP popupWindowDownloadAPP;
    private long exitTime = 0;

    public VersionUtil() {
        if (httpRequest == null) {
            httpRequest = new BaseService();
        }
    }


//    public static VersionUtil getInstance() {
//        if (instance == null) {
//            instance = new VersionUtil();
//        }
//        return instance;
//    }

    public void setContent(Activity context){
        this.mContext=context;
    }

    /**
     * 返回当前程序版本名
     */
    public static final Version getAppVersionName(Context context) {
        Version version = new Version();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            version.setPackageName(pi.packageName);
            version.setVersionCode(String.valueOf(pi.versionCode));
            version.setVersionName(pi.versionName);
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return version;
    }

    public void checkVersion() {
        try {
            Version localVersion = getAppVersionName(mContext);
            if (localVersion != null) {
                mVersionCode = localVersion.getVersionCode();
                Map<String, String> params = new HashMap<String, String>();
                params.put("app", "Version");
                params.put("class", "Update");
                String sign = MD5ChangeUtil.Md5_32("VersionUpdate" + HttpConstant.APP_SECRET);
                params.put("sign", sign);
                params.put("platform", "android");
                if (!NetworkUtil.CheckConnection(mContext)) {
                    return;
                }
                try {
                    httpRequest.postMap(HttpConstant.APP_URL, this, params, MSG_RESULT_CHECK);
                } catch (Exception e) {
                    httpRequest.cancel();
                    LogUtil.e("error", "error:在网络请求时与服务器连接出错" + e.getMessage());
                }
            }
        } catch (Exception e) {
            httpRequest.cancel();
            LogUtil.e(TAG, e.getMessage());
        }

    }

    /**
     * 提示新版本更新
     * 强制更新时，稍后更新不可点，文字与框颜色为999999
     */
    private void showNewVersionDialog(final Version mVersion) {
        String desc = mVersion.getDescription();
        String[] descs = null;
        if (desc.contains("，")) {
            descs = desc.split("，");
        } else {
            descs = new String[]{desc};
        }

        final int forced = mVersion.getForced();

        final PopupWindowUpdate popupWindow = new PopupWindowUpdate((Activity) mContext, forced, Integer.parseInt(mVersionCode), mVersion.getMinVersionCode(), descs);

        popupWindow.setOnUpdateListener(new PopupWindowUpdate.UpdateListener() {
            @Override
            public void onUpdateNow() {
                popupWindow.dismiss();
                String url=mVersion.getAppUrl();
                if(url!=null){
                    popupWindow.dismiss();
                    downLoadAPK(url,forced);
                }
            }
        });
        popupWindow.showPopupWindow(((Activity) mContext).getWindow().getDecorView(), Gravity.CENTER);
    }

    private void downLoadAPK(String apkUrl, int forced) {
//        this.forced=forced;
        String[] strs = apkUrl.split("/");
        String fileName = strs[strs.length - 1];
        File cacheDir = FileUtil.creatFileDirectory("apk");
        localUrl = cacheDir.getAbsolutePath() + File.separator + fileName;
        mHandler = new MyHandler();
        new DownLoadAPKUtil(mHandler, apkUrl, cacheDir.getAbsolutePath(),
                fileName).start();
    }

    private class MyHandler extends Handler {
        public MyHandler() {
            boolean isForced = (boolean) SharePreferenceUtil.get(mContext, SharePreferenceUtil.KEY_VERSION_ISFORCED, false);
            if (!isForced) {
                showNotificationBar();
            } else {
                showProgressBar();
            }
        }

        /**
         * 前台弹出下载进度条
         */
        private void showProgressBar() {
            popupWindowDownloadAPP = new PopupWindowDownloadAPP((Activity) mContext);
            popupWindowDownloadAPP.showPopupWindow(((MainActivity) mContext).getWindow().getDecorView(), Gravity.CENTER);
        }

        /**
         * 后台通知栏
         */
        private void showNotificationBar() {
            ToastUtil.showShort(mContext, "程序已经为您在下载升级软件，请在通知栏查看..");
            mBuilder = NotifyUtil.buildProgress(NOTIFICATION_ID, R.mipmap.ic_launcher, "软件升级", 0, 0);
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW); // 浏览网页的Action(动作)
            String type = "application/vnd.android.package-archive";
            intent.setDataAndType(Uri.fromFile(new File(localUrl)), type); // 设置数据类型
            mBuilder.setContentIntent(PendingIntent.getActivity(mContext, 100,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT));
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (DownLoadAPKUtil.MSG_NET_RES_OK == msg.what) {
                if (checkFileMD5()) {
                    installVersion(localUrl);
                } else {
                    Toast.makeText(mContext,
                            "下载失败",
                            Toast.LENGTH_SHORT).show();
                    boolean isForced = (boolean) SharePreferenceUtil.get(mContext, SharePreferenceUtil.KEY_VERSION_ISFORCED, false);
                    if (!isForced) {
                        mBuilder.setProgress(0, 0, false);
                        mBuilder.setContentText("升级失败");
                        mBuilder.setId(NOTIFICATION_ID);
                        mBuilder.show();
                    } else {
                        popupWindowDownloadAPP.getmProgressBar().setProgress(0);
                        popupWindowDownloadAPP.getTv_title().setText("升级失败");
                        popupWindowDownloadAPP.dismiss();
                        popupWindowDownloadAPP = null;
                    }
                }
            } else if (DownLoadAPKUtil.MSG_NET_RES_GET == msg.what) {
                boolean isForced = (boolean) SharePreferenceUtil.get(mContext, SharePreferenceUtil.KEY_VERSION_ISFORCED, false);
                if (!isForced) {
                    mBuilder.setProgress(100, (Integer) msg.obj, false);
                    mBuilder.setContentText("已下载" + msg.obj + "%,请耐心等待...");
                    mBuilder.setId(NOTIFICATION_ID);
                    mBuilder.show();
                } else {
                    SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_ISDOWNLOAD, true);
                    popupWindowDownloadAPP.getmProgressBar().setProgress((Integer) msg.obj);
                    popupWindowDownloadAPP.getTv_title().setText("已下载" + msg.obj + "%,请耐心等待...");
                }
            } else if (DownLoadAPKUtil.MSG_DOWNLOAD_OK == msg.what) {
                boolean isForced = (boolean) SharePreferenceUtil.get(mContext, SharePreferenceUtil.KEY_VERSION_ISFORCED, false);
                if (!isForced) {
                    mBuilder.setProgress(100, (Integer) msg.obj, false);
                    mBuilder.setContentText("下载成功，点击安装");
                    mBuilder.setId(NOTIFICATION_ID);
                    mBuilder.show();
                    SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_VERSION_ISFORCED, false);
                } else {
                    popupWindowDownloadAPP.getTv_title().setText("下载完成");
                    popupWindowDownloadAPP.getmProgressBar().setProgress(100);
                    popupWindowDownloadAPP.dismiss();
                    popupWindowDownloadAPP = null;
                }
            }
        }
    }

    private void installVersion(String filename) {
        try {
            File file = new File(filename);
            if (file.isFile()) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW); // 浏览网页的Action(动作)
                String type = "application/vnd.android.package-archive";
                intent.setDataAndType(Uri.fromFile(file), type); // 设置数据类型
                mContext.startActivity(intent);
                // ((Activity) mActivity).finish();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
    }

    private boolean checkFileMD5() {
        boolean bSuccess = false;
        String checkString;
        try {
            checkString = MD5ChangeUtil.getFileMD5String(new File(localUrl))
                    .toUpperCase();
            if (checkString != null) {
                bSuccess = true;
            }
        } catch (IOException e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return bSuccess;
    }


    @Override
    public void done(String msg, int what, JSONObject response) {
        switch (what) {
            case MSG_RESULT_CHECK:
                try {
                    JSONObject data = response.getJSONObject("data");
                    Version version = JsonUtil.getModle(data.toString(), Version.class);
                    int netVersionCode = Integer.parseInt(version.getVersionCode());
                    SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_VERSION_CODE, netVersionCode);
                    SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_VERSION_FORCED, version.getForced());
                    SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_VERSION_MINCODE, version.getMinVersionCode());
                    if (netVersionCode > Integer.parseInt(mVersionCode)) {
                        showNewVersionDialog(version);
                    } else {
                        SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_BOOL_VERSION_ISNEW, true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void error(String errorMsg) {
    }

}

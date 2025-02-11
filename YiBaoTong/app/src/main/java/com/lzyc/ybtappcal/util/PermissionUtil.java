package com.lzyc.ybtappcal.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import com.lzyc.ybtappcal.app.App;
import com.lzyc.ybtappcal.widget.popupwindow.PopupWindowTwoButton;

import java.util.List;

/**
 * 打开权限设置
 * Created by yang on 2016/9/20.
 */
public class PermissionUtil {
    private Context mContext;
    private static PermissionUtil instance;

    public PermissionUtil(){
        this.mContext= App.getInstance().getBaseContext();
    }

    public synchronized static PermissionUtil getInstance() {
        if (instance == null) {
            instance = new PermissionUtil();
        }
        return instance;
    }


    public void showDingwei() {
        try {
            String name = Build.MANUFACTURER;
            if ("HUAWEI".equals(name)) {
                goHuaWeiMainager();
            } else if ("vivo".equals(name)) {
                goVivoMainager();
            } else if ("Coolpad".equals(name)) {
                goCoolpadMainager();
            } else if ("Meizu".equals(name)) {
                goMeizuMainager();
                // getAppDetailSettingIntent();
            } else if ("Xiaomi".equals(name)) {
                goXiaoMiMainager();
            } else if ("samsung".equals(name)) {
                goSangXinMainager();
            } else {
                goIntentSetting();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSaoma(Context context) {
        this.mContext = context;
        final PopupWindowTwoButton popupWindowSaoma = new PopupWindowTwoButton((Activity) mContext);
        popupWindowSaoma.getTvTitle().setVisibility(View.INVISIBLE);
        popupWindowSaoma.getTv_content().setText("无法开启摄像头，请检查医保通是否有访问摄像头的权限，或重启设备后重试");
        popupWindowSaoma.getTvOk().setText("去设置");
        popupWindowSaoma.getTvCancel().setText("暂不");
        popupWindowSaoma.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowSaoma.dismiss();
            }
        });
        popupWindowSaoma.getTvOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = Build.MANUFACTURER;
                    if ("HUAWEI".equals(name)) {
                        goHuaWeiMainager();
                    } else if ("vivo".equals(name)) {
                        goVivoMainager();
                    } else if ("Coolpad".equals(name)) {
                        goCoolpadMainager();
                    } else if ("Meizu".equals(name)) {
                        goMeizuMainager();
                        // getAppDetailSettingIntent();
                    } else if ("Xiaomi".equals(name)) {
                        goXiaoMiMainager();
                    } else if ("samsung".equals(name)) {
                        goSangXinMainager();
                    } else {
                        goIntentSetting();
                    }
                } catch (Exception e) {
                    popupWindowSaoma.dismiss();
                    e.printStackTrace();
                }
                popupWindowSaoma.dismiss();
            }
        });
        popupWindowSaoma.showPopupWindow(((Activity) mContext).getWindow().getDecorView(), Gravity.CENTER);
        popupWindowSaoma.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindowSaoma.dismiss();
                    return true;
                }
                return false;
            }
        });

    }

    private void goHuaWeiMainager() {
        try {
            Intent intent = new Intent("demo.vincent.com.tiaozhuan");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            goIntentSetting();
        }
    }

    private void goXiaoMiMainager() {
        try {
            Intent localIntent = new Intent(
                    "miui.intent.action.APP_PERM_EDITOR");
            localIntent
                    .setClassName("com.miui.securitycenter",
                            "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", "com.lzyc.ybtappcal");
            mContext.startActivity(localIntent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            goIntentSetting();
        }
    }

    private void goMeizuMainager() {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", "xiang.settingpression");
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            localActivityNotFoundException.printStackTrace();
            goIntentSetting();
        }
    }

    private void goSangXinMainager() {
        //三星4.3可以直接跳转
        goIntentSetting();
    }

    private void goIntentSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", "com.lzyc.ybtappcal", null);
        intent.setData(uri);
        try {
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void goOppoMainager() {
//        doStartApplicationWithPackageName("com.coloros.safecenter");
//    }

    /**
     * doStartApplicationWithPackageName("com.yulong.android.security:remote")
     * 和Intent open = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
     * startActivity(open);
     * 本质上没有什么区别，通过Intent open...打开比调用doStartApplicationWithPackageName方法更快，也是android本身提供的方法
     */
    private void goCoolpadMainager() {
        doStartApplicationWithPackageName("com.yulong.android.security:remote");
      /*  Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
        startActivity(openQQ);*/
    }

    //vivo
    private void goVivoMainager() {
        doStartApplicationWithPackageName("com.bairenkeji.icaller");
     /*   Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.vivo.securedaemonservice");
        startActivity(openQQ);*/
    }

    /**
     * 此方法在手机各个机型设置中已经失效
     *
     * @return
     */
    private Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", "com.lzyc.ybtappcal", null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", "com.lzyc.ybtappcal");
        }
        return localIntent;
    }

    private void doStartApplicationWithPackageName(String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = mContext.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = mContext.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            try {
                mContext.startActivity(intent);
            } catch (Exception e) {
                goIntentSetting();
                e.printStackTrace();
            }
        }
    }
}

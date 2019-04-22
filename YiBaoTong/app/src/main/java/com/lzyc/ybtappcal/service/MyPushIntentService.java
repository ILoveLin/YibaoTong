package com.lzyc.ybtappcal.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.mine.DynMessageActivity;
import com.lzyc.ybtappcal.activity.mine.SysMessageActivity;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.LogUtil;
import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 友盟推送
 * 完全自定义处理类
 * http://dev.umeng.com/push/android/integration#4_10
 */
public class MyPushIntentService extends UmengMessageService  {
    private NotificationManager notificationManager;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onMessage(Context context, Intent intent) {
        this.mContext=context;
        String json = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        LogUtil.y("#####推送接收到的消息#######"+json);

        try {
            UMessage msg = new UMessage(new JSONObject(json));
            JSONObject jsonObject = new JSONObject(msg.extra);
            String type = jsonObject.getString("type");
            Intent intent1 = new Intent();
            intent1.setAction(Contants.ACTION_NAME_PUSH_READ_NO);
            sendBroadcast(intent1);
            Bundle bundle = new Bundle();
            if (type.equals("comment")) {
                String comment_id = jsonObject.getString("comment_id");
                bundle.putString("commentId", comment_id);
                setNotiType(R.mipmap.ic_launcher, msg.title, msg.text, DynMessageActivity.class, bundle);
            } else {
                String url = jsonObject.getString("url");
                bundle.putString("url", url);
                setNotiType(R.mipmap.ic_launcher, msg.title, msg.text, SysMessageActivity.class, bundle);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("NewApi")
    private void setNotiType(int iconId, String contentTitle, String contentText, Class activity, Bundle bundle) {
        Intent notifyIntent = new Intent(this, activity);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        notifyIntent.putExtras(bundle);
        PendingIntent appIntent = PendingIntent.getActivity(this, 0,
                notifyIntent, 0);
            Notification noti = new Notification.Builder(mContext)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(iconId)
                    .setContentIntent(appIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setTicker(contentTitle)
                    .build();
        notificationManager.notify(0,noti);
//        Notification myNoti = new Notification();
//        myNoti.flags = Notification.FLAG_AUTO_CANCEL;
////        myNoti.setLatestEventInfo(this, contentTitle, contentText, appIntent);
//        notificationManager.notify(0, myNoti);
    }

}

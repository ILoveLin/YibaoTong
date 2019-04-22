package com.lzyc.ybtappcal.handler;

import android.content.Context;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by lovelin on 2016/6/14.
 * (常见view的点击事件)
 * 通过tag来区分不同的listview的点击事件从而自定义式umeng自定义事件统计数据
 */
public class OnClickListenerHandler implements InvocationHandler {

    private final View.OnClickListener listener;
    private Context mContext;
    private String tag;

    public OnClickListenerHandler(View.OnClickListener listener, Context context, String tag) {
        this.listener = listener;
        this.mContext = mContext;
        this.tag = tag;

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object o = null;
        if (tag.equals(FunnelContants.FUNNEL2_CAMERA_CLICKED)) {
            //漏斗二,(1)首页扫描启动相机  camera_clicked
            MobclickAgent.onEvent(mContext, FunnelContants.FUNNEL2_CAMERA_CLICKED);
        } else if (tag.equals(FunnelContants.FUNNEL2_SCAN_START)) {
            //漏斗二,(2)进入扫描帮助页确定按钮被点击 camera_start
            MobclickAgent.onEvent(mContext, FunnelContants.FUNNEL2_SCAN_START);
        } else if (tag.equals(FunnelContants.FUNNEL3_DISTANCE_START)) {              //漏斗三,进入切换地址(药品详情界面的切换地址按钮)
            MobclickAgent.onEvent(mContext, FunnelContants.FUNNEL3_DISTANCE_START);
        } else if (tag.equals(FunnelContants.FUNNEL3_BAIDUMAP_CLICKED)) {            //漏斗三,选择地址成功(确定按钮被点击)
            MobclickAgent.onEvent(mContext, FunnelContants.FUNNEL3_BAIDUMAP_CLICKED);
        } else if (tag.equals(FunnelContants.FUNNEL1_SEARCH_START)) {                //漏斗一,进入搜索界面
            MobclickAgent.onEvent(mContext, FunnelContants.FUNNEL1_SEARCH_START);
        }
        o = method.invoke(listener, args);
        return o;
    }
    public View.OnClickListener create() {
        Object instance = Proxy.newProxyInstance(listener.getClass().getClassLoader(), listener.getClass().getInterfaces(), this);
        return (View.OnClickListener) instance;
    }
}

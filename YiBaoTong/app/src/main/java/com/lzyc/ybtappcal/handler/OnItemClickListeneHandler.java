package com.lzyc.ybtappcal.handler;

import android.content.Context;
import android.widget.AdapterView;

import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by lovelin on 2016/6/14.
 * <p>
 * ListView的OnItemClickListener点击事件的代理实现类
 * 通过tag来区分不同的View的点击事件从而自定义式umeng自定义事件统计数据
 */
public class OnItemClickListeneHandler implements InvocationHandler {
    private AdapterView.OnItemClickListener ItemListener;
    private Context mContext;
    private String tag;

    public OnItemClickListeneHandler(AdapterView.OnItemClickListener ItemListener, Context mContext, String tag) {
        this.mContext = mContext;
        this.ItemListener = ItemListener;
        this.tag = tag;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invoke = null;
        if (tag.equals(FunnelContants.FUNNEL1_DRUESEARCHRESULTS_START)) { //搜索出来listview列表界面,跳转到药品详情界面
            MobclickAgent.onEvent(mContext,FunnelContants.FUNNEL1_DRUESEARCHRESULTS_START);
        }else if(tag.equals(FunnelContants.EVENT13_SCANRESULTS_START)) {  //漏斗1,2目标事件
            MobclickAgent.onEvent(mContext,FunnelContants.EVENT13_SCANRESULTS_START);
        }
        invoke = method.invoke(ItemListener, args);
        return invoke;
    }

    public AdapterView.OnItemClickListener create() {
        Object o = Proxy.newProxyInstance(ItemListener.getClass().getClassLoader(), ItemListener.getClass().getInterfaces(), this);
        return (AdapterView.OnItemClickListener) o;
    }
}

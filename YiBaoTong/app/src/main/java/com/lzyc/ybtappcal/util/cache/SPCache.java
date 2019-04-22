package com.lzyc.ybtappcal.util.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.app.App;
import com.lzyc.ybtappcal.bean.MyInfoBean;
import com.lzyc.ybtappcal.bean.RefundBrief;
import com.lzyc.ybtappcal.bean.TopBanner;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;

import java.util.List;

/**
 * Created by yang on 2017/04/30.
 */

public class SPCache {
    private static SPCache instance;
    private Context mContext;
    public SPCache(){
        this.mContext=App.getInstance();
    }
    public static SPCache getInstance(){
        if(instance ==null){
            instance =new SPCache();
        }
        return instance;
    }

    public void putMineInfoCache(String json){
        SharePreferenceUtil.put(mContext, Contants.KEY_CHACHE_OBJ_MINEINFO,json);
    }

    public void putRefundBriefCache(String json){
        SharePreferenceUtil.put(mContext, Contants.KEY_CHACHE_OBJ_REFFUNDBRIEF,json);
    }

    public void putIsmessage(String ismessage){
        SharePreferenceUtil.put(mContext, Contants.KEY_CHACHE_OBJ_ISMESSAGE,ismessage);
    }

    public String getIsmessage(){
        String isMessage= (String) SharePreferenceUtil.get(mContext, Contants.KEY_CHACHE_OBJ_ISMESSAGE,"");
        return isMessage;
    }

    /**
     * 个人页返现相关的
     * @return
     */
    public RefundBrief getRefundBriefCache(){
        String json= (String) SharePreferenceUtil.get(mContext,Contants.KEY_CHACHE_OBJ_REFFUNDBRIEF,"");
        RefundBrief refunBrief=null;
        if(!TextUtils.isEmpty(json)){
            refunBrief=JsonUtil.getModle(json,RefundBrief.class);
        }
        return refunBrief;
    }

    /**
     * 个人页的数据
     * @return
     */
    public MyInfoBean getMyInfoBeanCache(){
        MyInfoBean myInfoBean=null;
        String json= (String) SharePreferenceUtil.get(mContext,Contants.KEY_CHACHE_OBJ_MINEINFO,"");
        if(!TextUtils.isEmpty(json)){
             myInfoBean=JsonUtil.getModle(json,MyInfoBean.class);
        }
        return myInfoBean;
    }

    /**
     * 退出登录需要清空的SP
     */
    public void clearCache(){
        SharePreferenceUtil.put(mContext, Contants.KEY_CHACHE_OBJ_MINEINFO,"");
        SharePreferenceUtil.put(mContext, Contants.KEY_CHACHE_OBJ_REFFUNDBRIEF,"");
//        isMessage不需要与是否登陆无关缓存
    }
}

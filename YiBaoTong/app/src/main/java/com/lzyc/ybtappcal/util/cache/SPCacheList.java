package com.lzyc.ybtappcal.util.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.app.App;
import com.lzyc.ybtappcal.bean.HistoryCache;
import com.lzyc.ybtappcal.bean.TopBanner;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 列表的缓存
 * Created by yang on 2017/05/08.
 */

public class SPCacheList {
    private static SPCacheList instance;
    private Context mContext;

    public SPCacheList() {
        this.mContext = App.getInstance();
    }

    public static SPCacheList getInstance() {
        if (instance == null) {
            instance = new SPCacheList();
        }
        return instance;
    }

    /**
     * 写入搜索历史
     *
     * @space sp命名空间
     */
    public void writeHistoryCache(String space, HistoryCache mHistoryCache, int count) {
        SharedPreferences sp = mContext.getSharedPreferences(space,
                Context.MODE_PRIVATE);
        ArrayList<HistoryCache> list = readHistory(space);
        clearHistory(space);
        Iterator<HistoryCache> it = list.iterator();
        while (it.hasNext()) {
            HistoryCache historyCache = it.next();
            if (null != historyCache) {
                String goodsName = mHistoryCache.getGoodsName();
                String oldGoodsName = historyCache.getGoodsName();
                String name = mHistoryCache.getName();
                String oldName = historyCache.getName();
                String oldCode=historyCache.getCode();
                String code=mHistoryCache.getCode();
                int OldCacheType=mHistoryCache.getCacheType();
                int cacheType=mHistoryCache.getCacheType();
                if (mHistoryCache.getCacheType() == HistoryCache.TYPE_CACHE_SEARCH) {//搜索
                    if (goodsName == null || TextUtils.isEmpty(goodsName)) {
                        if (name.equals(oldName)&code==null&&oldCode==null&&OldCacheType==cacheType) {
                            it.remove();
                        }
                    }else{
                        if (name.equals(oldName)&code==null&&oldCode==null&&OldCacheType==cacheType&&goodsName.equals(oldGoodsName)) {
                            it.remove();
                        }
                    }
                } else {//扫码
                    if (goodsName == null || TextUtils.isEmpty(goodsName)) {
                        if (name.equals(oldName)&code!=null&&oldCode!=null&&OldCacheType==cacheType) {
                            it.remove();
                        }
                    }else{
                        if (name.equals(oldName)&code!=null&&oldCode!=null&&OldCacheType==cacheType&&goodsName.equals(oldGoodsName)) {
                            it.remove();
                        }
                    }
                }
            }
        }
        mHistoryCache.setCreateTime(System.currentTimeMillis());
        list.add(0, mHistoryCache);
        SharedPreferences.Editor editor = sp.edit();
        int size = list.size() >= count ? count : list.size();
        editor.putInt("count", count);//存储列表限制数量
        for (int i = size - 1; i >= 0; i--) {
            if (mHistoryCache != null) {
                editor.putString("json" + i, "" + new Gson().toJson(list.get(i)));
            }
            editor.commit();
        }
    }

    /**
     * 写入商品搜索的历史
     */
    public void writeGoodsSearchHistory(HistoryCache mHistoryCache) {
        writeHistoryCache(SharePreferenceUtil.SPACE_HISTORY_GOODS, mHistoryCache, 30);
    }

    /**
     * 写入药品查询搜索的历史
     */
    public void writeDrugSearchHistory(HistoryCache mHistoryCache) {
        writeHistoryCache(SharePreferenceUtil.SPACE_HISTORY_DRUG_SEARCH, mHistoryCache, 60);
    }

    /**
     * 写入药品扫码历史
     */
    public void writeDrugScanHistory(HistoryCache mHistoryCache) {
        writeHistoryCache(SharePreferenceUtil.SPACE_HISTORY_DRUG_SEARCH, mHistoryCache, 60);
        writeHistoryCache(SharePreferenceUtil.SPACE_HISTORY_DRUG_SCAN, mHistoryCache, 60);
    }

    /**
     * 写入城市搜索的历史
     */
    public void writeCityHistory(HistoryCache mHistoryCache) {
        writeHistoryCache(SharePreferenceUtil.SPACE_HISTORY_CITY_SEARCH, mHistoryCache, 3);
    }

    /**
     * 读取城市搜索的历史
     */
    public List<HistoryCache> readCityHistory() {
        List<HistoryCache> list = readHistory(SharePreferenceUtil.SPACE_HISTORY_CITY_SEARCH);
        return list;
    }

    /**
     * 写入ImageList,不限制数量
     * @param list
     */
    public void writeListImageBean(List<TopBanner.DataBean.ImagesBean> list) {
        SharedPreferences sp = mContext.getSharedPreferences(SharePreferenceUtil.SPACE_BIYAO_BANNER, Context.MODE_PRIVATE);
        clearHistory(SharePreferenceUtil.SPACE_BIYAO_BANNER);//写入之前清空
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < list.size(); i++) {
            TopBanner.DataBean.ImagesBean imagesBean = list.get(i);
            editor.putString("json" + i, new Gson().toJson(imagesBean));
        }
        editor.putInt("count", list.size());
        editor.commit();
    }

    /**
     * 读取ImageList
     *
     * @return
     */
    public List<TopBanner.DataBean.ImagesBean> readLisImageBean() {
        List<TopBanner.DataBean.ImagesBean> listT = new ArrayList<>();
        SharedPreferences sp = mContext.getSharedPreferences(SharePreferenceUtil.SPACE_BIYAO_BANNER, Context.MODE_PRIVATE);
        int listSize = sp.getInt("count", Contants.INT_0);
        for (int i = 0; i < listSize; i++) {
            String json = sp.getString("json" + i, "");
            TopBanner.DataBean.ImagesBean imagesBean = JsonUtil.getModle(json, TopBanner.DataBean.ImagesBean.class);
            listT.add(imagesBean);
        }
        return listT;
    }

    /**
     * 读取药品扫码历史
     */
    public List<HistoryCache> readDrugScanHistory() {
        List<HistoryCache> listCache = readHistory(SharePreferenceUtil.SPACE_HISTORY_DRUG_SCAN);
        return listCache;
    }

    /**
     * 读取商品搜索的历史
     */
    public ArrayList<HistoryCache> readGoodsSearchHistory() {
        ArrayList<HistoryCache> listCache = readHistory(SharePreferenceUtil.SPACE_HISTORY_GOODS);
        return listCache;
    }

    /**
     * 读取商品搜索的历史
     */
    public ArrayList<HistoryCache> readDrugSearchHistory() {
        ArrayList<HistoryCache> listCache = readHistory(SharePreferenceUtil.SPACE_HISTORY_DRUG_SEARCH);
        return listCache;
    }


    /**
     * 读取搜索历史记录
     * content  json  createTime  三个字段是HistorySearch的字段,具体字段更具你的需求自己设定字段
     *
     * @param space
     * @return
     */
    public ArrayList<HistoryCache> readHistory(String space) {
        ArrayList<HistoryCache> list = new ArrayList<HistoryCache>();
        SharedPreferences sp = mContext.getSharedPreferences(space, Context.MODE_PRIVATE);
        int count = sp.getInt("count", 0);
        for (int i = 0; i < count; i++) {
            String json = sp.getString("json" + i, null);
            if (json != null) {
                HistoryCache hs = JsonUtil.getModle(json, HistoryCache.class);
                list.add(hs);
            }
        }
        return list;
    }

//关键字:清空历史

    /**
     * 清除历史记录
     * 当前用作清空搜索历史,扫描历史
     *
     * @param space
     */
    public void clearHistory(String space) {
        SharedPreferences sp = mContext.getSharedPreferences(space,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

}

package com.lzyc.ybtappcal.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 作者：xujm
 * 时间：2015/12/29
 * 备注：SharePreference的存储工具类
 */
public class SharePreferenceUtil {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";

    public static final String CACHEFILENAME = "Config.data";
    //    public static final String HISTORY_NAME = "history_data";
    public static final String IS_LOGIN = "islogin";
    public static final String IS_FIRST = "isfirst";
    public static final String IS_LAUNCH = "islunch";
    public static final String IS_DINGWEI = "isdingwei";
    public static final String IS_FIRST_REGISTER_OK = "IS_FIRST_REGISTER_OK";
    //    public static final String IS_FIRST_REGISTER_OK = "isfirstregisterok";
    public static final String DYNAMIC_SWITCH_TAB = "dynamic.switching.tab";
    public static final String DYNAMIC_SWITCH_TAB_ORDER_SUCCESS = "back_to_home";
    public static final String KEY_DW_STATUS = "dingweistatus";
    public static final String KEY_MESSAGE_COUNT_NOREAD_SYS = "message.count.noread.system";
    public static final String KEY_MESSAGE_COUNT_NOREAD_DYN = "message.count.noread.dyd";
    //登录信息
    public static final String UID = "uid";
    public static final String USER_NAME = "username";
    public static final String PHONE = "phone";
    public static final String NICKNAME = "nickname";
    public static final String NOSETTINGNAME = "noname";
    public static final String KEY_DEVICE_TOKEN = "devicetoken";
    public static final String KEY_MESSAGE_STATUS = "message_status";
    //定位城市
    public static final String CITY = "city";
    //选择的已开通城市
    public static final String CITY_TOP_CHOOSE = "cityopenois";

    //定位省份
    public static final String PROVINCE = "province";
    public static final String PROVICE_TOP_CHOOSE = "province_shoudong";

    //异地选择的医保省份
    public static final String PROVINCE_CANBAO = "province.choose";
    //定位地址
    public static final String ADDRESS = "address";
    //当前选择的地址
    public static final String ADDRESS_CURRENT_CHOOSRE = "address.current.choose";
    //手动选择,定位经纬度
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";

    //个人中心我的医保城市
    public static final String CITY_CANBAO = "ybcity";
    //就诊城市，选择就诊城市
    public static final String CITY_JIUZHEN = "city_selected";
    public static final String PROVICE_JIUZHEN = "provice_selected";
    //职位类型
    public static final String KEY_OBJ_TYPE_JOB = "typejob";
    //职位类型选择是否被记住
    public static final String KEY_BOOL_POP_JOB_ISCHEK = "typejob.popischeck";
    public static final String KEY_BOOL_VERSION_ISNEW = "isnew";
    //首页搜索历史保存空间
    public static final String SPACE_HISTORY_DRUG_SEARCH = "space_search_drug";
    //首页搜索城市搜索历史缓存空间
    public static final String SPACE_HISTORY_CITY_SEARCH = "space_history_search_city";
    //异地报销搜索历史缓存空间
    public static final String SPACE_HISTORY_DRUG_SCAN = "space.history.search.reimbursement.search";
    //首页扫描历史缓存空间
    public static final String SPACE_HISTORY_TOP_SCAN = "space.history.search.top.scan";
    //异地报销扫描历史缓存空间
    public static final String SPACE_HISTORY_REIMBURSEMENT_SCAN = "space.history.search.reimbursement.scan";
    public static final String SPACE_HISTORY_GOODS= "space_history_search_goods";
    public static final String SPACE_BIYAO_BANNER= "space_biyao_baner";//药品查询，比药banner的缓存控件
    public static final String SPACE_BIYAO_ADVERTISEMENT= "space_advertisement";//广告页的缓存
    //关键字：数据缓存
    public static  int SP_DATA_COUNT = 60;//设置缓存数据数量

    //搜索时候EditTest填入的文字
    public static final String KEY_WORD = "";
    public static final String HOME_IMAGE = "image";
    public static final boolean KEY_ISSCAN_ANIMA = true;
    public static final String CLICK = "click";                        //minefragment
    public static final String ISFIRSTIN = "isfirstin";                        //minefragment
    public static final String CLICK_AGAIN = "click.again";            //个人资料       显示/隐藏定点医院

    public static final String KEY_CACHE_TITLE = "KEY_CACHE_TITLE";
    public static final String KEY_CACHE_BANNER_HEIGHT = "KEY_CACHE_BANNER_HEIGHT";
    public static final String KEY_CACHE_BANNER = "KEY_CACHE_BANNER";
    public static final String KEY_CACHE_DATA = "KEY_CACHE_DATA";
    public static final String KEY_CACHE_DATA_CATEGORY = "KEY_CACHE_DATA_CATEGORY";
    public static final String KEY_VERSION_ISFORCED = "KEY_FORCED";
    public static final String KEY_VERSION_CODE = "KEY_VERSION_CODE";//服务器返回的当前版本号
    public static final String KEY_VERSION_MINCODE = "KEY_VERSION_CODE_MIN";//服务器返回的最小版本号
    public static final String KEY_VERSION_FORCED = "KEY_VERSION_FORCED";//服务器返回的是否强制更新
    public static final String KEY_ISDOWNLOAD="KEY_ISDOWNLOAD";//强制更新是否已经在下载
    public static final String KEY_FANXIAN_WIAT="KEY_FANXIAN";//待返现
    public static final String KEY_ALI_INFO="KEY_ALI_INFO";//待返现

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }


    public static void putString(Context mContext,String key,String value){
        SharedPreferences sp = mContext.getSharedPreferences(CACHEFILENAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getString(Context mContext,String key,String defaultValue){
        SharedPreferences sp = mContext.getSharedPreferences(CACHEFILENAME,
                Context.MODE_PRIVATE);
        return sp.getString(key,defaultValue);
    }
    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}

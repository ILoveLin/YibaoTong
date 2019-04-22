package com.lzyc.ybtappcal.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/*
 * Author: luxf
 * Created Date:2015-7-14
 * Copyright @ 2015 BU
 * Description: json的工具类
 *
 * History:
 */
public class JsonUtil {

    public static final String DefaultDateFormat = "yyyy/MM/dd HH:mm:ss";
    public static boolean isPrintException = true;

    /**
     * 提供指定日期格式的Gson对象
     *
     * @param dateFormat
     * @return
     */
    public static Gson getGson(String dateFormat) {
        return new GsonBuilder().setDateFormat(dateFormat).create();
    }


    // 将Json数据解析成相应的映射对象
    public  static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }

    /**
     * 提供默认日期解析格式的Gson对象
     *
     * @return
     */
    public static Gson getGson() {
        return new GsonBuilder().setDateFormat(DefaultDateFormat).create();
    }

    /**
     * json转指定对象
     *
     * @param json
     * @param cls
     * @return T
     * @author luxf 2015-7-14 下午9:18:48
     */
    public static <T> T getModle(String json, Class<T> cls) {
        return getGson().fromJson(json, cls);
    }

    /**
     * json转指定对象(key为json中为列表的字段名)
     *
     * @param json
     * @param cls
     * @return T
     * @author luxf 2015-7-14 下午9:18:48
     */
    public static <T> T getModle(String json, Class<T> cls, String key) {
        json = getString(json, key);
        return getGson().fromJson(json, cls);
    }

    /**
     * json转指定对象列表
     *
     * @param json
     * @return List<T>
     * @author luxf 2015-7-16 下午3:01:42
     */
    public static <T> List<T> getListModle(String json) {
        List<T> list = new ArrayList<T>();
        Type type = new TypeToken<ArrayList<T>>() {
        }.getType();
        list = getGson().fromJson(json, type);
        return list;
    }

    /**
     * json转指定对象列表(key为json中为列表的字段名)
     *
     * @param json
     * @param key
     * @return List<T>
     * @author luxf 2015-7-16 下午3:01:50
     */
    public static <T> List<T> getListModle(String json, String key, Type type) {
        json = getString(json, key);
        List<T> list = getGson().fromJson(json, type);
        return list;
    }

    /**
     * json转指定对象列表(key为json中为列表的字段名)
     *
     * @param json
     * @param key
     * @return List<T>
     * @author luxf 2015-7-16 下午3:01:50
     */
    public static <T> ArrayList<T> getListModle2(String json, String key, Type type) {
        json = getString(json, key);
        ArrayList<T> list = getGson().fromJson(json, type);
        return list;
    }

    /**
     * get String from jsonObject
     *
     * @param jsonObject
     * @param key
     * @return <ul>
     * <li>if jsonObject is null, return null</li>
     * <li>if key is null or empty, return null</li>
     * <li>if {@link JSONObject#getString(String)} exception, return
     * null</li>
     * <li>return {@link JSONObject#getString(String)}</li>
     * </ul>
     */
    public static String getString(String jsonObject, String key) {
        return getString(jsonObject, key, null);
    }

    /**
     * get String from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getString(String)} exception, return
     * defaultValue</li>
     * <li>return {@link JSONObject#getString(String)}</li>
     * </ul>
     */
    public static String getString(String jsonObject, String key,
                                   String defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return new JSONObject(jsonObject).getString(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }
}

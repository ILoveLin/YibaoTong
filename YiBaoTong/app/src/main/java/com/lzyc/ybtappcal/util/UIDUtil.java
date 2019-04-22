package com.lzyc.ybtappcal.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lxx on 2017/3/25.
 */
public class UIDUtil {
    private static final String UID = "UID";

    /**
     * 保存UID
     */
    public static void saveUID(Context context, String uid){
        SharedPreferences sharedPreferences = context.getSharedPreferences(UID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(UID, uid);
        editor.commit();
    }

    public static String getUID(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(UID, Context.MODE_PRIVATE);
        return sharedPreferences.getString(UID, "");
    }
}

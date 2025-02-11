package com.lzyc.ybtappcal.util;

import android.util.Log;

/**
 * log工具
 *
 * @author luxf 2015-7-15 下午1:46:50
 */
public class LogUtil {
    /**
     * 是否需要打印
     * <p/>
     * logOn=false :屏蔽打印
     * <p/>
     * logOn=true :显示打印
     */
    public static boolean logOn = true;


    /**
     * 打印LOG
     *
     * @param msg
     */
    public static void showLog(String msg) {
        if (logOn) {
            Log.i("yibaotong", ""+msg);
        }
    }


    /**
     * log.r 打印error类型的信息
     ** @param msg
     */
    public static void y(String msg) {
        if (logOn) {
            Log.e("yang", "###########"+msg);
        }
    }

    /**
     * log.i 打印info类型的信息
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (logOn) {
            Log.i(tag, ""+msg);
        }
    }

    /**
     * log.w 打印warm类型的信息
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (logOn) {
            Log.w(tag, ""+msg);
        }
    }

    /**
     * log.r 打印error类型的信息
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (logOn) {
            Log.e(tag, ""+msg);
        }
    }

    /**
     * log.r 打印error类型的信息
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg,Exception e) {
        if (logOn) {
            Log.e(tag, ""+msg,e);
        }
    }

    /**
     * log.v 打印verbose类型的信息
     *
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (logOn) {
            Log.v(tag, ""+msg);
        }
    }

    /**
     * log.d 打印debug类型的信息
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (logOn) {
            Log.d(tag, ""+msg);
        }
    }
}

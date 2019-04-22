package com.lzyc.ybtappcal.util;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 作者：xujm
 * 时间：2015/12/29
 * 备注：工具类
 */
public class Util {
    public static UnSupportEmoji getUnSupportEmojiListen(Context context, EditText et) {
        return new UnSupportEmoji(et,context);
    }

    /**
     * 密码
     *
     * @param pwd
     * @return
     */
    public static boolean isPwd(String pwd) {
        int length = pwd.length();
        if (length >= 6 && length < 20) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 手机验证
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    /**
     * 验证邮政编码
     *
     * @param post
     * @return
     */
    public static boolean checkPost(String post) {
        if (post.matches("[1-9]\\d{5}(?!\\d)")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否为视频频
     *
     * @param suffix
     * @return
     */
    public static boolean fileIsVideo(String suffix) {
        String[] fileIsAudios = new String[]{".3gp", ".avi", ".mp4", ".mpeg",
                ".mpe", ".mpg4", ".rmvb", ".3gp"};
        for (String s : fileIsAudios) {
            if (suffix.toLowerCase().endsWith(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为音频
     *
     * @param suffix
     * @return
     */
    public static boolean fileIsAudio(String suffix) {
        String[] fileIsAudios = new String[]{".amr", ".mp3", ".ogg", ".mp2",
                ".m3u", ".m4a", ".m4b", ".m4p", ".wav", ".wma", ".wmv"};
        for (String s : fileIsAudios) {
            if (suffix.toLowerCase().endsWith(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为图片
     */
    public static boolean fileIsImage(String suffix) {
        String[] fileIsAudios = new String[]{".jpg", ".png", ".jpeg"};
        for (String s : fileIsAudios) {
            if (suffix.toLowerCase().endsWith(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 保留index数量的小数
     *
     * @param number
     * @param index
     * @return
     */
    public static double roundNum(double number, int index) {
        double result = 0;
        double temp = Math.pow(10, index);
        result = Math.round(number * temp) / temp;
        return result;
    }

    /**
     * 是否为null
     *
     * @param str
     * @return boolean
     * @author luxf 2015-7-15 下午6:53:50
     */
    public static boolean isNull(String str) {
        if ("".equals(str) && str == null) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 计算金额
     *
     * @param argStr
     * @return
     */
    public static String getFloatDotStr(String argStr) {
        float arg = Float.valueOf(argStr);
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(arg);
    }

    /**
     * 计算绘制界面UIbutton
     *
     * @param view
     */
    public static void resetRL(View... view) {
        float rote = HandlerUI.getWidthRoate(ScreenUtils.getScreenWidth());
        if (view == null || rote == 1) {
            return;
        }
        for (View view2 : view) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view2
                    .getLayoutParams();
            layoutParams.leftMargin = (int) (layoutParams.leftMargin * rote);
            layoutParams.rightMargin = (int) (layoutParams.rightMargin * rote);
            layoutParams.topMargin = (int) (layoutParams.topMargin * rote);
            layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * rote);
            view2.setLayoutParams(layoutParams);
        }
    }

    public static void resetRLBack(View... view) {
        float rote = HandlerUI.getWidthRoate(ScreenUtils.getScreenWidth());
        if (view == null || rote == 1) {
            return;
        }
        for (View view2 : view) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view2
                    .getLayoutParams();
            layoutParams.height = (int) (layoutParams.height * rote);
            layoutParams.width = (int) (layoutParams.width * rote);
            view2.setLayoutParams(layoutParams);
        }
    }

    /**
     * 计算绘制界面UIbutton
     *
     * @param view
     */
    public static void resetLL(View... view) {
        float rote = HandlerUI.getWidthRoate(ScreenUtils.getScreenWidth());
        if (view == null || rote == 1) {
            return;
        }
        for (View view2 : view) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view2
                    .getLayoutParams();
            layoutParams.leftMargin = (int) (layoutParams.leftMargin * rote);
            layoutParams.rightMargin = (int) (layoutParams.rightMargin * rote);
            layoutParams.topMargin = (int) (layoutParams.topMargin * rote);
            layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * rote);
            view2.setLayoutParams(layoutParams);
        }
    }

    public static void resetLLBack(View... view) {
        float rote = HandlerUI.getWidthRoate(ScreenUtils.getScreenWidth());
        if (view == null || rote == 1) {
            return;
        }
        for (View view2 : view) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view2
                    .getLayoutParams();
            layoutParams.height = (int) (layoutParams.height * rote);
            layoutParams.width = (int) (layoutParams.width * rote);
            view2.setLayoutParams(layoutParams);
        }
    }


    /**
     * 将与关键字相同的高亮显示
     *
     * @param key
     * @param str
     * @param tv
     */
    public static void setHighLight(String key, String str, TextView tv) {
        SpannableString s = new SpannableString(str);
        Pattern p = Pattern.compile(key);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(s);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        window.setAttributes(lp);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void setBackgroundBgAlpha(FragmentActivity activity, float bgAlpha) {
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = activity.getWindow().getAttributes();
        p.height = (int) (d.getHeight() * 1.0);
        p.width = (int) d.getWidth();
        p.alpha = bgAlpha;
        p.dimAmount = 0.0f;
        activity.getWindow().setAttributes(p);
    }

    //改变ImageView宽高
    public static void changeWidthHeight(ImageView image, float w, int num) {
        ViewGroup.LayoutParams params = image.getLayoutParams();
        int cWidth = (ScreenUtils.getScreenWidth() - DensityUtils.dp2px(w)) / num;
        params.width = cWidth;
        params.height = cWidth;
        image.setLayoutParams(params);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    /**
     * HashMap按key值首字母排序
     *
     * @param hashMap
     * @return
     */
    public static String mapToSort(Map hashMap) {
        StringBuffer sb = new StringBuffer();
        Object[] keyArr = hashMap.keySet().toArray();
        Arrays.sort(keyArr);
        for (Object key : keyArr) {
            Object value = hashMap.get(key);
            sb.append(key.toString() + "" + value.toString());
        }
        return sb.toString();
    }


    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(Context context, String content) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

}

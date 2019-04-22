package com.lzyc.ybtappcal.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.LinearLayout;

import com.lzyc.ybtappcal.R;

/**
 * Created by lovelin on 2016/7/6.
 */
public class SpannableUtils {
    /**
     * 设置内部字体颜色
     *
     * @param text
     * @param keyworld
     * @return
     */
    public static SpannableStringBuilder getSpannableText(String text, String keyworld) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        if (text.contains(keyworld)) {
            int spanStartIndex = text.indexOf(keyworld);
            int spacEndIndex = spanStartIndex + keyworld.length();
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#f72d2d")), spanStartIndex, spacEndIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return spannableStringBuilder;
    }

}

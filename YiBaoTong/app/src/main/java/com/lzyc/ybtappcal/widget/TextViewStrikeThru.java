package com.lzyc.ybtappcal.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 作者：xujm
 * 时间：2015/12/28
 * 备注：删除线的TextView
 */
public class TextViewStrikeThru extends TextView {
    public TextViewStrikeThru(Context context) {
        super(context);
        getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public TextViewStrikeThru(Context context, AttributeSet attrs) {
        super(context, attrs);
        getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public TextViewStrikeThru(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

}

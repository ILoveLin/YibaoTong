package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by yang on 2017/03/24.
 */

public class FZCQJTTextView extends TextView{
    public FZCQJTTextView(Context context) {
        super(context);
        init(context);
    }


    public FZCQJTTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FZCQJTTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface fontFace = Typeface.createFromAsset(context.getAssets(),
                "fangzhengcuqianjianti.ttf");
        this.setTypeface(fontFace);
    }


}

package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * 去除listview滑动边界阴影
 * Created by yang on 2017/04/25.
 */

public class NoShadowListView extends ListView{

    public NoShadowListView(Context context) {
        super(context);
    }

    public NoShadowListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
            this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }
}

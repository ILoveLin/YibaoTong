package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * 比药界面的ListView
 * Created by yang on 2017/04/25.
 */

public class BiyaoListView extends ListView {

    public BiyaoListView(Context context) {
        super(context);
    }

    // 去除listview滑动边界阴影
    public BiyaoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
            this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }
   //重写该方法，达到使ListView适应ScrollView的效果
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}

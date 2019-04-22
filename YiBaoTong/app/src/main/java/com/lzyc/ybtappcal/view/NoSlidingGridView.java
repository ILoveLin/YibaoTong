package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by yang on 2016/12/26.
 */
public class NoSlidingGridView extends GridView {
    public NoSlidingGridView(Context context) {
        super(context);
    }

    public NoSlidingGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoSlidingGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}

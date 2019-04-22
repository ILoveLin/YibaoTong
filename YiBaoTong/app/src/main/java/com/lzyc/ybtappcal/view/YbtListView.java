package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 自定义ListView防止多层嵌套时，列表的item只显示一行的情况
 * package_name com.lzyc.ybtappcal.view
 * Created by yang on 2016/10/25.
 */
public class YbtListView extends ListView{
    public YbtListView(Context context) {
        super(context);
    }

    public YbtListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YbtListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}

package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ViewPager 控制滑动
 * Created by yang on 2016/12/16.
 */

public class ControlSlideViewPager extends ViewPager {

    public ControlSlideViewPager(Context context) {
        super(context);
    }

    public ControlSlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean isPagingEnabled = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}
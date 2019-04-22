package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * ScrollView并没有实现滚动监听，所以我们必须自行实现对ScrollView的监听，
 * 我们很自然的想到在onTouchEvent()方法中实现对滚动Y轴进行监听
 * ScrollView的滚动Y值进行监听
 * Created by yang on 2017/4/12.
 */
public class TopFloatScrollView extends NestedScrollView {
	private OnScrollListener onScrollListener;
    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */
    private int lastScrollY;

	public TopFloatScrollView(Context context) {
		super(context, null);
	}
	public TopFloatScrollView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}
	public TopFloatScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	/**
     * 设置滚动接口
     * @param onScrollListener
     */
	public void setOnScrollListener(OnScrollListener onScrollListener){
		this.onScrollListener = onScrollListener;
	}

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(onScrollListener != null){
            onScrollListener.onScroll(t);
        }
    }

    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener{
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         */
        public void onScroll(int scrollY);
    }
}

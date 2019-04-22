package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * 拦截webview的手势事件防止与scorllview等手势事件冲突
 */
public class CustomWebView extends WebView {
	public CustomWebView(Context context) {
		super(context);
	}

	public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressWarnings("deprecation")
	public CustomWebView(Context context, AttributeSet attrs, int defStyle,
			boolean privateBrowsing) {
		super(context, attrs, defStyle, privateBrowsing);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return false;
	}
}

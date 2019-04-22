package com.lzyc.ybtappcal.view.xlist.base;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.util.LogUtil;

/**
 * 
 * @author yang
 * @package me.maxwin.view
 * @date 2016-11-1
 */
public class XYbtListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;
	private Context mContext;
	private View mContentView;
	private int loadStyle=0;
	private ImageView imageView;

	public XYbtListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public XYbtListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public void setState(int state) {
		switch (state) {
		case STATE_NORMAL:
			mContentView.setVisibility(GONE);	
			break;
		case STATE_LOADING:

		default:
			mContentView.setVisibility(VISIBLE);	
			break;
		}

	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LayoutParams lp = (LayoutParams) mContentView
				.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LayoutParams lp = (LayoutParams) mContentView
				.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LayoutParams lp = (LayoutParams) mContentView
				.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LayoutParams lp = (LayoutParams) mContentView
				.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.ybt_listview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		imageView = (ImageView) moreView
				.findViewById(R.id.loadding_listview_image);
		setLoadStyle(loadStyle);

		this.setMinimumHeight(10);
	}

	public void setLoadStyle(int loadStyle) {
		this.loadStyle = loadStyle;
		if(loadStyle==0){
			imageView.setBackgroundResource(R.drawable.ybtlistview_load);
		}else{
			imageView.setBackgroundResource(R.drawable.ybtlistview_load_white);
		}
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		animationDrawable.start();
	}
}

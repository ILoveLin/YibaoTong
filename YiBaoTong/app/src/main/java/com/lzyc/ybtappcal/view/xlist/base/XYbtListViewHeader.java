package com.lzyc.ybtappcal.view.xlist.base;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lzyc.ybtappcal.R;

public class XYbtListViewHeader extends LinearLayout {
	private LinearLayout mContainer;
	private int mState = STATE_NORMAL;
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;

	public XYbtListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public XYbtListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度为0
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.ybt_listview_header, null);
		ImageView refresh_listview_image= (ImageView) mContainer.findViewById(R.id.refresh_listview_image);
		refresh_listview_image.setBackgroundResource(R.drawable.ybtlistview_refresh);
		AnimationDrawable animationDrawable = (AnimationDrawable) refresh_listview_image.getBackground();
		animationDrawable.start();
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);
	}

	public void setState(int state) {
		if (state == mState) return ;
		
		if (state == STATE_REFRESHING) {	// 显示进度
//			mArrowImageView.clearAnimation();
//			mArrowImageView.setVisibility(View.INVISIBLE);
		} else {	// 显示箭头图片
//			mArrowImageView.setVisibility(View.VISIBLE);
		}
		
		switch(state){
		case STATE_NORMAL:
			if (mState == STATE_READY) {
//				mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
//				mArrowImageView.clearAnimation();
			}
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
//				mArrowImageView.clearAnimation();
//				mArrowImageView.startAnimation(mRotateUpAnim);
			}
			break;
		case STATE_REFRESHING:
			break;
			default:
		}
		
		mState = state;
	}
	
	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}
	
	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

}

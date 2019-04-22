package com.lzyc.ybtappcal.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TabWidget;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;

/**
 * 设置未读此消息红点
 * 下载链接：https://github.com/stefanjauker/BadgeView
 * 参考链接：http://blog.csdn.net/crazy1235/article/details/42262369
 * Created by yang on 2016/9/14.
 */

public class BadgeView extends TextView {

	private boolean mHideOnNull = true;
	long a=~1;

	public BadgeView(Context context) {
		this(context, null);
	}

	public BadgeView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.textViewStyle);
	}

	public BadgeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		if (!(getLayoutParams() instanceof FrameLayout.LayoutParams)) {
			FrameLayout.LayoutParams layoutParams =
					new FrameLayout.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT,
							Gravity.RIGHT | Gravity.TOP);
			setLayoutParams(layoutParams);
		}

		setTextColor(Color.WHITE);
		setTypeface(Typeface.DEFAULT);
		setTextSize(TypedValue.COMPLEX_UNIT_SP, 8.2f);
		setGravity(Gravity.CENTER);
		setHideOnNull(true);
		setBadgeCount(0);
		getPaint().setAntiAlias(true);
		setIncludeFontPadding(false);
		setPadding(dip2Px(3.2f), dip2Px(0.5f), dip2Px(3.2f), dip2Px(0.5f));

		setBackground(9, getResources().getColor(R.color.color_f53838));
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void setBackground(int dipRadius, int badgeColor) {
		int radius = dip2Px(dipRadius);
		float[] radiusArray = new float[] { radius, radius, radius, radius, radius, radius, radius, radius };
		RoundRectShape roundRect = new RoundRectShape(radiusArray, null, null);
		ShapeDrawable bgDrawable = new ShapeDrawable(roundRect);
		bgDrawable.getPaint().setColor(badgeColor);
		setBackground(bgDrawable);
	}

	/**
	 * @return Returns true if view is hidden on badge value 0 or null;
	 */
	public boolean isHideOnNull() {
		return mHideOnNull;
	}

	/**
	 * @param hideOnNull the hideOnNull to set
	 */
	public void setHideOnNull(boolean hideOnNull) {
		mHideOnNull = hideOnNull;
		setText(getText());
	}

	/*
     * (non-Javadoc)
     *
     * @see android.widget.TextView#setText(java.lang.CharSequence, android.widget.TextView.BufferType)
     */
	@Override
	public void setText(CharSequence text, BufferType type) {
		if (isHideOnNull() && (text == null || text.toString().equalsIgnoreCase("0"))) {
			setVisibility(View.GONE);
		} else {
			setVisibility(View.VISIBLE);
		}
		super.setText(text, type);
	}

	public void setBadgeCount(int count) {
		setText(String.valueOf(count));
	}

	public void setBadgeCount(String countStr) {
		setText(countStr);
	}

	public Integer getBadgeCount() {
		if (getText() == null) {
			return null;
		}

		String text = getText().toString();
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public void setBadgeGravity(int gravity) {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
		params.gravity = gravity;
		setLayoutParams(params);
	}

	public int getBadgeGravity() {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
		return params.gravity;
	}

	public void setBadgeMargin(int dipMargin) {
		setBadgeMargin(dipMargin, dipMargin, dipMargin, dipMargin);
	}

	public void setBadgeMargin(int leftDipMargin, int topDipMargin, int rightDipMargin, int bottomDipMargin) {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
		params.leftMargin = dip2Px(leftDipMargin);
		params.topMargin = dip2Px(topDipMargin);
		params.rightMargin = dip2Px(rightDipMargin);
		params.bottomMargin = dip2Px(bottomDipMargin);
		setLayoutParams(params);
	}

	public int[] getBadgeMargin() {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
		return new int[] { params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin };
	}

	public void incrementBadgeCount(int increment) {
		Integer count = getBadgeCount();
		if (count == null) {
			setBadgeCount(increment);
		} else {
			setBadgeCount(increment + count);
		}
	}

	public void decrementBadgeCount(int decrement) {
		incrementBadgeCount(-decrement);
	}

	/*
     * Attach the BadgeView to the TabWidget
     *
     * @param target the TabWidget to attach the BadgeView
     *
     * @param tabIndex index of the tab
     */
	public void setTargetView(TabWidget target, int tabIndex) {
		View tabView = target.getChildTabViewAt(tabIndex);
		setTargetView(tabView);
	}

	/*
     * Attach the BadgeView to the target view
     *
     * @param target the view to attach the BadgeView
     */
	public void setTargetView(View target) {
		if (getParent() != null) {
			((ViewGroup) getParent()).removeView(this);
		}

		if (target == null) {
			return;
		}

		if (target.getParent() instanceof FrameLayout) {
			((FrameLayout) target.getParent()).addView(this);

		} else if (target.getParent() instanceof ViewGroup) {
			// use a new Framelayout container for adding badge
			ViewGroup parentContainer = (ViewGroup) target.getParent();
			int groupIndex = parentContainer.indexOfChild(target);
			parentContainer.removeView(target);

			FrameLayout badgeContainer = new FrameLayout(getContext());
			ViewGroup.LayoutParams parentLayoutParams = target.getLayoutParams();

			badgeContainer.setLayoutParams(parentLayoutParams);
			target.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

			parentContainer.addView(badgeContainer, groupIndex, parentLayoutParams);
			badgeContainer.addView(target);

			badgeContainer.addView(this);
		} else if (target.getParent() == null) {
			Log.e(getClass().getSimpleName(), "ParentView is needed");
		}

	}

	/*
     * converts dip to px
     */
	private int dip2Px(float dip) {
		return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
	}

}

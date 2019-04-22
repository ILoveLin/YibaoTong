package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.lzyc.ybtappcal.util.LogUtil;

/**
 * 软键盘事件监听
 * Created by yang on 2015/11/07.
 */
public class SoftKeyboardLayout extends RelativeLayout {

	public static final int KEYBOARD_STATE_INIT = 0x1;//初始化
	public static final int KEYBOARD_STATE_HIDE = 0x2;//隐藏
	public static final int KEYBOARD_STATE_SHOW = 0x3;//打开
	private boolean isInit=false;
	private boolean isKeybord=false;
	private int viewHeight;
	private onKeyboardsChangeListener keyboarddsChangeListener;

	public SoftKeyboardLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SoftKeyboardLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SoftKeyboardLayout(Context context) {
		super(context);
	}

	public void setOnkeyboarddStateListener(onKeyboardsChangeListener listener) {
		keyboarddsChangeListener = listener;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		LogUtil.y("##########SoftKeyboardLayout##########"+isInit);
		if (!isInit) {
			isInit = true;
			viewHeight = b;
			keyboardSateChange(KEYBOARD_STATE_INIT);
		} else {
			viewHeight = viewHeight < b ? b : viewHeight;
		}
		if (isInit && viewHeight > b) {
			isKeybord = true;
			keyboardSateChange(KEYBOARD_STATE_SHOW);
		}
		if (isInit && isKeybord && viewHeight == b) {
			isKeybord = false;
			keyboardSateChange(KEYBOARD_STATE_HIDE);
		}
	}

	public void keyboardSateChange(int state) {
		if (keyboarddsChangeListener != null) {
			keyboarddsChangeListener.onKeyBoardStateChange(state);
		}
	}

	public interface onKeyboardsChangeListener {
		 void onKeyBoardStateChange(int state);
	}
}
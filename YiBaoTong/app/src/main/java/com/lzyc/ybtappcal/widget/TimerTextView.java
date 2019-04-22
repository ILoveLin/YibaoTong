package com.lzyc.ybtappcal.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;

/**
 * 作者：xujm
 * 时间：2015/12/28
 * 备注：自动定时TextView，获取手机验证码
 */
public class TimerTextView extends TextView implements Runnable {

    private boolean isStart = false;// 是否启动
    private static Handler handler = new Handler();
    private static int time = 60;

    public TimerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerTextView(Context context) {
        super(context);
    }

    public void start() {
        if (!isStart) {
            // 开始
            handler.post(this);
            this.setText(time + "");
        }
    }

    @Override
    public void run() {
        if (time == 0) {
            setText("重新获取验证码");
            setTextColor(getResources().getColor(R.color.color_3e6398));
            setTextSize(12);
            time = 60;
            isStart = false;
            this.setEnabled(true);
        } else {
            isStart = true;
            handler.postDelayed(this, 1000);
            setTextColor(getResources().getColor(R.color.color_000000_80));
            setText(time-- + "s");
            this.setEnabled(false);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(this);
        time = 60;
    }

}

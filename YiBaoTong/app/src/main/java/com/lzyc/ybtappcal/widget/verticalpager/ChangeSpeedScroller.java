package com.lzyc.ybtappcal.widget.verticalpager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by abc on 2017/7/19.
 */

public class ChangeSpeedScroller extends Scroller {
    private int mDuration=200;

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public ChangeSpeedScroller(Context context){
        super(context);
    }
    public ChangeSpeedScroller(Context context, Interpolator interpolator){
        super(context,interpolator);
    }
    public ChangeSpeedScroller(Context context, Interpolator interpolator, boolean flywheel){
        super(context,interpolator,flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy);
    }
}
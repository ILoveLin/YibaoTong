package com.lzyc.ybtappcal.widget.verticalpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.animation.AccelerateInterpolator;

import com.lzyc.ybtappcal.adapter.HomeAttrDiscounterAdapter;

import java.lang.reflect.Field;

public class Proxy<T> {

    ViewPager viewPager;
    protected long mInterval = 4000;
    protected HomeAttrDiscounterAdapter mAdapter;
    protected LayoutInflater layoutInflater;
    PagerHandler pagerHandler;

    public Proxy(Context context, ViewPager viewPager, HomeAttrDiscounterAdapter pagerAdapter,CirclePageIndicator indicator) {
        layoutInflater = LayoutInflater.from(context);
        mAdapter = pagerAdapter;
        this.viewPager = viewPager;
        viewPager.setAdapter(mAdapter);
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ChangeSpeedScroller scroller = new ChangeSpeedScroller(viewPager.getContext(), new AccelerateInterpolator());
            scroller.setDuration(800);
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        indicator.setViewPager(viewPager, pagerHandler = new PagerHandler(viewPager, mInterval));
    }


    public void onBindView(int mSize) {
        pagerHandler.sendEmptyMessageDelayed(PagerHandler.MSG_UPDATE_IMAGE, PagerHandler.MSG_DELAY);

    }
}

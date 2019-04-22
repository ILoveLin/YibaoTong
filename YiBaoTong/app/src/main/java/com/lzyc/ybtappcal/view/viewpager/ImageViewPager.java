package com.lzyc.ybtappcal.view.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.lzyc.ybtappcal.util.LogUtil;

/**
 * 防止
 * 绘图的时候 多点触控 放大缩小 导致一场
 * 我在viewpager 里面  通过手势绘制图片 放大缩小 造成了这个bug
 * package_name com.lzyc.ybtappcal.view.viewpager
 * Created by yang on 2016/8/18.
 */
public class ImageViewPager extends ViewPager {
    private static final String TAG=ImageViewPager.class.getName();

   public ImageViewPager(Context context) {
        super(context);
    }

    public ImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean b = false;
        try {
            b = super.onInterceptTouchEvent(event);
        }catch (IllegalArgumentException e) {
            LogUtil.e(TAG,e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            LogUtil.e(TAG,e.getMessage());
        }catch(Exception e){
            LogUtil.e(TAG,e.getMessage());
        }
        return b; //网上看的方法是直接返回false，但是会导致ViewPager翻页有BUG
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

}

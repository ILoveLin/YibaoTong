package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lzyc.ybtappcal.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by lovelin on 16/8/25.
 */
public class HosptialGoCommunityViewPager extends PagerAdapter {

    private Context mContext;
    private ViewPager mViewPager;
    private ArrayList<ImageView> imageViewList;
    private boolean isOne;



    public HosptialGoCommunityViewPager(Context context, ArrayList<ImageView> mDatas, ViewPager viewPager, boolean isOne) {
        this.mContext = context;
        this.imageViewList = mDatas;
        this.mViewPager = viewPager;
        this.isOne = isOne;
    }

    @Override
    public int getCount() {
        if (isOne) {
            return 1;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View imageView;
        if (isOne) {
            imageView = imageViewList.get(position);
            mViewPager.addView(imageView);
        } else {
            imageView = imageViewList.get(position % imageViewList.size());
            mViewPager.addView(imageView);
        }
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (isOne) {
            mViewPager.removeView(imageViewList.get(position));
        } else {
            mViewPager.removeView(imageViewList.get(position % imageViewList.size()));
        }
    }
}

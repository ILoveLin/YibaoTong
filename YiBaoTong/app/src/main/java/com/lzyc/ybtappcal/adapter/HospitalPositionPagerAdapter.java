package com.lzyc.ybtappcal.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.lzyc.ybtappcal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 医院位置
 * Created by yang on 2016/11/03.
 */
public class HospitalPositionPagerAdapter extends PagerAdapter {
    private List<View> mDataViews;

    public HospitalPositionPagerAdapter(List<View> list){
        this.mDataViews=list;
    }
    @Override
    public int getCount() {
        if(mDataViews==null){
            mDataViews=new ArrayList<View>();
        }
        return mDataViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=mDataViews.get(position);
        view.findViewById(R.id.item_hp_pager_).setVisibility(View.GONE);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view=mDataViews.get(position);
        container.removeView(view);
    }
}

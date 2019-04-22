package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


import com.lzyc.ybtappcal.fragment.ViewPagerItemFragment;

import java.util.List;

import ybt.library.indicator.adapter.BaseCycleFragmentStatePagerAdapter;

/**
 * Created by yang on 16/10/31.
 */
public class CycleViewStatePagerAdapter extends BaseCycleFragmentStatePagerAdapter<String> {

    private final Context mContext;

    private boolean isRound;

    public void setRound(boolean round){
        isRound = round;
    }

    public CycleViewStatePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    public CycleViewStatePagerAdapter(final Context context, FragmentManager fragmentManager, List<String> listPath) {
        super(fragmentManager, listPath);
        this.mContext = context;
    }

    @Override
    protected Fragment getItemFragment(String item, int position) {

        return ViewPagerItemFragment.instantiateWithArgs(mContext, item, isRound);
    }
}

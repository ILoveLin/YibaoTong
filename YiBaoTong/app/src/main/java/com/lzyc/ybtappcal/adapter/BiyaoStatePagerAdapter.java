package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.lzyc.ybtappcal.bean.TopBanner;
import com.lzyc.ybtappcal.fragment.ViewPagerItemBiyaoFragment;

import java.util.List;

import ybt.library.indicator.adapter.BaseCycleFragmentStatePagerAdapter;

/**
 * 比药广告轮播图
 * Created by yang on 16/10/31.
 */
public class BiyaoStatePagerAdapter extends BaseCycleFragmentStatePagerAdapter<TopBanner.DataBean.ImagesBean> {

    private final Context mContext;

    public BiyaoStatePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    public BiyaoStatePagerAdapter(final Context context, FragmentManager fragmentManager, List<TopBanner.DataBean.ImagesBean> listImage) {
        super(fragmentManager, listImage);
        this.mContext = context;
    }

    @Override
    protected Fragment getItemFragment(TopBanner.DataBean.ImagesBean item, int position) {
        return ViewPagerItemBiyaoFragment.instantiateWithArgs(mContext, item);
    }
}

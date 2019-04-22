package com.lzyc.ybtappcal.activity.payment.order;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.fragment.OrderMineFragment;
import com.lzyc.ybtappcal.view.viewpager.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;

/**
 * 我的订单
 *  * Created by yang on 2017/05/03.
 */
public class OrderMineActivity extends BaseActivity implements OnTabSelectListener{

    @BindView(R.id.tablayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    NoScrollViewPager mViewPager;
    @BindArray(R.array.order_mine_titles)
    String[] strTitles;
    @BindString(R.string.mine_order_title)
    String titleName;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private OrderMinePagerAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_mine;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void init() {
        setTitleName(titleName);
        for (int i = 0; i < strTitles.length; i++) {
            mFragments.add(OrderMineFragment.getInstance(strTitles[i],i));
        }
        mViewPager.setNoScroll(true);
        mAdapter = new OrderMinePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mViewPager);
        mTabLayout.setOnTabSelectListener(this);
        mTabLayout.setViewPager(mViewPager, strTitles, this, mFragments);
    }

    @Override
    public void onTabSelect(int position) {
        mAdapter.getItemPosition(position);
    }

    @Override
    public void onTabReselect(int position) {
    }

    private class OrderMinePagerAdapter extends FragmentPagerAdapter {
        public OrderMinePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return strTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            int tabValue= (int) object;
            OrderMineFragment or= (OrderMineFragment) mFragments.get(tabValue);
            or.onRefresh();
            return tabValue;
        }
    }

}
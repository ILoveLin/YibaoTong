package com.lzyc.ybtappcal.activity.payment.order;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.OrderBuyPagerAdapter;
import com.lzyc.ybtappcal.app.ActivityCollector;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.fragment.InstructionsFragment;
import com.lzyc.ybtappcal.fragment.OrderBuyFragment;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.umeng.socialize.UMShareAPI;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 购买药品
 * Created by lxx on 2017/04/27.
 *
 * 1、药品商品名+药品通用名+规格
 * 2、售价￥38.99，换行医保通【返现】￥25.88
 * 3、药盒图片
 * 4、医保通logo
 *
 */

public class OrderbyActivity extends BaseActivity {

    @BindView(R.id.ib_left_title)
    ImageButton ibLeftTitle;
    @BindView(R.id.rel_content)
    RelativeLayout relContent;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.tv_title_goods)
    TextView tvTitle;

    OrderBuyPagerAdapter mAdapter;

    OrderBuyFragment orderBuyFragment;
    InstructionsFragment instructionsFragment;

    Bundle bundle;

    TabLayout.Tab tabGoods, tabInstructions;

    TextView tvGoods, tvInstructions;

    String DKID;
    String UID;

    @Override
    public int getContentViewId() {
        return R.layout.activity_orderby;
    }

    @Override
    protected void init() {
        setTitleBarVisibility(View.GONE);

        setPageStateView(relContent);

        ActivityCollector.getInstance().activityOrderAbout(this);

        initData();

        initTablayout();

        initArguments();

        mAdapter = new OrderBuyPagerAdapter(getSupportFragmentManager());

        mAdapter.addFragment(orderBuyFragment);

        initViewPager();

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(Contants.ACTION_ORDER_INSTRU_REFRESH);

        registerReceiver(receiver, intentFilter);
    }

    private void initTab(){

        View vGoods = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_order_buy, null, false);

        View vInstructions = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_order_buy, null, false);

        tvGoods = (TextView) vGoods.findViewById(R.id.tv_title);

        tvInstructions = (TextView) vInstructions.findViewById(R.id.tv_title);

        tvGoods.setTextSize(17);

        tvGoods.setText("商品");

        tvInstructions.setText("说明书");

        tabGoods = tabLayout.newTab().setCustomView(vGoods);

        tabInstructions = tabLayout.newTab().setCustomView(vInstructions);

    }

    private void initTablayout(){

        initTab();

        tabLayout.addTab(tabGoods);

        tabLayout.addTab(tabInstructions);

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                viewpager.setCurrentItem(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initData(){
        if (null != getIntent().getExtras()) {
            bundle = getIntent().getExtras();
        }

        DKID = bundle.getString(HttpConstant.SHOP_GOODS_PARAM_DKID);

        UID = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "");

        orderBuyFragment = new OrderBuyFragment();
        instructionsFragment = new InstructionsFragment();
    }

    private void initArguments(){
        Bundle bundleOrder = new Bundle();

        bundleOrder.putString(SharePreferenceUtil.UID, UID);
        bundleOrder.putString(HttpConstant.SHOP_GOODS_PARAM_DKID, DKID);
        orderBuyFragment.setArguments(bundleOrder);
    }

    private void initViewPager(){

        viewpager.setAdapter(mAdapter);

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(0 == position){
                    tabGoods.select();
                    tvGoods.setTextSize(17);
                    tvInstructions.setTextSize(16);
                } else if(1 == position){
                    tabInstructions.select();
                    tvGoods.setTextSize(16);
                    tvInstructions.setTextSize(17);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.ib_left_title})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.ib_left_title:
                finish();
                break;

        }
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(Contants.ACTION_ORDER_INSTRU_REFRESH == intent.getAction()){

                String instrUrl = intent.getStringExtra("url");

                if(TextUtils.isEmpty(instrUrl)) return;

                tvTitle.setVisibility(View.GONE);

                tabLayout.setVisibility(View.VISIBLE);

                mAdapter.addFragment(instructionsFragment);

                Bundle bundleOrder = new Bundle();

                bundleOrder.putString("url", instrUrl);

                instructionsFragment.setArguments(bundleOrder);

                mAdapter.notifyDataSetChanged();
            }

        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        UMShareAPI.get(this).release();
        ActivityCollector.getInstance().removeActivityOrderAbout(this);
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}

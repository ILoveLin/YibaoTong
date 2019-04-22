package com.lzyc.ybtappcal.activity.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.fragment.BrowseFragment;
import com.lzyc.ybtappcal.view.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 浏览记录
 * Created by yang on 2017/05/24.
 */
public class BrowseHistoryActivity extends BaseActivity {

    public static final int ACTION_GOODS_EDIT = 100;
    public static final int ACTION_GOODS_COMPLETE = 101;
    public static final int ACTION_DRUG_EDIT = 110;
    public static final int ACTION_DRUG_COMPLETE = 111;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.rel_all)
    RelativeLayout relAll;
    @BindArray(R.array.browser_history_titles)
    String[] strTitles;
    @BindView(R.id.tablayout)
    SlidingTabLayout mTablayout;
    @BindView(R.id.ib_titlebar_left)
    ImageButton ibTtilebarLeft;
    @BindView(R.id.tv_titlebar_edit)
    TextView tvTitlebarEdit;
    @BindView(R.id.line_title)
    View lineTitle;
    @BindView(R.id.tv_titlebar_clear)
    TextView tvTitlebarClear;
    @BindView(R.id.iv_selected)
    ImageView ivSelected;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.rel_bottom)
    RelativeLayout relBottom;
    @BindString(R.string.edit)
    String edit;
    @BindString(R.string.complete)
    String complete;

    private int tabValue = 0;
    private boolean isEmptyGoods = false;
    private boolean isEmptyDrug = false;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private BrowseHistoryPagerAdapter mAdapter;
    private int actionTypeGoods = ACTION_GOODS_EDIT;
    private int actionTypeDrug = ACTION_DRUG_EDIT;
    private boolean isSelecteActionGoods = false;
    private boolean isSelecteActionDrug = false;

    @Override
    public int getContentViewId() {
        return R.layout.activity_browse_history;
    }

    @Override
    protected void init() {
        setTitleBarVisibility(View.GONE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contants.ACTION_NAME_BROWSE_SELECTED);
        intentFilter.addAction(Contants.ACTION_NAME_BROWSE);
        registerReceiver(receiver, intentFilter);
        setPageStateView();
        for (int i = 0; i < strTitles.length; i++) {
            BrowseFragment browseFragment=new BrowseFragment();
            browseFragment.setTabvalue(i);
            mFragments.add(browseFragment);
        }
        mAdapter = new BrowseHistoryPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTablayout.setViewPager(mViewPager);
        mTablayout.setViewPager(mViewPager, strTitles, this, mFragments);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabValue = position;
                if (position == 0) {
                    if (!isEmptyGoods) {
                        relBottom.setVisibility(View.VISIBLE);
                        tvTitlebarClear.setVisibility(View.VISIBLE);
                        tvTitlebarEdit.setVisibility(View.VISIBLE);
                        lineTitle.setVisibility(View.VISIBLE);
                    }
                    if (actionTypeGoods == ACTION_GOODS_EDIT) {
                        tvTitlebarEdit.setText(edit);
                        lineTitle.setVisibility(View.VISIBLE);
                        tvTitlebarClear.setVisibility(View.VISIBLE);
                        relBottom.setVisibility(View.GONE);
                        relBottom.setClickable(false);
                    } else if (actionTypeGoods == ACTION_GOODS_COMPLETE) {
                        tvTitlebarEdit.setText(complete);
                        lineTitle.setVisibility(View.GONE);
                        tvTitlebarClear.setVisibility(View.GONE);
                        relBottom.setVisibility(View.VISIBLE);
                        relBottom.setClickable(true);
                    }
                    ivSelected.setSelected(isSelecteActionGoods);
                    if (isEmptyGoods) {
                        relBottom.setVisibility(View.GONE);
                        tvTitlebarClear.setVisibility(View.GONE);
                        tvTitlebarEdit.setVisibility(View.GONE);
                        lineTitle.setVisibility(View.GONE);
                    }
                    requestEventCode("a-5001");//买药浏览
                } else {
                    requestEventCode("a-5002");//查药浏览
                    if (!isEmptyDrug) {
                        relBottom.setVisibility(View.VISIBLE);
                        tvTitlebarClear.setVisibility(View.VISIBLE);
                        tvTitlebarEdit.setVisibility(View.VISIBLE);
                        lineTitle.setVisibility(View.VISIBLE);
                    }
                    if (actionTypeDrug == ACTION_DRUG_EDIT) {
                        tvTitlebarEdit.setText(edit);
                        lineTitle.setVisibility(View.VISIBLE);
                        tvTitlebarClear.setVisibility(View.VISIBLE);
                        relBottom.setVisibility(View.GONE);
                        relBottom.setClickable(false);
                    } else if (actionTypeDrug == ACTION_DRUG_COMPLETE) {
                        tvTitlebarEdit.setText(complete);
                        lineTitle.setVisibility(View.GONE);
                        tvTitlebarClear.setVisibility(View.GONE);
                        relBottom.setVisibility(View.VISIBLE);
                        relBottom.setClickable(true);
                    }
                    ivSelected.setSelected(isSelecteActionDrug);
                    if (isEmptyDrug) {
                        relBottom.setVisibility(View.GONE);
                        tvTitlebarClear.setVisibility(View.GONE);
                        tvTitlebarEdit.setVisibility(View.GONE);
                        lineTitle.setVisibility(View.GONE);
                    }
                }
                mAdapter.getItemPosition(position);

//                mTablayout.updateTabStyles();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.ib_titlebar_left, R.id.tv_titlebar_edit, R.id.tv_titlebar_clear, R.id.iv_selected, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_left:
                BrowseHistoryActivity.this.finish();
                break;
            case R.id.tv_titlebar_edit://编辑按钮
                if (tabValue == 0) {
                    if (actionTypeGoods == ACTION_GOODS_EDIT) {
                        tvTitlebarEdit.setText(complete);
                        lineTitle.setVisibility(View.GONE);
                        tvTitlebarClear.setVisibility(View.GONE);
                        actionTypeGoods = ACTION_GOODS_COMPLETE;
                        relBottom.setVisibility(View.VISIBLE);
                        requestEventCode("a-5004");
                    } else if (actionTypeGoods == ACTION_GOODS_COMPLETE) {
                        tvTitlebarEdit.setText(edit);
                        lineTitle.setVisibility(View.VISIBLE);
                        tvTitlebarClear.setVisibility(View.VISIBLE);
                        actionTypeGoods = ACTION_GOODS_EDIT;
                        relBottom.setVisibility(View.GONE);
                        requestEventCode("a-5006");
                    }
                    ivSelected.setSelected(isSelecteActionGoods);
                    mAdapter.onEditAction(tabValue, actionTypeGoods);
                } else {
                    if (actionTypeDrug == ACTION_DRUG_EDIT) {
                        tvTitlebarEdit.setText(complete);
                        lineTitle.setVisibility(View.GONE);
                        tvTitlebarClear.setVisibility(View.GONE);
                        actionTypeDrug = ACTION_DRUG_COMPLETE;
                        relBottom.setVisibility(View.VISIBLE);
                        requestEventCode("a-5004");
                    } else if (actionTypeDrug == ACTION_DRUG_COMPLETE) {
                        tvTitlebarEdit.setText(edit);
                        lineTitle.setVisibility(View.VISIBLE);
                        tvTitlebarClear.setVisibility(View.VISIBLE);
                        actionTypeDrug = ACTION_DRUG_EDIT;
                        relBottom.setVisibility(View.GONE);
                        requestEventCode("a-5006");
                    }
                    ivSelected.setSelected(isSelecteActionDrug);
                    mAdapter.onEditAction(tabValue, actionTypeDrug);
                }
                break;
            case R.id.tv_titlebar_clear://清空按钮
                requestEventCode("a-5003");
                mAdapter.onDeleteAction(tabValue);
                break;
            case R.id.iv_selected://全选按钮
                requestEventCode("a-5005");
                if (tabValue == 0) {
                    if (ivSelected.isSelected()) {
                        isSelecteActionGoods = false;
                    } else {
                        isSelecteActionGoods = true;
                    }
                    ivSelected.setSelected(isSelecteActionGoods);
                    mAdapter.onSelectedAction(tabValue, actionTypeGoods, isSelecteActionGoods);
                } else {
                    if (ivSelected.isSelected()) {
                        isSelecteActionDrug = false;
                    } else {
                        isSelecteActionDrug = true;
                    }
                    ivSelected.setSelected(isSelecteActionDrug);
                    mAdapter.onSelectedAction(tabValue, actionTypeDrug, isSelecteActionDrug);
                }
                break;
            case R.id.tv_delete:
                mAdapter.onDeleteActionSome(tabValue);
                break;
        }
    }

    private class BrowseHistoryPagerAdapter extends FragmentPagerAdapter {
        public BrowseHistoryPagerAdapter(FragmentManager fm) {
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
            int tabValue = (int) object;
            BrowseFragment browseFragment = (BrowseFragment) mFragments.get(tabValue);
            browseFragment.onRefresh(tabValue);
            return tabValue;
        }

        public void onEditAction(int tabValue, int actionType) {
            BrowseFragment browseFragment = (BrowseFragment) mFragments.get(tabValue);
            browseFragment.onEditAction(tabValue, actionType);
            notifyDataSetChanged();
        }

        public void onSelectedAction(int tabValue, int actionType, boolean isSelected) {
            BrowseFragment browseFragment = (BrowseFragment) mFragments.get(tabValue);
            browseFragment.onSelectedAction(tabValue, actionType, isSelected);
            notifyDataSetChanged();
        }

        public void onDeleteAction(int tabValue) {
            BrowseFragment browseFragment = (BrowseFragment) mFragments.get(tabValue);
            browseFragment.onDeleteAction(tabValue);
            notifyDataSetChanged();
        }

        public void onDeleteActionSome(int tabValue) {
            requestEventCode("a-5007");
            BrowseFragment browseFragment = (BrowseFragment) mFragments.get(tabValue);
            browseFragment.onDeleteActionSome(tabValue);
            notifyDataSetChanged();
        }

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Contants.ACTION_NAME_BROWSE_SELECTED)) {
                boolean isSelected = intent.getBooleanExtra("isSelected", false);
                if (tabValue == 0) {
                    isSelecteActionGoods = isSelected;
                } else {
                    isSelecteActionDrug = isSelected;
                }
                ivSelected.setSelected(isSelected);
            } else if (intent.getAction().equals(Contants.ACTION_NAME_BROWSE)) {
                int count = intent.getIntExtra(Contants.KEY_COUNT, 0);
                int tabValue = intent.getIntExtra(Contants.KEY_TABVALUE, 0);
                if (count == 0&&tabValue==0) {
                    tvTitlebarClear.setVisibility(View.GONE);
                    tvTitlebarEdit.setVisibility(View.GONE);
                    lineTitle.setVisibility(View.GONE);
                    isEmptyGoods = true;
                } else if (count == 0&&tabValue==1) {
                    tvTitlebarClear.setVisibility(View.GONE);
                    tvTitlebarEdit.setVisibility(View.GONE);
                    lineTitle.setVisibility(View.GONE);
                    isEmptyDrug = true;
                }  else if (count > 0&&tabValue==0) {
                    tvTitlebarClear.setVisibility(View.VISIBLE);
                    tvTitlebarEdit.setVisibility(View.VISIBLE);
                    lineTitle.setVisibility(View.VISIBLE);
                    isEmptyGoods = false;
                }else if (count > 0&&tabValue==1){
                    lineTitle.setVisibility(View.VISIBLE);
                    tvTitlebarEdit.setVisibility(View.VISIBLE);
                    tvTitlebarClear.setVisibility(View.VISIBLE);
                    isEmptyDrug = false;
                }
                relBottom.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}

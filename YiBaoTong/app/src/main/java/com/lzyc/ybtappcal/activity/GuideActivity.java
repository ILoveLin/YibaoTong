package com.lzyc.ybtappcal.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.view.viewpager.ImageViewPager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 引导页
 * @author yang
 */
public class GuideActivity extends BaseActivity {
    private static final String TAG=GuideActivity.class.getSimpleName();

    @BindView(R.id.iv_enter)
    ImageView ivEnter;
    @BindView(R.id.viewpager)
    ImageViewPager mViewPager;
    private ArrayList<ImageView> mImageViewList;
    private int[] mImageIds = new int[]{R.drawable.image_guide_1, R.drawable.image_guide_2,R.drawable.image_guide_3};

    @Override
    public int getContentViewId() {
        return R.layout.activity_guide;
    }

    @Override
    public void init() {
        setTitleBarVisibility(View.GONE);
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    private void initListener() {
        mViewPager.setAdapter(new GuideAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == mImageViewList.size() - 1) {
                    ivEnter.setVisibility(View.VISIBLE);
                } else {
                    ivEnter.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ivEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferenceUtil.put(GuideActivity.this, SharePreferenceUtil.IS_FIRST, false);
//                SharePreferenceUtil.put(GuideActivity.this, SharePreferenceUtil.PROVINCE, "北京");

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    private void initData() {
        mImageViewList = new ArrayList<ImageView>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setImageResource(mImageIds[i]);
            mImageViewList.add(view);
        }
    }


    public class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mImageViewList == null) {
                mImageViewList = new ArrayList<ImageView>();
            }
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

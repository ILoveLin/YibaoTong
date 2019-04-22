package com.lzyc.ybtappcal.activity.top;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.ResultsNayaoPagerAdapter;
import com.lzyc.ybtappcal.bean.BYDrugDetailBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.fragment.adopt.QiYeFragment;
import com.lzyc.ybtappcal.fragment.adopt.ShuoMingShuFragment;
import com.lzyc.ybtappcal.fragment.adopt.YBInfoFragment;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.StringUtils;
import com.lzyc.ybtappcal.util.Util;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.popupwindow.lib.DensityUtil;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 药品详情界面
 */

public class ResultsDetailActivity extends BaseActivity {
    private static final String TAG=ResultsDetailActivity.class.getSimpleName();
    public static final String VAL_DEFAULT_TYPE_JOB = "ZZ00";
    @BindView(R.id.nayao_detail_vp)
    ViewPager nayao_detail_vp;
    @BindView(R.id.tv_drug_sign)
    TextView tvDrugSign;
    @BindView(R.id.tv_hosp_price)
    TextView tvHospPrice;
    @BindView(R.id.tv_drug_price)
    TextView tv_drug_price;
    @BindView(R.id.tv_vender)
    TextView tv_vender;
    @BindView(R.id.tv_drug_detail_name)
    TextView tv_drug_detail_name;
    @BindView(R.id.img_drug_type)
    ImageView img_drug_type;
    @BindView(R.id.results_top_layout)
    RelativeLayout results_top_layout;
    @BindView(R.id.results_detail_image)
    ImageView results_detail_image;
    @BindView(R.id.nayao_detail_anim)
    TextView nayao_detail_anim;
    @BindView(R.id.nayao_detail_yibaoinfo)
    TextView nayao_detail_yibaoinfo;
    @BindView(R.id.nayao_detail_shuomingshu)
    TextView nayao_detail_shuomingshu;
    @BindView(R.id.nayao_detail_qiye)
    TextView nayao_detail_qiye;
    @BindView(R.id.results_detail_drugname_center)
    LinearLayout results_detail_drugname_center;
    @BindView(R.id.results_detail_linear_bottom)
    LinearLayout results_detail_linear_bottom;
    @BindView(R.id.results_detail_linear)
    LinearLayout results_detail_linear;
    String drugID;
    String typeJob;
    BYDrugDetailBean.DataBean drugDetailBean;
    String ybType;
    RelativeLayout.LayoutParams layoutParams;
    int width;

    int typePage;
    View mRootView;
    boolean isFirstEnter=true;

    boolean isClose;

    String currentProvince;

    YBInfoFragment ybInfoFragment;
    ShuoMingShuFragment shuoMingShuFragment;
    QiYeFragment qiYeFragment;

    @Override
    public int getContentViewId() {
        return R.layout.activity_results_detail_biyao;
    }

    @Override
    public void init() {

        currentProvince = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "北京");

        setTitleName("药品详情");

        setPageStateView();

        showLoading();

        initData();

        requestDrugDetails();

        initFragment();

//        startAnimation();
    }

    private void initFragment(){
        ybInfoFragment=new YBInfoFragment();
        shuoMingShuFragment=new ShuoMingShuFragment();
        qiYeFragment = new QiYeFragment();
    }

    private void initArguments(){
        Bundle bundle=new Bundle();
        bundle.putSerializable("drugdetailbean",drugDetailBean);
        bundle.putString("Province", currentProvince);
        ybInfoFragment.setArguments(bundle);

        bundle=new Bundle();
        bundle.putString("url",drugDetailBean.getDetail().getSpecification());
        shuoMingShuFragment.setArguments(bundle);

        bundle=new Bundle();
        if(!TextUtils.isEmpty(drugDetailBean.getDetail().getVenderDetailUrl())){
            bundle.putString("url",drugDetailBean.getDetail().getVenderDetailUrl());
        }else{
            bundle.putString("url","");
        }

        qiYeFragment.setArguments(bundle);
    }

    private void initData() {
        drugID = getIntent().getStringExtra("drug_id");
        typePage = getIntent().getIntExtra(Contants.KEY_PAGE, typePage);
        typeJob = (String) SharePreferenceUtil.get(ResultsDetailActivity.this, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, VAL_DEFAULT_TYPE_JOB);
        width=(getWindowManager().getDefaultDisplay().getWidth()- DensityUtil.dip2px(this,48))/3;
        layoutParams= (RelativeLayout.LayoutParams) nayao_detail_anim.getLayoutParams();
        layoutParams.width=width;
        layoutParams.leftMargin=DensityUtil.dip2px(this,24);
        nayao_detail_anim.setLayoutParams(layoutParams);
    }

    private void setUpView() {
        if (nayao_detail_vp != null) {
            setupViewPager(nayao_detail_vp);
        }

        showContent();
    }

    private void setupViewPager(ViewPager viewPager) {
        ResultsNayaoPagerAdapter adapter = new ResultsNayaoPagerAdapter(getSupportFragmentManager());

        initArguments();

        adapter.addFragment(shuoMingShuFragment);
        adapter.addFragment(ybInfoFragment);
        adapter.addFragment(qiYeFragment);

        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int leftMargin = DensityUtil.dip2px(ResultsDetailActivity.this,24)+(int) (width * positionOffset) + position * width;

                layoutParams.leftMargin = leftMargin;

                nayao_detail_anim.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                changeTextColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int position) {
            }
        });
    }

    private void changeTextColor(int position) {

        nayao_detail_yibaoinfo.setTextColor(getResources().getColor(R.color.color_777777));
        nayao_detail_shuomingshu.setTextColor(getResources().getColor(R.color.color_777777));
        nayao_detail_qiye.setTextColor(getResources().getColor(R.color.color_777777));
        nayao_detail_shuomingshu.setTextSize(15);
        nayao_detail_yibaoinfo.setTextSize(15);
        nayao_detail_qiye.setTextSize(15);

        switch(position){
            case 0:
                nayao_detail_shuomingshu.setTextColor(getResources().getColor(R.color.color_000000));
                nayao_detail_shuomingshu.setTextSize(16);
                break;
            case 1:
                nayao_detail_yibaoinfo.setTextColor(getResources().getColor(R.color.color_000000));
                nayao_detail_yibaoinfo.setTextSize(16);
                break;
            case 2:
                nayao_detail_qiye.setTextColor(getResources().getColor(R.color.color_000000));
                nayao_detail_qiye.setTextSize(16);
                break;
        }
    }

    private void fullInfo() {
        if (drugDetailBean == null) {
            return;
        }

        ybType = drugDetailBean.getDetail().getYyType();
        String goodsName = drugDetailBean.getDetail().getGoodsName();
        String name = drugDetailBean.getDetail().getName();
        String specifications = drugDetailBean.getDetail().getSpecifications();

        double price = 0;
        try{
            price = Double.parseDouble(drugDetailBean.getDetail().getPrice());

        } catch (Exception e){
            e.printStackTrace();
        }

        if(0 == price){
            tv_drug_price.setText("暂无报价");
            tvDrugSign.setVisibility(View.GONE);
            tvHospPrice.setVisibility(View.GONE);
        } else {
            tv_drug_price.setText(Util.getFloatDotStr(String.valueOf(price)));
        }

        tv_vender.setText(drugDetailBean.getDetail().getVender());

        if (TextUtils.isEmpty(goodsName)) {
            tv_drug_detail_name.setText(StringUtils.getSpannableText(name + " ", specifications, 13));
        } else {
            tv_drug_detail_name.setText(StringUtils.getSpannableText(goodsName + " " + name + " ", specifications, 13));
        }

        if("甲类".equals(ybType)){
            Picasso.with(mContext).load(R.mipmap.icon_jieguo_jialei).into(img_drug_type);
        }else if("自费".equals(ybType)) {
            Picasso.with(mContext).load(R.mipmap.icon_jieguo_zifei).into(img_drug_type);
        } else {
            Picasso.with(mContext).load(R.mipmap.icon_jieguo_yilei).into(img_drug_type);
        }

        String imagePath = drugDetailBean.getDetail().getImage();
        if (imagePath != null) {
            Picasso.with(ResultsDetailActivity.this).load(imagePath)
                    .placeholder(R.mipmap.image_empty_baner)
                    .error(R.mipmap.image_empty_baner)
                    .into(results_detail_image);
        }

        setUpView();
    }

    @OnClick({R.id.nayao_detail_yibaoinfo, R.id.nayao_detail_shuomingshu, R.id.nayao_detail_qiye})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.nayao_detail_yibaoinfo:
                requestEventCode("h001");
                nayao_detail_vp.setCurrentItem(1);
                break;

            case R.id.nayao_detail_shuomingshu:
                requestEventCode("h003");
                nayao_detail_vp.setCurrentItem(0);
                break;

            case R.id.nayao_detail_qiye:
                requestEventCode("h002");
                nayao_detail_vp.setCurrentItem(2);
                break;
        }
    }

    /**
     * 药品详情
     */
    public void requestDrugDetails() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "Home");
        params.put("class", "DrugsDetails3");
        String sign = MD5ChangeUtil.Md5_32("HomeDrugsDetails3" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("DrugID", drugID);
        params.put("UID", SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString());

        if (typePage == Contants.VAL_PAGE_MINEPLAN) {
            params.put("Province", "北京");
        } else {
            params.put("Province", currentProvince);
        }

        request(params, HttpConstant.TOP_DRUG_DETAIL);

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

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.TOP_DRUG_DETAIL:
                try {
                    drugDetailBean = JsonUtil.getModle(response.getJSONObject("data").toString(), BYDrugDetailBean.DataBean.class);
                    fullInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onClickRetry() {
        super.onClickRetry();

        showLoading();
        requestDrugDetails();
    }

    @Override
    public void error(String errorMsg) {
        super.error(errorMsg);
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);

            if (errorResponse.isNetError()) {
                showError();
            } else {
                showToast(errorResponse.getMsg());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void startAnimation(){
//        mRootView = getWindow().getDecorView();
//        float touchY = getIntent().getFloatExtra("touchY", mRootView.getHeight() / 2);
//        startRootAnimation(mRootView, touchY);
//    }
//
//    /**
//     * 启动动画
//     * @param rootView
//     */
//    private void startRootAnimation(final View rootView, final float touchY) {
//        if (isFirstEnter) { //只初始化的时候启动一次
//            isFirstEnter=false;
//            mRootView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                @Override
//                public boolean onPreDraw() {
//                    rootView.getViewTreeObserver().removeOnPreDrawListener(this);
//                    rootView.setScaleY(0f);
//                    rootView.setPivotY(touchY);
//
//                    rootView.animate()
//                            .scaleY(1)
//                            .setDuration(400)
//                            .setListener(new AnimatorListenerAdapter() {
//                                @Override
//                                public void onAnimationEnd(Animator animation) {
//                                    super.onAnimationEnd(animation);
//                                    startChildAnim();
//                                }
//                            }).start();
//                    return true;
//                }
//            });
//        }
//    }

//    /**
//     * 内容启动动画
//     */
//    private void startChildAnim() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Animation animTop = AnimationUtils.loadAnimation(ResultsDetailActivity.this,R.anim.push_detail_top_in);
//                Animation animBottom = AnimationUtils.loadAnimation(ResultsDetailActivity.this,R.anim.push_detail_bottom_in);
//                Animation animRight = AnimationUtils.loadAnimation(ResultsDetailActivity.this,R.anim.push_detail_right_in);
//
//                results_top_layout.startAnimation(animTop);
//                results_detail_drugname_center.startAnimation(animRight);
//                results_detail_linear_bottom.startAnimation(animBottom);
//
//                results_top_layout.setVisibility(View.VISIBLE);
//                results_detail_drugname_center.setVisibility(View.VISIBLE);
//                results_detail_linear_bottom.setVisibility(View.VISIBLE);
//            }
//        },500);
//    }
//
//    private void closeActivity() {
//
//        if(isClose) return;
//
//        Animation animTopOut = AnimationUtils.loadAnimation(ResultsDetailActivity.this,R.anim.push_detail_top_out);
//        Animation animBottomOut = AnimationUtils.loadAnimation(ResultsDetailActivity.this,R.anim.push_detail_bottom_out);
//        Animation animRightOut = AnimationUtils.loadAnimation(ResultsDetailActivity.this,R.anim.push_detail_right_out);
//
//        results_top_layout.startAnimation(animTopOut);
//        results_detail_drugname_center.startAnimation(animRightOut);
//        results_detail_linear_bottom.startAnimation(animBottomOut);
//        animRightOut.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                isClose = true;
//            }
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                results_top_layout.setVisibility(View.GONE);
//                results_detail_drugname_center.setVisibility(View.GONE);
//                results_detail_linear_bottom.setVisibility(View.GONE);
//                float touchY = getIntent().getFloatExtra("touchY", mRootView.getHeight() / 2);
//                endRootAnimation(mRootView, touchY);
//            }
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//    }

//    /**
//     * 退出动画
//     * @param rootView
//     * @param touchY
//     */
//    private void endRootAnimation(View rootView, float touchY) {
//        rootView.setPivotY(touchY);
//        rootView.animate()
//                .scaleY(0)
//                .setDuration(400)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        finish();
//                    }
//                }).start();
//    }

//    @Override
//    public void onBackPressed() {
//
//        if(null == drugDetailBean){
//            super.onBackPressed();
//            return;
//        }
//
//        closeActivity();
//    }

}

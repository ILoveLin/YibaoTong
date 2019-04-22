//package com.lzyc.ybtappcal.fragment;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.graphics.Bitmap;
//import android.os.Build;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.android.pc.ioc.event.EventBus;
//import com.android.pc.ioc.inject.InjectAll;
//import com.android.pc.ioc.inject.InjectBinder;
//import com.android.pc.ioc.view.listener.OnClick;
//import com.android.pc.util.Handler_Inject;
//import com.lzyc.ybtappcal.R;
//import com.lzyc.ybtappcal.activity.SearchActivity;
//import com.lzyc.ybtappcal.activity.base.BaseFragment;
//import com.lzyc.ybtappcal.bean.Event;
//import com.lzyc.ybtappcal.constant.Contants;
//import com.lzyc.ybtappcal.util.DensityUtils;
//import com.lzyc.ybtappcal.util.NetworkUtil;
//import com.lzyc.ybtappcal.util.ScreenShotUtil;
//import com.lzyc.ybtappcal.util.ScreenUtils;
//import com.lzyc.ybtappcal.util.SharePreferenceUtil;
//import com.lzyc.ybtappcal.util.StringUtils;
//import com.lzyc.ybtappcal.view.DiffuseView;
//import com.umeng.analytics.MobclickAgent;
//
//import ybt.library.dialog.wheel.RegionPickerDialog;
//
///**
// * 异地报销
// */
//public class ReimbursementFragment extends BaseFragment {
//    private static  final  String TAG=ReimbursementFragment.class.getSimpleName();
//
//
//    @InjectAll
//    protected Views v;
//
//    class Views {
//        @InjectBinder(method = "onClick", listeners = {OnClick.class})
//        DiffuseView diffuseview;
//        @InjectBinder(method = "onClick", listeners = {OnClick.class})
//        TextView tv_search;
//        @InjectBinder(method = "onClick", listeners = {OnClick.class})
//        LinearLayout city_jiuzhen;
//        @InjectBinder(method = "onClick", listeners = {OnClick.class})
//        LinearLayout city_canbao;
//        @InjectBinder(method = "onClick", listeners = {OnClick.class})
//        TextView tv_scan;
//
//        TextView tv_canbao;
//        TextView tv_jiuzhen;
//
//        @InjectBinder(method = "onClick", listeners = {OnClick.class})
//        ImageView img_jiuzhen;
//        @InjectBinder(method = "onClick", listeners = {OnClick.class})
//        ImageView img_canbao;
//
//        DiffuseView diffuseview_jz;
//        DiffuseView diffuseview_cb;
//    }
//
//    private String cityJiuzhen;//就诊城市
//    private String proviceJiuzhen;//就诊省份
//    private String provinceCanbao;//选中的医保省份
//    private String cityCanbao;//选中的医保城市
//
//    private static final int CITY_JIUZHEN = 0x0;//就诊城市
//    private static final int CITY_CANBAO = 0x1;//参保城市
//    private int typeCity;//城市类型
//
//    public static Bitmap bitmap;
//
//    Bundle mBundle = new Bundle();
//
//    public int getHeight() {
//        int location[] = new int[2];
//        v.tv_scan.getLocationOnScreen(location);
//        int height = v.tv_scan.getHeight() / 2 + location[1] - ScreenUtils.getStatusHeight() - DensityUtils.sp2px(8) - DensityUtils.dp2px(3);
//        return height;
//    }
//
//    @Override
//    public int getContentViewId() {
//        return R.layout.fragment_main_reimbursement;
//    }
//
//    @Override
//    protected void init() {
//        startUPAndUnderAnimationLeft(v.img_jiuzhen);
//        startUPAndUnderAnimationRight(v.img_canbao);
//        v.diffuseview.start();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        MobclickAgent.onPageStart(TAG);
//        v.diffuseview_jz.start();
//        v.diffuseview_cb.start();
//        showCity();
//    }
//
//    /**
//     * 显示的城市
//     */
//    private void showCity() {
//        cityCanbao = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.CITY_CANBAO, "");
//        provinceCanbao = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.PROVINCE_CANBAO, "");
//        cityJiuzhen = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.CITY_JIUZHEN, "");
//        proviceJiuzhen = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.PROVICE_JIUZHEN, "");
//        SharePreferenceUtil.put(mContext, SharePreferenceUtil.CITY_CANBAO, cityCanbao);
//        SharePreferenceUtil.put(mContext, SharePreferenceUtil.PROVINCE_CANBAO, provinceCanbao);
//        v.tv_jiuzhen.setText(StringUtils.getCity(mContext, cityJiuzhen));
//        if (TextUtils.isEmpty(cityCanbao)) {
//            cityCanbao = "未选择";
//        }
//        if (TextUtils.isEmpty(cityJiuzhen)) {
//            String locationCity = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.CITY, "");
//            String locationProvince = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.PROVINCE, "");
//            if (TextUtils.isEmpty(locationCity)) {
//                cityJiuzhen = "未定位";
//            } else {
//                SharePreferenceUtil.put(mContext, SharePreferenceUtil.CITY_JIUZHEN, locationCity);
//                SharePreferenceUtil.put(mContext, SharePreferenceUtil.PROVICE_JIUZHEN, locationProvince);
//                cityJiuzhen = locationCity;
//            }
//        }
//        v.tv_canbao.setText(StringUtils.getCity(mContext, cityCanbao));
//        v.tv_jiuzhen.setText(StringUtils.getCity(mContext, cityJiuzhen));
//    }
//
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv_scan:
//                if (v.tv_jiuzhen.getText().equals("未定位")) {
//                    showPrompt(v.tv_scan, "定位未开启，请选择就诊城市");
//                    return;
//                }
//                if (v.tv_canbao.getText().equals("未选择")) {
//                    showPrompt(v.tv_canbao, "未选择参保城市,请先选择参保城市");
//                    return;
//                }
//                EventBus.getDefault().post(new Event("a004"));
//                if (!NetworkUtil.CheckConnection(mContext)) {
//                    showNetDialog();
//                    return;
//                }
//                EventBus.getDefault().post(new Event("b004"));
//                if (Build.MODEL.equals("Le X820")) {
//                    mBundle.putInt(Contants.KEY_PAGE_SEARCH, Contants.VAL_PAGE_SEARCH_REIMBURSEMENT);
//                    openActivityNoAnim(com.lzyc.zbar.activity.CaptureActivity.class, mBundle);
//                    getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
//                } else {
//                    mBundle.putInt(Contants.KEY_PAGE_SEARCH, Contants.VAL_PAGE_SEARCH_REIMBURSEMENT);
//                    mBundle.putInt("midData", getHeight());
//                    mBundle.putInt("h", 100);
//                    mBundle.putInt("topSplitHeight", 100);
//                    mBundle.putString("province", "");
//                    openActivityNoAnim(com.lzyc.zxing.activity.CaptureActivity.class, mBundle);
//                }
//                TopFragment.bitmap = ScreenShotUtil.takeScreenShot(getActivity());
//                break;
//
//            case R.id.city_jiuzhen:
//                typeCity = CITY_JIUZHEN;
//                EventBus.getDefault().post(new Event("b001"));
//                showDialogRegion();
//                break;
//
//            case R.id.city_canbao:
//                typeCity = CITY_CANBAO;
//                EventBus.getDefault().post(new Event("b002"));
//                showDialogRegion();
//                break;
//
//            case R.id.tv_search:
//                if (v.tv_jiuzhen.getText().equals("未定位")) {
//                    showPrompt(v.tv_scan, "定位未开启，请选择就诊城市");
//                    return;
//                }
//                if (v.tv_canbao.getText().equals("未选择")) {
//                    showPrompt(v.tv_canbao, "未选择参保城市,请先选择参保城市");
//                    return;
//                }
//                EventBus.getDefault().post(new Event("a004"));
//                if (!NetworkUtil.CheckConnection(mContext)) {
//                    showNetDialog();
//                    return;
//                }
//                EventBus.getDefault().post(new Event("b003"));
//                mBundle.putInt(Contants.KEY_PAGE_SEARCH, Contants.VAL_PAGE_SEARCH_REIMBURSEMENT);
//                openActivity(SearchActivity.class, mBundle);
//                break;
//        }
//    }
//
//    private void startUPAndUnderAnimationLeft(final View v) {
//        float redPackedY = v.getTranslationY();
//        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationY", redPackedY, redPackedY - DensityUtils.dp2px(5), redPackedY);
//        AnimatorSet set = new AnimatorSet();
//        set.play(animator);
//        set.setDuration(3000);
//        set.start();
//        set.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                startUPAndUnderAnimationRight(v);
//            }
//        });
//    }
//
//    private void startUPAndUnderAnimationRight(final View v) {
//        float redPackedY = v.getTranslationY();
//        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationY", redPackedY, redPackedY - DensityUtils.dp2px(5), redPackedY);
//        AnimatorSet set = new AnimatorSet();
//        set.play(animator);
//        set.setDuration(3000);
//        set.start();
//        set.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                startUPAndUnderAnimationLeft(v);
//            }
//        });
//    }
//
//    /**
//     * 省市列表3D滚轮弹窗
//     */
//    private final void showDialogRegion() {
//        RegionPickerDialog.Builder builder = new RegionPickerDialog.Builder(mContext);
//        RegionPickerDialog dialog = builder.setOnRegionSelectedListener(new RegionPickerDialog.OnTitlebarClickListener() {
//            @Override
//            public void onTitlebarOkClickListener(int pIndex, int cIndex, String provice, String city) {
//                chooseCity(pIndex, cIndex, provice, city);
//            }
//        }).create();
//        switch (typeCity) {
//            case CITY_CANBAO:
//                if (TextUtils.isEmpty(provinceCanbao) || TextUtils.isEmpty(cityCanbao)) {
//                    builder.setDefaultValue(0, 0);
//                } else {
//                    builder.setDefaultValue(provinceCanbao, cityCanbao);
//                }
//                break;
//            case CITY_JIUZHEN:
//                if(proviceJiuzhen.contains("市")){
//                    proviceJiuzhen= proviceJiuzhen.substring(0,proviceJiuzhen.length()-1);
//                }
//                if (TextUtils.isEmpty(proviceJiuzhen) || TextUtils.isEmpty(cityJiuzhen) || cityJiuzhen.equals("未定位")) {
//                    builder.setDefaultValue(0, 0);
//                } else {
//                    builder.setDefaultValue(proviceJiuzhen, cityJiuzhen);
//                }
//                break;
//        }
//        dialog.show();
//    }
//
//    /**
//     * 选择城市
//     *
//     * @param pIndex
//     * @param cIndex
//     * @param provice
//     * @param city
//     */
//    private void chooseCity(int pIndex, int cIndex, String provice, String city) {
//        switch (typeCity) {
//            case CITY_CANBAO:
//                v.tv_canbao.setText("" + city);
//                provinceCanbao = provice;
//                cityCanbao = city;
//                v.tv_canbao.setText(StringUtils.getCity(mContext, city));
//                SharePreferenceUtil.put(mContext, SharePreferenceUtil.PROVINCE_CANBAO, provice);
//                SharePreferenceUtil.put(mContext, SharePreferenceUtil.CITY_CANBAO, city);
//
//                break;
//            case CITY_JIUZHEN:
//                proviceJiuzhen = provice;
//                cityJiuzhen = city;
//                v.tv_jiuzhen.setText(city);
//                v.tv_jiuzhen.setText(StringUtils.getCity(mContext, city));
//                SharePreferenceUtil.put(mContext, SharePreferenceUtil.PROVICE_JIUZHEN, provice);
//                SharePreferenceUtil.put(mContext, SharePreferenceUtil.CITY_JIUZHEN, city);
//                break;
//        }
//    }
//
//    @Override
//    public void error(String errorMsg) {
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        MobclickAgent.onPageEnd(TAG);
//    }
//
//}

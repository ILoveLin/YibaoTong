package com.lzyc.ybtappcal.activity.top;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.view.RatingBar;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：xujm
 * 时间：2016/2/22
 * 备注：方案详情 动画播放,修改界面
 */
public class PlanDetailActivity extends BaseActivity {
    private static final String TAG=PlanDetailActivity.class.getSimpleName();
    @BindView(R.id.tv_plan_detail_drug_scanname)
    TextView tv_plan_detail_drug_scanname;
    @BindView(R.id.linear_plan_detail_price)
    LinearLayout linearPlanDetailPrice;
    @BindView(R.id.tv_plan_detail_drug_name)
    TextView tv_plan_detail_drug_name;
    @BindView(R.id.tv_plan_detail_drug_money)
    TextView tv_plan_detail_drug_money;
    @BindView(R.id.tv_plan_detail_drug_money_price)
    TextView tv_plan_detail_drug_money_price;
    @BindView(R.id.tv_plan_detail_drug_red_money_price)
    TextView tv_plan_detail_drug_red_money_price;
    @BindView(R.id.linear_plan_detail_goto_there)
    LinearLayout linear_plan_detail_goto_there;
    @BindView(R.id.tv_plan_detail_start)
    RatingBar tv_plan_detail_start;
    @BindView(R.id.tv_drugs_detail_hospital_level)
    TextView tv_drugs_detail_hospital_level;
    @BindView(R.id.tv_plan_detail_hospital)
    TextView tv_plan_detail_hospital;
    @BindView(R.id.tv_drugs_detail_hospital_distance)
    TextView tv_drugs_detail_hospital_distance;
    @BindView(R.id.tv_plan_commniuty_count)
    TextView tv_plan_commniuty_count;
    @BindView(R.id.hc_fuwutaidu_tv_desc)
    TextView hc_fuwutaidu_tv_desc;
    @BindView(R.id.tv_plan_detail_hospital_pic)
    ImageView tv_plan_detail_hospital_pic;
    @BindString(R.string.title_plan_detail)
    String titleName;
    private DrugBean plan;
    private String lat, lng, address;
    private String currentProvince;
    private int typePage;
    private float mTouchY;

    @Override
    public int getContentViewId() {
        return R.layout.activity_plan_detail2;
    }

    @Override
    public void init() {
        setTitleName(titleName);
//        setTitleRightBtnResources(R.drawable.selector_titlebar_right_share);
        plan = (DrugBean) getIntent().getSerializableExtra("plan");
        currentProvince = (String) SharePreferenceUtil.get(PlanDetailActivity.this, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "北京");
        lat = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.LATITUDE, "");
        lng = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.LONGITUDE, "");
        address = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.ADDRESS, "");
        initData();
        linearPlanDetailPrice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mTouchY = motionEvent.getRawY();
                }
                return false;
            }
        });
    }

    private void initData() {
        if (!TextUtils.isEmpty(plan.getJuli())) {
            double a = Double.parseDouble(plan.getJuli());
            double dis = (a / 1000);
            tv_drugs_detail_hospital_distance.setText(String.format("%.1f", dis).toString() + "km");
        }
        if (plan.getHospitalImages().isEmpty()) {
            tv_plan_detail_hospital_pic.setImageResource(R.mipmap.icon_hospital_photo_empity);
            tv_plan_detail_hospital_pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            Picasso.with(this).load(plan.getHospitalImages().get(0))
                    .placeholder(R.mipmap.icon_hospital_photo_empity)
                    .error(R.mipmap.icon_hospital_photo_empity).into(tv_plan_detail_hospital_pic);
            tv_plan_detail_hospital_pic.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }
        tv_plan_detail_start.setmClickable(false);
        tv_plan_detail_start.setStar(Float.parseFloat(plan.getAverage()));
        hc_fuwutaidu_tv_desc.setText("" + plan.getAverage());
        if ("".equals(plan.getGoodsName())) {
            tv_plan_detail_drug_name.setText("" + plan.getName());
        } else {
            tv_plan_detail_drug_name.setText("" + plan.getGoodsName());
            tv_plan_detail_drug_scanname.setText("" + plan.getName());
        }
        tv_drugs_detail_hospital_level.setText("" + plan.getLevel() + plan.getJibie());
        tv_plan_detail_hospital.setText("" + plan.getHostopShortName());
        tv_plan_commniuty_count.setText("" + plan.getCommentCount() + "人评论");
        tv_plan_detail_drug_money.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tv_plan_detail_drug_money_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tv_plan_detail_drug_money.setText("" + "￥");
        tv_plan_detail_drug_money_price.setText("" + plan.getPrice());
        tv_plan_detail_drug_red_money_price.setText("" + plan.getAmountMonryForPay());
    }
    @OnClick({R.id.ib_left, R.id.ib_right, R.id.linear_plan_detail_price,R.id.linear_plan_detail_goto_there})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_left:
                finish();
                /*//左边进来,右边出去
                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);*/
                break;
            case R.id.ib_right:  //分享
                //分享
//                if (plan != null) {
//                    initPopupShare();
//                } else {
////                    showToast("方案详情为空，暂时不能分享！");
//                    showPrompt(linear_plan_detail_goto_there, "方案详情为空，暂时不能分享！");
//                }
                break;
            case R.id.linear_plan_detail_price: //进入药品详情
//                Intent drugIntent = new Intent(PlanDetailActivity.this, ResultsDetailActivity.class);
//                drugIntent.putExtra("drugNameID", plan.getDrugNameID());
//                drugIntent.putExtra("specificationsID", plan.getSpecificationsID());
//                drugIntent.putExtra("venderID", plan.getVenderID());
//                drugIntent.putExtra(Contants.KEY_PAGE, Contants.VAL_PAGE_MINEPLAN);
//                drugIntent.putExtra("touchY", mTouchY);
//                startActivity(drugIntent);
                break;
            case R.id.linear_plan_detail_goto_there:   //地图
                requestEventCode("e005");
                startNavi(plan.getLat(), plan.getLng(), plan.getName());
                break;
            case R.id.tv_plan_goto_detail_hospital:   //去其他医院

//                Intent intent1 = new Intent(PlanDetailActivity.this, ScanResultActivity.class);
//                intent1.putExtra("drugNameID", plan.getDrugNameID());
//                intent1.putExtra("venderID", plan.getVenderID());
//                intent1.putExtra("specificationsID", plan.getSpecificationsID());
//                intent1.putExtra("drug_id", plan.getDrug_id());
//                intent1.putExtra(Contants.KEY_PAGE, Contants.VAL_PAGE_MINEPLAN);
//                intent1.putExtra("drug_id", plan.getDrug_id());
////                if (!currentProvince.equals("北京")) {
////                    intent1.putExtra("plan", "comeFromPlanDetail");
////                }
//                startActivity(intent1);
//                Intent intent = new Intent();
//                intent.putExtra("drugNameID", plan.getDrugNameID());
//                intent.putExtra(Contants.KEY_PAGE, typePage);
//                intent.putExtra("drugId", plan.getDrugID());
//                intent.setClass(this, ScanResultActivity.class);
//                startActivity(intent);

                break;
            default:
                super.onClickTitleLeftBtn(view);
                break;
        }
    }

    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi(String latTarget, String lngTarget, String addressTarget) {
        String address = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.ADDRESS, "");
        LatLng pt1 = null;
        LatLng pt2 = null;
        if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng)) {
            pt1 = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        } else {
            ToastUtil.showToastCenter(PlanDetailActivity.this, "医院经纬度为空，无法导航");
            return;
        }
        if (!TextUtils.isEmpty(latTarget) && !TextUtils.isEmpty(lngTarget)) {
            pt2 = new LatLng(Double.parseDouble(latTarget), Double.parseDouble(lngTarget));
        } else {
            ToastUtil.showShort(PlanDetailActivity.this, "我的定位经纬度为空");
            return;
        }
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2)
                .startName(address).endName(addressTarget);
        try {
            BaiduMapNavigation.openBaiduMapNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
        }
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

    /**
     * 分享
     */
    private void initPopupShare() {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
//        final UMImage image = new UMImage(this, bitmap);
//        final String url = plan.getShareUrl();
//        final String text = plan.getName() + "  原价：" + plan.getPrice() + "  自付：" + plan.getAmountMonryForPay();
//
//        final BasePopupWindow popup = new BasePopupWindow(this, R.layout.popup_share, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        View conentView = popup.getConentView();
//        conentView.findViewById(R.id.tv_popup_share_cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popup.dismiss();
//            }
//        });
//
//        conentView.findViewById(R.id.tv_popup_share_wb).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ShareAction(PlanDetailActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
//                        .withText(text)
//                        .withMedia(image)
//                        .withTargetUrl(url)
//                        .share();
//                popup.dismiss();
//            }
//        });
//        conentView.findViewById(R.id.tv_popup_share_weixin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ShareAction(PlanDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
//                        .withText(text)
//                        .withMedia(image)
//                        .withTargetUrl(url)
//                        .share();
//                popup.dismiss();
//            }
//        });
//        conentView.findViewById(R.id.tv_popup_share_pyq).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ShareAction(PlanDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
//                        .withText(text)
//                        .withMedia(image)
//                        .withTargetUrl(url)
//                        .share();
//                popup.dismiss();
//            }
//        });
//        conentView.findViewById(R.id.tv_popup_share_qq).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ShareAction(PlanDetailActivity.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
//                        .withText(text)
//                        .withMedia(image)
//                        .withTargetUrl(url)
//                        .share();
//                popup.dismiss();
//            }
//        });
//
//
//        popup.showPopupWindow(linear_plan_detail_goto_there, Gravity.BOTTOM);

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(PlanDetailActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(PlanDetailActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            //Toast.makeText(PlanDetailActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(PlanDetailActivity.this).onActivityResult(requestCode, resultCode, data);
    }
}


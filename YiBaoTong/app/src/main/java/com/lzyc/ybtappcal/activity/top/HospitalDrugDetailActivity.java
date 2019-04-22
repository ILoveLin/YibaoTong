package com.lzyc.ybtappcal.activity.top;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.account.LoginActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.CycleViewStatePagerAdapter;
import com.lzyc.ybtappcal.bean.Event;
import com.lzyc.ybtappcal.bean.HospitalBean;
import com.lzyc.ybtappcal.bean.PlacedDataBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.view.StarBar;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ybt.library.indicator.indicator.PageIndicatorView;

/**
 * 医院药品详情
 * Created by lovelin 2016/23.
 */
public class HospitalDrugDetailActivity extends BaseActivity {
    private static final String TAG=HospitalDrugDetailActivity.class.getSimpleName();
    @BindView(R.id.tv_drugs_detail_drugname)
    TextView tv_drugs_detail_drugname;
    @BindView(R.id.tv_drugs_detail_scanname)
    TextView tv_drugs_detail_scanname;
    @BindView(R.id.tv_drugs_detail_drug_pay)
    TextView tv_drugs_detail_drug_pay;
    @BindView(R.id.tv_drugs_detail_hospital)
    TextView tv_drugs_detail_hospital;
    @BindView(R.id.tv_drugs_detail_hospital_level)
    TextView tv_drugs_detail_hospital_level;
    @BindView(R.id.tv_drugs_detail_hospital_distance)
    TextView tv_drugs_detail_hospital_distance;
    @BindView(R.id.tv_drugs_detail_hospital_commonitypoint)
    TextView tv_drugs_detail_hospital_commonitypoint;
    @BindView(R.id.tv_drugs_detail_hospital_star)
    TextView tv_drugs_detail_hospital_star;
    @BindView(R.id.hc_fuwutaidu_comment_star_first)
    StarBar hc_fuwutaidu_comment_star_first;
    @BindView(R.id.detail_vp_bottom_linear)
    LinearLayout detail_vp_bottom_linear;
    private HospitalBean mHospitalBean;
    private PlacedDataBean mPlacedDataBean;
    private Boolean isLogin;
    @BindView(R.id.detail_vp)
    ViewPager detail_vp;
    @BindView(R.id.detail_vp_indicator)
    PageIndicatorView detail_vp_indicator;

    @Override
    public int getContentViewId() {
        return R.layout.activity_detail_hospitaldrug;
    }

    @Override
    public void init() {
        setTitleBarVisibility(View.GONE);
        initView();
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

    private void initView() {
        mHospitalBean = (HospitalBean) getIntent().getSerializableExtra(Contants.KEY_OBJ_HOSPITALBEAN);
        mPlacedDataBean = (PlacedDataBean) getIntent().getSerializableExtra(Contants.KEY_OBJ_PLACEDATA);
        String hospital = mHospitalBean.getName();
        String price = mHospitalBean.getPrice();
        String goodsName = mPlacedDataBean.getGoodsName();
        String scanName = mPlacedDataBean.getName();
        tv_drugs_detail_drug_pay.setText(price + "");
        tv_drugs_detail_hospital.setText(hospital + "");
        String distance = mHospitalBean.getDistance();
        if (!TextUtils.isEmpty(distance)) {
            double a = Double.parseDouble(distance);
            double dis = (a / 1000);
            tv_drugs_detail_hospital_distance.setText(String.format("%.1f", dis).toString() + "km");
        }

        if (!(TextUtils.isEmpty(goodsName)) && (!TextUtils.isEmpty(scanName))) {
            tv_drugs_detail_drugname.setText("" + goodsName);
            tv_drugs_detail_scanname.setText("" + scanName);
        } else if (TextUtils.isEmpty(goodsName) && (!TextUtils.isEmpty(scanName))) {
            tv_drugs_detail_drugname.setText("" + scanName);
            tv_drugs_detail_scanname.setText("" + goodsName);
        } else if (!(TextUtils.isEmpty(goodsName)) && TextUtils.isEmpty(scanName)) {
            tv_drugs_detail_drugname.setText("" + scanName);
            tv_drugs_detail_scanname.setText("" + goodsName);
        }


        int commentCount = mHospitalBean.getCommentCount();
        String average = mHospitalBean.getAverage();
        String level = mHospitalBean.getLevel();
        String jibie = mHospitalBean.getJibie();
        tv_drugs_detail_hospital_level.setText(level + "" + jibie);
        tv_drugs_detail_hospital_commonitypoint.setText("(" + commentCount + ")");
        tv_drugs_detail_hospital_star.setText(average + "");
        hc_fuwutaidu_comment_star_first.setStarMark(Float.parseFloat(average));
        initViewPager();
    }

    private void initViewPager() {
        mHospitalBean = (HospitalBean) getIntent().getSerializableExtra(Contants.KEY_OBJ_HOSPITALBEAN);
        List<String> hospitalImages = mHospitalBean.getHospitalImages();
        if (hospitalImages.isEmpty()) {
            hospitalImages.add("#");
            detail_vp_bottom_linear.setVisibility(View.GONE);
        } else {
            detail_vp_bottom_linear.setVisibility(View.VISIBLE);
        }
        CycleViewStatePagerAdapter mCycleViewStatePagerAdapter = new CycleViewStatePagerAdapter(this, getSupportFragmentManager(), hospitalImages);
        mCycleViewStatePagerAdapter.setRound(true);
        detail_vp.setAdapter(mCycleViewStatePagerAdapter);
        detail_vp_indicator.setCount(hospitalImages.size());
        detail_vp_indicator.setViewPager(detail_vp);
        detail_vp_indicator.setAutoScroll();
    }

    @OnClick({R.id.iv_drugs_detail_finash, R.id.linear_drug_detail_go_commnoty, R.id.linear_drug_detail_goto_commnoty, R.id.linear_drug_detail_save_plan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_drugs_detail_finash:
                this.finish();
                overridePendingTransition(0, 0);
                requestEventCode("f003");
                break;
            case R.id.linear_drug_detail_go_commnoty:
            case R.id.linear_drug_detail_goto_commnoty:
                switchToHospitalDrugDetailActivity(mHospitalBean);
                requestEventCode("f001");
                break;
            case R.id.linear_drug_detail_save_plan:
                isLogin = (Boolean) SharePreferenceUtil.get(HospitalDrugDetailActivity.this, SharePreferenceUtil.IS_LOGIN, false);
                if (isLogin) {
                    requestAddPlan();
                    requestEventCode("f002");
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            default:
                super.onClickTitleLeftBtn(view);
                break;

        }
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        if (what == HttpConstant.TOP_SCAN_RESULT_ADD_PLAN) {
            this.finish();
            overridePendingTransition(0, 0);
            showToast("已保存至我的方案");
        }
    }

    @Override
    public void error(String errorMsg) {
        this.finish();
        overridePendingTransition(0, 0);
        showToast("该方案已存在");
    }

    /**
     * 添加方案
     */
    private void requestAddPlan() {
        String loginPhone = (String) SharePreferenceUtil.get(HospitalDrugDetailActivity.this, SharePreferenceUtil.PHONE, "");
        String city = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.CITY_CANBAO, "");
        if (TextUtils.isEmpty(city)) {
            city = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.CITY, "");
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "Home");
        params.put("class", "AddPlan");
        String sign = MD5ChangeUtil.Md5_32("HomeAddPlan" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("phone", loginPhone);
        params.put("DrugNameID", mPlacedDataBean.getDrugNameID());
        params.put("SpecificationsID", mPlacedDataBean.getSpecificationsID());
        params.put("VenderID", mPlacedDataBean.getVenderID());
        params.put("Price", mPlacedDataBean.getPrice());
        if (!TextUtils.isEmpty(mHospitalBean.getPrice())) {
            params.put("AmountMonryForPay", mHospitalBean.getPrice());
        } else {
            params.put("AmountMonryForPay", "");
        }
        params.put("HostopShortName", mHospitalBean.getName());
        params.put("Yyid", mHospitalBean.getYyid());
        params.put("Yidi", "0");
        params.put("city", city);
        request(params, HttpConstant.TOP_SCAN_RESULT_ADD_PLAN);
    }

    private void switchToHospitalDrugDetailActivity(HospitalBean hospitalBean) {
        if (hospitalBean != null && mPlacedDataBean != null) {
            Intent intent = new Intent(this, AboutHospitalOrCommunityActivity.class);
            intent.putExtra(Contants.KEY_OBJ_HOSPITALBEAN, hospitalBean);
            intent.putExtra(Contants.KEY_OBJ_PLACEDATA, mPlacedDataBean);
            startActivity(intent);
        } else {
            ToastUtil.showShort(this, "药品信息为空！");
        }
    }
}

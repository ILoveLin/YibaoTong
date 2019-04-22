package com.lzyc.ybtappcal.activity.reimbursement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.account.LoginActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 异地报销
 * 非参保城市报销详情
 */
public class ReimbursementNoYbDetailsActivity extends BaseActivity {
    private static final String TAG=ReimbursementNoYbDetailsActivity.class.getSimpleName();

    @BindView(R.id.activity_reimbursement_utils_brand)
    TextView brandTextView;     //官方名
    @BindView(R.id.activity_reimbursement_utils_drugs_name)
    TextView nameTextView;      //别名
    @BindView(R.id.activity_reimbursement_utils_drugs_reimbursement)
    TextView reimbursementTextView;
    @BindView(R.id.activity_reimbursement_utils_drugs_condition)
    TextView conditionTextView;
    @BindView(R.id.field_activity_reimbursement_utils_save)
    TextView saveButton;

    private int typePage;
    private DrugBean drugBean;

    @Override
    public int getContentViewId() {
        return R.layout.activity_reimbursement_noyb_detail;
    }

    @Override
    public void init() {
        setTitleName("报销详情");
        Bundle bundle = getIntent().getExtras();
        typePage = bundle.getInt(Contants.KEY_PAGE_DRUG);
        drugBean = (DrugBean) bundle.getSerializable(Contants.KEY_OBJ_DRUGBEAN);
        initView();
        if (typePage == Contants.VAL_PAGE_MINEPLAN) {
            saveButton.setVisibility(View.GONE);
        } else {
            saveButton.setVisibility(View.VISIBLE);
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

    public void initView() {
        String GoodsName = drugBean.getGoodsName();
        String Name = drugBean.getName();
        String other = drugBean.getOther();
        String Baoxiao = drugBean.getBaoxiao();
        String yType = drugBean.getyType();
        ArrayList<String> conditions = drugBean.getConditions();
        brandTextView.setVisibility(View.VISIBLE);  //官方名
        nameTextView.setVisibility(View.VISIBLE);  //别名
        if (GoodsName != null && !GoodsName.equals("") && Name != null && !Name.equals("")) {   //两个名字都存在
            brandTextView.setVisibility(View.VISIBLE);  //官方名
            nameTextView.setVisibility(View.VISIBLE);  //别名
            brandTextView.setText(GoodsName);
            nameTextView.setText("(" + Name + ")");
        } else if (GoodsName != null && !GoodsName.equals("")) {   //官方名 存在
            brandTextView.setText(GoodsName);
            brandTextView.setVisibility(View.VISIBLE);
            nameTextView.setVisibility(View.INVISIBLE);

        } else if (Name != null && !Name.equals("")) {
            brandTextView.setText(Name);
            brandTextView.setVisibility(View.VISIBLE);
            nameTextView.setVisibility(View.INVISIBLE);

        } else {
            nameTextView.setText("无");
        }
        if (Baoxiao != null && !Baoxiao.equals("")) {
            reimbursementTextView.setText(Baoxiao);
        } else {
            reimbursementTextView.setText("无");
        }
        if (!TextUtils.isEmpty(other)) {
            conditionTextView.setText(other);
        } else {
            conditionTextView.setText("无");
        }
    }

    @OnClick(R.id. field_activity_reimbursement_utils_save)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.field_activity_reimbursement_utils_save:
                boolean isLogin = (Boolean) SharePreferenceUtil.get(this, SharePreferenceUtil.IS_LOGIN, false);
                LogUtil.e("是否登入====","===isLogin======"+isLogin);
                if (isLogin) {
                    requestSave();
                    LogUtil.e("是否登入====","===保存======"+isLogin);

                } else {
                    openActivity(LoginActivity.class);

                }
                break;
        }
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        ToastUtil.showToastCenter(this,"已保存至我的方案");
    }
    @Override
    public void error(String errorMsg) {
        ToastUtil.showToastCenter(this,"该方案已存在");
    }
    public void requestSave() {
        String ybCity = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.CITY_CANBAO, "");
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "Home");
        params.put("class", "AddPlan");
        String sign = MD5ChangeUtil.Md5_32("HomeAddPlan" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("phone", (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, ""));
        if (drugBean.getDrugNameID() != null) {
            params.put("DrugNameID", drugBean.getDrugNameID());
        } else {
            params.put("DrugNameID", "");
        }
        if (drugBean.getSpecificationsID() != null) {
            params.put("SpecificationsID", drugBean.getSpecificationsID());
        } else {
            params.put("SpecificationsID", "");
        }
        if (drugBean.getVenderID() != null) {
            params.put("VenderID", "" + drugBean.getVenderID());
        } else {
            params.put("VenderID", "");
        }
        if (drugBean.getPrice() != null) {
            params.put("Price", drugBean.getPrice());
        } else {
            params.put("Price", "");
        }
        if (drugBean.getAmountMonryForPay() != null) {
            params.put("AmountMonryForPay", drugBean.getAmountMonryForPay());
        } else {
            params.put("AmountMonryForPay", "");
        }
        if (drugBean.getHostopShortName() != null) {
            params.put("HostopShortName", drugBean.getHostopShortName());
        } else {
            params.put("HostopShortName", "");
        }
        if (drugBean.getYyid() != null) {
            params.put("Yyid", drugBean.getYyid());
        } else {
            params.put("Yyid", "");
        }
        params.put("city",ybCity);
        if (drugBean.getZifei() != null) {
            params.put("zifei", drugBean.getZifei());
        } else {
            params.put("zifei", "");
        }
        if (drugBean.getYiyuan() != null) {
            params.put("yiyuan", drugBean.getYiyuan());
        } else {
            params.put("yiyuan", "");
        }
        if (drugBean.getShequ() != null) {
            params.put("shequ", drugBean.getShequ());
        } else {
            params.put("shequ", "");
        }
        params.put("Yidi", "1");
        request(params, HttpConstant.REMOTE_DRUGS_SAVE);
    }
}


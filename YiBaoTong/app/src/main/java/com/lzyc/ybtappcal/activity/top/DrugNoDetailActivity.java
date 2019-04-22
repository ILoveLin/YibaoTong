
package com.lzyc.ybtappcal.activity.top;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 第三方平台扫描到的数据
 * @author yang
 */
public class DrugNoDetailActivity extends BaseActivity {
    private static final String TAG=DrugNoDetailActivity.class.getSimpleName();
    private int recDrug;
    private int page;
    @BindView(R.id.tv_drug_detail_name)
    TextView tv_drug_detail_name;
    @BindView(R.id.tv_drug_detail_nickname)
    TextView tv_drug_detail_nickname;
    @BindView(R.id.tv_drug_detail_vender)
    TextView tv_drug_detail_vender;
    //    @InjectView(R.id.tv_drug_detail_spec)
//    private TextView tv_drug_detail_spec;
    @BindView(R.id.tv_desc_drug)
    TextView tv_desc_drug;
    @BindView(R.id.empty_drug_recomment)
    TextView empty_drug_recomment;
    private DrugBean drugBean;


    @Override
    public int getContentViewId() {
        return R.layout.activity_drug_no_detail;
    }

    @Override
    public void init() {
        setTitleName("药品详情");
        drugBean = (DrugBean) getIntent().getExtras().getSerializable(Contants.KEY_OBJ_DRUGBEAN);
        recDrug = drugBean.getRecDrug();
        fillInfo();
    }

    @OnClick({R.id.empty_drug_recomment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.empty_drug_recomment:
                String keyworld = "";
                String goodsName = drugBean.getGoodsName();
                String name = drugBean.getName();
                if (!TextUtils.isEmpty(goodsName)) {
                    keyworld = goodsName;
                } else {
                    keyworld = name;
                }
                Bundle bundle = new Bundle();
                bundle.putString(HttpConstant.PARAM_KEY_KEYWORD, name);
                openActivity(RecommentIFEmptyActivity.class, bundle);
                break;
            default:
                super.onClickTitleLeftBtn(view);
                break;
        }
    }

    private void fillInfo() {
        if (drugBean == null) {
            return;
        }
        if (recDrug == 1) {
            tv_desc_drug.setText("请点击推荐查看其他同类药品情况!");
            empty_drug_recomment.setVisibility(View.VISIBLE);
            requestEventCode("a012");
        } else {
            empty_drug_recomment.setVisibility(View.GONE);
        }
        String goodsName = drugBean.getGoodsName();
        String name = drugBean.getName();
        if (!TextUtils.isEmpty(goodsName)) {
            tv_drug_detail_name.setText(goodsName);
            tv_drug_detail_nickname.setText("" + name);
        } else {
            tv_drug_detail_name.setText("" + name);
            tv_drug_detail_nickname.setText("");
        }
        tv_drug_detail_vender.setText("" + drugBean.getVender());
//        tv_drug_detail_spec.setText("规格：" + drugBean.getSpecifications());
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
}

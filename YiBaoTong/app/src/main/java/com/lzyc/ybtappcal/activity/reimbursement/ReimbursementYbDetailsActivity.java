package com.lzyc.ybtappcal.activity.reimbursement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.account.LoginActivity;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.adapter.DrugsPopupListAdapter;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.MD5ChangeUtil;
import com.lzyc.ybtappcal.util.ScreenUtils;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.lzyc.ybtappcal.view.RoundProgressBar;
import com.lzyc.ybtappcal.widget.popupwindow.BasePopupWindow;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 异地报销
 * 参保城市详情
 */
public class ReimbursementYbDetailsActivity extends BaseActivity {
    private static final String TAG=ReimbursementYbDetailsActivity.class.getSimpleName();
    @BindView(R.id.activity_reimbursement_utils_workers)
    RoundProgressBar workerRoundProgressBar;
    @BindView(R.id.activity_reimbursement_utils_resident)
    RoundProgressBar residentRoundProgressBar;
    @BindView(R.id.activity_reimbursement_utils_xin_nong_he)
    RoundProgressBar xinNongHeRoundProgressBar;
    @BindView(R.id.activity_reimbursement_utils_student)
    RoundProgressBar studentRoundProgressBar;
    @BindView(R.id.activity_reimbursement_utils_old)
    RoundProgressBar oldRoundProgressBar;
    @BindView(R.id.activity_reimbursement_utils_brand)
    TextView brandName;
    @BindView(R.id.activity_reimbursement_utils_drugs_name)
    TextView drugsName;
    @BindView(R.id.activity_reimbursement_utils_drugs_specifications)
    TextView specifications;
    @BindView(R.id.activity_reimbursement_utils_drugs_one)
    TextView dType;
    @BindView(R.id.activity_reimbursement_utils_drugs_two)
    TextView drugsType;
    @BindView(R.id.activity_reimbursement_utils_drugs_three)
    TextView conditionTextView;
    @BindView(R.id.activity_reimbursement_utils_next)
    ImageView nextImageView;
    @BindView(R.id.activity_reimbursement_utils_save)
    Button saveButton;
    DrugsPopupListAdapter drugsPopupListAdapter;
    ArrayList<String> condition;
    DrugBean drugBean;
    int typePage;


    @Override
    public int getContentViewId() {
        return R.layout.activity_reimbursement_yb_detail;
    }

    @Override
    public void init() {
        setTitleName("报销详情");
        setTitleRightBtnResources(R.drawable.selector_titlebar_right_share);
        Bundle bundle = getIntent().getExtras();
        drugBean = (DrugBean) bundle.getSerializable(Contants.KEY_OBJ_DRUGBEAN);
        typePage = bundle.getInt(Contants.KEY_PAGE_DRUG);
        if (typePage == Contants.VAL_PAGE_MINEPLAN) {
            saveButton.setVisibility(View.GONE);
        } else {
            saveButton.setVisibility(View.VISIBLE);
        }

        String ZZ00Percent = drugBean.getZZ00().getPercent();
        String ZZ01Percent = drugBean.getZZ01().getPercent();
        String ZZ02Percent = drugBean.getZZ02().getPercent();
        String ZZ03Percent = drugBean.getZZ03().getPercent();
        String ZZ04Percent = drugBean.getZZ04().getPercent();
        String GoodsName = drugBean.getGoodsName();
        String Specifications = drugBean.getSpecifications();
        condition = drugBean.getConditions();
        if (ZZ00Percent != null && !ZZ00Percent.equals("")) {
            workerRoundProgressBar.setProgress(Float.parseFloat(ZZ00Percent));

        } else {
            workerRoundProgressBar.setProgress(0);
        }
        if (ZZ01Percent != null && !ZZ01Percent.equals("")) {
            residentRoundProgressBar.setProgress(Float.parseFloat(ZZ01Percent));
        } else {
            residentRoundProgressBar.setProgress(0);
        }
        if (ZZ02Percent != null && !ZZ02Percent.equals("")) {
            xinNongHeRoundProgressBar.setProgress(Float.parseFloat(ZZ02Percent));
        } else {
            xinNongHeRoundProgressBar.setProgress(0);
        }
        if (ZZ03Percent != null && !ZZ03Percent.equals("")) {
            studentRoundProgressBar.setProgress(Float.parseFloat(ZZ03Percent));
        } else {
            studentRoundProgressBar.setProgress(0);
        }
        if (ZZ04Percent != null && !ZZ04Percent.equals("")) {
            oldRoundProgressBar.setProgress(Float.parseFloat(ZZ04Percent));
        } else {
            oldRoundProgressBar.setProgress(0);
        }
        if (GoodsName != null && !GoodsName.equals("")) {
            brandName.setText(GoodsName);
            drugsName.setText("" + drugBean.getName());
        } else {
            brandName.setText("" + drugBean.getName());
            drugsName.setText("");
        }
        if (Specifications != null && !Specifications.equals("")) {
            specifications.setText("规格：" + Specifications);
        } else {
            specifications.setText("规格：" + "无");
        }

        if (drugBean.getyType() != null && !drugBean.getyType().equals("")) {
            dType.setVisibility(View.VISIBLE);
            dType.setText(drugBean.getyType());
        } else {
            dType.setVisibility(View.GONE);
        }
        if (drugBean.getZy() != null && !drugBean.getZy().equals("")) {
            drugsType.setVisibility(View.VISIBLE);
            drugsType.setText(drugBean.getZy());
        } else {
            drugsType.setVisibility(View.GONE);
        }
        if (condition != null && !condition.equals("") && condition.size() != 0) {
            conditionTextView.setText(condition.get(0));
            if (condition.get(0) != null) {
                conditionTextView.setVisibility(View.GONE);
            } else {
                conditionTextView.setVisibility(View.VISIBLE);
            }
            conditionTextView.setVisibility(View.GONE);
        } else {
            conditionTextView.setVisibility(View.GONE);
        }
        ArrayList<String> list = new ArrayList<String>();
        if (!condition.equals("") && condition != null) {
            drugsPopupListAdapter = new DrugsPopupListAdapter(this, R.layout.item_popup_druginfo, condition);
        } else {
            drugsPopupListAdapter = new DrugsPopupListAdapter(this, R.layout.item_popup_druginfo, list);
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

    @OnClick({R.id.activity_reimbursement_utils_save, R.id.activity_reimbursement_utils_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_reimbursement_utils_save:
                boolean isLogin = (Boolean) SharePreferenceUtil.get(this, SharePreferenceUtil.IS_LOGIN, false);
                if (isLogin) {
                    requestSave();
                } else {
                    openActivity(LoginActivity.class);
                }
                break;
            case R.id.activity_reimbursement_utils_next:
                //弹出框
                initPopupwindow();
                break;
        }
    }

    @Override
    public void onClickTitleRightBtn(View v) {
        super.onClickTitleRightBtn(v);
        if (!TextUtils.isEmpty(drugBean.getShareUrl())) {
            initPopupShare();
        } else {
            showPrompt(new View(this), "暂无异地方案详情，不能分享！");
        }
    }

    /**
     * 分享
     */
    private void initPopupShare() {
        //UMImage image = new UMImage(ShareActivity.this, "http://blog.thegmic.com/wp-content/uploads/2012/05/umeng.jpg");
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.info_icon_1);
//        UMImage image = new UMImage(ShareActivity.this, bitmap);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
        final UMImage image = new UMImage(this, bitmap);
        final String url = drugBean.getShareUrl();
        final String text = drugBean.getName() + "  参保城市：北京市";

        final BasePopupWindow popup = new BasePopupWindow(this, R.layout.popup_share, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View conentView = popup.getConentView();
        conentView.findViewById(R.id.tv_popup_share_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        conentView.findViewById(R.id.tv_popup_share_wb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new ShareAction(ReimbursementYbDetailsActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
//                        .withText(text)
//                        .withMedia(image)
//                        .withTargetUrl(url)
//                        .share();
                popup.dismiss();
            }
        });
        conentView.findViewById(R.id.tv_popup_share_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new ShareAction(ReimbursementYbDetailsActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
//                        .withText(text)
//                        .withMedia(image)
//                        .withTargetUrl(url)
//                        .share();
                popup.dismiss();
            }
        });
        conentView.findViewById(R.id.tv_popup_share_pyq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new ShareAction(ReimbursementYbDetailsActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
//                        .withText(text)
//                        .withMedia(image)
//                        .withTargetUrl(url)
//                        .share();
                popup.dismiss();
            }
        });
        conentView.findViewById(R.id.tv_popup_share_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new ShareAction(ReimbursementYbDetailsActivity.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
//                        .withText(text)
//                        .withMedia(image)
//                        .withTargetUrl(url)
//                        .share();
                popup.dismiss();
            }
        });


        popup.showPopupWindow(new View(this), Gravity.BOTTOM);

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(ReimbursementYbDetailsActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ReimbursementYbDetailsActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(ReimbursementYbDetailsActivity.this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        switch (what) {
            case HttpConstant.REMOTE_DRUGS_SAVE:
                ToastUtil.showToastCenter(this,"已保存至我的方案");
                break;
        }
    }

    public void requestSave() {
        String ybCity= (String) SharePreferenceUtil.get(ReimbursementYbDetailsActivity.this, SharePreferenceUtil.CITY_CANBAO,"");
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "Home");
        params.put("class", "AddPlan");
        String sign = MD5ChangeUtil.Md5_32("HomeAddPlan" + HttpConstant.APP_SECRET);
        params.put("sign", sign);
        params.put("phone", (String) SharePreferenceUtil.get(this, SharePreferenceUtil.PHONE, ""));
        params.put("DrugNameID", drugBean.getDrugNameID());
        params.put("SpecificationsID", drugBean.getSpecificationsID());
        params.put("VenderID", drugBean.getVenderID());
        params.put("Price", drugBean.getPrice());
        params.put("Yidi", "1");
        params.put("city", ybCity);
        request(params, HttpConstant.REMOTE_DRUGS_SAVE);
    }

    /**
     * 药品信息
     */
    private void initPopupwindow() {
        int w = ScreenUtils.getScreenWidth() - DensityUtils.dp2px(60);
        final BasePopupWindow popup = new BasePopupWindow(this,R.layout.popup_druginfo, w,ViewGroup.LayoutParams.WRAP_CONTENT);
        View conentView = popup.getConentView();
        conentView.findViewById(R.id.iv_popup_druginfo_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        ListView lv = (ListView) conentView.findViewById(R.id.lv_popup_druginfo);
        TextView tv_type = (TextView) conentView.findViewById(R.id.tv_druginfo_type);
        TextView tv_drug = (TextView) conentView.findViewById(R.id.tv_druginfo_drug_type);
        if (!TextUtils.isEmpty(drugBean.getyType())) {
            tv_type.setText("医保" + drugBean.getyType());
            tv_type.setVisibility(View.VISIBLE);
        } else {
            tv_type.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(drugBean.getZy())) {
            tv_drug.setText("" + drugBean.getZy());
            tv_drug.setVisibility(View.VISIBLE);
        } else {
            tv_drug.setVisibility(View.GONE);
        }
        lv.setAdapter(drugsPopupListAdapter);
        popup.showPopupWindow(nextImageView, Gravity.CENTER);
    }

    @Override
    public void error(String errorMsg) {
        ToastUtil.showToastCenter(this,"该方案已存在");
    }
}


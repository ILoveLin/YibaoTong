package com.lzyc.ybtappcal.activity.reimbursement;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.top.ScanResultActivity;
import com.lzyc.ybtappcal.adapter.TypeJobStatusAdapter;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.bean.DrugInfo;
import com.lzyc.ybtappcal.bean.TypeJobStatus;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.ScreenUtils;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.StringUtils;
import com.lzyc.ybtappcal.view.WaveProgressView;
import com.lzyc.ybtappcal.widget.RiseNumberTextView;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 报销查询
 * Created by lxx on 2017/4/18.
 */
public class ReimbursementDetailsActivity extends BaseActivity {
    private static final String TAG=ReimbursementDetailsActivity.class.getSimpleName();
    @BindView(R.id.scroll_all)
    ScrollView scrollAll;
    @BindView(R.id.ib_left)
    ImageButton sresultTitlebarLeft;
    @BindView(R.id.tv_content)
    TextView sresultTitlebarContent;
    @BindView(R.id.iv_arrow)
    ImageView sresultTitlebarContentIv;
    @BindView(R.id.ib_right)
    ImageButton sresultTitlebarRight;
    @BindView(R.id.rel_titlebar)
    RelativeLayout sresultTitlebarRelative;
    @BindView(R.id.img_drug)
    ImageView imgDrug;
    @BindView(R.id.img_drug_type)
    ImageView imgDrugType;
    @BindView(R.id.tv_drug_name)
    TextView tvDrugName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_vender)
    TextView tvVender;
    @BindView(R.id.tv_baoxiao)
    TextView tvBaoxiao;
    @BindView(R.id.img_zifei)
    ImageView imgZifei;
    @BindView(R.id.img_yy)
    ImageView imgYy;
    @BindView(R.id.tv_balance_old_yy)
    TextView tvBalanceOldYy;
    @BindView(R.id.round_left_yy)
    ImageView roundLeftYy;
    @BindView(R.id.tv_balance_new_yy)
    TextView tvBalanceNewYy;
    @BindView(R.id.round_right_yy)
    ImageView roundRightYy;
    @BindView(R.id.wave_hosiptal)
    WaveProgressView waveHosiptal;
    @BindView(R.id.tv_return_yy)
    RiseNumberTextView tvReturnYy;
    @BindView(R.id.frame_yy)
    FrameLayout frameYy;
    @BindView(R.id.lin_hosptial)
    LinearLayout linHosptial;
    @BindView(R.id.img_sq)
    ImageView imgSq;
    @BindView(R.id.tv_balance_old_sq)
    TextView tvBalanceOldSq;
    @BindView(R.id.round_left_sq)
    ImageView roundLeftSq;
    @BindView(R.id.tv_balance_new_sq)
    TextView tvBalanceNewSq;
    @BindView(R.id.round_right_sq)
    ImageView roundRightSq;
    @BindView(R.id.wave_cummunity)
    WaveProgressView waveCommunity;
    @BindView(R.id.tv_return_sq)
    RiseNumberTextView tvReturnSq;
    @BindView(R.id.frame_sq)
    FrameLayout frameSq;
    @BindView(R.id.lin_shequ)
    LinearLayout linShequ;
    @BindView(R.id.tv_conditions_visibility)
    TextView tvConditionsVisibility;
    @BindView(R.id.tv_conditions)
    TextView tvConditions;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.lin_conditions)
    LinearLayout linConditions;
    @BindView(R.id.tv_see_hospital)
    TextView tvSeeHospital;
    @BindView(R.id.linear_pop_top)
    LinearLayout popLinearTop;

    PopupWindow popupWindow;

    PopupWindow popJob;
    TypeJobStatusAdapter typeJobStatusAdapter;
    String typeJob;

    private static final int one = 0X0001;
    private int progress;
    private int progress_sq;
    private int progress_yy;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress++;
            switch (msg.what) {
                case one:
                    if (progress <= progress_sq) {
                        waveCommunity.setCurrent(progress, "");
                        sendEmptyMessageDelayed(one, 100);
                    }
                    if (progress <= progress_yy) {
                        waveHosiptal.setCurrent(progress, "");
                        sendEmptyMessageDelayed(one, 100);
                    }
                    break;
            }
        }
    };

    private DrugBean drugBean;
    boolean flag = true;

    private String cbCity;//参保城市
    private String jzCity;//就诊城市

    private Context mContext;

    @Override
    public int getContentViewId() {
        return R.layout.activity_reimbursement_detail;
    }

    @Override
    protected void init() {

        setTitleBarVisibility(View.GONE);

        setPageStateView(scrollAll);

        showLoading();

        mContext = this;

        cbCity = SharePreferenceUtil.get(mContext, SharePreferenceUtil.PROVINCE_CANBAO, "") + "";
        jzCity = SharePreferenceUtil.get(mContext, SharePreferenceUtil.PROVICE_JIUZHEN, "") + "";
        drugBean = (DrugBean) getIntent().getSerializableExtra(Contants.KEY_OBJ_DRUGBEAN);

        sresultTitlebarContent.setText(StringUtils.getYBJobType(mContext));

        initWaves();
        requestDrugDetail();
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

    private void initWaves() {
        waveCommunity.setText("#333333", 80);
        waveCommunity.setWaveColor("#66a5ff");

        waveHosiptal.setText("#333333", 80);
        waveHosiptal.setWaveColor("#66a5ff");

    }

    /**
     * 设置展开更多按钮背景图
     */
    private void setTvMoreDrawable(int img) {
        Drawable drawable = mContext.getResources().getDrawable(img);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvMore.setCompoundDrawables(null, null, drawable, null);
    }

    @OnClick({R.id.tv_more, R.id.tv_see_hospital, R.id.ib_left, R.id.tv_content, R.id.iv_arrow, R.id.ib_right, R.id.rel_titlebar, R.id.rel_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            
            //展开更多
            case R.id.tv_more:
                if (flag) {
                    flag = false;
                    tvConditions.setEllipsize(null); // 展开
                    tvConditions.setSingleLine(flag);
                    setTvMoreDrawable(R.mipmap.icon_forword_less);
                    tvMore.setTextColor(getResources().getColor(R.color.color_4a92f7));
                    tvMore.setText("收起");
                } else {
                    flag = true;
                    tvConditions.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                    tvConditions.setSingleLine(flag);
                    setTvMoreDrawable(R.mipmap.icon_forword_down_little);
                    tvMore.setText("查看更多");
                }
                break;
            
            //查看医院和社区
            case R.id.tv_see_hospital:
                
                Bundle mBundle = new Bundle();
                mBundle.putString("drugNameID", drugBean.getDrugNameID());
                mBundle.putString("venderID", drugBean.getVenderID());
                mBundle.putString("specificationsID", drugBean.getSpecificationsID());
                mBundle.putString("drug_id", drugBean.getDrug_id());
                mBundle.putInt(Contants.KEY_PAGE, Contants.VAL_PAGE_SEARCH_DURUG);
                openActivity(ScanResultActivity.class, mBundle);
                
                break;
            
            //返回
            case R.id.ib_left:
                finish();
                break;
            
            //标题点击
            case R.id.tv_content:
                
            case R.id.iv_arrow:
                if (popJob == null) {
                    LogUtil.d("lxx","219");
                    popupJobType();
                } else {
                    LogUtil.d("lxx","222");
                    dismissPopJob();
                }
                break;
            case R.id.rel_all:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                break;
        }
    }

    /**
     * 弹出职位状态
     */
    private void popupJobType() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(sresultTitlebarContentIv, "rotation", 0F, 180F);
        animator.setDuration(300);
        animator.start();
        popLinearTop.setVisibility(View.VISIBLE);
        View conentView = View.inflate(this, R.layout.popup_type_job, null);
        popJob = new PopupWindow(conentView, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight()-ScreenUtils.getStatusHeight()- DensityUtils.dp2px(50));
        String[] typeJobs = getResources().getStringArray(R.array.type_job);
        String[] typeJobsType = getResources().getStringArray(R.array.type_job_status);
        List<TypeJobStatus> list = new ArrayList<TypeJobStatus>();
        for (int i = 0; i < typeJobs.length; i++) {
            TypeJobStatus s = new TypeJobStatus();
            typeJob = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, Contants.STR_TYPE_JOB);
            s.setTypeJob(typeJobsType[i]);
            if (typeJob.equals(typeJobsType[i])) {
                s.setSelected(true);
                SharePreferenceUtil.put(this, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, s.getTypeJob());
            } else {
                s.setSelected(false);
            }
            s.setDesc(typeJobs[i]);
            list.add(s);
        }
        ListView jobTypeListView = (ListView) conentView.findViewById(R.id.pop_typejob_lv);
        typeJobStatusAdapter = new TypeJobStatusAdapter(this, R.layout.item_pop_type_job, list);
        jobTypeListView.setAdapter(typeJobStatusAdapter);
        jobTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                TypeJobStatus ts = (TypeJobStatus) adapterView.getItemAtPosition(i);
                ts.setSelected(true);
                typeJob = ts.getTypeJob();
                SharePreferenceUtil.put(mContext, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, typeJob);
                typeJobStatusAdapter.updateItem(i, ts);
                sresultTitlebarContent.setText(ts.getDesc());
                dismissPopJob();
                requestDrugDetail();
            }
        });
        popJob.setAnimationStyle(R.style.PopStyleTranslate);
        popJob.showAsDropDown(sresultTitlebarRelative);
        conentView.findViewById(R.id.pop_out).setOnClickListener(typeJobOnClickListener);
    }


    private View.OnClickListener typeJobOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.pop_out:
                    dismissPopJob();
                    break;
            }
        }
    };

    private void dismissPopJob() {
        popLinearTop.setVisibility(View.GONE);
        popJob.dismiss();
        popJob = null;
        ObjectAnimator animator = ObjectAnimator.ofFloat(sresultTitlebarContentIv, "rotation", 180F, 0F);
        animator.setDuration(300);
        animator.start();
    }

    private void initData(DrugInfo drugInfo) {

        //药品内容
        DrugInfo.DrugInfoBean bean = drugInfo.getDrugInfo();
        Picasso.with(mContext)
                .load(bean.getImages())
                .placeholder(R.mipmap.image_empty_bg_white)
                .error(R.mipmap.image_drug_detail_place)
                .into(imgDrug);
        String htmlStr = "<font color='#333333' size='3'>"+bean.getGoodsName()+" "+bean.getName()+"</font>"
                + "<font color='#999999' size='7'><small> "+bean.getSpecifications()+"</small></font>";
        tvDrugName.setText(Html.fromHtml(htmlStr));
        tvPrice.setText(bean.getPrice());
        tvVender.setText(bean.getVender());

        //甲类 乙类
        if ("甲类".equals(drugInfo.getBaoxiaoType())) {
            Picasso.with(mContext).load(R.mipmap.icon_jieguo_jialei).into(imgDrugType);
        } else if("自费".equals(drugInfo.getBaoxiaoType())) {
            Picasso.with(mContext).load(R.mipmap.icon_jieguo_zifei).into(imgDrugType);
        } else {
            Picasso.with(mContext).load(R.mipmap.icon_jieguo_yilei).into(imgDrugType);
        }

        //限制条件
        if (null != drugInfo.getConditions() && !drugInfo.getConditions().isEmpty()) {
            StringBuilder sbr = new StringBuilder();
            for (int i = 0; i < drugInfo.getConditions().size(); i++) {
                if ("无".equals(drugInfo.getConditions().get(i))) {
                    tvConditionsVisibility.setVisibility(View.VISIBLE);
                    linConditions.setVisibility(View.GONE);
                } else {
                    linConditions.setVisibility(View.VISIBLE);
                    sbr.append(drugInfo.getConditions().get(i));
                    tvConditionsVisibility.setVisibility(View.GONE);
                }
            }
            tvConditions.setText(sbr);
        } else {
            tvConditions.setVisibility(View.GONE);
            tvMore.setVisibility(View.GONE);
            tvConditionsVisibility.setVisibility(View.VISIBLE);
        }

        //报销详情
        List<DrugInfo.BeijingBaoxiaoDetailBean> bxBeanList = drugInfo.getBeijingBaoxiaoDetail();

        if (null != bxBeanList && !bxBeanList.isEmpty()) {
            float num_sq = 0;
            float num_yy = 0;

            if (bxBeanList.size() == 1) {
                DrugInfo.BeijingBaoxiaoDetailBean bean1 = bxBeanList.get(0);
                try {
                    num_sq = Float.parseFloat(bean1.getRatio().toString() + "f");
                    progress_sq = (int) Math.floor(Float.parseFloat(bean1.getRatio().toString())*100);

                    progress = 0;
                    waveCommunity.reset();
                    waveHosiptal.reset();
                    waveHosiptal.setmWaveSpeed(70);
                    waveCommunity.setmWaveSpeed(70);
                    waveCommunity.setCurrent(progress, "");
                    handler.sendEmptyMessageDelayed(one, 20);
                } catch (Exception e) {
                }

                if ("医院".equals(bxBeanList.get(0).getType())) {
                    linHosptial.setVisibility(View.VISIBLE);
                    tvBalanceOldYy.setText("¥"+bean1.getYPrice());
                    tvBalanceNewYy.setText("¥"+bean1.getBxPrice());
                    Picasso.with(mContext).load(R.mipmap.icon_word_baoxiao).into(imgYy);
                    setRatio(tvReturnYy, num_yy, 5000);
                } else {
                    tvBalanceOldYy.setText("¥"+bean1.getYPrice());
                    tvBalanceNewYy.setText("¥"+bean1.getBxPrice());
                    setRatio(tvReturnSq, num_sq, 5000);
                    Picasso.with(mContext).load(R.mipmap.icon_word_baoxiao).into(imgSq);
                    linShequ.setVisibility(View.VISIBLE);
                }
            } else {
                linHosptial.setVisibility(View.VISIBLE);
                linShequ.setVisibility(View.VISIBLE);

                DrugInfo.BeijingBaoxiaoDetailBean beanSheQu = bxBeanList.get(1);
                DrugInfo.BeijingBaoxiaoDetailBean beanHospital = bxBeanList.get(0);

                tvBalanceOldSq.setText("¥"+beanSheQu.getYPrice());
                tvBalanceNewSq.setText("¥"+beanSheQu.getBxPrice());

                tvBalanceOldYy.setText("¥"+beanHospital.getYPrice());
                tvBalanceNewYy.setText("¥"+beanHospital.getBxPrice());

                try {
                    num_sq = Float.parseFloat(beanSheQu.getRatio().toString() + "f");
                    num_yy = Float.parseFloat(beanHospital.getRatio().toString() + "f");
                    progress_sq = (int) Math.floor(Float.parseFloat(beanSheQu.getRatio().toString())*100);
                    progress_yy = (int) Math.floor(Float.parseFloat(beanHospital.getRatio().toString())*100);

                    progress = 0;
                    waveCommunity.reset();
                    waveHosiptal.reset();
                    waveHosiptal.setmWaveSpeed(70);
                    waveHosiptal.setCurrent(progress, "");
                    waveCommunity.setCurrent(progress, "");
                    waveCommunity.setmWaveSpeed(70);
                    handler.sendEmptyMessageDelayed(one, 20);
                } catch (Exception e) {
                }

                setRatio(tvReturnSq, num_sq, 2000);
                setRatio(tvReturnYy, num_yy, 2000);
            }
        } else {
            linShequ.setVisibility(View.GONE);
            linHosptial.setVisibility(View.GONE);
        }

        //是否自费  1 自费  0 报销
//        自费。是。不报销；   否。北京  卡片。  非北京。 报销。部分报销。  甲类。报销。乙类 部分报销
//        参保北京  就诊非北京 卡片显示医院图片
        if (0 == drugInfo.getZifei()) {
            //参保城市  北京  就诊城市  北京  不显示报销
            if (cbCity.contains("北京")) {
                imgZifei.setVisibility(View.GONE);
                tvBaoxiao.setText("报销详情");
            } else {
                if("甲类".equals(drugInfo.getBaoxiaoType())){
                    Picasso.with(mContext).load(R.mipmap.icon_jieguo_baoxiao).into(imgZifei);
                } else {
                    Picasso.with(mContext).load(R.mipmap.icon_jieguo_bufenbaoxiao).into(imgZifei);
                }
            }

        } else {
            Picasso.with(mContext).load(R.mipmap.icon_jieguo_bubaoxiao).into(imgZifei);
        }

        //查看医院和社区
        if (0 != drugInfo.getHospitalCount()) {
            tvSeeHospital.setVisibility(View.VISIBLE);
        } else if(0 == drugInfo.getHospitalCount()){
            tvSeeHospital.setVisibility(View.GONE);
        }

        //限制条件
        tvConditions.post(new Runnable() {
            @Override
            public void run() {
                if(1 == tvConditions.getLineCount()){
                    tvMore.setVisibility(View.GONE);
                } else if(1 < tvConditions.getLineCount()) {
                    tvMore.setVisibility(View.VISIBLE);
                }
            }
        });

        //参保城市非北京 标题栏显示普通文字
        if(!cbCity.contains("北京")){
            sresultTitlebarContent.setText("报销详情");
            sresultTitlebarContent.setTextColor(getResources().getColor(R.color.color_333333));
            sresultTitlebarContent.setTextSize(18);
            sresultTitlebarContentIv.setVisibility(View.GONE);
            sresultTitlebarContent.setClickable(false);
            sresultTitlebarContent.setEnabled(false);
            sresultTitlebarContentIv.setClickable(false);
            sresultTitlebarContentIv.setEnabled(false);
        }

    }

    private void setRatio(RiseNumberTextView v, float num, int duration) {
        v.withNumber(num*100);
        v.setDuration(1300);
        v.setTextColorAndSize(R.color.color_333333, 17);
        v.setPercent("%");
        v.start();
    }

    @Override
    protected void onClickRetry() {
        showLoading();
        requestDrugDetail();
    }

    private void requestDrugDetail() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_BAOXIAO);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.BAOXIAO_DETAIL_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.BAOXIAO_DETAIL_SIGN);
        params.put(HttpConstant.APP_UID, SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString());

        params.put(HttpConstant.PARAM_KEY_ID_DRUG, drugBean.getDrug_id());
        params.put(HttpConstant.PARAM_KEY_ID_DRUGNAME, drugBean.getDrugNameID());

        params.put(HttpConstant.PARAM_KEY_ID_SPECIFICATIONS, drugBean.getSpecificationsID());
        params.put(HttpConstant.PARAM_KEY_ID_VENDER, drugBean.getVenderID());
        params.put(HttpConstant.BAOXIAO_DETAIL_JZCITY, jzCity);
        params.put(HttpConstant.BAOXIAO_DETAIL_CBCITY, cbCity);
        params.put(HttpConstant.BAOXIAO_DETAIL_TBTYPE, SharePreferenceUtil.get(mContext, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, "ZZ00") + "");

        if (!NetworkUtil.CheckConnection(mContext)) {
            showError();
        }

        request(params, HttpConstant.BAOXIAO_INFO);

        LogUtil.d("lxx", "479-->" + params.toString());
    }

    @Override
    public void done(String msg, int what, JSONObject response) {
        super.done(msg, what, response);
        try {
            JSONObject data = response.getJSONObject(HttpConstant.DATA);
            LogUtil.d("lxx", "159-->" + data.toString());

            DrugInfo drugInfo = new Gson().fromJson(data.toString(), DrugInfo.class);

            initData(drugInfo);

            showContent();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

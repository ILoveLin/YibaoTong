package com.lzyc.ybtappcal.fragment.adopt;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseFragment;
import com.lzyc.ybtappcal.bean.BYDrugDetailBean;
import com.lzyc.ybtappcal.view.WaveProgressView;
import com.lzyc.ybtappcal.widget.RiseNumberTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;

/**
 * 医保信息
 */
public class YBInfoFragment extends BaseFragment {

    @BindView(R.id.v_hosp_right_num_big)
    View vHRBig;
    @BindView(R.id.v_hosp_right_num_small)
    View vHRSmall;
    @BindView(R.id.v_hosp_left_num_big)
    View vHLBig;
    @BindView(R.id.v_hosp_left_num_small)
    View vHLSmall;
    @BindView(R.id.tv_hosp_balance_old)
    TextView tvHBalanceOld;
    @BindView(R.id.tv_hosp_balance_pay)
    TextView tvHBalancePay;

    @BindView(R.id.tv_bx_detail)
    TextView tvBxDetail;
    @BindView(R.id.img_community)
    ImageView imgCommunity;
    @BindView(R.id.v_comm_left_num_small)
    View vCommLeftNumSmall;
    @BindView(R.id.v_comm_right_num_small)
    View vCommRightNumSmall;
    @BindView(R.id.tv_comm_balance_old)
    TextView tvCommBalanceOld;
    @BindView(R.id.v_comm_left_num_big)
    View vCommLeftNumBig;
    @BindView(R.id.tv_comm_balance_pay)
    TextView tvCommBalancePay;
    @BindView(R.id.v_comm_right_num_big)
    View vCommRightNumBig;
    @BindView(R.id.img_comm_round)
    ImageView imgCommRound;
    @BindView(R.id.wave_community)
    WaveProgressView waveCommunity;
    @BindView(R.id.tv_comm_return)
    RiseNumberTextView tvCommReturn;
    @BindView(R.id.lin_community)
    LinearLayout linCommunity;
    @BindView(R.id.img_hospital)
    ImageView imgHospital;
    @BindView(R.id.img_hosp_round)
    ImageView imgHospRound;
    @BindView(R.id.wave_hosiptal)
    WaveProgressView waveHosiptal;
    @BindView(R.id.tv_hosp_return)
    RiseNumberTextView tvHospReturn;
    @BindView(R.id.lin_hosptial)
    LinearLayout linHosptial;
    @BindView(R.id.tv_conditions)
    TextView tvConditions;

    int progress;
    int progress_sq;
    int progress_yy;

    static final int one = 0X0001;

    final int HUNDRED = 100;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            progress++;

            try{
                switch (msg.what) {
                    case one:
                        if (progress <= progress_sq) {
                            waveCommunity.setCurrent(progress, "");
                            sendEmptyMessageDelayed(one, HUNDRED);
                        }
                        if (progress <= progress_yy) {
                            waveHosiptal.setCurrent(progress, "");
                            sendEmptyMessageDelayed(one, HUNDRED);
                        }
                        break;
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.fragment_detail_ybinfo;
    }

    @Override
    protected void init() {
        initBundle();
    }

    private void initBundle() {

        Bundle bundle = getArguments();

        BYDrugDetailBean.DataBean drugDetailBean = (BYDrugDetailBean.DataBean) bundle.getSerializable("drugdetailbean");

        String province = bundle.getString("Province");

        initData(drugDetailBean, province);
    }

    /**
     * ① 如果当前省份为北京，并且BaoxiaoType为“报销”的时候，报销详情的样式是展示报销比例，也就是那两个球型
     * ② 如果当前省份为北京，并且BaoxiaoType不等于“报销”的时候，那么报销详情那里展示BaoxiaoType文本对应的图标，比如“自费”就对应自费的图标
     * ③ 如果当前省份不为北京的时候，那么直接展示BaoxiaoType文本对应的图标，比如“部分报销”对应部分报销的图标
     */
    private void initData(BYDrugDetailBean.DataBean drugDetailBean, String province) {

        StringBuilder sbr = new StringBuilder();

        for(int i = 0; i < drugDetailBean.getDetail().getConditions().size(); i++){
            sbr.append(drugDetailBean.getDetail().getConditions().get(i));

            if(i != drugDetailBean.getDetail().getConditions().size()-1){
                sbr.append("\r\n");
            }
        }

        tvConditions.setText(sbr.toString());

        String bxType = drugDetailBean.getDetail().getBaoxiaoType();

        if("北京".equals(province)){

            if("报销".equals(bxType)){

                linCommunity.setVisibility(View.VISIBLE);

                linHosptial.setVisibility(View.VISIBLE);

            } else {

                if("自费".equals(bxType)){

                    setDrawableRight(R.mipmap.icon_jieguo_zifei);

                } else if("甲类".equals(bxType)){

                    setDrawableRight(R.mipmap.icon_jieguo_jialei);

                } else if("乙类".equals(bxType)) {

                    setDrawableRight(R.mipmap.icon_jieguo_yilei);
                }
            }
        } else {

            if("报销".equals(bxType)){

                setDrawableRight(R.mipmap.icon_jieguo_baoxiao);

            } else if("不报销".equals(bxType)){

                setDrawableRight(R.mipmap.icon_jieguo_bubaoxiao);

            } else if("部分报销".equals(bxType)) {

                setDrawableRight(R.mipmap.icon_jieguo_bufenbaoxiao);

            } else if("自费".equals(bxType)){

                setDrawableRight(R.mipmap.icon_jieguo_zifei);
            }

        }

        List<BYDrugDetailBean.DataBean.DetailBean.BaoxiaoDetailBean> data = drugDetailBean.getDetail().getBaoxiaoDetail();

        if(data == null || data.isEmpty()) return;

        String yyRatio = "", sqRatio = "";

        for(BYDrugDetailBean.DataBean.DetailBean.BaoxiaoDetailBean bean : data){

            if("医院".equals(bean.getType())){

                tvHBalanceOld.setText("¥" + bean.getPrice());

                tvHBalancePay.setText("¥" + bean.getBxPrice());

                yyRatio = bean.getRatio();

            } else if("社区".equals(bean.getType())){

                tvCommBalanceOld.setText("¥" + bean.getPrice());

                tvCommBalancePay.setText("¥" + bean.getBxPrice());

                sqRatio = bean.getRatio();
            }
        }

        initWave(yyRatio, sqRatio);

        changeLineVisibility();
    }

    private void initWave(String ycount, String scount){

        float numComm = Float.parseFloat(scount + "f");

        float numHosp = Float.parseFloat(ycount + "f");

        progress_sq = (int) Math.floor(numComm * HUNDRED);

        progress_yy = (int) Math.floor(numHosp * HUNDRED);


        progress = 0;

        waveCommunity.reset();

        waveHosiptal.reset();

        waveHosiptal.setmWaveSpeed(30);

        waveHosiptal.setCurrent(progress, "");

        waveCommunity.setCurrent(progress, "");

        waveCommunity.setmWaveSpeed(30);


        mHandler.sendEmptyMessageDelayed(one, 20);

        setRatio(tvCommReturn, numComm, 2000);
        setRatio(tvHospReturn, numHosp, 2000);

        waveCommunity.setText("#000000", 80);
        waveCommunity.setWaveColor("#1cd0ab");

        waveHosiptal.setText("#000000", 80);
        waveHosiptal.setWaveColor("#1cd0ab");
    }

    private void setRatio(RiseNumberTextView v, float num, int duration) {

        v.withNumber(num*HUNDRED);

        v.setDuration(duration);

        v.setTextColorAndSize(R.color.color_333333, 17);

        v.setPercent("%");

        v.start();
    }

    private void setDrawableRight(int img){

        Drawable drawable= getResources().getDrawable(img);

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        tvBxDetail.setCompoundDrawables(null, null, drawable, null);

        tvBxDetail.setText("报销详情：");
    }

    private void changeLineVisibility() {

        if (3 <= tvHBalanceOld.getText().toString().length()) {

            vHLBig.setVisibility(View.GONE);

            vHLSmall.setVisibility(View.VISIBLE);
        } else {

            vHLBig.setVisibility(View.VISIBLE);

            vHLSmall.setVisibility(View.GONE);
        }

        if (3 <= tvHBalancePay.getText().toString().length()) {

            vHRBig.setVisibility(View.GONE);

            vHRSmall.setVisibility(View.VISIBLE);
        } else {

            vHRBig.setVisibility(View.VISIBLE);

            vHRSmall.setVisibility(View.GONE);
        }
    }

    private static final String TAG=YBInfoFragment.class.getSimpleName();

    @Override
    public void onResume() {
        MobclickAgent.onPageStart(TAG );
        super.onResume();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(TAG);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if(mHandler !=null){
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroyView();
    }
}

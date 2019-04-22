package com.lzyc.ybtappcal.adapter;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.DrugDetailBean;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.widget.RiseNumberTextView;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lovelin on 16/8/10.
 */
public class DrugsMessageAdapter extends CommonAdapter<DrugDetailBean> {
    private LinearLayout shequ_anima_pop;
    private RiseNumberTextView shequ_paymyself;
    private TextView shequ_payall;
    private TextView shequ_text;        // 社区 用于动态获取参数的媒介
    private ImageView shequ_imageview;
    private LinearLayout hospital_anima_pop;
    private RiseNumberTextView hospital_paymyself;
    private TextView hospital_payall;
    private TextView imit_conditions;
    private RelativeLayout drugsDetailsMessage;
    private View shequ_go_on;
    private View hospital_go_on;
    private View shequ_go_on_all;
    private View hospital_go_on_all;
    private LinearLayout id_linear_anima_yiyuan_isgone;
    private RelativeLayout id_relat_show_emptyview;
    private LinearLayout emty_linear_shequ_view;
    private LinearLayout id_linear_anima_view;
    private View shequ_go_null;
    private LinearLayout linear_yibao_view_all;
    private LinearLayout emty_linear_info_enmty_view;
    private WebView wb_lv_instruction_info;
    private RelativeLayout id_drugs_details_info;
    private RelativeLayout id_drugs_details_factory;
    private RelativeLayout linear_item_drugs_message;
    private ProgressBar wb_pb;
    private ProgressBar wb_pb_2;

    public DrugsMessageAdapter(Context context, int layoutId, List<DrugDetailBean> datas) {
        super(context, layoutId, datas);
    }


    @Override
    public void convert(ViewHolder helper, DrugDetailBean bean, int position) {
        drugsDetailsMessage = helper.getView(R.id.linear_item_drugs_message);
        initView(helper);
        isOrNotShowEmptyView(bean);
        layout(bean);
        initData(bean);

    }


    /**
     * 无医院采纳的时候显示单个动画或者隐藏动画
     *
     * @param bean
     */
    public void isOrNotShowEmptyView(DrugDetailBean bean) {
        if (provide.equals("北京") || typePage == Contants.VAL_PAGE_MINEPLAN) {
            String sCount = bean.getsCount();
            String yCount = bean.getyCount();
            if (sCount.equals(0 + "") && yCount.equals(0 + "")) {   // 无医院采纳哦
                id_linear_anima_view.setVisibility(View.GONE);
                emty_linear_shequ_view.setVisibility(View.VISIBLE);
            } else if (sCount.equals(0 + "") || yCount.equals(0 + "")) { //有一个医院采纳
                id_linear_anima_yiyuan_isgone.setVisibility(View.GONE);
                if (sCount.equals(0 + "")) {      //社区
                    shequ_text.setText("医院");
                } else {
                }
            } else {   //不变
                id_linear_anima_view.setVisibility(View.VISIBLE);
                emty_linear_shequ_view.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 数字自动增长
     *
     * @param number 数值的目标数(最后的值)
     * @param view   自定义的RiseNumberTextView
     */
    private void startNumberAutoUp(RiseNumberTextView view, String number) {
        view.withNumber(Float.parseFloat(number));
        view.setDuration(1500);
        view.start();
    }

    private void initData(DrugDetailBean bean) {
        if (provide.equals("北京") || typePage == Contants.VAL_PAGE_MINEPLAN) {
            startNumberAutoUp(shequ_paymyself, bean.getShequ());
            startNumberAutoUp(hospital_paymyself, bean.getYiyuan());
            shequ_payall.setText("" + bean.getsPrice());
            hospital_payall.setText("" + bean.getyPrice());
            ArrayList<String> limt = bean.getConditions();
            if (limt != null && limt.size() > 0) {
                StringBuffer sb = new StringBuffer();
                for (String mt : limt) {
                    sb.append(mt);
                }
                imit_conditions.setText("" + sb.toString());
            }
        } else {
            showShouMingShuView(bean.getSpecification());
        }

    }

    /**
     * 说明书
     * @param specification
     */
    private void showShouMingShuView(String specification) {
        if (specification.isEmpty()) {
            emty_linear_info_enmty_view.setVisibility(View.VISIBLE);
            wb_lv_instruction_info.setVisibility(View.GONE);
        } else {
            wb_lv_instruction_info.setVisibility(View.VISIBLE);
            emty_linear_info_enmty_view.setVisibility(View.GONE);
        }
        wb_lv_instruction_info.getSettings().setJavaScriptEnabled(true);
        wb_lv_instruction_info.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        wb_lv_instruction_info.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                wb_pb_2.setProgress(newProgress);
                if (newProgress == 100) {
                    wb_pb_2.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
        wb_lv_instruction_info.loadUrl(specification);
    }

    private void layout(final DrugDetailBean bean) {
        if (provide.equals("北京") ||typePage == Contants.VAL_PAGE_MINEPLAN) {
            shequ_go_on.setVisibility(View.GONE);
            drugsDetailsMessage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    int leftDistance = shequ_text.getWidth() + shequ_imageview.getWidth() + DensityUtils.dp2px(20);
                    int halfWhidth = (int) (shequ_anima_pop.getWidth() * 0.5);
                    int Distance = leftDistance + DensityUtils.dp2px(5) + DensityUtils.dp2px(25) - (int) (shequ_anima_pop.getWidth() * 0.5);   //pop自费 所以的位置距离
                    int Distance2 = leftDistance + DensityUtils.dp2px(7) + DensityUtils.dp2px(60) - (int) (shequ_anima_pop.getWidth() * 0.5);   //pop自费 所以的位置距离
                    //自费
                    Double yiyuan = Double.parseDouble(bean.getYiyuan());
                    Double shequ = Double.parseDouble(bean.getShequ());
                    //原价
                    Double sPrice = Double.parseDouble(bean.getsPrice());
                    Double yPrice = Double.parseDouble(bean.getyPrice());
                    Double y = yiyuan / yPrice;
                    Double s = shequ / sPrice;
                    double vs = (shequ_go_null.getWidth() * s);
                    double vy = (shequ_go_null.getWidth() * y);
                    int start = leftDistance - halfWhidth + DensityUtils.dp2px(5);
                    //播放动画
                    if (y == 1.0 || s == 1.0) {
                        //播放全动画
                        shequ_go_on.setVisibility(View.GONE);
                        shequ_go_on_all.setVisibility(View.VISIBLE);
                        hospital_go_on.setVisibility(View.GONE);
                        hospital_go_on_all.setVisibility(View.VISIBLE);
                        layoutPop((int) (start + (vs - 2)));
                        layoutPop2((int) (start + (vy - 2)));
                        animateOpenShequ(shequ_go_on_all, (int) vs);
                        animateOpenHospital(hospital_go_on_all, (int) vy);
                    } else if (bean.getYiyuan().equals(bean.getShequ())) {
                        shequ_go_on.setVisibility(View.VISIBLE);
                        shequ_go_on_all.setVisibility(View.GONE);
                        hospital_go_on.setVisibility(View.VISIBLE);
                        hospital_go_on_all.setVisibility(View.GONE);
                        //动态设置布局位置
                        layoutPop(Distance);
                        layoutPop2(Distance + 10);
                        animateOpenShequ(shequ_go_on);
                        animateOpenShequ(hospital_go_on);

                    } else {
                        shequ_go_on.setVisibility(View.VISIBLE);
                        shequ_go_on_all.setVisibility(View.GONE);
                        hospital_go_on.setVisibility(View.VISIBLE);
                        hospital_go_on_all.setVisibility(View.GONE);
                        //动态设置布局位置
                        layoutPop(Distance);
                        layoutPop2(Distance2);
                        animateOpenShequ(shequ_go_on);
                        animateOpenHospital(hospital_go_on);
                    }
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        drugsDetailsMessage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        drugsDetailsMessage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }

    //社区动画
    private void animateOpenShequ(View linear_hidden) {
        ValueAnimator animator = createDropAnimator(linear_hidden, 0, DensityUtils.dp2px(25));
        animator.setDuration(1500);
        animator.start();
    }    //社区动画

    private void animateOpenShequ(View linear_hidden, int i) {
        ValueAnimator animator = createDropAnimator(linear_hidden, 0, i);
        animator.setDuration(1500);
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                Integer animatedValue = (Integer) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.width = animatedValue;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private void animateOpenHospital(View linear_hidden) {
        ValueAnimator animator = createDropAnimator2(linear_hidden, 0, DensityUtils.dp2px(58));
        animator.setDuration(1500);
        animator.start();
    }

    private void animateOpenHospital(View linear_hidden, int i) {
        ValueAnimator animator = createDropAnimator2(linear_hidden, 0, i);
        animator.setDuration(1500);
        animator.start();
    }

    private ValueAnimator createDropAnimator2(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                Integer animatedValue = (Integer) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.width = animatedValue;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    //设置自费位置
    private void layoutPop(int distance) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = distance;
        shequ_anima_pop.setLayoutParams(layoutParams);
    }

    private void layoutPop2(int distance) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = distance;
        hospital_anima_pop.setLayoutParams(layoutParams);
    }

    private void initView(ViewHolder drugsDetailsMessage) {
        //医保信息整体
        linear_yibao_view_all = drugsDetailsMessage.getView(R.id.linear_yibao_view_all);
        if (provide.equals("北京") || typePage == Contants.VAL_PAGE_MINEPLAN) {
            linear_item_drugs_message = drugsDetailsMessage.getView(R.id.linear_item_drugs_message);
            linear_yibao_view_all.setVisibility(View.VISIBLE);
            linear_item_drugs_message.setVisibility(View.VISIBLE);
            shequ_anima_pop = drugsDetailsMessage.getView(R.id.id_drugs_detail_shequ_anima_pop);
            id_linear_anima_yiyuan_isgone = drugsDetailsMessage.getView(R.id.id_linear_anima_yiyuan_isgone);
            emty_linear_shequ_view = drugsDetailsMessage.getView(R.id.emty_linear_shequ_view);
            id_linear_anima_view = drugsDetailsMessage.getView(R.id.id_linear_anima_view);
            shequ_paymyself = drugsDetailsMessage.getView(R.id.id_drugs_detail_shequ_paymyself);
            //社区原价
            shequ_payall = drugsDetailsMessage.getView(R.id.id_drugs_detail_shequ_payall);
            shequ_text = drugsDetailsMessage.getView(R.id.id_drugs_detail_shequ_text);
            shequ_imageview = drugsDetailsMessage.getView(R.id.id_drugs_detail_shequ);
            shequ_go_on = drugsDetailsMessage.getView(R.id.shequ_go_on);
            shequ_go_on_all = drugsDetailsMessage.getView(R.id.shequ_go_on_all);
            shequ_go_null = drugsDetailsMessage.getView(R.id.shequ_go_null);

            hospital_go_on = drugsDetailsMessage.getView(R.id.hospital_go_on);
            hospital_go_on_all = drugsDetailsMessage.getView(R.id.hospital_go_on_all);
            hospital_anima_pop = drugsDetailsMessage.getView(R.id.id_drugs_detail_hospital_anima_pop);
            hospital_paymyself = drugsDetailsMessage.getView(R.id.id_drugs_detail_hospital_paymyself);
            //医院原价
            hospital_payall = drugsDetailsMessage.getView(R.id.id_drugs_detail_hospital_payall);
            imit_conditions = drugsDetailsMessage.getView(R.id.id_drugs_detail_limit_conditions);
            id_linear_anima_view.setVisibility(View.VISIBLE);
            emty_linear_shequ_view.setVisibility(View.GONE);
        } else {

            linear_yibao_view_all.setVisibility(View.GONE);
            linear_item_drugs_message = drugsDetailsMessage.getView(R.id.linear_item_drugs_message);
            id_drugs_details_factory = drugsDetailsMessage.getView(R.id.id_drugs_details_factory);
            id_drugs_details_info = drugsDetailsMessage.getView(R.id.id_drugs_details_info);
            //说明书
            emty_linear_info_enmty_view = drugsDetailsMessage.getView(R.id.emty_linear_info_view);
            wb_lv_instruction_info = drugsDetailsMessage.getView(R.id.wb_lv_instruction_info);

            wb_pb = drugsDetailsMessage.getView(R.id.progressbar);
            wb_pb_2 = drugsDetailsMessage.getView(R.id.wb_pb_2);

            id_drugs_details_info.setVisibility(View.VISIBLE);
            linear_item_drugs_message.setVisibility(View.GONE);
            id_drugs_details_factory.setVisibility(View.GONE);

        }


    }

    private String provide;
//    private String comeFromPlanDetail;
    private int typePage;

    public void setCurrentProvince(String currentProvince,  int type) {
        this.provide = currentProvince;
        this.typePage = type;
    }
}

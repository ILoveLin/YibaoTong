package com.lzyc.ybtappcal.activity.top;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.base.BaseActivity;
import com.lzyc.ybtappcal.activity.mine.medicine.AddMemberActivity;
import com.lzyc.ybtappcal.adapter.ResultsAdapter;
import com.lzyc.ybtappcal.adapter.ResultsFamilyMedicineMemberAdapter;
import com.lzyc.ybtappcal.adapter.ResultsSpecificationsAdapter;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.bean.MedicineFamilyBean;
import com.lzyc.ybtappcal.bean.RankingDrugDetail;
import com.lzyc.ybtappcal.bean.RankingInfo;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.listener.OnYbtRefreshListener;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.JsonUtil;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.NetworkUtil;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.StringUtils;
import com.lzyc.ybtappcal.view.MyFoldingCell;
import com.lzyc.ybtappcal.view.RoundedRectListView;
import com.lzyc.ybtappcal.view.dialog.LoadingProgressBar;
import com.lzyc.ybtappcal.view.xlist.XYbtRefreshListView;
import com.lzyc.ybtappcal.volley.entity.ErrorResponse;
import com.lzyc.ybtappcal.widget.popupwindow.BasePopupWindow;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 结果界面
 * Created by yang on 2017/02/15.
 */
public class ResultsActivity extends BaseActivity implements OnYbtRefreshListener, ResultsAdapter.OnItemChildListenr {
    private static final String TAG=ResultsActivity.class.getSimpleName();
    private static final int TIME_OPEN_CLOSE = 500;//展开关闭的时间

    @BindView(R.id.rl_current_drug)
    RelativeLayout rlCurrentDrug;
    @BindView(R.id.linear_title_view)
    LinearLayout linearTitleView;
    @BindView(R.id.top_foldingcell)
    MyFoldingCell topFoldingcell;
    @BindView(R.id.results_lv)
    XYbtRefreshListView resultsLv;
    @BindView(R.id.rl_results)
    RelativeLayout rlResults;
    @BindView(R.id.rl_titlebar)
    RelativeLayout rlTitlebar;
    @BindView(R.id.tv_title_drugname)
    TextView tvTitleDrugname;
    @BindView(R.id.tv_title_vender)
    TextView tvTitleVender;
    @BindView(R.id.linear_title)
    LinearLayout mLinearTitle;
    @BindView(R.id.fold_img_bg)
    ImageView mFoldImgBg;
    @BindView(R.id.linear_sort_renkedu_current)
    LinearLayout lineaSortRenkeduCurrent;
    @BindView(R.id.linear_sort_renkedu)
    LinearLayout lineaSortRenkedu;
    @BindView(R.id.tv_capture_info)
    TextView tvCaptureInfo;
    @BindView(R.id.iv_renkedu_bg)
    ImageView ivRenkeduBg;
    @BindView(R.id.renkedu_desc)
    TextView renkeduDesc;
    @BindView(R.id.linear_content_title_renkedu)
    LinearLayout linearContentTitleRenkedu;
    @BindView(R.id.iv_drug_detail)
    ImageView ivDrugDetail;
    @BindView(R.id.tv_drug_name)
    TextView tvDrugName;
    @BindView(R.id.tv_drug_price_qian)
    TextView tvDrugPriceQian;
    @BindView(R.id.tv_drug_price)
    TextView tvDrugPrice;
    @BindView(R.id.tv_baowujia)
    TextView tvBaoWuJia;
    @BindView(R.id.tv_yy_shoujia)
    TextView tvYyShoujia;
    @BindView(R.id.linear_drug_detail)
    LinearLayout linearDrugDetail;
    @BindView(R.id.results_add_drug)
    TextView resultsAddDrug;
    @BindView(R.id.guige_desc)
    TextView guigeDesc;
    @BindView(R.id.results_recycler_specifications)
    RecyclerView resultsRecyclerSpecifications;
    @BindView(R.id.tv_adopt)
    TextView tvAdopt;
    @BindView(R.id.tv_shequ)
    TextView tvShequ;
    @BindView(R.id.tv_sq_price)
    TextView tvSqPrice;
    @BindView(R.id.linear_price_shequ)
    LinearLayout linearPriceShequ;
    @BindView(R.id.pb_shequ)
    ProgressBar pbShequ;
    @BindView(R.id.iv_price_shequ)
    ImageView ivPriceShequ;
    @BindView(R.id.linear_yiyuan)
    LinearLayout linearYiyuan;
    @BindView(R.id.tv_yiyuan)
    TextView tvYiyuan;
    @BindView(R.id.tv_yy_price)
    TextView tvYyPrice;
    @BindView(R.id.linear_price_yiyuan)
    LinearLayout linearPriceYiyuan;
    @BindView(R.id.pb_yiyuan)
    ProgressBar pbYiyuan;
    @BindView(R.id.iv_price_yiyuan)
    ImageView ivPriceYiyuan;
    @BindView(R.id.tv_baoxiao)
    TextView tvBaoxiao;
    @BindView(R.id.cell_mengban)
    View cellMengban;
    @BindView(R.id.view_clickable)
    View viewClickable;
    @BindView(R.id.rel_sqyy)
    RelativeLayout relSqyy;
    @BindView(R.id.iv_fold_shadow)
    ImageView foldShadow;
    @BindView(R.id.tv_title_drugname_current)
    TextView tvTitleDrugnameCurrent;
    @BindView(R.id.linear_sq)
    LinearLayout linearSq;
    private String ranking;
    private Bundle mBundle;
    private int typePage;
    private int pageIndex = 0;
    private ResultsAdapter mAdapter;//
    private String drugId, currentProvince;
    private DrugBean drugBean;
    private BasePopupWindow popupWindow;
    private MedicineFamilyBean familyBean;
    private static final int REQ_FAMILY_MEMBER_ADD = 100;
    private ResultsFamilyMedicineMemberAdapter adapter;//家庭药箱成员列表
    private RoundedRectListView mListView;
    private LinearLayout popAddMemberLinear, popAddMember;
    private RelativeLayout popAddMemberLinearOk;
    private View viewBottom;
    private View whiteMengban;
    private ImageView emptyMember;
    private TextView textView;
    private Handler mHandler = new Handler();
    private LayoutInflater mLayoutInflater;
    private FrameLayout headerLinearTitleView;
    private LinearLayout headerLinearSortRenkedu;
    private TextView headerTvTitleDrugname;
    private TextView headerTvTitleVender;
    private long firstRequestTime;
    private int currrentPosition;
    private LoadingProgressBar loaddingBar;
    private RankingInfo currentRankingInfo;
    private List<RankingInfo> mDatas;
    private String memberName;
    private View viewFooter;

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Contants.ACTION_LOGION_SUCCESS)){
                UID = SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString();
                if (!TextUtils.isEmpty(UID)) {
                    requestMemberList();
                }
            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_results;
    }

    @Override
    protected void init() {
        registerBroadcastReceiver();
        setTitleBarVisibility(View.GONE);
        setPageStateView(resultsLv);
        loaddingBar = new LoadingProgressBar(mContext);
        loaddingBar.show();
        currentProvince = (String) SharePreferenceUtil.get(ResultsActivity.this, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "北京");
        mBundle = getIntent().getExtras();
        typePage = mBundle.getInt(Contants.KEY_PAGE_RESULTS, Contants.VAL_PAGE);
        drugBean = (DrugBean) mBundle.getSerializable(Contants.KEY_OBJ_DRUGBEAN);
        mLayoutInflater = getLayoutInflater();
        switch (typePage) {
            case Contants.VAL_PAGE_SEARCH_DURUG:
                break;
            case Contants.VAL_PAGE_SEARCH_SCAN:
                break;
        }
        drugId = drugBean.getDrugID();
        if (TextUtils.isEmpty(drugId)) {
            drugId = "";
        }
        UID = SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString();
        firstRequestTime = System.currentTimeMillis();
        rlCurrentDrug.setVisibility(View.INVISIBLE);
        mFoldImgBg.setVisibility(View.INVISIBLE);
        UID = SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString();
        if (!TextUtils.isEmpty(UID)) {
            requestMemberList();
        }
        requestResultsDrugCurrent();
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Contants.ACTION_LOGION_SUCCESS);
        registerReceiver(receiver,intentFilter);
    }

    private void initListView() {
        resultsLv.setPullLoadEnable(true);
        resultsLv.setPullRefreshEnable(false);
        resultsLv.setLoadStyle(1);
        View headerView = mLayoutInflater.inflate(R.layout.header_results, null);
        headerLinearSortRenkedu = (LinearLayout) headerView.findViewById(R.id.linear_sort_renkedu_header);
        headerLinearTitleView = (FrameLayout) headerView.findViewById(R.id.header_title_view);
        headerTvTitleDrugname = (TextView) headerView.findViewById(R.id.tv_title_drugname_header);
        headerTvTitleVender = (TextView) headerView.findViewById(R.id.tv_title_vender_header);
        headerTvTitleVender.setText("" + currentRankingInfo.getVender());
        headerLinearSortRenkedu.removeAllViews();
        for (int i = 0; i < ranking.length(); i++) {
            View view = new View(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(DensityUtils.dp2px(14), DensityUtils.dp2px(19));
            char numChar = ranking.charAt(i);
            String numStr = String.valueOf(numChar);
            int res = getCurrentNumRes(Integer.parseInt(numStr));
            view.setBackgroundResource(res);
            view.setLayoutParams(layoutParams);
            headerLinearSortRenkedu.addView(view);
        }
        String name = currentRankingInfo.getGoodsName();
        if (TextUtils.isEmpty(name)) {
            name = currentRankingInfo.getName();
            headerTvTitleDrugname.setText("" + name);
        } else {
            headerTvTitleDrugname.setText(StringUtils.getSpannableText(name, " " + currentRankingInfo.getName(), 14, "#999ea1"));
        }
        resultsLv.addHeaderView(headerView, null, false);
        mAdapter = new ResultsAdapter(this, R.layout.item_results2, mDatas);
        mAdapter.setDrugId(drugId);
        mAdapter.setCurrentRenkeduPosition(ranking);
        View footerView=mLayoutInflater.inflate(R.layout.footer_results,null);
        viewFooter=footerView.findViewById(R.id.view_footer);
        resultsLv.addFooterView(footerView,null,false);
        resultsLv.setAdapter(mAdapter);
        mAdapter.setOnItemChildListenr(this);
        resultsLv.setOnRefreshListener(this);
        viewClickable.setClickable(false);
        headerLinearTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewClickable.setClickable(true);
                if (rlCurrentDrug.getVisibility() == View.INVISIBLE) {
                    rlCurrentDrug.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            topHeaderUnfold();
                        }
                    }, 500);
                }
            }
        });
        linearTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewClickable.setClickable(true);
                topHeaderUnfold();
            }
        });
        headerLinearTitleView.setVisibility(View.INVISIBLE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                headerLinearTitleView.setVisibility(View.VISIBLE);
                resultsLv.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if(scrollState==SCROLL_STATE_IDLE){
                            viewFooter.setVisibility(View.VISIBLE);
                        }else if(scrollState==SCROLL_STATE_TOUCH_SCROLL){
                            viewFooter.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (firstVisibleItem > 0) {
                            rlCurrentDrug.setVisibility(View.VISIBLE);
                            mFoldImgBg.setVisibility(View.VISIBLE);
                            foldShadow.setVisibility(View.VISIBLE);
                        } else {
                            mFoldImgBg.setVisibility(View.INVISIBLE);
                            rlCurrentDrug.setVisibility(View.INVISIBLE);
                            foldShadow.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }, 1000);
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
     * 顶部展开
     * true 自动展开
     * false手动展开
     *
     * @param autoOpen
     */
    private final void unFoldCellAnimation(final boolean autoOpen) throws Exception {
        foldShadow.setVisibility(View.GONE);
        cellMengban.setVisibility(View.VISIBLE);
        lineaSortRenkedu.setAlpha(1f);
        lineaSortRenkedu.setVisibility(View.VISIBLE);
        ValueAnimator va = ValueAnimator.ofInt(mLinearTitle.getHeight(), mLinearTitle.getHeight() + DensityUtils.dp2px(55));
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                try{
                    int h = (Integer) valueAnimator.getAnimatedValue();
                    mLinearTitle.getLayoutParams().height = h;
                    mLinearTitle.requestLayout();
                    rlCurrentDrug.getLayoutParams().height = h;
                    rlCurrentDrug.requestLayout();
                }catch (Exception e){

                }
            }
        });
        ObjectAnimator animatorRenkeduCurrent = ObjectAnimator.ofFloat(lineaSortRenkeduCurrent, "alpha", 0f, 1f);
        ObjectAnimator animatorRenkedu = ObjectAnimator.ofFloat(lineaSortRenkedu, "alpha", 1f, 0f);
        ObjectAnimator animatorMengban = ObjectAnimator.ofFloat(cellMengban, "alpha", 0f, 1f);
        ObjectAnimator animatorDrugnameCurrent = ObjectAnimator.ofFloat(tvTitleDrugnameCurrent, "alpha", 0f, 1f);
        ObjectAnimator animatorDrugnam = ObjectAnimator.ofFloat(tvTitleDrugname, "alpha", 1f, 0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animatorRenkeduCurrent).with(animatorRenkedu).with(va).with(animatorMengban).with(animatorDrugnameCurrent).with(animatorDrugnam);
        animSet.setDuration(TIME_OPEN_CLOSE);
        animSet.start();
        animSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                viewClickable.setClickable(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                lineaSortRenkedu.setAlpha(0f);
                lineaSortRenkedu.setVisibility(View.INVISIBLE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (autoOpen) {
                            autoShowFoldCell();
                        } else {
                            topFoldingcell.unfold(false, null);
                            viewClickable.setClickable(false);
                        }
                        //解决返回时，父控件不拉伸
                        ViewGroup.LayoutParams layoutParams = rlCurrentDrug.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        rlCurrentDrug.setLayoutParams(layoutParams);
                    }
                }, 200);
            }
        });
    }

    /**
     * 顶部收起
     * true 自动收起
     * false手动收起
     *
     * @param autoClose
     */
    public void foldCellAnimation(final boolean autoClose) throws Exception {
        ValueAnimator va = ValueAnimator.ofInt(mLinearTitle.getHeight(), mLinearTitle.getHeight() - DensityUtils.dp2px(55));
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int h = (Integer) valueAnimator.getAnimatedValue();
                mLinearTitle.getLayoutParams().height = h;
                mLinearTitle.requestLayout();
                rlCurrentDrug.getLayoutParams().height = h;
                rlCurrentDrug.requestLayout();
            }
        });
        lineaSortRenkedu.setVisibility(View.VISIBLE);
        lineaSortRenkedu.setAlpha(0f);
        ObjectAnimator animatorRenkeduCurrent = ObjectAnimator.ofFloat(lineaSortRenkeduCurrent, "alpha", 1f, 0f);
        ObjectAnimator animatorRenkedu = ObjectAnimator.ofFloat(lineaSortRenkedu, "alpha", 0f, 1f);
        ObjectAnimator animatorMengban = ObjectAnimator.ofFloat(cellMengban, "alpha", 1f, 0f);
        ObjectAnimator animatorDrugnameCurrent = ObjectAnimator.ofFloat(tvTitleDrugnameCurrent, "alpha", 1f, 0f);
        ObjectAnimator animatorDrugnam = ObjectAnimator.ofFloat(tvTitleDrugname, "alpha", 0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animatorRenkeduCurrent).with(animatorRenkedu).with(va).with(animatorMengban).with(animatorDrugnameCurrent).with(animatorDrugnam);
        animSet.setDuration(TIME_OPEN_CLOSE);
        animSet.start();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                viewClickable.setClickable(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                cellMengban.setVisibility(View.GONE);
                foldShadow.setVisibility(View.VISIBLE);
                if (autoClose) {
                    fillData();
                    topFoldingcell.samSungReFold();
                } else {
                    viewClickable.setClickable(false);
                }
            }
        });
    }

    /**
     * 顶部手动展开
     */
    public void topHeaderUnfold() {
        try {
            viewClickable.setClickable(true);
            unFoldCellAnimation(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 顶部手动收起
     */
    public void topHeaderFold() {
        viewClickable.setClickable(true);
        topFoldingcell.fold(false, new MyFoldingCell.FoldAnimListener() {
            @Override
            public void foldAnimEnd() {
                try {
                    foldCellAnimation(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 弹窗，添加药箱
     *
     * @param drugId
     * @param count
     */
    private void popupWindowDrugAdd(final String drugId, int count) {
        popupWindow = new BasePopupWindow(this, R.layout.pop_drug_add_member, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showPopupWindow(resultsLv, Gravity.CENTER);
        View conentView = popupWindow.getConentView();
//        TwinklingRefreshLayout twinklingRefreshLayout = (TwinklingRefreshLayout) conentView.findViewById(R.id.twinklingRefreshLayout);
//        twinklingRefreshLayout.setPureScrollModeOn();
        popAddMemberLinear = (LinearLayout) conentView.findViewById(R.id.pop_add_member_linear);
        popAddMemberLinearOk = (RelativeLayout) conentView.findViewById(R.id.pop_add_member_linear_ok);
        mListView = (RoundedRectListView) conentView.findViewById(R.id.listview);
        whiteMengban = conentView.findViewById(R.id.white_mengban);
        popAddMember = (LinearLayout) conentView.findViewById(R.id.pop_add_member);
        ImageView popCancel = (ImageView) conentView.findViewById(R.id.pop_cancel);
        emptyMember = (ImageView) conentView.findViewById(R.id.empty_member);
        popAddMemberLinear.setOnClickListener(popOnClickListener);
        viewBottom = conentView.findViewById(R.id.view_bottom);
        popCancel.setOnClickListener(popOnClickListener);
        List<MedicineFamilyBean.ListBean> list = familyBean.getList();
        if (list.isEmpty()) {
            list = new ArrayList<>();
        }
        adapter = new ResultsFamilyMedicineMemberAdapter(mContext, R.layout.item_results_family_medicine_member, list);
        popAddMember.setBackgroundResource(0);
        View view = View.inflate(mContext, R.layout.header_results_member, null);
        textView = (TextView) view.findViewById(R.id.textview);
        mListView.addFooterView(view, null, false);
        if (count >= 10) {
            popAddMemberLinear.setClickable(false);
            popAddMemberLinearOk.setClickable(true);
            popAddMemberLinearOk.setVisibility(View.VISIBLE);
            popAddMemberLinear.setVisibility(View.GONE);
            popAddMember.setVisibility(View.GONE);
            viewBottom.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        } else {
            viewBottom.setVisibility(View.VISIBLE);
            popAddMemberLinear.setClickable(true);
            popAddMemberLinearOk.setClickable(false);
            popAddMember.setVisibility(View.VISIBLE);
            popAddMemberLinearOk.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);

        }
//        mListView.addFooterView(view, null, false);
//        mListView.addHeaderView(view, null, false);
        mListView.setAdapter(adapter);
        if (TextUtils.isEmpty(drugId)) {
            whiteMengban.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            popAddMember.setBackgroundResource(R.drawable.shape_drugadd_member);
            emptyMember.setVisibility(View.VISIBLE);
        } else {
            emptyMember.setVisibility(View.GONE);
            whiteMengban.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MedicineFamilyBean.ListBean bean = (MedicineFamilyBean.ListBean) parent.getItemAtPosition(position);
                memberName = bean.getNickname();
                loaddingBar.show();
                requestDrugAdd(drugId, bean.getID());
            }
        });
    }

    private View.OnClickListener popOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.pop_add_member_linear:
                    Intent intent = new Intent(ResultsActivity.this, AddMemberActivity.class);
                    startActivityForResult(intent, REQ_FAMILY_MEMBER_ADD);
                    break;
                case R.id.pop_cancel:
                    dismissPopupWindow();
                    break;
            }
        }
    };

    private void dismissPopupWindow() {
        if (null != popupWindow) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    @OnClick({R.id.rel_cancel, R.id.title_back, R.id.results_add_drug, R.id.tv_adopt, R.id.iv_drug_detail, R.id.linear_drug_detail})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.linear_drug_detail:
            case R.id.iv_drug_detail:
                onDrugDetail(currrentPosition, currentRankingInfo);
                break;
            case R.id.rel_cancel:
                topHeaderFold();
                break;
            case R.id.tv_adopt:
                onAdoptDetail(currrentPosition, currentRankingInfo);
                break;
            case R.id.results_add_drug:
                onAddBag(currrentPosition, currentRankingInfo);
                break;
        }
    }

    @Override
    public void onDownPullRefresh() {
        pageIndex = 0;
        requestResultsDrugRanking();
    }

    @Override
    public void onLoadingMore() {
        pageIndex++;
        requestResultsDrugRanking();
    }

    //网络请求

    /**
     * 获取认可度排名
     */
    public void requestResultsDrugRanking() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.RESULRS_CLZ_RANKING);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.RESULRS_SIGN_RANKING);
        params.put(HttpConstant.PARAM_KEY_ID_DRUG2, drugId);
        params.put(HttpConstant.PARAM_KEY_PROVINCE2, currentProvince);
        params.put(HttpConstant.APP_UID, UID);
        if (!NetworkUtil.CheckConnection(this)) {
            showError();
            return;
        }
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX2, "" + pageIndex);
        request(params, HttpConstant.RESULTS_RANKING);
    }


    /**
     * 获取当前药品的数据
     */
    public void requestResultsDrugCurrent() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.DRUG_CURRENRT_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.DRUG_CURRENRT_SIGN);
        params.put(HttpConstant.PARAM_KEY_ID_DRUG2, drugId);
        params.put(HttpConstant.PARAM_KEY_PROVINCE2, currentProvince);
        params.put(HttpConstant.APP_UID, UID);
        params.put(HttpConstant.PARAM_KEY_PAGEINDEX2, "" + pageIndex);
        if (!NetworkUtil.CheckConnection(mContext)) {
            showError();
            loaddingBar.dismiss();
            return;
        }
        request(params, HttpConstant.DRUG_CURRENT);
    }


    /**
     * 家庭成员列表
     */
    private void requestMemberList() {
        Map<String, String> params = new HashMap<>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_MEDICINE_CHEST_MEMBER_LIST_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_MEDICINE_CHEST_MEMBER_LIST_SIGN);
        params.put(HttpConstant.APP_UID, UID);
        if (!NetworkUtil.CheckConnection(mContext)) {
            return;
        }
        request(params, HttpConstant.HOME_MEDICINE_CHEST_MEMBER_LIST);
    }

    /**
     * 添加药箱
     */
    private void requestDrugAdd(String drugId, String memberId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpConstant.PARAM_KEY_APP, HttpConstant.APP_HOME);
        params.put(HttpConstant.PARAM_KEY_CLASS, HttpConstant.HOME_DRUG_ADD_CLZ);
        params.put(HttpConstant.PARAM_KEY_SIGN, HttpConstant.HOME_DRUG_ADD_SIGN);
        params.put(HttpConstant.MEMBERID, memberId);
        params.put(HttpConstant.DRUGID, drugId);
        if (!NetworkUtil.CheckConnection(mContext)) {
            showToast("网络不给力");
            loaddingBar.dismiss();
            dismissPopupWindow();
            return;
        }
        request(params, HttpConstant.MINE_DURG_BAG_ADD);
    }


    @SuppressLint("NewApi")
    @Override
    public void done(String msg, int what, JSONObject response) {
        switch (what) {
            case HttpConstant.DRUG_CURRENT:
                loaddingBar.dismiss();
                try {
                    JSONObject joData = response.getJSONObject(HttpConstant.DATA);
                    final Object current = joData.get(HttpConstant.CURRENT);
                    if (current.toString().length() > 3) {
                        currentRankingInfo = JsonUtil.getModle(current.toString(), RankingInfo.class);
                        if (currentRankingInfo != null) {
                            ranking = currentRankingInfo.getRanking();
                            requestResultsDrugRanking();
                            pushCurrentView();
                        } else {
                            showErrorServer();
                        }
                    } else {
                        showErrorServer();
                    }
                } catch (JSONException e) {
                    showErrorServer();
                } catch (Exception e) {
                    showErrorServer();
                }
                break;
            case HttpConstant.RESULTS_RANKING:
                try {
                    JSONObject joData = response.getJSONObject(HttpConstant.DATA);
                    Type type = new TypeToken<ArrayList<RankingInfo>>() {
                    }.getType();
                    mDatas = JsonUtil.getListModle(joData.toString(), HttpConstant.LIST, type);
                    if (pageIndex != 0) {
                        if (mDatas.isEmpty()) {
                            showToast("没有更多数据了");
                            pageIndex = pageIndex - 1;
                        } else {
                            mAdapter.addList(mDatas);
                        }
                        resultsLv.onRefreshComplete();
                    }
                } catch (Exception e) {
                    if (pageIndex > 0) {
                        pageIndex--;
                    }
                }
                break;
            case HttpConstant.HOME_MEDICINE_CHEST_MEMBER_LIST:
                try {
                    JSONObject data = response.getJSONObject(HttpConstant.DATA);
                    familyBean = new Gson().fromJson(data.toString(), MedicineFamilyBean.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case HttpConstant.MINE_DURG_BAG_ADD:
                loaddingBar.dismiss();
//                try {
//                    showToast("" + response.getJSONObject(HttpConstant.DATA).getString("Message").toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                showToast("已添加到" + memberName + "的药箱");
                dismissPopupWindow();
                break;

        }
    }

    /**
     * 从左边推出当前药品
     */
    private void pushCurrentView() {
        viewClickable.setClickable(true);
        List<RankingDrugDetail> rankingDrugDetailList = currentRankingInfo.getRankingDrugDetailList();
        for (int i = 0; i < rankingDrugDetailList.size(); i++) {
            RankingDrugDetail mRankingDrugDetail = rankingDrugDetailList.get(i);
            if (drugId.equals(mRankingDrugDetail.getDrugID())) {
                fillCurrent(i);
                break;
            }
        }
        rlCurrentDrug.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(ResultsActivity.this, R.anim.list_anim);
        rlCurrentDrug.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        autoOpenFoldCellAnimation();
                    }
                }, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 自动展开，包含cell拉伸动画
     */
    private void autoOpenFoldCellAnimation() {
        try {
            unFoldCellAnimation(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * fold cell 自动展开，不包含cell拉伸动画
     */
    private void autoShowFoldCell() {
        topFoldingcell.autoOpenAndClose(new MyFoldingCell.FoldAnimListener() {
            @Override
            public void foldAnimEnd() {
                try {
                    foldCellAnimation(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 填充列表数据
     */
    private void fillData() {
        initListView();
    }

    /**
     * 填充当前药品，悬浮view的数据
     *
     * @param position
     */
    private void fillCurrent(int position) {
        ivRenkeduBg.setBackgroundResource(R.drawable.bg_results_show_top_current);
        this.currrentPosition = position;
        RankingDrugDetail drugDetail = currentRankingInfo.getDetail().get(position);
        linearContentTitleRenkedu.removeAllViews();
        LinearLayout mLinearLayout = new LinearLayout(mContext);
        mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        for (int i = 0; i < ranking.length(); i++) {
            View view = new View(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtils.dp2px(14), DensityUtils.dp2px(19));
            view.setLayoutParams(layoutParams);
            char numChar = ranking.charAt(i);
            String numStr = String.valueOf(numChar);
            int res = getNumRes(Integer.parseInt(numStr));
            view.setBackgroundResource(res);
            mLinearLayout.addView(view);
        }
        linearContentTitleRenkedu.addView(mLinearLayout);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) mLinearLayout.getLayoutParams();
        layoutParams1.bottomMargin = DensityUtils.dp2px(4);
        layoutParams1.leftMargin = DensityUtils.dp2px(3);
        mLinearLayout.setLayoutParams(layoutParams1);
        lineaSortRenkeduCurrent.removeAllViews();
        for (int i = 0; i < ranking.length(); i++) {
            View view = new View(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(DensityUtils.dp2px(14), DensityUtils.dp2px(19));
            char numChar = ranking.charAt(i);
            String numStr = String.valueOf(numChar);
            int res = getNumRes(Integer.parseInt(numStr));
            view.setBackgroundResource(res);
            view.setLayoutParams(layoutParams);
            lineaSortRenkeduCurrent.addView(view);
        }
        lineaSortRenkedu.removeAllViews();
        for (int i = 0; i < ranking.length(); i++) {
            View view = new View(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(DensityUtils.dp2px(14), DensityUtils.dp2px(19));
            char numChar = ranking.charAt(i);
            String numStr = String.valueOf(numChar);
            int res = getCurrentNumRes(Integer.parseInt(numStr));
            view.setBackgroundResource(res);
            view.setLayoutParams(layoutParams);
            lineaSortRenkedu.addView(view);
        }
        String vender = currentRankingInfo.getVender();
        String name = currentRankingInfo.getGoodsName();
        if (TextUtils.isEmpty(name)) {
            name = currentRankingInfo.getName();
            tvDrugName.setText("" + name);
            tvTitleDrugname.setText(name);
            tvTitleDrugnameCurrent.setText(name);
        } else {
            tvDrugName.setText(name + " " + currentRankingInfo.getName());
            tvTitleDrugname.setText(StringUtils.getSpannableText(name, " " + currentRankingInfo.getName(), 14, "#999ea1"));
            tvTitleDrugnameCurrent.setText(StringUtils.getSpannableText(name, " " + currentRankingInfo.getName(), 14, "#999ea1"));
        }
        tvTitleVender.setText("" + vender);
        Picasso.with(mContext).load(drugDetail.getImage()).placeholder(R.mipmap.image_cache_drug)
                .error(R.mipmap.image_cache_drug).into(ivDrugDetail);
        tvDrugPrice.setText("" + drugDetail.getPrice());
        List<RankingDrugDetail> rankingDrugDetailList = currentRankingInfo.getRankingDrugDetailList();
        drugDetail.setSelected(true);
        rankingDrugDetailList.set(position, drugDetail);
        final ResultsSpecificationsAdapter specificationsAdapter = new ResultsSpecificationsAdapter(mContext, rankingDrugDetailList, currrentPosition);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        resultsRecyclerSpecifications.setLayoutManager(linearLayoutManager);
        resultsRecyclerSpecifications.setAdapter(specificationsAdapter);
        resultsRecyclerSpecifications.scrollToPosition(position);
        specificationsAdapter.setOnItemClickLitener(new ResultsSpecificationsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, RankingDrugDetail rankingDrugDetail) {
                specificationsAdapter.updateItem(position);
                fillCurrent(position);
            }
        });
        String price = drugDetail.getPrice();
        Double dprice = Double.parseDouble(price);
        if (dprice <= 0) {
            tvDrugPrice.setVisibility(View.GONE);
            tvDrugPriceQian.setVisibility(View.GONE);
            tvYyShoujia.setVisibility(View.GONE);
            tvBaoWuJia.setVisibility(View.VISIBLE);
        } else {
            tvDrugPriceQian.setVisibility(View.VISIBLE);
            tvYyShoujia.setVisibility(View.VISIBLE);
            tvDrugPrice.setVisibility(View.VISIBLE);
            tvBaoWuJia.setVisibility(View.GONE);
            tvDrugPrice.setText(price);
        }
        ViewGroup.LayoutParams layoutParams = relSqyy.getLayoutParams();
        if (currentProvince.equals("北京")) {
            if (drugDetail.getBaoxiaoType().equals("报销")) {
                topFoldingcell.initialize(30, 1500, ContextCompat.getColor(mContext, R.color.cellBackSideviewCurrent), 4);
                layoutParams.height = DensityUtils.dp2px(120);
                relSqyy.setLayoutParams(layoutParams);
                tvBaoxiao.setVisibility(View.GONE);
                String sqPrice = drugDetail.getSqPrice();
                String yyPrice = drugDetail.getYyPrice();
                Double dyyPrice = Double.parseDouble(yyPrice);
                Double dsqPrice = Double.parseDouble(sqPrice);
                Double proportionYyPrice = dyyPrice / dprice;
                Double proportionsqPrice = dsqPrice / dprice;
                tvSqPrice.setText("" + sqPrice);
                tvYyPrice.setText("" + yyPrice);
                setProgress(rlResults, linearPriceYiyuan, ivPriceYiyuan, pbYiyuan, proportionYyPrice);
                setProgress(rlResults, linearPriceShequ, ivPriceShequ, pbShequ, proportionsqPrice);
                tvBaoxiao.setVisibility(View.GONE);
                linearYiyuan.setVisibility(View.VISIBLE);
                linearSq.setVisibility(View.VISIBLE);
            } else {
                topFoldingcell.initialize(30, 1500, ContextCompat.getColor(mContext, R.color.cellBackSideviewCurrent), 3);
                layoutParams.height = DensityUtils.dp2px(60);
                relSqyy.setLayoutParams(layoutParams);
                tvBaoxiao.setText(drugDetail.getBaoxiaoType());
                tvBaoxiao.setVisibility(View.VISIBLE);
                linearYiyuan.setVisibility(View.INVISIBLE);
                linearSq.setVisibility(View.INVISIBLE);
            }
        } else {
            topFoldingcell.initialize(30, 1500, ContextCompat.getColor(mContext, R.color.cellBackSideviewCurrent), 3);
            tvBaoxiao.setText(drugDetail.getBaoxiaoType());
            layoutParams.height = DensityUtils.dp2px(60);
            relSqyy.setLayoutParams(layoutParams);
            tvBaoxiao.setVisibility(View.VISIBLE);
            linearYiyuan.setVisibility(View.INVISIBLE);
            linearSq.setVisibility(View.INVISIBLE);
        }
        int adoptNum = drugDetail.getAdoptNum();
        Drawable drawableRight = ContextCompat.getDrawable(mContext, R.mipmap.icon_forword_right0);
        drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        if (adoptNum == 0) {
            tvAdopt.setCompoundDrawables(null, null, null, null);
        } else {
            tvAdopt.setCompoundDrawables(null, null, drawableRight, null);
        }
        tvAdopt.setText(drugDetail.getAdoptMsg());
        showContent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQ_FAMILY_MEMBER_ADD:
                    MedicineFamilyBean.ListBean bean = (MedicineFamilyBean.ListBean) data.getSerializableExtra(Contants.KEY_MEDICINE_BEAN);
                    if (bean != null) {
                        adapter.addItem(bean);
                        mListView.setSelection(0);
                        emptyMember.setVisibility(View.GONE);
                        whiteMengban.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        if (adapter.getCount() >= 10) {
                            popAddMemberLinearOk.setClickable(true);
                            popAddMemberLinearOk.setVisibility(View.VISIBLE);
                            popAddMemberLinear.setVisibility(View.GONE);
                            popAddMemberLinear.setClickable(false);
                            viewBottom.setVisibility(View.GONE);
                            popAddMember.setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                        } else {
                            popAddMember.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
            }

        }
    }

    public void onActionEvent(final int position, final MyFoldingCell foldingCell, View v) {
        viewClickable.setClickable(true);
        int[] topP = new int[2];
        int[] bottomP = new int[2];
        v.getLocationInWindow(bottomP);
        rlCurrentDrug.getLocationInWindow(topP);
        resultsLv.smoothScrollBy(bottomP[1] - topP[1] - rlCurrentDrug.getHeight() - DensityUtils.dp2px(15), 500);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                foldingCell.toggle(false);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewClickable.setClickable(false);
                    }
                }, 1150);//延迟时间与动画时间1500ms一致
            }
        }, 350);
        if(foldingCell.isUnfolded()&&position==mAdapter.getCount()-1){
            viewFooter.setVisibility(View.GONE);
        }else{
            viewFooter.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDrugDetail(int position, RankingInfo rankingInfo) {
        if (!NetworkUtil.CheckConnection(mContext)) {
            showNetDialog();
            return;
        }
        Intent intent = new Intent(this, ResultsDetailActivity.class);
        intent.putExtra(Contants.KEY_DRUG_ID, rankingInfo.getRankingDrugDetailList().get(position).getDrugID());
        startActivity(intent);
    }

    @Override
    public void onAddBag(int position, RankingInfo rankingInfo) {
        if (familyBean != null) {
            int count = familyBean.getCount();
            if (count == 0) {
                popupWindowDrugAdd(null, count);//加入药箱
            } else {
                popupWindowDrugAdd(rankingInfo.getRankingDrugDetailList().get(position).getDrugID(), count);//加入药箱
            }
        } else {
            popupLogin();
        }
    }

    @Override
    public void onBackPressed() {
        if (popupWindow != null) {
            dismissPopupWindow();
            return;
        }
        mHandler.removeCallbacksAndMessages(null);
        super.onBackPressed();
    }

    public void onAdoptDetail(int position, RankingInfo rankingInfo) {
        if (rankingInfo.getRankingDrugDetailList().get(position).getAdoptNum() > 0) {
            Intent intent = new Intent();
            intent.putExtra("drugNameID", rankingInfo.getDrugNameID());
            intent.putExtra(Contants.KEY_PAGE, typePage);
            LogUtil.y("###########currentProvince#############"+currentProvince);
            if (currentProvince.equals("北京") || currentProvince.equals("北京市")) {
                intent.putExtra("drug_id", rankingInfo.getRankingDrugDetailList().get(position).getDrugID());
                intent.setClass(this, ScanResultActivity.class);
            } else {
                intent.putExtra("drugId", rankingInfo.getRankingDrugDetailList().get(position).getDrugID());
                intent.setClass(this, HispitalListActivity.class);
            }
            startActivity(intent);
        }
    }

    @Override
    public void error(String errorMsg) {
        loaddingBar.dismiss();
        try {
            ErrorResponse errorResponse = JsonUtil.getModle(errorMsg, ErrorResponse.class);
            switch (errorResponse.getWhat()) {
                case HttpConstant.MINE_DURG_BAG_ADD:
                    if (errorResponse.isNetError()) {
                        showToast("网络不给力");
                    } else {
                        String msg = errorResponse.getMsg();
                        if (!TextUtils.isEmpty(msg)) {
                            showToast("" + msg);
                        }
                    }
                    dismissPopupWindow();
                    break;
                case HttpConstant.RESULTS_RANKING:
                    if (errorResponse.isNetError()) {
                        showToast("网络不给力");
                    } else {
                        String msg = errorResponse.getMsg();
                        if (!TextUtils.isEmpty(msg)) {
                            showToast("" + msg);
                        }
                    }
                    resultsLv.onRefreshComplete();
                    break;
            }
        } catch (Exception e) {

        }

    }


    public int getCurrentNumRes(int num) {
        int res = 0;
        switch (num) {
            case 0:
                res = R.mipmap.cur_num0;
                break;
            case 1:
                res = R.mipmap.cur_num1;
                break;
            case 2:
                res = R.mipmap.cur_num2;
                break;
            case 3:
                res = R.mipmap.cur_num3;
                break;
            case 4:
                res = R.mipmap.cur_num4;
                break;
            case 5:
                res = R.mipmap.cur_num5;
                break;
            case 6:
                res = R.mipmap.cur_num6;
                break;
            case 7:
                res = R.mipmap.cur_num7;
                break;
            case 8:
                res = R.mipmap.cur_num8;
                break;
            case 9:
                res = R.mipmap.cur_num9;
                break;

        }
        return res;
    }

    public int getNumRes(int num) {
        int res = 0;
        switch (num) {
            case 0:
                res = R.mipmap.short_num0;
                break;
            case 1:
                res = R.mipmap.short_num1;
                break;
            case 2:
                res = R.mipmap.short_num2;
                break;
            case 3:
                res = R.mipmap.short_num3;
                break;
            case 4:
                res = R.mipmap.short_num4;
                break;
            case 5:
                res = R.mipmap.short_num5;
                break;
            case 6:
                res = R.mipmap.short_num6;
                break;
            case 7:
                res = R.mipmap.short_num7;
                break;
            case 8:
                res = R.mipmap.short_num8;
                break;
            case 9:
                res = R.mipmap.short_num9;
                break;

        }
        return res;
    }

    /**
     * 设置进度
     *
     * @param linear
     * @param imageview
     * @param pbBar
     * @param proportion
     */
    private void setProgress(final RelativeLayout parent, final LinearLayout linear, final ImageView imageview, final ProgressBar pbBar, final double proportion) {
        parent.post(new Runnable() {
            @Override
            public void run() {
                if (!topFoldingcell.isUnfolded()) {
                    topFoldingcell.samSungReFold();
                }
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linear.getLayoutParams();
                int leftWidthTop = (int) (pbBar.getWidth() * proportion) - DensityUtils.dp2px(15);
                if (leftWidthTop > pbBar.getWidth()) {
                    leftWidthTop = pbBar.getWidth();
                }
                layoutParams.leftMargin = leftWidthTop;
                linear.setLayoutParams(layoutParams);
                pbBar.setProgress((int) (proportion * 100));
                LinearLayout.LayoutParams layoutParamsivPrice = (LinearLayout.LayoutParams) imageview.getLayoutParams();
                int leftWidth = (int) (pbBar.getWidth() * proportion) + DensityUtils.dp2px(8);
                if (leftWidth > pbBar.getWidth()) {
                    leftWidth = pbBar.getWidth();
                }
                layoutParamsivPrice.leftMargin = leftWidth;
                imageview.setLayoutParams(layoutParamsivPrice);
            }
        });
    }

    protected void onClickRetry() {
        loaddingBar.show();
        requestResultsDrugCurrent();
    }

    @Override
    protected void onDestroy() {
        if(receiver!=null){
            ResultsActivity.this.unregisterReceiver(receiver);
        }
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}

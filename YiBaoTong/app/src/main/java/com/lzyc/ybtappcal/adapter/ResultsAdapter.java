package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.top.HispitalListActivity;
import com.lzyc.ybtappcal.activity.top.ScanResultActivity;
import com.lzyc.ybtappcal.bean.RankingDrugDetail;
import com.lzyc.ybtappcal.bean.RankingInfo;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.lzyc.ybtappcal.util.StringUtils;
import com.lzyc.ybtappcal.view.MyFoldingCell;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;


/**
 * Created by yang on 2017/03/24.
 */
public class ResultsAdapter extends CommonAdapter<RankingInfo> {

    private OnItemChildListenr listener;
    private static final int ACTION_SHOW = 0;//需要显示
    private static final int ACTION_HIDDEN = 1;//需要隐藏
    private String currentRanking;
    private String drugId;
    private int count = 0;

    public ResultsAdapter(Context context, int layoutId, List<RankingInfo> datas) {
        super(context, layoutId, datas);
    }

    public void setOnItemChildListenr(OnItemChildListenr listener) {
        this.listener = listener;
    }

    public void setDrugId(String dId) {
        this.drugId = dId;
//        notifyDataSetChanged();
    }

    public void setCurrentRenkeduPosition(String randing) {
        this.currentRanking = randing;
    }

    public void setList(List<RankingInfo> list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public void addList(List<RankingInfo> list) {
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void updateItem(int position, RankingInfo bean) {
        this.mDatas.set(position, bean);
        notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder helper, final RankingInfo item, final int position) {
        final MyFoldingCell foldingCell = helper.getView(R.id.results_foldingcell);
        LinearLayout linearRenkedu = helper.getView(R.id.linear_sort_renkedu);
        LinearLayout linearTitle = helper.getView(R.id.linear_title);
        RelativeLayout rlCancel = helper.getView(R.id.rel_cancel);
        TextView resultsAddDrug = helper.getView(R.id.results_add_drug);
        ImageView ivDrugDetail = helper.getView(R.id.iv_drug_detail);
        LinearLayout linearDrugDetail = helper.getView(R.id.linear_drug_detail);
        final LinearLayout linearShequ = helper.getView(R.id.linear_price_shequ);
        final LinearLayout linearYiyuan = helper.getView(R.id.linear_price_yiyuan);
        final ProgressBar pbYiyuan = helper.getView(R.id.pb_yiyuan);
        final ProgressBar pbShequ = helper.getView(R.id.pb_shequ);
        final ImageView ivPriceShequ = helper.getView(R.id.iv_price_shequ);
        final ImageView ivPriceYiyuan = helper.getView(R.id.iv_price_yiyuan);
        final LinearLayout linearContentView = helper.getView(R.id.linear_contentview);
        TextView tvDrugName = helper.getView(R.id.tv_drug_name);
        TextView tvDrugNameTitle = helper.getView(R.id.tv_title_drugname);
        TextView tvVenderTitle = helper.getView(R.id.tv_title_vender);
        TextView tvDrugPrice = helper.getView(R.id.tv_drug_price);
        TextView tvBaoJiaWu = helper.getView(R.id.tv_baowujia);
        TextView tvSqPrice = helper.getView(R.id.tv_sq_price);
        TextView tvYyPrice = helper.getView(R.id.tv_yy_price);
        TextView tvAdopt = helper.getView(R.id.tv_adopt);
        TextView tvDrugPriceQian = helper.getView(R.id.tv_drug_price_qian);
        TextView tvYyShoujia = helper.getView(R.id.tv_yy_shoujia);
        LinearLayout linearContenTitleRenkedu = helper.getView(R.id.linear_content_title_renkedu);
        final RelativeLayout relSqyy = helper.getView(R.id.rel_sqyy);
        TextView tvBaoxiao = helper.getView(R.id.tv_baoxiao);
        ImageView ivRenkeduBg = helper.getView(R.id.iv_renkedu_bg);
        String provice = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "北京");
        foldingCell.post(new Runnable() {
            @Override
            public void run() {
                if (!foldingCell.isUnfolded()){
                    foldingCell.samSungReFold();
                }
            }
        });
        List<RankingDrugDetail> detailList = item.getRankingDrugDetailList();
        int relationPosiiton = 0;
        for (int i = 0; i < detailList.size(); i++) {
            if (drugId.equals(detailList.get(i).getDrugID())) {
                relationPosiiton = i;
                break;
            }
        }
        RankingDrugDetail detailDrug = detailList.get(relationPosiiton);
        String ranking = item.getRanking();
        if (detailDrug != null) {
            Picasso.with(mContext).load(detailDrug.getImage()).placeholder(R.mipmap.image_cache_drug)
                    .error(R.mipmap.image_cache_drug).into(ivDrugDetail);
            String name = item.getGoodsName();
            if (TextUtils.isEmpty(name)) {
                name = item.getName();
                tvDrugName.setText(name);
                tvDrugNameTitle.setText(name);
            } else {
                SpannableStringBuilder spannableText = StringUtils.getSpannableText(name, " " + item.getName(), 14, "#999ea1");
                tvDrugName.setText(name + " " + item.getName());
                tvDrugNameTitle.setText(spannableText);
            }
            tvVenderTitle.setText(item.getVender());
            String price = detailDrug.getPrice();
            double dprice = Double.parseDouble(price);
            if (dprice <= 0) {
                tvBaoJiaWu.setVisibility(View.VISIBLE);
                tvDrugPriceQian.setVisibility(View.GONE);
                tvYyShoujia.setVisibility(View.GONE);
                tvDrugPrice.setVisibility(View.GONE);
            } else {
                tvDrugPriceQian.setVisibility(View.VISIBLE);
                tvYyShoujia.setVisibility(View.VISIBLE);
                tvBaoJiaWu.setVisibility(View.GONE);
                tvDrugPrice.setVisibility(View.VISIBLE);
                tvDrugPrice.setText(price);
            }
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) relSqyy.getLayoutParams();
            if (provice.equals("北京")) {
                if (detailDrug.getBaoxiaoType().equals("报销")) {
                    tvBaoxiao.setVisibility(View.GONE);
                    linearYiyuan.setVisibility(View.VISIBLE);
                    linearShequ.setVisibility(View.VISIBLE);
                    layoutParams.height = DensityUtils.dp2px(120);
                    relSqyy.setLayoutParams(layoutParams);
                    tvBaoxiao.setVisibility(View.GONE);
                    String sqPrice = detailDrug.getSqPrice();
                    String yyPrice = detailDrug.getYyPrice();

                    tvYyPrice.setText(yyPrice);
                    tvSqPrice.setText(sqPrice);
                    double dyyPrice = Double.parseDouble(yyPrice);
                    double dsqPrice = Double.parseDouble(sqPrice);
                    final double proportionYyPrice = dyyPrice / dprice;
                    final double proportionsqPrice = dsqPrice / dprice;

                    pbYiyuan.setProgress((int) (proportionYyPrice * 100));
                    pbShequ.setProgress((int) (proportionsqPrice * 100));
                    relSqyy.setLayoutParams(layoutParams);
                    pbYiyuan.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!foldingCell.isUnfolded())
                                foldingCell.samSungReFold();
                            pbYiyuan.setTag(true);
                            int pbYiyuanW = pbYiyuan.getWidth();
                            int pbShequW = pbYiyuan.getWidth();
                            if (pbYiyuanW > 0) {
                                setProgress(linearContentView, linearYiyuan, ivPriceYiyuan, pbYiyuanW, proportionYyPrice);
                            }
                            if (pbShequW > 0) {
                                setProgress(linearContentView, linearShequ, ivPriceShequ, pbShequW, proportionsqPrice);
                            }
                        }
                    });

                    if (currentRanking.equals(ranking)) {
                        foldingCell.initialize(30, 1500, ContextCompat.getColor(mContext, R.color.cellBackSideviewCurrent), 4);
                        ivRenkeduBg.setBackgroundResource(R.drawable.bg_results_show_top_current);
                        linearRenkedu.setBackgroundResource(R.drawable.shape_results_item_left_current);
                    } else {
                        foldingCell.initialize(30, 1500, ContextCompat.getColor(mContext, R.color.cellBackSideviewOther), 4);
                        ivRenkeduBg.setBackgroundResource(R.drawable.bg_results_show_top);
                        linearRenkedu.setBackgroundResource(R.drawable.shape_results_item_left);
                    }
                    foldingCell.setStateToFolded();
                } else {
                    linearYiyuan.setVisibility(View.INVISIBLE);
                    linearShequ.setVisibility(View.INVISIBLE);
                    layoutParams.height = DensityUtils.dp2px(60);
                    relSqyy.setLayoutParams(layoutParams);
                    tvBaoxiao.setVisibility(View.VISIBLE);
                    tvBaoxiao.setText(detailDrug.getBaoxiaoType());
                    foldingCell.setStateToFolded();
                    if (currentRanking.equals(ranking)) {
                        foldingCell.initialize(30, 1200, ContextCompat.getColor(mContext, R.color.cellBackSideviewCurrent), 3);
                        ivRenkeduBg.setBackgroundResource(R.drawable.bg_results_show_top_current);
                        linearRenkedu.setBackgroundResource(R.drawable.shape_results_item_left_current);
                    } else {
                        foldingCell.initialize(30, 1200, ContextCompat.getColor(mContext, R.color.cellBackSideviewOther), 3);
                        ivRenkeduBg.setBackgroundResource(R.drawable.bg_results_show_top);
                        linearRenkedu.setBackgroundResource(R.drawable.shape_results_item_left);
                    }
                    foldingCell.setStateToFolded();
                }
            } else {
                if (currentRanking.equals(ranking)) {
                    foldingCell.initialize(30, 1200, ContextCompat.getColor(mContext, R.color.cellBackSideviewCurrent), 3);
                    ivRenkeduBg.setBackgroundResource(R.drawable.bg_results_show_top_current);
                    linearRenkedu.setBackgroundResource(R.drawable.shape_results_item_left_current);
                } else {
                    foldingCell.initialize(30, 1200, ContextCompat.getColor(mContext, R.color.cellBackSideviewOther), 3);
                    ivRenkeduBg.setBackgroundResource(R.drawable.bg_results_show_top);
                    linearRenkedu.setBackgroundResource(R.drawable.shape_results_item_left);
                }
                tvBaoxiao.setVisibility(View.VISIBLE);
                linearYiyuan.setVisibility(View.INVISIBLE);
                linearShequ.setVisibility(View.INVISIBLE);
                layoutParams.height = DensityUtils.dp2px(60);
                relSqyy.setLayoutParams(layoutParams);
                tvBaoxiao.setVisibility(View.VISIBLE);
                tvBaoxiao.setText(detailDrug.getBaoxiaoType());
                foldingCell.setStateToFolded();
            }
            Drawable drawableRight = ContextCompat.getDrawable(mContext, R.mipmap.icon_forword_right0);
            drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
            if (detailDrug.getAdoptNum() == 0) {
                tvAdopt.setCompoundDrawables(null, null, null, null);
            } else {
                tvAdopt.setCompoundDrawables(null, null, drawableRight, null);
            }
            tvAdopt.setText(detailDrug.getAdoptMsg());

        }
        RecyclerView specificationsRecyclerView = helper.getView(R.id.results_recycler_specifications);
        final ResultsSpecificationsAdapter specificationsAdapter = new ResultsSpecificationsAdapter(mContext, detailList, relationPosiiton);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        specificationsRecyclerView.setLayoutManager(linearLayoutManager);
        specificationsRecyclerView.setAdapter(specificationsAdapter);
        specificationsRecyclerView.scrollToPosition(relationPosiiton);
        specificationsAdapter.setOnItemClickLitener(new ResultsSpecificationsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, RankingDrugDetail rankingDrugDetail) {
                drugId = rankingDrugDetail.getDrugID();
                specificationsAdapter.updateItem(position);
                setDrugId(drugId);
                ResultsAdapter.this.notifyDataSetChanged();
            }
        });
        if (item.getCellActionType() == ACTION_SHOW && item.getCellPosition() == position) {
            foldingCell.unfold(true, null);//铺开
        } else {
            foldingCell.fold(true, null);//隐藏
        }
        rlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    foldingCell.fold(false, null);
                    item.setCellActionType(ACTION_HIDDEN);
                    item.setCellPosition(position);
                    listener.onActionEvent(position, foldingCell, v);
                }
            }
        });

        final int finalRelationPosiiton = relationPosiiton;
        resultsAddDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAddBag(finalRelationPosiiton, item);
                }
            }
        });
        final int finalRelationPosiiton1 = relationPosiiton;
        ivDrugDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDrugDetail(finalRelationPosiiton1, item);
                }
            }
        });
        final int finalRelationPosiiton2 = relationPosiiton;
        linearDrugDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDrugDetail(finalRelationPosiiton2, item);
                }
            }
        });
        final RankingDrugDetail finalDetailDrug = detailDrug;
        tvAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (listener != null) {
//                    listener.onAdoptDetail(relationPosiiton, item);
//                }
                if (finalDetailDrug.getAdoptNum() > 0) {
                    Intent intent = new Intent();
                    String provice = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "北京");
                    intent.putExtra("drugNameID", item.getDrugNameID());
                    if (provice.equals("北京")) {
                        intent.putExtra("drug_id", finalDetailDrug.getDrugID());
                        intent.setClass(mContext, ScanResultActivity.class);
                    } else {
                        intent.putExtra("drugId", finalDetailDrug.getDrugID());
                        intent.setClass(mContext, HispitalListActivity.class);
                    }
                    mContext.startActivity(intent);
                }
            }
        });
        linearRenkedu.removeAllViews();
        for (int i = 0; i < ranking.length(); i++) {
            View view = new View(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(DensityUtils.dp2px(14), DensityUtils.dp2px(19));
            char numChar = ranking.charAt(i);
            String numStr = String.valueOf(numChar);
            int res = getNumRes(Integer.parseInt(numStr));
            view.setBackgroundResource(res);
            view.setLayoutParams(layoutParams);
            linearRenkedu.addView(view);
        }
        linearContenTitleRenkedu.removeAllViews();
        LinearLayout mLinearLayout = new LinearLayout(mContext);
        mLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        for (int i = 0; i < ranking.length(); i++) {
            View view = new View(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtils.dp2px(14), DensityUtils.dp2px(19));
            char numChar = ranking.charAt(i);
            String numStr = String.valueOf(numChar);
            int res = getNumRes(Integer.parseInt(numStr));
            view.setBackgroundResource(res);
            view.setLayoutParams(layoutParams);
            mLinearLayout.addView(view);
        }
        linearContenTitleRenkedu.addView(mLinearLayout);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mLinearLayout.getLayoutParams();
        layoutParams.bottomMargin = DensityUtils.dp2px(4);
        layoutParams.leftMargin = DensityUtils.dp2px(3);
        mLinearLayout.setLayoutParams(layoutParams);
        linearTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    item.setCellActionType(ACTION_SHOW);
                    item.setCellPosition(position);
                    listener.onActionEvent(position, foldingCell, v);
                }

            }
        });
    }

    /**
     * 设置进度
     *
     * @param linear
     * @param imageview * @param proportion
     */
    private void setProgress(final LinearLayout parent, final LinearLayout linear, final ImageView imageview, final int pBarWidth, final double proportion) {
//        ViewTreeObserver vto = pbBar.getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
////                LogUtil.y("onPreDraw");
//
//                return true;
//            }
//        });
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                LogUtil.y("onGlobalLayout");
//
//                pbBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });
        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linear.getLayoutParams();
        int leftWidthTop = (int) (pBarWidth * proportion) - DensityUtils.dp2px(15);
        if (leftWidthTop > pBarWidth) {
            leftWidthTop = pBarWidth;
        }
        layoutParams.leftMargin = leftWidthTop;
        linear.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParamsivPrice = (LinearLayout.LayoutParams) imageview.getLayoutParams();
        int leftWidth = (int) (pBarWidth * proportion) + DensityUtils.dp2px(8);
        if (leftWidth > pBarWidth) {
            leftWidth = pBarWidth;
        }
        layoutParamsivPrice.leftMargin = leftWidth;
        imageview.setLayoutParams(layoutParamsivPrice);

    }

    public interface OnItemChildListenr {
        void onActionEvent(int position, MyFoldingCell foldView, View v);//0,需要展开，1，需要隐藏

        void onDrugDetail(int position, RankingInfo rankingInfo);//药品详情

        void onAddBag(int position, RankingInfo rankingInfo);//加入药箱

//        void onAdoptDetail(int position, RankingInfo rankingInfo);//医院，社区拿药列表
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

}

package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.HCCommentBean;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.widget.MyRoundImageView;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lovelin on 16/8/24.
 * 医院社区评价
 */
public class CommunityAdapter extends CommonAdapter<HCCommentBean.hcData.HcDataListBean> {

    private TextView item_hc_emptyview;
    private LinearLayout item_hc_linear_content;

    public CommunityAdapter(Context context, int layoutId, List<HCCommentBean.hcData.HcDataListBean> datas) {
        super(context, layoutId, datas);
    }


    public void setLikeStatus(int position, HCCommentBean.hcData.HcDataListBean bean) {
        this.mDatas.set(position, bean);
        notifyDataSetChanged();
    }

    private boolean isFirstClickLike = true;

    public void setList(List<HCCommentBean.hcData.HcDataListBean> list) {
        if (list == null) {
            list = new ArrayList<HCCommentBean.hcData.HcDataListBean>();
        }
        if (this.mDatas != null) {
            this.mDatas.clear();
        }
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public void addList(List<HCCommentBean.hcData.HcDataListBean> list) {
        if (list == null) {
            list = new ArrayList<HCCommentBean.hcData.HcDataListBean>();
        }
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder helper, final HCCommentBean.hcData.HcDataListBean bean, final int position) {
        LinearLayout loadding_listview_linear = helper.getView(R.id.loadding_listview_linear);
        item_hc_linear_content = helper.getView(R.id.item_hc_linear_content);
        LinearLayout id_about_community_item = helper.getView(R.id.id_about_community_item);
        TextView item_hc_emptyview = helper.getView(R.id.item_hc_emptyview);
        com.lzyc.ybtappcal.view.StarBar starBar = helper.getView(R.id.test_star);
        TextView item_hc_tv_phone_num = helper.getView(R.id.item_hc_tv_phone_num);
        TextView item_hc_tv_content = helper.getView(R.id.item_hc_tv_content);
        TextView item_hc_tv_date = helper.getView(R.id.item_hc_tv_date);
        TextView item_community_start = helper.getView(R.id.item_community_start);
        boolean emptyView = bean.isEmptyView();

        TextView item_hc_tv_like_count = helper.getView(R.id.item_hc_tv_like_count);
        TextView item_hc_tv_comment_count = helper.getView(R.id.item_hc_tv_comment_count);
        item_hc_emptyview = helper.getView(R.id.item_hc_emptyview);

        ImageView item_hc_iv_like = helper.getView(R.id.item_hc_iv_like);
        ImageView item_hc_iv_commeity = helper.getView(R.id.item_hc_iv_comment);
        MyRoundImageView item_recomment_iv_covert = helper.getView(R.id.item_recomment_iv_covert);
        LinearLayout item_hc_linea_comment = helper.getView(R.id.item_hc_linea_comment);
        LinearLayout item_hc_linea_like = helper.getView(R.id.item_hc_linea_like);
        if (!(TextUtils.isEmpty(bean.getHeadImg()))) {
            Picasso.with(mContext).load(bean.getHeadImg())
                    .error(R.mipmap.icon_system_logo)
                    .into(item_recomment_iv_covert);
        }else{
            item_recomment_iv_covert.setImageResource(R.mipmap.icon_system_logo);
        }
        if (emptyView) {
            item_hc_emptyview.setVisibility(View.VISIBLE);
            item_hc_linear_content.setVisibility(View.GONE);

        } else {
            item_hc_emptyview.setVisibility(View.GONE);
            item_hc_linear_content.setVisibility(View.VISIBLE);
            item_hc_linea_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onChildLike(position, bean);
                    }
                }
            });

            item_hc_linea_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onChildComment(position, bean);
                    }
                }
            });
            id_about_community_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClickListener(position, bean);
                    }
                }
            });
            String mark = bean.getAverage2();
            if (!TextUtils.isEmpty(mark)) {
                starBar.setStarMark(Float.parseFloat(mark));
                item_community_start.setText(Float.parseFloat(mark) + "");

            }
            if (bean.getParaiseStatus().equals("0")) {
                item_hc_iv_like.setImageResource(R.mipmap.icon_hospital_like_nor);
            } else {
                item_hc_iv_like.setImageResource(R.mipmap.icon_hospital_like_pre);
            }


            if (bean.isClickLike()) {  //被点击了
                int i = Integer.parseInt(bean.getPraisecount());
                int one = Integer.parseInt(1 + "");
                item_hc_iv_like.setImageResource(R.mipmap.icon_hospital_like_pre);
                item_hc_tv_like_count.setText("(" + (i + one) + ")");
            } else {
                item_hc_tv_like_count.setText("(" + bean.getPraisecount() + ")");
            }

            if (bean.isClickCommunity()) {
                int i = Integer.parseInt(bean.getReplycount());
                int one = Integer.parseInt(1 + "");

                item_hc_tv_comment_count.setText("(" + (i + one) + ")");

            } else {
                item_hc_tv_comment_count.setText("(" + bean.getReplycount() + ")");
            }

            item_hc_tv_phone_num.setText("" + bean.getNickname());
            item_hc_tv_content.setText("" + bean.getContent());
            item_hc_tv_date.setText("" + bean.getDate());
        }

    }

    public void setEmpty() {
        HCCommentBean.hcData.HcDataListBean hcDataListBean = new HCCommentBean.hcData.HcDataListBean();
        hcDataListBean.setEmptyView(true);
        mDatas = new ArrayList<HCCommentBean.hcData.HcDataListBean>();
        mDatas.add(hcDataListBean);
        this.notifyDataSetChanged();
//        if (isTrue) {
//            item_hc_emptyview.setVisibility(View.VISIBLE);
//            LogUtil.e("哈哈哈哈哈", "哈哈哈哈哈哈来嘞");
//            item_hc_linear_content.setVisibility(View.GONE);
//        } else {
//            item_hc_emptyview.setVisibility(View.GONE);
//            item_hc_linear_content.setVisibility(View.VISIBLE);
//        }
    }

    private OnHcChildClickListemer mListener;

    public void setOnHcChildClickListemer(OnHcChildClickListemer listener) {
        this.mListener = listener;
    }

    public interface OnHcChildClickListemer {
        void onChildLike(int position, HCCommentBean.hcData.HcDataListBean bean);

        void onChildComment(int position, HCCommentBean.hcData.HcDataListBean bean);

        void onItemClickListener(int position, HCCommentBean.hcData.HcDataListBean bean);
    }

}

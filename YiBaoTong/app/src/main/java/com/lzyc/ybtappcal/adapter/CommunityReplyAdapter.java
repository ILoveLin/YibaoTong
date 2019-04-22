package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.CommunityReplyBean;
import com.lzyc.ybtappcal.widget.MyRoundImageView;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lovelin on 16/8/24.
 */
public class CommunityReplyAdapter extends CommonAdapter<CommunityReplyBean.DataBean.ListBean> {

    public CommunityReplyAdapter(Context context, int layoutId, List<CommunityReplyBean.DataBean.ListBean> datas) {
        super(context, layoutId, datas);
    }

    public void setList(List<CommunityReplyBean.DataBean.ListBean> list) {
        if (list == null) {
            list = new ArrayList<CommunityReplyBean.DataBean.ListBean>();
        }
        if (this.mDatas != null) {
            this.mDatas.clear();
        }
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public void addList(List<CommunityReplyBean.DataBean.ListBean> list) {
        if (list == null) {
            list = new ArrayList<CommunityReplyBean.DataBean.ListBean>();
        }
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder helper, final CommunityReplyBean.DataBean.ListBean bean, final int position) {
        TextView item_community_phone = helper.getView(R.id.item_community_phone);
        TextView item_community_reply = helper.getView(R.id.item_community_reply);
        TextView item_community_add_date = helper.getView(R.id.item_community_add_date);
        LinearLayout item_community_click = helper.getView(R.id.item_community_click);
        MyRoundImageView item_recomment_iv_covert = helper.getView(R.id.item_recomment_iv_covert);

        item_community_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMyItemClick(bean, position);
                }
            }
        });
        if (!(TextUtils.isEmpty(bean.getHeadImg()))) {
            Picasso.with(mContext).load(bean.getHeadImg()).error(R.mipmap.icon_system_logo).into(item_recomment_iv_covert);
        }else{
            item_recomment_iv_covert.setImageResource(R.mipmap.icon_system_logo);
        }
        item_community_phone.setText("" + bean.getNickname());
        item_community_reply.setText("回复 " + bean.getP_nickname() + " : " + bean.getContent());
        item_community_add_date.setText("" + bean.getAdd_date());

    }

    private OnMyItemClickListener mListener;

    public void setOnMyItemClickListener(OnMyItemClickListener onMyItemClickListener) {
        this.mListener = onMyItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onMyItemClick(CommunityReplyBean.DataBean.ListBean bean, int position);
    }

}

package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.RecommendBean;
import com.lzyc.ybtappcal.view.roundimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * package_name com.lzyc.ybtappcal.adapter
 * Created by yang on 2016/5/26.
 */
public class RecommendedAdapter extends CommonAdapter<RecommendBean> {
    public RecommendedAdapter(Context context, int layoutId, List<RecommendBean> datas) {
        super(context, layoutId, datas);
    }

    public void setList(List<RecommendBean> list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public void addList(List<RecommendBean> list) {
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder helper, RecommendBean item, int position) {
        RoundedImageView ivCovert = helper.getView(R.id.item_recomment_iv_covert);
        TextView tvTitle = helper.getView(R.id.item_recomment_tv_title);
        TextView tvReadeNum = helper.getView(R.id.info_readcount);
        tvTitle.setText("" + item.getTitle());
//        tvTime.setText(""+item.getCtime());
        Picasso.with(mContext).load(item.getImages()).placeholder(R.mipmap.image_cache_drug).placeholder(R.mipmap.image_cache_drug).into(ivCovert);

        tvReadeNum.setText("" + item.getReadnum());
    }

}

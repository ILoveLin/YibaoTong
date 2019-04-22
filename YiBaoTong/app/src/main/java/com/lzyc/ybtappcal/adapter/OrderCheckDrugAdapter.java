package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.GoodsBean;
import com.lzyc.ybtappcal.util.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 检查订单 药品列表
 * Created by lxx on 2017/4/26.
 */

public class OrderCheckDrugAdapter extends RecyclerView.Adapter<OrderCheckDrugAdapter.OrderCheckDrugViewHolder> {
    Context mContext;
    LayoutInflater mInflater;

    List<GoodsBean> mData = new ArrayList<>();

    public OrderCheckDrugAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void updata(List<GoodsBean> data) {
        mData.clear();
        if (null != data && !data.isEmpty()) {
            mData = data;
        }
        notifyDataSetChanged();
    }

    @Override
    public OrderCheckDrugViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderCheckDrugViewHolder(mInflater.inflate(R.layout.item_order_check_drugs, parent, false));
    }

    @Override
    public void onBindViewHolder(OrderCheckDrugViewHolder holder, int position) {
        GoodsBean bean = mData.get(position);

        LogUtil.d("lxx", "56-->"+bean.toString());

        Picasso.with(mContext)
                .load(bean.getImage())
                .error(R.mipmap.image_empty)
                .placeholder(R.mipmap.image_empty)
                .into(holder.imgDrug);
        holder.tvName.setText(bean.getGoodsName()+" "+bean.getName());
        holder.tvPriceNew.setText(bean.getDeKaiPrice());
        holder.tvPriceOld.setText(bean.getRetailPrice());
        holder.tvPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvDrugNum.setText("×"+bean.getGoodsCount());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class OrderCheckDrugViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_drug)
        ImageView imgDrug;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_specifications)
        TextView tvSpecifications;
        @BindView(R.id.tv_price_new)
        TextView tvPriceNew;
        @BindView(R.id.tv_price_old)
        TextView tvPriceOld;
        @BindView(R.id.tv_drug_num)
        TextView tvDrugNum;

        public OrderCheckDrugViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

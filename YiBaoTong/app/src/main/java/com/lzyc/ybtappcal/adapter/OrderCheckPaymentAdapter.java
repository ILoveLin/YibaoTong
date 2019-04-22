package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.GoodsBean;
import com.lzyc.ybtappcal.bean.ShopingCart;
import com.lzyc.ybtappcal.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxx on 2017/4/26.
 */

public class OrderCheckPaymentAdapter extends RecyclerView.Adapter<OrderCheckPaymentAdapter.OrderCheckPaymentViewHolder> {

    Context mContext;
    LayoutInflater mInflater;

    List<ShopingCart> mData = new ArrayList<>();

    int type = 0;

    public OrderCheckPaymentAdapter(Context mContext, int type) {
        this.mContext = mContext;
        this.type = type;
        mInflater = LayoutInflater.from(mContext);
    }

    public void updata(List<ShopingCart> data) {
        if(null != data && !data.isEmpty()){
            this.mData.addAll(data);
            try{
                String typeStr = "";
                for(int i = 0; i < data.size() ;i++){
                    for(GoodsBean bean : data.get(i).getGoodsList()){
                        typeStr += bean.getOnlinePay();
                    }
                    if(typeStr.contains("1") && 0 == type){
                        mData.remove(i);
                    }
                    if(typeStr.contains("0") && 1 == type ){
                        mData.remove(i);
                    }
                }
            } catch (Exception e){
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public OrderCheckPaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderCheckPaymentViewHolder(mInflater.inflate(R.layout.item_order_check_payment, parent, false));
    }

    @Override
    public void onBindViewHolder(OrderCheckPaymentViewHolder holder, int position) {

        ShopingCart bean = mData.get(position);

        Picasso.with(mContext)
                .load(bean.getLogo())
                .placeholder(R.mipmap.image_empty_order)
                .error(R.mipmap.image_empty_order)
                .into(holder.imgPharmacy);

        holder.tvPharmacy.setText(bean.getName());

        try{

            int count = 0;//数量

            double priceD = 0;

            double freight = 0;
            try{
                freight = Double.parseDouble(bean.getFreight());
            } catch (Exception e){
                e.printStackTrace();;
            }

            for(GoodsBean goodsBean : bean.getGoodsList()){
                count += goodsBean.getGoodsCount();

                try{

                    priceD += (Double.parseDouble(goodsBean.getDeKaiPrice()) * goodsBean.getGoodsCount());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            if(Double.parseDouble(bean.getFreeShipp()) > priceD && !"德开大药房".equals(bean.getName())){
                priceD += freight;
            }

            holder.tvDrugNum.setText("共计"+count+"件商品");
            holder.tvPrice.setText(Util.getFloatDotStr(String.valueOf(priceD)));
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class OrderCheckPaymentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_pharmacy)
        ImageView imgPharmacy;
        @BindView(R.id.tv_pharmacy)
        TextView tvPharmacy;
        @BindView(R.id.tv_drug_num)
        TextView tvDrugNum;
        @BindView(R.id.tv_price)
        TextView tvPrice;

        public OrderCheckPaymentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

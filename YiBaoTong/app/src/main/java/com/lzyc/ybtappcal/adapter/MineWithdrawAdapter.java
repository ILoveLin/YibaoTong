package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.mine.withdraw.MineWithdrawActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxx on 2017/6/28.
 */

public class MineWithdrawAdapter extends RecyclerView.Adapter<MineWithdrawAdapter.CacheViewHolder> {

    List<MineWithdrawActivity.AliBean> mData = new ArrayList<>();

    LayoutInflater mInflater;

    public MineWithdrawAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void updata(List<MineWithdrawActivity.AliBean> data){

        if(null != data && !data.isEmpty()){
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void deleteData(int pos){
        mData.remove(pos);
        notifyDataSetChanged();
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, getItemCount());
    }

    @Override
    public CacheViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CacheViewHolder(mInflater.inflate(R.layout.item_withdraw_alipay, parent, false));
    }

    @Override
    public void onBindViewHolder(CacheViewHolder holder, final int position) {

        holder.tvAlipay.setText(mData.get(position).getPhone());

        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != mineWithdrawListener){
                    mineWithdrawListener.onDelete(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == mData ? 0 : mData.size();
    }

    class CacheViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_alipay)
        TextView tvAlipay;
        @BindView(R.id.img_del)
        ImageView imgDel;

        public CacheViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private MineWithdrawListener mineWithdrawListener;

    public void setMineWithdrawListener(MineWithdrawListener mineWithdrawListener) {
        this.mineWithdrawListener = mineWithdrawListener;
    }

    public interface MineWithdrawListener{
        void onDelete(int pos);
    }

}

package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.BalanceDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lxx on 2017/4/12.
 */
public class MineWithdrawReturnAdapter extends RecyclerView.Adapter<MineWithdrawReturnAdapter.WithdrawViewHolder> {
    private LayoutInflater mInflater;
    private List<BalanceDetail.ListBean> mData = new ArrayList<>();

    public MineWithdrawReturnAdapter(Context mContext) {
        mInflater = LayoutInflater.from(mContext);
    }

    public void update(List<BalanceDetail.ListBean> data){
        mData.clear();
        if(null != data && !data.isEmpty()){
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public WithdrawViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WithdrawViewHolder(mInflater.inflate(R.layout.item_mine_withdraw_return, parent, false));
    }

    @Override
    public void onBindViewHolder(WithdrawViewHolder holder, int position) {
        if(position == mData.size()-1){
            holder.vLine.setVisibility(View.GONE);
            holder.vBottom.setVisibility(View.VISIBLE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
            holder.vBottom.setVisibility(View.GONE);
        }

        if(position == 0){
            holder.vTop.setVisibility(View.VISIBLE);
        } else {
            holder.vTop.setVisibility(View.GONE);
        }

        final BalanceDetail.ListBean bean = mData.get(position);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvCreateTime.setText(bean.getTime());
        holder.tvBalance.setText(bean.getReturnMoney());

        if("1".equals(bean.getDelete())){
            holder.tvState.setBackgroundResource(R.mipmap.icon_money_recorder_in_del);
            holder.tvDeleted.setVisibility(View.VISIBLE);
            holder.tvIcon.setVisibility(View.INVISIBLE);
            holder.tvIconDel.setVisibility(View.INVISIBLE);
            holder.vRight.setVisibility(View.GONE);
        } else {
            holder.tvState.setBackgroundResource(R.mipmap.icon_money_recorder_in);
            holder.tvIcon.setVisibility(View.VISIBLE);
            holder.tvDeleted.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != onWithdrawReturnListener){
                    onWithdrawReturnListener.onItem(bean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class WithdrawViewHolder extends RecyclerView.ViewHolder{
        View vLine;
        TextView tvState;
        TextView tvTitle;
        TextView tvCreateTime;
        TextView tvBalance;
        ImageView tvIcon;
        ImageView tvIconDel;
        TextView tvDeleted;

        View vTop, vBottom, vRight;

        public WithdrawViewHolder(View itemView) {
            super(itemView);
            vLine = itemView.findViewById(R.id.v_line);
            tvState = (TextView) itemView.findViewById(R.id.tv_state);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvBalance = (TextView) itemView.findViewById(R.id.tv_balance);
            tvIcon = (ImageView) itemView.findViewById(R.id.tv_icon);
            tvIconDel = (ImageView) itemView.findViewById(R.id.tv_icon_del);
            tvDeleted = (TextView) itemView.findViewById(R.id.tv_deleted);

            vTop = itemView.findViewById(R.id.v_top);
            vBottom = itemView.findViewById(R.id.v_bottom);
            vRight = itemView.findViewById(R.id.v_right);
        }
    }

    private WithdrawReturnListener onWithdrawReturnListener;
    public void setOnWithdrawReturnListener(WithdrawReturnListener onWithdrawReturnListener){
        this.onWithdrawReturnListener = onWithdrawReturnListener;
    }
    public interface WithdrawReturnListener{
        void onItem(BalanceDetail.ListBean bean);
    }
}

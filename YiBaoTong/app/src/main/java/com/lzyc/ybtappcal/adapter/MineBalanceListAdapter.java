package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.BalanceListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxx on 2017/6/5.
 */

public class MineBalanceListAdapter extends RecyclerView.Adapter<MineBalanceListAdapter.BalanceViewHolder> {

    List<BalanceListBean.ListBean> mData = new ArrayList<>();

    LayoutInflater mInflater;

    Context mContext;

    public MineBalanceListAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void updata(List<BalanceListBean.ListBean> data) {
        mData.clear();
        if (null != data && !data.isEmpty()) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public BalanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BalanceViewHolder(mInflater.inflate(R.layout.item_mine_withdraw_return, parent, false));
    }

    @Override
    public void onBindViewHolder(BalanceViewHolder holder, int position) {

        holder.tvDeleted.setVisibility(View.GONE);
        holder.tvIcon.setVisibility(View.GONE);
        holder.tvIconDel.setVisibility(View.GONE);
        holder.vRight.setVisibility(View.GONE);
        holder.tvSign.setVisibility(View.GONE);

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

        final BalanceListBean.ListBean bean = mData.get(position);

        String sign = "";

        if("1".equals(bean.getType())){
            holder.tvState.setBackgroundResource(R.mipmap.icon_balance_in);
            holder.tvBalance.setTextColor(mContext.getResources().getColor(R.color.color_43a047));
            sign = "+ ";
        } else {
            holder.tvBalance.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            holder.tvState.setBackgroundResource(R.mipmap.icon_balance_out);
            sign = "- ";
        }

        holder.tvTitle.setText(bean.getInfo());
        holder.tvCreateTime.setText(bean.getTime());
        holder.tvBalance.setText(sign + bean.getMoney());

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class BalanceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.v_top)
        View vTop;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_create_time)
        TextView tvCreateTime;
        @BindView(R.id.v_right)
        View vRight;
        @BindView(R.id.tv_balance)
        TextView tvBalance;
        @BindView(R.id.tv_icon)
        ImageView tvIcon;
        @BindView(R.id.tv_deleted)
        TextView tvDeleted;
        @BindView(R.id.tv_icon_del)
        ImageView tvIconDel;
        @BindView(R.id.v_line)
        View vLine;
        @BindView(R.id.v_bottom)
        View vBottom;
        @BindView(R.id.tv_sign)
        TextView tvSign;

        public BalanceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

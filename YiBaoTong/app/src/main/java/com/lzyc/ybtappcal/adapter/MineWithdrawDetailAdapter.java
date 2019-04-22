package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.PersonWithdrawList;
import com.lzyc.ybtappcal.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lxx on 2017/4/11.
 */
public class MineWithdrawDetailAdapter extends RecyclerView.Adapter<MineWithdrawDetailAdapter.MineWithdrawDetailViewHolder> {

    private List<PersonWithdrawList.ListBean> mData = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;

    final String STATE_IN_HAND = "1";
    final String STATE_SUCCESS = "2";
    final String STATE_ERROR = "-1";

    public MineWithdrawDetailAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void update(List<PersonWithdrawList.ListBean> data){
//        mData.clear();
        if(null != data && !data.isEmpty()){
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public MineWithdrawDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MineWithdrawDetailViewHolder(mInflater.inflate(R.layout.item_mine_withdraw_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(MineWithdrawDetailViewHolder holder, int position) {
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

        final PersonWithdrawList.ListBean bean = mData.get(position);
        switch (bean.getState()){
            case STATE_IN_HAND:
                holder.tvMsg.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                holder.tvMsg.setText("处理中");
                break;
            case STATE_SUCCESS:
                holder.tvMsg.setTextColor(mContext.getResources().getColor(R.color.color_43a047));
                holder.tvMsg.setText("成功");
                break;
            case STATE_ERROR:
                holder.tvMsg.setTextColor(mContext.getResources().getColor(R.color.color_f55959));
                holder.tvMsg.setText("失败");
                break;
        }

        String aliPay = bean.getAlipay();
        if(StringUtils.isMobileNum(aliPay)){
            aliPay = StringUtils.replacePhone(bean.getAlipay());
        } else if(StringUtils.isEmail(bean.getAlipay())){
            aliPay = StringUtils.replaceEmail(bean.getAlipay());
        }
        String date = bean.getCreateTime().substring(0, 10);
        holder.tvAlipay.setText("账号:" + aliPay);
        holder.tvBalance.setText(bean.getBalance());
        holder.tvCreateTime.setText(date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != onDetailListener){
                    onDetailListener.onItem(bean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class MineWithdrawDetailViewHolder extends RecyclerView.ViewHolder{
        View vLine;
        TextView tvMsg;
        TextView tvAlipay;
        TextView tvBalance;
        TextView tvCreateTime;

        View vTop, vBottom;

        public MineWithdrawDetailViewHolder(View itemView) {
            super(itemView);
            vLine = itemView.findViewById(R.id.v_line);
            tvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            tvAlipay = (TextView) itemView.findViewById(R.id.tv_alipay);
            tvBalance = (TextView) itemView.findViewById(R.id.tv_balance);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);

            vTop = itemView.findViewById(R.id.v_top);
            vBottom = itemView.findViewById(R.id.v_bottom);

        }
    }

    private DetailListener onDetailListener;
    public void setOnDetailListener(DetailListener onDetailListener){
        this.onDetailListener = onDetailListener;
    }
    public interface DetailListener{
        void onItem(PersonWithdrawList.ListBean bean);
    }
}

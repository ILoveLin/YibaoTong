package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.RankingDrugDetail;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.LogUtil;

import java.util.List;

/**
 * Created by yang on 2017/06/14.
 */
public class ResultsSpecificationsAdapter extends
        RecyclerView.Adapter<ResultsSpecificationsAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<RankingDrugDetail> mDatas;
    private OnItemClickLitener mOnItemClickLitener;
    private Context mContext;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, RankingDrugDetail item);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public ResultsSpecificationsAdapter(Context context, List<RankingDrugDetail> datats,int relationPosiiton) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        for (int i = 0; i <datats.size() ; i++) {
            RankingDrugDetail d=datats.get(i);
            if(relationPosiiton==i){
                d.setSelected(true);
            }else{
                d.setSelected(false);
            }
            datats.set(i,d);
            if(d.getSpecifications().isEmpty()){
                datats.remove(i);
            }
        }
        mDatas = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView textViewSpec;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void updateItem(int positon) {
        for (int i = 0; i < mDatas.size(); i++) {
            RankingDrugDetail item = mDatas.get(i);
            if (positon == i) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
            mDatas.set(i, item);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_results_specifications, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.textViewSpec = (TextView) view.findViewById(R.id.specifications);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final RankingDrugDetail item = mDatas.get(i);
        final String spec = item.getSpecifications();
        viewHolder.textViewSpec.setText(spec);
        if(i==mDatas.size()-1){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.textViewSpec.getLayoutParams();
            layoutParams.rightMargin= DensityUtils.dp2px(30);
            viewHolder.textViewSpec.setLayoutParams(layoutParams);
        }
        if(i==0){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.textViewSpec.getLayoutParams();
            layoutParams.leftMargin= DensityUtils.dp2px(10);
            viewHolder.textViewSpec.setLayoutParams(layoutParams);
        }
        if (item.isSelected()) {
            viewHolder.textViewSpec.setTextColor(ContextCompat.getColor(mContext,R.color.color_343d43));
            viewHolder.textViewSpec.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_specifications_nor));
        } else {
            viewHolder.textViewSpec.setTextColor(ContextCompat.getColor(mContext,R.color.color_999999));
            viewHolder.textViewSpec.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_specifications_pre ));
        }
        if (mOnItemClickLitener != null) {
            viewHolder.textViewSpec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i, item);
                }
            });
        }

    }

}

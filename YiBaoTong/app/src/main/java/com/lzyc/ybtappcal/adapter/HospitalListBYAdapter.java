package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;

import java.util.List;

/**
 * Created by abc on 2017/4/19.
 */

public class HospitalListBYAdapter extends RecyclerView.Adapter<HospitalListBYAdapter.HospitalViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mData;

    public HospitalListBYAdapter(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void update(List<String> data){

    }

    @Override
    public HospitalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HospitalViewHolder(mInflater.inflate(R.layout.item_hospital_by, parent, false));
    }

    @Override
    public void onBindViewHolder(HospitalViewHolder holder, int position) {

        if(position == 0){
            holder.v_top_line.setVisibility(View.VISIBLE);
        } else {
            holder.v_top_line.setVisibility(View.GONE);
        }

        if(1 == 1){//
            holder.tv_phone.setText("13189876968");
            holder.tv_phone.setTextColor(mContext.getResources().getColor(R.color.color_3a95e2));
            Drawable drawable= mContext.getResources().getDrawable(R.mipmap.icon_liebiao_dianhua_you);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tv_phone.setCompoundDrawables(drawable,null,null,null);
        } else {
            holder.tv_phone.setText("暂无电话");
            holder.tv_phone.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            Drawable drawable= mContext.getResources().getDrawable(R.mipmap.icon_liebiao_dianhua_wu);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tv_phone.setCompoundDrawables(drawable,null,null,null);
        }

        holder.tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "13189876968");
                intent.setData(data);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
//        return mData == null ? 0 : mData.size();
        return 10;
    }

    class HospitalViewHolder extends RecyclerView.ViewHolder{

        View v_top_line;
        TextView tv_phone;

        public HospitalViewHolder(View itemView) {
            super(itemView);
            v_top_line = itemView.findViewById(R.id.v_top_line);
            tv_phone = (TextView) itemView.findViewById(R.id.et_phone);
        }
    }
}

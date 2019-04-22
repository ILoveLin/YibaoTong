package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.DrugBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxx on 2017/5/1.
 */

public class DrugSearchDimAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    List<DrugBean> mData = new ArrayList<>();

    public DrugSearchDimAdapter(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void updata(List<DrugBean> data){
        if(null != data && !data.isEmpty()){
            this.mData = data;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    ViewHolder vh;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(null == view){
            view = mInflater.inflate(R.layout.item_drug_search_dim, viewGroup, false);
            vh = new ViewHolder();
            vh.tvName = (TextView) view.findViewById(R.id.tv_name);
            vh.tvGoodsName = (TextView) view.findViewById(R.id.tv_goods_name);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        if(null == mData.get(i).getGoodsName() || "".equals(mData.get(i).getGoodsName())){
            vh.tvGoodsName.setText(mData.get(i).getName());
        } else {
            vh.tvGoodsName.setText(mData.get(i).getGoodsName());
            vh.tvName.setText(mData.get(i).getName());
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onDrugSearchDimListener != null){
                    onDrugSearchDimListener.onItem();
                }
            }
        });
        return view;
    }

    class ViewHolder{
        TextView tvName;
        TextView tvGoodsName;
    }

    private DrugSearchDimListener onDrugSearchDimListener;
    public void setOnDrugSearchDimListener(DrugSearchDimListener onDrugSearchDimListener){
        this.onDrugSearchDimListener = onDrugSearchDimListener;
    }
    public interface DrugSearchDimListener{
        void onItem();
    }
}

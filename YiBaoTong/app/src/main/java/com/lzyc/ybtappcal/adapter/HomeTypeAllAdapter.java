package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.home.HotActivity;
import com.lzyc.ybtappcal.bean.GoodsGetCategoryBean;
import com.lzyc.ybtappcal.constant.Contants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxx on 2017/5/1.
 */

public class HomeTypeAllAdapter extends RecyclerView.Adapter<HomeTypeAllAdapter.TypeViewHolder> {

    Context mContext;
    LayoutInflater mInflater;

    List<GoodsGetCategoryBean.CategoryBean> mData = new ArrayList<>();

    public HomeTypeAllAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void updata(List<GoodsGetCategoryBean.CategoryBean> data) {
        if (null != data && !data.isEmpty()) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public TypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TypeViewHolder(mInflater.inflate(R.layout.item_home_type_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(TypeViewHolder holder, final int position) {

        holder.tvType.setText(mData.get(position).getTitle());

        HomeTypeAllDrugsAdapter mAdapter = new HomeTypeAllDrugsAdapter(mContext);
        holder.recyclerview.setLayoutManager(new GridLayoutManager(mContext, 4));
        holder.recyclerview.setAdapter(mAdapter);
        mAdapter.updata(mData.get(position).getList());

        mAdapter.setOnTypeListener(new HomeTypeAllDrugsAdapter.TypeListener() {
            @Override
            public void onItem(String idStr, String title, String type) {

                Intent intent = new Intent(mContext, HotActivity.class);
                intent.putExtra(Contants.KEY_HOME_TITLE, title);
                intent.putExtra(Contants.KEY_HOME_TYPE, type);
                intent.putExtra(Contants.KEY_HOME_ID, idStr);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class TypeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;

        public TypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

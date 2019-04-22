package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.GoodsGetCategoryBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxx on 2017/5/9.
 */

public class HomeTypeAllDrugsAdapter extends RecyclerView.Adapter {

    Context mContext;
    LayoutInflater mInflater;
    List<GoodsGetCategoryBean.CategoryBean.ListBean> mData = new ArrayList<>();

    public HomeTypeAllDrugsAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void updata(List<GoodsGetCategoryBean.CategoryBean.ListBean> data) {
        if (null != data && !data.isEmpty()) {
            if(0 == mData.size()){
                mData.addAll(data);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new DrugViewHolder(mInflater.inflate(R.layout.item_category, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, final int position) {

        if(vh instanceof DrugViewHolder){
            DrugViewHolder holder = (DrugViewHolder) vh;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null != onTypeListener){
                        onTypeListener.onItem(mData.get(position).getID(), mData.get(position).getName(), "category");
                    }
                }
            });

            holder.tvDrug.setText(mData.get(position).getName());

            if(TextUtils.isEmpty(mData.get(position).getImage())) return;

            Picasso.with(mContext)
                    .load(mData.get(position).getImage())
                    .placeholder(R.mipmap.image_empty_category_big)
                    .error(R.mipmap.image_empty_category_big)
                    .into(holder.imgDrug);

        }
    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    class DrugViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_drug)
        ImageView imgDrug;
        @BindView(R.id.tv_drug)
        TextView tvDrug;

        public DrugViewHolder(View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    private TypeListener onTypeListener;

    public void setOnTypeListener(TypeListener onTypeListener) {
        this.onTypeListener = onTypeListener;
    }

    public interface TypeListener {
        void onItem(String idStr,  String title, String type);
    }
}

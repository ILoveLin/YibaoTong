package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.CategoryBean;
import com.lzyc.ybtappcal.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxx on 2017/5/12.
 */

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.CategoryViewHolder> {

    List<CategoryBean> mData = new ArrayList<>();
    LayoutInflater mInflater;
    Context mContext;

    public HomeCategoryAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void updata(List<CategoryBean> data) {

        mData.clear();
        if (null != data && !data.isEmpty()) {
            try{
                mData.addAll(data);

                mData.remove(mData.size()-1);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(mInflater.inflate(R.layout.item_home_type, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != onCategoryListener){
                    onCategoryListener.onHotClickListener(mData.get(position).getName(), "category", mData.get(position).getID(), position);
                }
            }
        });

        holder.tvCategory.setText(mData.get(position).getName());

        if(TextUtils.isEmpty(mData.get(position).getIcon())) return;

//        Picasso.with(mContext)
//                .load(mData.get(position).getIcon())
//                .placeholder(R.mipmap.image_empty_category)
//                .error(R.mipmap.image_empty_category)
//                .into(holder.imgCategory);
        holder.imgCategory.setImageURI(Uri.parse(mData.get(position).getIcon()));

//        ImageUtil.showThumb(mContext, Uri.parse(mData.get(position).getIcon()), holder.imgCategory, RecyclerView.LayoutParams.MATCH_PARENT, 29);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_category)
        SimpleDraweeView imgCategory;
        @BindView(R.id.tv_category)
        TextView tvCategory;
        @BindView(R.id.layout)
        LinearLayout layout;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth() / 4, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private CategoryListener onCategoryListener;

    public void setOnCategoryListener(CategoryListener onCategoryListener) {
        this.onCategoryListener = onCategoryListener;
    }

    public interface CategoryListener {

        void onHotClickListener(String title, String type, String idStr, int postion);

    }

}

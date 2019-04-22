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
import com.lzyc.ybtappcal.bean.MedicineChestBean;
import com.lzyc.ybtappcal.bean.MedicineFamilyBean;
import com.lzyc.ybtappcal.util.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxx on 2017/5/25.
 */

public class MedicinePersonAdapter extends RecyclerView.Adapter {

    LayoutInflater mInflater;

    ArrayList<MedicineChestBean.ListBean> mData = new ArrayList<>();

    MedicineFamilyBean.ListBean titleBean = new MedicineFamilyBean.ListBean();

    Context mContext;

    int pos = -1;

    public MedicinePersonAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void updata(ArrayList<MedicineChestBean.ListBean> data) {
        mData.clear();
        if (null != data && !data.isEmpty()) {

            if(-1 != pos && data.size()-1 >= pos){
                data.remove(pos);

                pos = -1;
            }
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void updataRemove(int pos) {
        this.pos = pos;
        mData.remove(pos);
        notifyDataSetChanged();
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, getItemCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MPViewHolder(mInflater.inflate(R.layout.item_drug_medicine, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder, final int pos) {

        if (itemHolder instanceof MPViewHolder) {
            MPViewHolder holder = (MPViewHolder) itemHolder;

            final MedicineChestBean.ListBean bean = mData.get(pos);

            String name = bean.getName();
            String goodsName = bean.getGoodsName();
            String specifications = bean.getSpecifications();

            if (TextUtils.isEmpty(goodsName)) {
                holder.tvName.setText(StringUtils.getSpannableText(name + " ", specifications));
            } else {
                holder.tvName.setText(StringUtils.getSpannableText(goodsName + " " + name + " ", specifications));
            }

            holder.tvVender.setText(bean.getVender());

            if (pos == mData.size() - 1) {
                holder.vBottom.setVisibility(View.VISIBLE);
            } else {
                holder.vBottom.setVisibility(View.GONE);
            }

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (null != onMedicinePersonListener) {
                        onMedicinePersonListener.onItemLong(bean.getID(), pos);
                    }

                    return true;
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onMedicinePersonListener) {
                        onMedicinePersonListener.onItem(bean.getDrugID());
                    }
                }
            });

            if (TextUtils.isEmpty(bean.getImage())) return;

            Picasso.with(mContext)
                    .load(bean.getImage())
                    .placeholder(R.mipmap.image_cache_drug)
                    .error(R.mipmap.image_cache_drug)
                    .into(holder.imgGoods);
        }

    }

    public int getDataSize(){
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class MPViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_goods)
        ImageView imgGoods;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_vender)
        TextView tvVender;
        @BindView(R.id.v_bottom)
        View vBottom;

        public MPViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private MedicinePersonListener onMedicinePersonListener;

    public void setOnMedicinePersonListener(MedicinePersonListener onMedicinePersonListener) {
        this.onMedicinePersonListener = onMedicinePersonListener;
    }

    public interface MedicinePersonListener {

        void onItemLong(String id, int pos);

        void onItem(String id);
    }

}

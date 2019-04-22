package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.TBanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxx on 2017/5/2.
 */

public class HomeTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    LayoutInflater mInflater;
    List<String> mData = new ArrayList<>();

    public HomeTypeAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void updata(List<String> data){
        if(null != data && !data.isEmpty()){
            this.mData = data;
        }

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (1 == viewType) {
            return new BannerViewHolder(mInflater.inflate(R.layout.item_home_banner, parent, false));
        }
        return new DrugsViewHolder(mInflater.inflate(R.layout.item_type_drug, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        LogUtil.d("lxx", "43");
        if(holder instanceof BannerViewHolder){
            final BannerViewHolder vh = (BannerViewHolder) holder;

//            vh.tbanner.update(mList);

            vh.tbanner.setOnImgRollChanged(new TBanner.ImgRollChanged() {
                @Override
                public void onChanged(int p) {
                    if(null != onHomeTypeListener){
                        onHomeTypeListener.onBannerListener();
                    }
                }
            });
        }

        if(holder instanceof DrugsViewHolder){
            final DrugsViewHolder vh = (DrugsViewHolder) holder;

            vh.tvPrice.setText(mData.get(position).toString());

            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null != onHomeTypeListener){
                        onHomeTypeListener.onDrugListener();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null != mData ? mData.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position) {
            return 1;
        }
        return 2;
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tbanner)
        TBanner tbanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class DrugsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_drug)
        ImageView imgDrug;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_return_price)
        TextView tvReturnPrice;

        public DrugsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private HomeTypeListener onHomeTypeListener;

    public void setHomeTypeListener(HomeTypeListener homeTypeListener){
        this.onHomeTypeListener = homeTypeListener;
    }

    public interface HomeTypeListener{
        void onBannerListener();
        void onDrugListener();
    }
}

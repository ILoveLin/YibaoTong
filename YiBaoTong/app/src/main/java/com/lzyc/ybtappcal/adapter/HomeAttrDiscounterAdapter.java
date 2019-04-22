package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.GoodsListBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxx on 2017/7/19.
 */

public class HomeAttrDiscounterAdapter extends PagerAdapter {

    protected Context mContext;

    protected View[] mInflatedViews;
    protected View[] mImageViews;

    List<GoodsListBean> mData = new ArrayList<>();

    public HomeAttrDiscounterAdapter(Context context) {
        this.mContext = context;
    }



    private void initViews(List<GoodsListBean> data) {
        for (int i = 0; i < getItemCount(); i++) {
            mInflatedViews[i] = LayoutInflater.from(mContext).inflate(R.layout.item_home_drugs_small, null, false);
            mImageViews[i] = showView(mInflatedViews[i], i);
            final int finalI = i;
            mImageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null  != onHomeDiscounterListener){
                        onHomeDiscounterListener.onItenClick(mData.get(finalI).getDKID());
                    }
                }
            });
        }
    }

    public void updata(List<GoodsListBean> data){
        mData.clear();
        if(null != data && !data.isEmpty()){
            mData.addAll(data);
            mInflatedViews = new View[mData.size()];
            mImageViews = new View[mData.size()];
        }
        initViews(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(1 == getItemCount()){
            return 1;
        }
        return getItemCount() * 1000;
    }

    protected int getItemCount(){
        return mData.size();
    }

    protected View showView(View inflatedLayout, int position){
        AttributeViewHolderSmall holder;
        holder = new AttributeViewHolderSmall();
        holder.imgDrugSmall = (ImageView) inflatedLayout.findViewById(R.id.item_home_drug_small);
        holder.tvReturnPriceSmall = (TextView) inflatedLayout.findViewById(R.id.item_home_return_price_small);
        holder.tvGoodsNameSmall = (TextView) inflatedLayout.findViewById(R.id.tv_goods_name_small);
        holder.tvNameSmall = (TextView) inflatedLayout.findViewById(R.id.tv_name_small);
        holder.tvPriceSmall = (TextView) inflatedLayout.findViewById(R.id.item_home_drug_price_small);

        final GoodsListBean b = mData.get(position);

        String goodsName = "";
        String name = "";

        if (TextUtils.isEmpty(b.getGoodsName())) {
            goodsName = b.getName();
        } else {
            goodsName = b.getGoodsName();
            name = b.getName();
        }

        holder.tvGoodsNameSmall.setText(goodsName);
        holder.tvNameSmall.setText(name);
        holder.tvPriceSmall.setText(b.getDeKaiPrice());
        holder.tvReturnPriceSmall.setText("返现"+b.getReturnMoney());

        if(!TextUtils.isEmpty(b.getImage())){
            Picasso.with(mContext)
                    .load(b.getImage())
                    .placeholder(R.mipmap.image_empty_bg_white)
                    .error(R.mipmap.image_empty_bg_white)
                    .config(Bitmap.Config.RGB_565)
                    .tag("list")
                    .into(holder.imgDrugSmall);
        }
        return inflatedLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = mInflatedViews[position % getItemCount()];
        if (view.getParent() != null) {
            ((ViewGroup) (view.getParent())).removeView(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    class AttributeViewHolderSmall{
        ImageView imgDrugSmall;
        TextView tvReturnPriceSmall;
        TextView tvGoodsNameSmall;
        TextView tvNameSmall;
        TextView tvPriceSmall;

    }

    private HomeDiscounterListener onHomeDiscounterListener;
    public void setOnHomeDiscounterListener(HomeDiscounterListener onHomeDiscounterListener){
        this.onHomeDiscounterListener = onHomeDiscounterListener;
    }
    public interface HomeDiscounterListener{
        void onItenClick(String idStr);
    }
}

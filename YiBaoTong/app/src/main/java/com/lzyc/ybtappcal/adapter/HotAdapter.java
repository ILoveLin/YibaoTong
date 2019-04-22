package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.GoodsListBean;
import com.lzyc.ybtappcal.util.StringUtils;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页-->热销／父母用药-->药品
 * Created by lxx on 2017/5/1.
 */

public class HotAdapter extends CommonAdapter<GoodsListBean> {

    private int listSize = 0;
    private int listRecommendSize=0;

    public HotAdapter(Context context, int layoutId, List<GoodsListBean> datas) {
        super(context, layoutId, datas);
    }

    public void setList(List<GoodsListBean> list) {
        if(list==null){
            list=new ArrayList<>();
        }
        mDatas = list;
        listSize = list.size();
        notifyDataSetChanged();
    }

    public void addList(List<GoodsListBean> listRecommend) {
        if(listRecommend==null){
            listRecommend=new ArrayList<>();
        }
        mDatas.addAll(listRecommend);
        listRecommendSize = listRecommend.size();
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder helper, GoodsListBean item, int position) {
        TextView goodsPrice=helper.getView(R.id.item_home_drug_price);
        TextView goodsReturnPrice=helper.getView(R.id.item_home_return_price);
        TextView goodsManufacturer = helper.getView(R.id.item_home_manufacturer);
        View viewTop = helper.getView(R.id.view_top);
        if(position==0&&listSize>0){
            viewTop.setVisibility(View.VISIBLE);
        }else{
            viewTop.setVisibility(View.GONE);
        }
        ImageView goodsImage = helper.getView(R.id.item_goods_image);
        Picasso.with(mContext)
                .load(item.getImage())
                .placeholder(R.mipmap.image_cache_drug)
                .error(R.mipmap.image_cache_drug)
                .into(goodsImage);
        TextView tvGoodsName = helper.getView(R.id.item_goods_drugname);
        String name = item.getName();
        String goodsName = item.getGoodsName();
        if(TextUtils.isEmpty(goodsName)){
            tvGoodsName.setText(StringUtils.getSpannableText(name+" ",item.getSpecifications()));
        }else{
            tvGoodsName.setText(StringUtils.getSpannableText(goodsName+" "+name+" ",item.getSpecifications()));
        }
        goodsPrice.setText(item.getDeKaiPrice());
        goodsReturnPrice.setText(item.getReturnMoney());
        goodsManufacturer.setText(item.getVender());
    }

}

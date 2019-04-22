package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.GoodsSearch;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2017/05/09.
 */
public class HomeGoodsSearchAdapter extends CommonAdapter<GoodsSearch> {
    private int listSize = 0;
    private int listRecommendSize=0;

    public HomeGoodsSearchAdapter(Context context, int layoutId, List<GoodsSearch> datas) {
        super(context, layoutId, datas);
    }

    public void setList(List<GoodsSearch> list) {
        if(list==null){
            list=new ArrayList<>();
        }
        mDatas = list;
        listSize = list.size();
    }

    public void addListRecommend(List<GoodsSearch> listRecommend) {
        if(listRecommend==null){
            listRecommend=new ArrayList<>();
        }
        listRecommendSize = listRecommend.size();
        mDatas.addAll(listRecommend);
        notifyDataSetChanged();
    }


    @Override
    protected void convert(ViewHolder helper, GoodsSearch item, int position) {
        LinearLayout goodsSearchHeader = helper.getView(R.id.goods_search_header);
        TextView goodsHeaderDesc = helper.getView(R.id.goods_header_desc);
        TextView goodsPrice=helper.getView(R.id.item_home_drug_price);
        TextView goodsReturnPrice=helper.getView(R.id.item_home_return_price);
        TextView goodsManufacturer = helper.getView(R.id.item_home_manufacturer);
        View viewTop = helper.getView(R.id.view_top);
        if(position==0&&listSize>0){
            viewTop.setVisibility(View.VISIBLE);
        }else{
            viewTop.setVisibility(View.GONE);
        }
        if (listSize > 0&&listRecommendSize==0) {//如果有搜索数据，没有推荐
            goodsSearchHeader.setVisibility(View.GONE);
        }
        if (listSize > 0 && listRecommendSize > 0) {//如果有数据并且有推荐
            if (position ==listSize) {
                goodsSearchHeader.setVisibility(View.VISIBLE);
                goodsHeaderDesc.setVisibility(View.GONE);
            }else{
                goodsSearchHeader.setVisibility(View.GONE);
            }
        }
        if (listSize == 0 && listRecommendSize > 0) {//如果没有数据但是有推荐
            if (position == 0) {
                goodsSearchHeader.setVisibility(View.VISIBLE);
                goodsHeaderDesc.setVisibility(View.VISIBLE);
            }else{
                goodsSearchHeader.setVisibility(View.GONE);
            }
        }
        ImageView goodsImage = helper.getView(R.id.item_goods_image);
        Picasso.with(mContext).load(item.getImage()).placeholder(R.mipmap.image_empty_order).error(R.mipmap.image_empty_order).into(goodsImage);
        TextView tvGoodsName = helper.getView(R.id.item_goods_drugname);
        String name = item.getName();
        String goodsName = item.getGoodsName();
        if(TextUtils.isEmpty(goodsName)){
            tvGoodsName.setText(getSpannableText(name+" ",item.getSpecifications()));
        }else{
            tvGoodsName.setText(getSpannableText(goodsName+" "+name+" ",item.getSpecifications()));
        }
        goodsPrice.setText(item.getDeKaiPrice());
        goodsReturnPrice.setText(item.getReturnMoney());
        goodsManufacturer.setText(item.getVender());
    }


    /**
     * 设置内部字体颜色
     *
     * @param text
     * @return
     */
    private SpannableStringBuilder getSpannableText(String text,String speci) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(speci);
        AbsoluteSizeSpan sizeSpan=new AbsoluteSizeSpan(DensityUtils.sp2px(13));
        spannableStringBuilder2.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), 0, speci.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder2.setSpan(sizeSpan,0,speci.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.append(spannableStringBuilder2);
        return spannableStringBuilder;
    }

}

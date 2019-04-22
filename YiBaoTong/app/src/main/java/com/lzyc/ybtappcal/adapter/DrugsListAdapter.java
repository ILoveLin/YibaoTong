package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 作者：xujm
 * 时间：2016/3/4
 * 备注：DrugsListAdapter
 */
public class DrugsListAdapter extends CommonAdapter<DrugBean> {

    private String keyworld="";

    public DrugsListAdapter(Context context, int layoutId, List<DrugBean> datas) {
        super(context, layoutId, datas);
    }

    public void setItemSeleted(int position,DrugBean drugBean){
        mDatas.set(position,drugBean);
        notifyDataSetChanged();
    }

    public void setKeyworldSpan(String keyworld) {
        this.keyworld = keyworld;
    }

    @Override
    public void convert(final ViewHolder helper, final DrugBean item, final int position) {
        final ImageView iv = helper.getView(R.id.item_drugs_search_photo);
        TextView tv_drugname = helper.getView(R.id.item_drugs_search_drugname);
        TextView tv_num = helper.getView(R.id.item_drugs_search_drugs_price);
        TextView tv_address = helper.getView(R.id.item_drugs_search_address);
        //认可度第几名的背景图
        TextView item_drugs_search_drugs_qian = helper.getView(R.id.item_drugs_search_drugs_qian);
        LinearLayout item_linear_drug = helper.getView(R.id.ll_bgitem_item_drug);
        ImageView imgAdded = helper.getView(R.id.item_drugs_search_added);
        String images = item.getImage();
        if(item.getMedicineChest().equals("1")){
            item_linear_drug.setBackgroundColor(Color.parseColor("#08000000"));
            imgAdded.setVisibility(View.VISIBLE);
        }else{
            imgAdded.setVisibility(View.GONE);
            item_linear_drug.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
        }
        if (!TextUtils.isEmpty(images)) {
            Picasso.with(mContext)
                    .load(images)
                    .placeholder(R.mipmap.image_cache_drug)
                    .error(R.mipmap.image_cache_drug)
                    .into(iv);
        } else {
            iv.setImageResource(R.mipmap.icon_sanca_history);
        }
//        iv.setBackgroundResource(R.drawable.shape_image_drug);
        String goodsName = item.getGoodsName();
//        int renkedu = Integer.parseInt(item.getRenkedu());
        int adopt = item.getAdopt();
        String name = item.getName();
        String speci= item.getSpecifications();
        if (!TextUtils.isEmpty(goodsName) && goodsName != null) {
            tv_drugname.setText(getSpannableText(goodsName+" "+name, keyworld, adopt,speci));
        } else {
            tv_drugname.setText(getSpannableText(name, keyworld, adopt,speci));
        }
        String price = item.getPrice();
        if (TextUtils.isEmpty(price)) {
            tv_num.setVisibility(View.VISIBLE);
            tv_num.setText("暂无报价");
            item_drugs_search_drugs_qian.setVisibility(View.GONE);
        } else {
            if(Double.parseDouble(price)==0){
                tv_num.setVisibility(View.VISIBLE);
                tv_num.setText("暂无报价");
                item_drugs_search_drugs_qian.setVisibility(View.GONE);
            }else{
                tv_num.setVisibility(View.VISIBLE);
                item_drugs_search_drugs_qian.setVisibility(View.VISIBLE);
                item_drugs_search_drugs_qian.setText("￥");
                tv_num.setText(price);
            }
        }
        tv_address.setText("" + item.getVender());
        if (adopt == 0) { //无医院采纳
            item_drugs_search_drugs_qian.setTextColor(ContextCompat.getColor(mContext,R.color.color_999999));
            tv_num.setTextColor(ContextCompat.getColor(mContext,R.color.color_999999));
        } else {
            item_drugs_search_drugs_qian.setTextColor(ContextCompat.getColor(mContext,R.color.color_666666));
            tv_num.setTextColor(ContextCompat.getColor(mContext,R.color.color_666666));
        }
        item_linear_drug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.getMedicineChest().equals("0")){
                    if(listener!=null){
                        listener.onItemClickListener(iv,position,item);
                    }
                }else{
                    ToastUtil.showToastCenter(mContext,"该药品已添加到药箱!");
                }
            }
        });
        tv_drugname.setTextColor(ContextCompat.getColor(mContext,R.color.color_333333));
    }

    /**
     * 设置内部字体颜色
     *
     * @param text
     * @param keyworld
     * @return
     */
    private SpannableStringBuilder getSpannableText(String text, String keyworld, int adopt,String speci) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        if (text.contains(keyworld)) {
            int spanStartIndex = text.indexOf(keyworld);
            int spacEndIndex = spanStartIndex + keyworld.length();
            if (adopt == 0) {
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#ff6969")), spanStartIndex, spacEndIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            } else {
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#d82626")), spanStartIndex, spacEndIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(speci);
        AbsoluteSizeSpan sizeSpan=new AbsoluteSizeSpan(DensityUtils.dp2px(13));
        spannableStringBuilder2.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), 0, speci.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder2.setSpan(sizeSpan,0,speci.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.append(spannableStringBuilder2);
        return spannableStringBuilder;
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    public interface OnItemClickListener{
       void onItemClickListener(ImageView imageView,int position,DrugBean drugBean);
    }
}

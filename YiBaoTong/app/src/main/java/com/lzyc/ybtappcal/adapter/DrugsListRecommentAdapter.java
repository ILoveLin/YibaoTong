package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 作者：xujm
 * 时间：2016/3/4
 * 备注：DrugsListAdapter
 */
public class DrugsListRecommentAdapter extends CommonAdapter<DrugBean> {

    private String keyworld;

    public DrugsListRecommentAdapter(Context context, int layoutId, List<DrugBean> datas) {
        super(context, layoutId, datas);
    }


    public void setKeyworldSpan(String keyworld){
        this.keyworld=keyworld;
    }

    @Override
    public void convert(ViewHolder helper, DrugBean item, int position) {
        ImageView iv = helper.getView(R.id.item_drugs_search_photo);
        TextView tv_drugname = helper.getView(R.id.item_drugs_search_drugname);
        TextView tv_name = helper.getView(R.id.item_drugs_search_name);
        TextView tv_num = helper.getView(R.id.item_drugs_search_drugs_spec);
        TextView tv_address = helper.getView(R.id.item_drugs_search_address);
        LinearLayout item_linear_drug=helper.getView(R.id.ll_bgitem_item_drug);
        String images = item.getScanName();
        if (!TextUtils.isEmpty(images)) {
            Picasso.with(mContext)
                    .load(images)
                    .placeholder(R.mipmap.image_cache_drug_list)
                    .error(R.mipmap.image_cache_drug_list)
                    .resize(138,136)
                    .into(iv);
        }
        iv.setBackgroundResource(R.drawable.shape_image_drug);
        String goodsName = item.getGoodsName();
        String name = item.getName();
        if (!TextUtils.isEmpty(goodsName)&&goodsName!=null) {
            tv_name.setText(getSpannableText(name,keyworld));
            tv_drugname.setText(getSpannableText(goodsName,keyworld));
        } else {
            tv_drugname.setText(getSpannableText(name,keyworld));
            tv_name.setText("");
        }
//        if (!TextUtils.isEmpty(item.getSpecifications())&&item.getSpecifications()!=null) {
//            getSpannableText(item.getSpecifications(),keyworld));
//        }else{
//            tv_num.setText(""+item.getSpecifications());
//        }
        tv_num.setText("￥"+item.getPrice());
        tv_address.setText(""+item.getVender());
        switch (position%2){
            case 0:
                item_linear_drug.setBackgroundResource(R.drawable.selector_item_fdfdfe);
                break;
            case 1:
                item_linear_drug.setBackgroundResource(R.drawable.selector_item_f9f9fb);
                break;
        }
    }

    /**
     * 设置内部字体颜色
     * @param text
     * @param keyworld
     * @return
     */
    private SpannableStringBuilder getSpannableText(String text,String keyworld){
        SpannableStringBuilder spannableStringBuilder=new SpannableStringBuilder(text);
        if(text.contains(keyworld)){
            int spanStartIndex=text.indexOf(keyworld);
            int spacEndIndex=spanStartIndex+keyworld.length();
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#d82626")),spanStartIndex,spacEndIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return spannableStringBuilder;
    }
}

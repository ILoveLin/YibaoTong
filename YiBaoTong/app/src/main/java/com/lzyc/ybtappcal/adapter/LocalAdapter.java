package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xujm
 * 时间：2016/2/22
 * 备注：LocalAdapter
 */
public class LocalAdapter extends CommonAdapter<DrugBean> {

    public LocalAdapter(Context context, int layoutId, List<DrugBean> datas) {
        super(context, layoutId, datas);
    }

    public void setList(List<DrugBean> drugBeanList){
        if(this.mDatas!=null){
            mDatas.clear();
        }else{
            mDatas=new ArrayList<DrugBean>();
        }
        this.mDatas=drugBeanList;
        notifyDataSetChanged();
    }

    public void addList(List<DrugBean> drugBeanList){
        if(this.mDatas==null){
            mDatas=new ArrayList<DrugBean>();
        }
        mDatas.addAll(drugBeanList);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder helper, DrugBean item, int position) {
        ImageView iv = helper.getView(R.id.item_drugs_search_photo);
        TextView tv_drugname = helper.getView(R.id.item_drugs_search_drugname);
        TextView tv_name = helper.getView(R.id.item_drugs_search_name);
        TextView tv_spec = helper.getView(R.id.item_drugs_search_drugs_spec);
        TextView tv_address = helper.getView(R.id.item_drugs_search_address);
        TextView tv_time = helper.getView(R.id.item_drugs_search_time);
        LinearLayout ll_bgitem_item_drug = helper.getView(R.id.ll_bgitem_item_drug);
        String images = item.getScanName();
        if (!TextUtils.isEmpty(images)) {
            Picasso.with(mContext)
                    .load(images)
                    .placeholder(R.mipmap.image_cache_drug_list)
                    .error(R.mipmap.image_cache_drug_list)
                    .into(iv);
        } else {
            iv.setImageResource(R.mipmap.icon_sanca_history);
        }
        String goodsName = item.getGoodsName();
        String name = item.getName();
        if (!TextUtils.isEmpty(goodsName)) {
            tv_drugname.setText(goodsName);
            tv_name.setText(name);
        } else {
            tv_drugname.setText(name);
            tv_name.setText("");
        }
        tv_spec.setText(item.getSpecifications()+"(每片量) *"+item.getConversion()+"/"+item.getUnit());
        tv_address.setText(item.getVender());
        tv_time.setText(item.getDate());
        switch (position%2){
            case 0:
                ll_bgitem_item_drug.setBackgroundResource(R.drawable.selector_item_fdfdfe);
                break;
            case 1:
                ll_bgitem_item_drug.setBackgroundResource(R.drawable.selector_item_f9f9fb);
                break;
        }
    }
}

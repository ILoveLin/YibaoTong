package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.DimGoods;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 商品联想搜素列表
 * Created by yang on 2017/05/09.
 */
public class SearchDimGoodsAdapter extends CommonAdapter<DimGoods> {


    public SearchDimGoodsAdapter(Context context, int layoutId, List<DimGoods> datas) {
        super(context, layoutId, datas);
    }

    public void setList(List<DimGoods> mDatas){
        this.mDatas=mDatas;
        notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder helper, DimGoods item, int position) {
        TextView textViewGoodsName = helper.getView(R.id.item_search_dim_name_goods);
        TextView textViewName = helper.getView(R.id.item_search_dim_name);
        String goodsName=item.getGoodsName();
        String name=item.getName();
        if (!TextUtils.isEmpty(goodsName)&&goodsName!=null) {
            textViewGoodsName.setText(goodsName);
            textViewName.setText(name);
        }else {
            textViewGoodsName.setText(name);
            textViewName.setText("");
        }
    }

}

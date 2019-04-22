package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.HotGoods;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 商品热门搜索标签
 * create by yang
 */
public class SearchHotGoodsAdapter extends CommonAdapter<HotGoods> {

    public SearchHotGoodsAdapter(Context context, int layoutId, List<HotGoods> datas) {
        super(context, layoutId, datas);
    }

    public void setList(List<HotGoods> mDatas){
        this.mDatas=mDatas;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder helper, HotGoods item, int position) {
        TextView textView = helper.getView(R.id.item_search_hot);
        textView.setText(""+item.getName());
        switch (item.getColorIndex()){
            case 0:
                textView.setBackgroundResource(R.drawable.shape_item_hot);
                break;
            case 1:
                textView.setBackgroundResource(R.drawable.shape_item_hot_fafffa);
                break;
            case 2:
                textView.setBackgroundResource(R.drawable.shape_item_hot_fff9f9);
                break;
            case 3:
                textView.setBackgroundResource(R.drawable.shape_item_hot_f3f8ff);
                break;
            case 4:
                textView.setBackgroundResource(R.drawable.shape_item_hot_fffef1);
                break;
            case 5:
                textView.setBackgroundResource(R.drawable.shape_item_hot_fcf5ff);
                break;
        }
    }

}

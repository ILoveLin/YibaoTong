package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.HistoryCache;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 商品搜索历史记录
 * Created by yang on 2017/05/08.
 */
public class SearchHistoryGoodsAdapter extends CommonAdapter<HistoryCache> {

    public SearchHistoryGoodsAdapter(Context context, int layoutId, List<HistoryCache> datas) {
        super(context, layoutId, datas);
    }


    public void setList(List<HistoryCache> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder helper, HistoryCache item, int position) {
        TextView textView = helper.getView(R.id.item_search_hot);
        String goodsName=item.getGoodsName();
        String name=item.getName();
        if(!TextUtils.isEmpty(goodsName)){
            name=goodsName+" "+item.getName();
        }
        textView.setText("" + name);
    }

}

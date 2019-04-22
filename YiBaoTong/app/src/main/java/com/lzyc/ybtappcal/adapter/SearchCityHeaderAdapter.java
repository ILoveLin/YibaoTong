package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.WenziBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 热门搜索城市标签
 * create by yang
 */
public class SearchCityHeaderAdapter extends CommonAdapter<WenziBean> {

    public SearchCityHeaderAdapter(Context context, int layoutId, List<WenziBean> datas) {
        super(context, layoutId, datas);
    }

    public void setList(List<WenziBean> mDatas){
        this.mDatas=mDatas;
        notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder helper, WenziBean item, int position) {
        TextView textView = helper.getView(R.id.item_search_citylist);
        textView.setText(""+item.getName());
    }

}

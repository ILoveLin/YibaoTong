package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.HotDrug;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 热门搜索标签
 * create by yang
 */
public class SearchHotAdapter extends CommonAdapter<HotDrug> {

    public SearchHotAdapter(Context context, int layoutId, List<HotDrug> datas) {
        super(context, layoutId, datas);
    }

    public void setList(List<HotDrug> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder helper, HotDrug item, int position) {
        TextView textView = helper.getView(R.id.item_search_hot);
        textView.setText("" + item.getDrug_name());
        switch (item.getColorIndex()) {
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

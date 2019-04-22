package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.WenziBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by yang on 2017/01/13.
 */

public class SearchCityLXAdapter extends CommonAdapter<WenziBean> {


    public SearchCityLXAdapter(Context context, int layoutId, List<WenziBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder helper, WenziBean item, int position) {
        TextView tvName = helper.getView(R.id.item_citylx_tv);
        tvName.setText(item.getName());

    }
}

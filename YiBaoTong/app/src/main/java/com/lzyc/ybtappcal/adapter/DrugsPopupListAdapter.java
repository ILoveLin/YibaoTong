package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 作者：xujm
 * 时间：2016/3/4
 * 备注：DrugsListAdapter
 */
public class DrugsPopupListAdapter extends CommonAdapter<String> {

    public DrugsPopupListAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder helper, String item, int position) {
        TextView tv_name = helper.getView(R.id.item_popup_durginfo_name);
        tv_name.setText(item);


    }
}

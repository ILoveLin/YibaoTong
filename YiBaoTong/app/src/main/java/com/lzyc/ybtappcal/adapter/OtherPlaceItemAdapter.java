package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 作者：xujm
 * 时间：2016/2/22
 * 备注：OtherPlaceItemAdapter
 */
public class OtherPlaceItemAdapter extends CommonAdapter<DrugBean> {

    public OtherPlaceItemAdapter(Context context, int layoutId, List<DrugBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder helper, DrugBean item, int position) {
        TextView tv_item_other_place_name = helper.getView(R.id.tv_item_other_place_name);
        TextView item_other_place_goodsname = helper.getView(R.id.item_other_place_goodsname);
        TextView tv_time = helper.getView(R.id.tv_item_other_place_time);
        String goodsName=item.getGoodsName();
        String name=item.getName();
        if (!TextUtils.isEmpty(goodsName)&&goodsName!=null) {
            tv_item_other_place_name.setText(name);
            item_other_place_goodsname.setText(goodsName);
        }else {
            item_other_place_goodsname.setText(name);
            tv_item_other_place_name.setText("");
        }
        tv_time.setText(item.getDate());
    }

}

package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.GoodsBean;
import com.lzyc.ybtappcal.util.StringUtils;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by yang on 2017/05/03.
 */

public class OrderDetailAdapter extends CommonAdapter<GoodsBean> {

    public OrderDetailAdapter(Context context, int layoutId, List<GoodsBean> datas) {
        super(context, layoutId, datas);
    }

    public void setList(List<GoodsBean> list) {
        mDatas = list;
        notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder helper, GoodsBean item, int position) {
        ImageView orderDetailImage=helper.getView(R.id.order_detail_image);
        TextView orderDetailDrugname=helper.getView(R.id.order_detail_drugname);
        TextView orderDetailType = helper.getView(R.id.order_detail_type);
        TextView order_detail_price_retail=helper.getView(R.id.order_detail_price_retail);
        TextView order_detail_price_unit=helper.getView(R.id.order_detail_price_unit);
        TextView order_detail_count=helper.getView(R.id.order_detail_count);
        order_detail_price_unit.setText(item.getDeKaiPrice());
        order_detail_count.setText("x" + item.getCount());//商品数量
        String typeDrug = "";
        String textName="";
        if ("1".equals(item.getType())) {
            typeDrug = "【非处方】";
            textName="                 "+ item.getGoodsName() + " " + item.getName();
        } else {
            typeDrug = "【处方】";
            textName="             "+ item.getGoodsName() + " " + item.getName();
        }
        orderDetailType.setText(typeDrug);
        orderDetailDrugname.setText(StringUtils.getSpannableText(textName, " " + item.getSpecifications()));
        Picasso.with(mContext).load(item.getImage()).error(R.mipmap.image_empty_order2).placeholder(R.mipmap.image_empty_order2).into(orderDetailImage);
        order_detail_price_retail.setText(item.getRetailPrice());
        order_detail_price_retail.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

    }
}

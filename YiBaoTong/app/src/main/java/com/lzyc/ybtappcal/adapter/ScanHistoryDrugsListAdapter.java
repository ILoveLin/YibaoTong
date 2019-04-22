package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.HistoryCache;
import com.lzyc.ybtappcal.util.StringUtils;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 作者：xujm
 * 时间：2016/3/4
 * 备注：DrugsListAdapter
 */
public class ScanHistoryDrugsListAdapter extends CommonAdapter<HistoryCache> {

    public ScanHistoryDrugsListAdapter(Context context, int layoutId, List<HistoryCache> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder helper, HistoryCache item, int position) {
        ImageView item_drugs_search_photo = helper.getView(R.id.item_drugs_search_photo);
        TextView item_drugs_search_drugname = helper.getView(R.id.item_drugs_search_drugname);
        TextView item_drugs_search_drug_bar_code = helper.getView(R.id.item_drugs_search_drug_bar_code);

        String scanInageUrl = item.getImageUrl();
        String scanName = item.getName();
        String goodsName = item.getGoodsName();
        String scanCode = item.getCode();

        Picasso.with(mContext)
                .load(scanInageUrl)
                .placeholder(R.mipmap.icon_sanca_history)
                .error(R.mipmap.icon_sanca_history)
                .into(item_drugs_search_photo);

        if (!TextUtils.isEmpty(goodsName)) {
            item_drugs_search_drugname.setText(StringUtils.getSpannableText(goodsName + " ", scanName));
        } else {
            item_drugs_search_drugname.setText(StringUtils.getSpannableText(scanName, ""));
        }

        item_drugs_search_drug_bar_code.setText(scanCode);
    }

}

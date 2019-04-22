package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 联想搜索
 * Created by yang on 2017/03/06.
 */
public class DrugDimAdapter extends CommonAdapter<DrugBean> {
    private int typePage;
    private String keyworld;
    private boolean isDim;//是否是联想搜索

    public DrugDimAdapter(Context context, int layoutId, List<DrugBean> datas) {
        super(context, layoutId, datas);
    }

    public DrugDimAdapter(Context context, int layoutId, List<DrugBean> datas, int typePage) {
        super(context, layoutId, datas);
        this.typePage = typePage;
    }

    public void setKeyworldSpan(String keyworld, boolean isDim) {
        this.keyworld = keyworld;
        this.isDim = isDim;
    }


    @Override
    public void convert(ViewHolder helper, DrugBean item, int position) {
        TextView textViewGoodsName = helper.getView(R.id.item_search_dim_name_goods);
        TextView textViewName = helper.getView(R.id.item_search_dim_name);
        textViewGoodsName.setCompoundDrawables(null, null, null, null);
        String goodsName = item.getGoodsName();
        String name = item.getName();
        textViewName.setVisibility(View.VISIBLE);
        switch (typePage) {
            case Contants.VAL_PAGE_SEARCH_HISTORY:
            case Contants.VAL_PAGE_SEARCH_SCAN:
            case Contants.VAL_PAGE_SEARCH_DURUG:
            case Contants.VAL_PAGE_SEARCH_DRUG_ADD:
                if (!TextUtils.isEmpty(goodsName) && goodsName != null) {
                    textViewName.setText(name);
                    textViewGoodsName.setText(goodsName);
                } else {
                    textViewGoodsName.setText(name);
                    textViewName.setText("");
                }
                break;
            case Contants.VAL_PAGE_SEARCH_REIMBURSEMENT:
                goodsName = item.getGoodsName();
                name = item.getName();
                if (isDim) {
                    if (!TextUtils.isEmpty(goodsName) && goodsName != null) {
                        textViewName.setText(name);
                        textViewGoodsName.setText(goodsName);
                    } else {
                        textViewGoodsName.setText(name);
                        textViewName.setText("");
                    }
                } else {
                    textViewName.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(goodsName) && goodsName != null) {
                        textViewName.setText(getSpannableText(name, keyworld));
                        textViewGoodsName.setText(getSpannableText(goodsName, keyworld));
                    } else {
                        textViewGoodsName.setText(getSpannableText(name, keyworld));
                        textViewName.setText("");
                    }
                }
                break;
        }
    }

    /**
     * 设置内部字体颜色
     *
     * @param text
     * @param keyworld
     * @return
     */
    private SpannableStringBuilder getSpannableText(String text, String keyworld) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        if (text.contains(keyworld)) {
            int spanStartIndex = text.indexOf(keyworld);
            int spacEndIndex = spanStartIndex + keyworld.length();
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#d82626")), spanStartIndex, spacEndIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return spannableStringBuilder;
    }
}

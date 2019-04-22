package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.PointHospitalSearchBean.DataBean.SearchListBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by lovelin on 2016/11/7.
 * 附近医院的  联想搜索结果的  adapter
 */
public class PointHospitalSearchAdapter extends CommonAdapter<SearchListBean> {
    private String keyword;
    public PointHospitalSearchAdapter(Context context, int layoutId, List<SearchListBean> datas) {
        super(context, layoutId, datas);
    }

    public void setKeyword(String key) {
        this.keyword = key;
    }

    @Override
    public void convert(ViewHolder helper, SearchListBean item, final int position) {
        TextView tv_personal_point_hospital_name = helper.getView(R.id.tv_personal_point_hospital_name);
        tv_personal_point_hospital_name.setText(getSpannableText(item.getName(), keyword));
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
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")),
                    spanStartIndex, spacEndIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return spannableStringBuilder;
    }
}

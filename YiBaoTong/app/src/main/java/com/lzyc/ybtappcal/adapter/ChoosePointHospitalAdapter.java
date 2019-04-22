package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.ChoosePointBean.DataBean.ListBean;
import com.lzyc.ybtappcal.view.StarBar;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by lovelin on 2016/11/7.
 * 附近医院的adapter
 */
public class ChoosePointHospitalAdapter extends CommonAdapter<ListBean> {


    public ChoosePointHospitalAdapter(Context context, int layoutId, List<ListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder helper, ListBean item, final int position) {
        TextView tv_personal_point_hospital_name = helper.getView(R.id.tv_choose_name);
        StarBar tv_choose_start = helper.getView(R.id.tv_choose_start);
        TextView tv_choose_type = helper.getView(R.id.tv_choose_type);
        tv_personal_point_hospital_name.setText(""+item.getName());
        tv_choose_start.setStarMark(Float.parseFloat(item.getAverage()));
        tv_choose_type.setText(""+item.getLevel()+item.getJibie());
    }
}

package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.PointHospitalBean.DataBean.ListBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by lovelin on 2016/11/7.
 * 附近医院的adapter
 */
public class PointHospitalAdapter extends CommonAdapter<ListBean> {

    public PointHospitalAdapter(Context context, int layoutId, List<ListBean> datas) {
        super(context, layoutId, datas);
    }


    @Override
    public void convert(ViewHolder helper, ListBean item, final int position) {
        TextView tv_personal_point_hospital_name = helper.getView(R.id.tv_personal_point_hospital_name);
        tv_personal_point_hospital_name.setText(""+item.getName());
    }
}

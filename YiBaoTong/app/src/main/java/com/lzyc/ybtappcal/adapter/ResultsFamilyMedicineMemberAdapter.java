package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.MedicineFamilyBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;


/**
 * Created by yang on 2017/06/20.
 */
public class ResultsFamilyMedicineMemberAdapter extends CommonAdapter<MedicineFamilyBean.ListBean> {
    public ResultsFamilyMedicineMemberAdapter(Context context, int layoutId, List<MedicineFamilyBean.ListBean> datas) {
        super(context, layoutId, datas);
    }

    public void addItem(MedicineFamilyBean.ListBean bean){
        mDatas.add(0,bean);
        notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder helper, MedicineFamilyBean.ListBean item, int position) {
        TextView tvMember = helper.getView(R.id.tv_member);
        tvMember.setText(item.getNickname());
    }
}

package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.HospitalListBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 作者：xujm
 * 时间：2016/2/22
 * 备注：HispitalAdapter
 */
public class HispitalAdapter extends CommonAdapter<HospitalListBean> {

    public HispitalAdapter(Context context, int layoutId, List<HospitalListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder helper, final HospitalListBean item, int position) {
        View v_top_line = helper.getView(R.id.v_top_line);
        TextView tv_phone = helper.getView(R.id.et_phone);
        TextView tv_address = helper.getView(R.id.tv_address);
        TextView tv_title = helper.getView(R.id.tv_title);
        TextView tv_type = helper.getView(R.id.tv_type);

        tv_address.setText(item.getAddress());
        tv_title.setText(item.getHosName());
        tv_type.setText(item.getLevel()+item.getJibie());

        if(position == 0){
            v_top_line.setVisibility(View.VISIBLE);
        } else {
            v_top_line.setVisibility(View.GONE);
        }

        if("一级".equals(item.getLevel())){
            tv_type.setBackgroundResource(R.mipmap.icon_hospital_by3);
            tv_type.setTextColor(mContext.getResources().getColor(R.color.color_e49200));
        } else if("二级".equals(item.getLevel())){
            tv_type.setBackgroundResource(R.mipmap.icon_hospital_by2);
            tv_type.setTextColor(mContext.getResources().getColor(R.color.color_2772b2));
        } else if("三级".equals(item.getLevel())){
            tv_type.setBackgroundResource(R.mipmap.icon_hospital_by1);
            tv_type.setTextColor(mContext.getResources().getColor(R.color.color_149c4e));
        } else{
            tv_type.setBackgroundResource(R.mipmap.icon_hospital_by4);
            tv_type.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            tv_type.setText(item.getLevel()+item.getJibie()+"  ");
        }

        if(!TextUtils.isEmpty(item.getPhone())){
            tv_phone.setText(item.getPhone());
            tv_phone.setTextColor(mContext.getResources().getColor(R.color.color_3a95e2));
            Drawable drawable= mContext.getResources().getDrawable(R.mipmap.icon_liebiao_dianhua_you);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_phone.setCompoundDrawables(drawable,null,null,null);
        } else {
            tv_phone.setText("暂无电话");
            tv_phone.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            Drawable drawable= mContext.getResources().getDrawable(R.mipmap.icon_liebiao_dianhua_wu);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_phone.setCompoundDrawables(drawable,null,null,null);
        }

        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(item.getPhone())) return;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + item.getPhone());
                intent.setData(data);
                mContext.startActivity(intent);
            }
        });



//        TextView tv_name = helper.getView(R.id.tv_item_hispital_name);
//        TextView tv_level = helper.getView(R.id.tv_item_hispital_level);
//
//
//
//        tv_name.setText(item.getHosName());
//        tv_level.setText(item.getLevel()+item.getJibie());

    }
}

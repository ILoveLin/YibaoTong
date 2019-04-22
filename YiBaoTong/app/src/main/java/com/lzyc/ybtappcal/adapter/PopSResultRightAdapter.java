package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.SresultStreetBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by yang on 2016/11/21.
 */
public class PopSResultRightAdapter extends CommonAdapter<SresultStreetBean> {

    public PopSResultRightAdapter(Context context, int layoutId, List<SresultStreetBean> datas) {
        super(context, layoutId, datas);
    }

    public void setItemSelected(String street){
        for (int i = 0; i <mDatas.size() ; i++) {
            SresultStreetBean s=mDatas.get(i);
            if(s.getStreet().equals(street)){
                s.setSelected(true);
            }else{
                s.setSelected(false);
            }
            mDatas.set(i,s);
        }
        notifyDataSetChanged();
    }

    public void setItemSelected(int position){
        for (int i = 0; i <mDatas.size() ; i++) {
            SresultStreetBean s=mDatas.get(i);
            if(i==position){
                s.setSelected(true);
            }else{
                s.setSelected(false);
            }
            mDatas.set(i,s);
        }
        notifyDataSetChanged();
    }


    public void updateItem(SresultStreetBean childAddressBean){
        for (int i = 0; i <mDatas.size() ; i++) {
            SresultStreetBean child=mDatas.get(i);
            if(child.getStreet().equals(childAddressBean.getStreet())){
                child.setSelected(true);
            }else{
                child.setSelected(false);
            }
            mDatas.set(i,child);
        }
        notifyDataSetChanged();
    }
    @Override
    protected void convert(ViewHolder helper, SresultStreetBean item, int position) {
        TextView item_lvright_tvdesc=helper.getView(R.id.item_lvright_tvdesc);
        LinearLayout item_lvright_linear=helper.getView(R.id.item_lvright_linear);
        View item_view=helper.getView(R.id.item_view);
        item_lvright_tvdesc.setText(item.getStreet());
        if(position%2==0){
            item_lvright_linear.setBackgroundResource(R.color.color_f9f9f9);
        }else{
            item_lvright_linear.setBackgroundResource(R.color.color_ffffff);
        }
        if(item.isSelected()){
            item_view.setVisibility(View.VISIBLE);
            item_lvright_tvdesc.setTextColor(mContext.getResources().getColor(R.color.base_btn_color));
        }else{
            item_view.setVisibility(View.INVISIBLE);
            item_lvright_tvdesc.setTextColor(mContext.getResources().getColor(R.color.color_444444));
        }
    }
}

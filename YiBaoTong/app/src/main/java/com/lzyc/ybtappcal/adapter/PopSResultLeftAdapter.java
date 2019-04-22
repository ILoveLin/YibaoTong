package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.SresultAreaBean;
import com.lzyc.ybtappcal.bean.SresultStreetBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by yang on 2016/11/21.
 */
public class PopSResultLeftAdapter extends CommonAdapter<SresultAreaBean> {

    public PopSResultLeftAdapter(Context context, int layoutId, List<SresultAreaBean> datas) {
        super(context, layoutId, datas);
    }

    public void setItemSelected(String area){
        for (int i = 0; i <mDatas.size() ; i++) {
            SresultAreaBean s=mDatas.get(i);
            if(s.getArea().equals(area)){
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
            SresultAreaBean s=mDatas.get(i);
            if(i==position){
                s.setSelected(true);
            }else{
                s.setSelected(false);
            }
            mDatas.set(i,s);
        }
        notifyDataSetChanged();
    }


    public void updateItem(SresultAreaBean groupAddressBean){
        for (int i = 0; i <mDatas.size() ; i++) {
            SresultAreaBean group=mDatas.get(i);
            if(group.getArea().equals(groupAddressBean.getArea())){
                group.setSelected(true);
            }else{
                group.setSelected(false);
            }
            mDatas.set(i,group);
        }
        notifyDataSetChanged();
    }

    public void updateItem(int position,SresultAreaBean groupAddressBean,String childItemSelected){
        for (int i = 0; i <mDatas.size() ; i++) {//数据重置
            SresultAreaBean group=mDatas.get(i);
            group.setSelected(false);
            mDatas.set(i,group);
        }
        SresultAreaBean groupSelected=mDatas.get(position);
        groupSelected.setSelected(true);
        List<SresultStreetBean> listChildSelected=groupSelected.getStreetList();
        for (int i = 0; i <listChildSelected.size() ; i++) {
            SresultStreetBean childItem=listChildSelected.get(i);
            if(childItemSelected.equals(childItem)){
                childItem.setSelected(true);
            }else{
                childItem.setSelected(false);
            }
            listChildSelected.set(i,childItem);
        }
        groupSelected.setStreetList(listChildSelected);
        mDatas.set(position,groupSelected);
        notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder helper, SresultAreaBean item, int position) {
        TextView item_lvleft_tvdesc=helper.getView(R.id.item_lvleft_tvdesc);
        LinearLayout item_lvleft_linear=helper.getView(R.id.item_lvleft_linear);
        View item_view=helper.getView(R.id.item_view);
        if(item.isSelected()){
            item_lvleft_tvdesc.setTextColor(mContext.getResources().getColor(R.color.base_btn_color));
            item_view.setVisibility(View.VISIBLE);
            item_lvleft_linear.setBackgroundResource(R.color.color_ffffff);
        }else{
            item_view.setVisibility(View.INVISIBLE);
            item_lvleft_tvdesc.setTextColor(mContext.getResources().getColor(R.color.color_444444));
            item_lvleft_linear.setBackgroundResource(R.color.color_e9e9e9);
        }
        item_lvleft_tvdesc.setText(item.getArea());
    }
}

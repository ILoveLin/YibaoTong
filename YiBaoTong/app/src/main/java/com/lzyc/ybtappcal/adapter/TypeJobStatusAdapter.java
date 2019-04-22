package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.TypeJobStatus;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 当前职位状态
 */
public class TypeJobStatusAdapter extends CommonAdapter<TypeJobStatus> {

    public TypeJobStatusAdapter(Context context, int layoutId, List<TypeJobStatus> datas) {
        super(context, layoutId, datas);
    }

    public void updateItem(int position, TypeJobStatus t) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (i != position) {//没有被选中
                TypeJobStatus tt = mDatas.get(i);
                tt.setSelected(false);
                this.mDatas.set(i, tt);
            } else {//选中
                this.mDatas.set(i, t);
            }
        }
        this.notifyDataSetChanged();
    }


    @Override
    public void convert(ViewHolder helper, TypeJobStatus item, int position) {
        View item_pop_type_line=helper.getView(R.id.item_pop_type_line);
        LinearLayout item_pop_type_linear=helper.getView(R.id.item_pop_type_linear);
        if(position%2==0){
            item_pop_type_linear.setBackgroundResource(R.color.color_ffffff);
        }else{
            item_pop_type_linear.setBackgroundResource(R.color.color_f9f9f9);
        }
        TextView tv = helper.getView(R.id.item_pop_type_tv);
        tv.setText("" + item.getDesc());
        if(item.isSelected()){
            tv.setSelected(true);
            item_pop_type_line.setVisibility(View.VISIBLE);
        }else{
            tv.setSelected(false);
            item_pop_type_line.setVisibility(View.INVISIBLE);
        }
    }

}

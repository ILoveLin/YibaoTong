package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.MessageSystem;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by yang on 2016/9/21.
 */
public class SysMessageAdapter extends CommonAdapter<MessageSystem> {
    private OnItemChildClickListener listener;

    public SysMessageAdapter(Context context, int layoutId, List<MessageSystem> datas) {
        super(context, layoutId, datas);
    }

    public void setList(List<MessageSystem> list){
        this.mDatas=list;
        notifyDataSetChanged();
    }

    public void addList(List<MessageSystem> list){
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnItemChildClickListener(OnItemChildClickListener listener){
        this.listener=listener;
    }

    @Override
    public void convert(ViewHolder helper, final MessageSystem item, final int position) {
        TextView tvDate=helper.getView(R.id.item_message_system_date);
        TextView tvTitle=helper.getView(R.id.item_message_system_title);
        TextView tvContent=helper.getView(R.id.item_message_system_content);
        LinearLayout linearContent=helper.getView(R.id.item_message_system_linear);
        tvDate.setText(""+item.getDate());
        tvTitle.setText(""+item.getTitle());
        tvContent.setText(item.getContent()+item.getContent());
        linearContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(listener!=null){
                  listener.onItemChildListener(position,item);
              }
            }
        });
        linearContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(listener!=null){
                    listener.onItemChildLongLostener(position,item.getMsg_id());
                }
                return true;
            }
        });
    }

    public interface OnItemChildClickListener{
        void onItemChildListener(int position, MessageSystem messageSystem);
        void onItemChildLongLostener(int position, String msg_id);
    }
}

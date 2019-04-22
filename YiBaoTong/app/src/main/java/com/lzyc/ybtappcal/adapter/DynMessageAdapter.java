package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.MessageComment;
import com.lzyc.ybtappcal.view.roundimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by yang on 2016/9/21.
 */
public class DynMessageAdapter extends CommonAdapter<MessageComment> {


    public DynMessageAdapter(Context context, int layoutId, List<MessageComment> datas) {
        super(context, layoutId, datas);
    }

    public void setList(List<MessageComment> list){
        this.mDatas=list;
        notifyDataSetChanged();
    }
    public void addList(List<MessageComment> list){
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder helper, MessageComment item,int position) {
        TextView tvNickName=helper.getView(R.id.item_message_dyn_nickname);
        TextView tvDate=helper.getView(R.id.item_message_dyn_date);
        TextView tvContent=helper.getView(R.id.item_message_dyn_content);
        TextView tvContent2=helper.getView(R.id.item_message_dyn_content2);
        RoundedImageView item_message_dyn_avatar=helper.getView(R.id.item_message_dyn_avatar);
        String nickName=item.getNickname();
        tvNickName.setText(""+nickName);
        tvContent.setText(""+item.getContent());
        tvContent2.setText(""+item.getContent2());
        tvDate.setText(""+item.getDate());
        String headImage=item.getHeadImg();
        if (!TextUtils.isEmpty(headImage)) {
            Picasso.with(mContext)
                    .load(headImage)
                    .error(R.mipmap.icon_system_logo)
                    .into(item_message_dyn_avatar);
        } else {
            item_message_dyn_avatar.setImageResource(R.mipmap.icon_system_logo);
        }
    }
//
//    public boolean isNumeric(String str){
//        Pattern pattern = Pattern.compile("[0-9]*");
//        return pattern.matcher(str).matches();
//    }
}

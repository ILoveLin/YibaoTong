package com.lzyc.ybtappcal.widget.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.mine.withdraw.MineWithdrawActivity;
import com.lzyc.ybtappcal.util.DensityUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 提现
 * Created by lxx on 2017/6/28.
 */

public class PopupWindowWithdraw {
    Context mContext;
    List<MineWithdrawActivity.AliBean> mData = new ArrayList<>();
    PopupWindow popupWindow;
    View contentView;
    WithdrawAdapter myAdapter;
    ListView listview;

    OnWithdrawListener onWithdrawListener;

    public void setOnWithdrawListener(OnWithdrawListener onWithdrawListener){
        this.onWithdrawListener = onWithdrawListener;
    }

    public interface OnWithdrawListener{
        void onDel(int pos);
        void onSelected(int pos);
    }

    public PopupWindowWithdraw(Context mContext) {

        this.mContext = mContext;

        initPopWindow();
    }

    private void initPopWindow() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_withdraw, null);

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);

        popupWindow.setTouchable(true);

        popupWindow.setOutsideTouchable(true);

        popupWindow.update();

        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        listview = (ListView) contentView.findViewById(R.id.listview);

        myAdapter = new WithdrawAdapter();

        listview.setAdapter(myAdapter);
    }

    public void updata(List<MineWithdrawActivity.AliBean> data){

        mData.clear();

        if(null != data && !data.isEmpty()){
            mData.addAll(data);
            Collections.reverse(mData);
        }

        if(3 < mData.size()){

            listview.setLayoutParams(new LinearLayout.LayoutParams(DensityUtils.dp2px(230), DensityUtils.dp2px(159)));
        }

        myAdapter.notifyDataSetChanged();
    }

    public void updataRemove(int pos){
        mData.remove(pos);

        if(3 > mData.size()){
            listview.setLayoutParams(new LinearLayout.LayoutParams(DensityUtils.dp2px(230), ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        myAdapter.notifyDataSetChanged();
    }

    public void showView(View v) {
        if (popupWindow == null) return;

        popupWindow.showAsDropDown(v);

    }

    public void dismissView(){
        popupWindow.dismiss();
    }

    class WithdrawAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;

            if (convertView == null){

                viewHolder = new ViewHolder();

                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_withdraw_alipay, null);

                viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.et_phone);

                viewHolder.imgDel = (ImageView) convertView.findViewById(R.id.img_del);

                viewHolder.vLine = convertView.findViewById(R.id.v_line);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (position == mData.size() - 1){
                viewHolder.vLine.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.vLine.setVisibility(View.VISIBLE);
            }

            viewHolder.tvPhone.setText(mData.get(position).getPhone());

            viewHolder.imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null != onWithdrawListener){
                        onWithdrawListener.onDel(position);
                    }
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null != onWithdrawListener){
                        onWithdrawListener.onSelected(position);
                    }
                }
            });

            return convertView;
        }

        class ViewHolder{
            TextView tvPhone;
            ImageView imgDel;
            View vLine;
        }

    }
}
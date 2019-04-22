package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.HospitalBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

public class ScanResultAdapter extends CommonAdapter<HospitalBean> {

    private int mOldCount = 0;
    private boolean isAnimed = true;
    private OnItemClickListener listenr;

    public ScanResultAdapter(Context context, int layoutId, List<HospitalBean> datas,OnItemClickListener listenr) {
        super(context, layoutId, datas);
        this.listenr=listenr;
    }


   public List<HospitalBean> getList(){
       return mDatas;
   }
    public void banAnim(boolean isAnimed) {
        this.isAnimed = isAnimed;
        notifyDataSetChanged();
    }
    public void addLoadding() throws Exception {
        HospitalBean h = this.mDatas.get(mDatas.size() - 1);
        if (h != null) {
            this.mDatas.add(null);
        }
        this.isAnimed = true;
        this.mOldCount = mDatas.size() - 1;
        notifyDataSetChanged();
    }

    public void removeLoadding(int index) {
        if (index < this.mDatas.size()) {
            this.mDatas.remove(index);
        }
        isAnimed = false;
        notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder helper, final HospitalBean item, final int position) {
        ImageView image_loadding = helper.getView(R.id.loadding_listview_image);
        View item_view_padding=helper.getView(R.id.item_view_padding);
        LinearLayout loadding_listview_linear=helper.getView(R.id.loadding_listview_linear);
        LinearLayout ll = helper.getView(R.id.ll_item_hispital_left);
        LinearLayout linearRight=helper.getView(R.id.linear_right);
        LinearLayout item_scanresult_linear_left=helper.getView(R.id.item_scanresult_linear_left);
        View item_scanresult_line=helper.getView(R.id.item_scanresult_line);
        image_loadding.setVisibility(View.GONE);
        ll.setVisibility(View.VISIBLE);
//        TextView item_scanresult_navigation_hospital=helper.getView(R.id.item_scanresult_navigation_hospital);
        TextView item_scanresult_navigation_community=helper.getView(R.id.item_scanresult_navigation_community);
        TextView item_hospital_level = helper.getView(R.id.item_hospital_level);
//        StarBar item_hospital_star=helper.getView(R.id.item_hospital_star);
        TextView item_hospital_dingdian=helper.getView(R.id.item_hospital_dingdian);
        if (item == null) {
            image_loadding.setVisibility(View.VISIBLE);
            ll.setVisibility(View.GONE);
            loadding_listview_linear.setVisibility(View.VISIBLE);
            image_loadding.setBackgroundResource(R.drawable.ybtlistview_load);
            AnimationDrawable animationDrawable = (AnimationDrawable) image_loadding.getBackground();
            animationDrawable.start();
        } else {
            loadding_listview_linear.setVisibility(View.GONE);
            TextView tvCommunity = helper.getView(R.id.item_hospital_type_community);
            TextView tvHospital = helper.getView(R.id.item_hospital_type_hospital);
            int typeAddress = item.getTypeAddress();
            String average=item.getAverage();
//            if(!TextUtils.isEmpty(average)){//评分
//                item_hospital_star.setStarMark(Float.parseFloat(average));
//            }else{
//                item_hospital_star.setStarMark(0);
//            }
            String yyType=item.getYyType();
            if(TextUtils.isEmpty(yyType)){//无需定点
                item_hospital_dingdian.setVisibility(View.INVISIBLE);
            }else{
                item_hospital_dingdian.setText(yyType);
                item_hospital_dingdian.setVisibility(View.VISIBLE);
            }
            if (typeAddress == 0) {
                tvCommunity.setVisibility(View.GONE);
                tvHospital.setVisibility(View.VISIBLE);
//                item_scanresult_linear_left.setBackgroundResource(R.drawable.selector_item_radius_f9fbfe);
//                item_scanresult_linear_right.setBackgroundResource(R.drawable.selector_item_radius_f5f7fc);
//                item_scanresult_line.setBackgroundResource(R.color.color_f0f3fa);
//                item_scanresult_navigation_hospital.setVisibility(View.VISIBLE);
//                item_scanresult_navigation_community.setVisibility(View.GONE);
            } else {
                tvCommunity.setVisibility(View.VISIBLE);
                tvHospital.setVisibility(View.GONE);
//                item_scanresult_linear_left.setBackgroundResource(R.drawable.selector_item_radius_fafffe);
//                item_scanresult_linear_right.setBackgroundResource(R.drawable.selector_item_radius_f5fefc);
//                item_scanresult_line.setBackgroundResource(R.color.color_e6f8f5);
//                item_scanresult_navigation_hospital.setVisibility(View.GONE);
//                item_scanresult_navigation_community.setVisibility(View.VISIBLE);
            }
            TextView tv_name = helper.getView(R.id.item_scanresult_hospital);
            TextView tv_position = helper.getView(R.id.tv_item_hispital_left_position);
            TextView tv_price = helper.getView(R.id.item_scanresult_price);
            tv_name.setText(item.getName());
            item_hospital_level.setText(item.getLevel() + item.getJibie());
            String d = item.getDistance();
            if (!TextUtils.isEmpty(d)) {
                double distance = Double.parseDouble("" + d);
                double dis = (distance / 1000);
                tv_position.setText(String.format("%.1f", dis).toString());
            }
            tv_price.setText("" + item.getPrice());
            if (position == 6 && mDatas.size() > 6) {
                isAnimed = false;
            }
            if (position > mOldCount - 1 && isAnimed) {
                mOldCount = position;
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.list_drug_scalresult);
                ll.startAnimation(animation);
            }
            item_scanresult_linear_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(listenr!=null) {
                       listenr.OnChildLeftClickListener(position,item);
                   }
                }
            });
            linearRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenr!=null){
                        listenr.OnChildRightClickListener(position,item);
                    }
               }
            });
            if(position==0){
                item_view_padding.setVisibility(View.VISIBLE );
            }else{
                item_view_padding.setVisibility(View.GONE);
            }
        }
    }

    public interface OnItemClickListener{
        void OnChildLeftClickListener(int position, HospitalBean item);
        void OnChildRightClickListener(int position, HospitalBean item);
    }
}

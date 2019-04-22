package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.MedicineFamilyBean;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxx on 2017/5/24.
 */
public class FamilyMedicineAdapter extends CommonAdapter<MedicineFamilyBean.ListBean> {

    public FamilyMedicineAdapter(Context context, int layoutId, List<MedicineFamilyBean.ListBean> datas) {
        super(context, layoutId, datas);
    }

    public void updata(List<MedicineFamilyBean.ListBean> list) {
        if(list==null){
            list=new ArrayList<>();
        }
        mDatas = list;
        notifyDataSetChanged();
    }

    public void updataRemove(int pos){
        mDatas.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder helper, final MedicineFamilyBean.ListBean bean, final int position) {
        ImageView imgIcon = helper.getView(R.id.img_icon);
        TextView tvAge = helper.getView(R.id.tv_age);
        TextView tvName = helper.getView(R.id.tv_name);
        TextView tvMedicineNum = helper.getView(R.id.tv_medicine_num);
        View vBottom = helper.getView(R.id.v_bottom);
        RelativeLayout relLayout = helper.getView(R.id.rel_layout);

        if(position == mDatas.size()-1){
            vBottom.setVisibility(View.VISIBLE);
        } else {
            vBottom.setVisibility(View.GONE);
        }

        String msg = bean.getAge()+"岁";
        if(!TextUtils.isEmpty(bean.getNote())){
            msg += " "+bean.getNote();
        }

        tvAge.setText(msg);
        tvName.setText(bean.getNickname());
        if(0 < bean.getCount()){
            tvMedicineNum.setText(bean.getCount() + "种药品");
        } else {
            tvMedicineNum.setText("暂时还没有药品");
        }


        if("1".equals(bean.getSex())){

            Picasso.with(mContext)
                    .load(R.mipmap.icon_medicine_boy)
                    .placeholder(R.mipmap.icon_medicine_boy)
                    .into(imgIcon);
        } else {
            Picasso.with(mContext)
                    .load(R.mipmap.icon_medicine_girl)
                    .placeholder(R.mipmap.icon_medicine_girl)
                    .into(imgIcon);
        }

        relLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != onMedicineListener){
                    onMedicineListener.onItem(bean);
                }
            }
        });

        relLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(null != onMedicineLongListener){
                    onMedicineLongListener.onItemLong(position, bean.getID(), bean);
                }
                return true;
            }
        });
    }

    private MedicineListener onMedicineListener;

    public void setOnMedicineListener(MedicineListener onMedicineListener) {
        this.onMedicineListener = onMedicineListener;
    }

    public interface MedicineListener {
        void onItem(MedicineFamilyBean.ListBean bean);
    }

    private MedicineLongListener onMedicineLongListener;

    public void setOnMedicineLongListener(MedicineLongListener onMedicineLongListener){
        this.onMedicineLongListener = onMedicineLongListener;
    }

    public interface MedicineLongListener{
        void onItemLong(int position, String id, MedicineFamilyBean.ListBean bean);
    }

}

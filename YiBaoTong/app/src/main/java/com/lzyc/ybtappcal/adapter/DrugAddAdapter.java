package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.mine.medicine.DrugAddActivity;
import com.lzyc.ybtappcal.bean.BrowseBean;
import com.lzyc.ybtappcal.util.StringUtils;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxx on 2017/6/6.
 */
public class DrugAddAdapter extends CommonAdapter<BrowseBean> {

    private List<String> mDataSelected = new ArrayList<>();

    private DrugAddActivity activity;

    public DrugAddAdapter(Context context, int layoutId, List<BrowseBean> datas) {
        super(context, layoutId, datas);
    }

    public void updataActivity(DrugAddActivity activity){
        this.activity = activity;
    }

    public void setList(List<BrowseBean> list) {
        if(list==null){
            list=new ArrayList<>();
        }
        mDatas = list;
        notifyDataSetChanged();
    }

    public void addList(List<BrowseBean> list) {
        if(list==null){
            list=new ArrayList<>();
        }
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public List<String> getmSelectedIDS(){
        return mDataSelected;
    }

    public void removeSelectedID(String drugID){
        mDataSelected.remove(drugID);
    }

    public List<BrowseBean> getData(){
        return mDatas;
    }

    @Override
    protected void convert(ViewHolder helper, final BrowseBean bean, int position) {
        TextView tvName = helper.getView(R.id.tv_drug_name);
        TextView tvVender = helper.getView(R.id.tv_vender);
        ImageView imgDrug = helper.getView(R.id.img_drug);
        View vLine = helper.getView(R.id.v_top_line);
        ImageView imgAdd = helper.getView(R.id.img_added);

        final ImageView imgSelecter = helper.getView(R.id.img_selecter);
        View vShade = helper.getView(R.id.v_shade);
        LinearLayout linLayout = helper.getView(R.id.rel_layout);

        if(0 == position){
            vLine.setVisibility(View.GONE);
        } else {
            vLine.setVisibility(View.VISIBLE);
        }

        if("1".equals(bean.getMedicineChest())){

            if(bean.isAdded()){
                imgAdd.setVisibility(View.VISIBLE);
                vShade.setVisibility(View.VISIBLE);
                linLayout.setClickable(false);
                linLayout.setEnabled(false);
            }

            imgSelecter.setSelected(true);
        } else {
            imgAdd.setVisibility(View.GONE);
            vShade.setVisibility(View.GONE);
            imgSelecter.setSelected(false);
            linLayout.setClickable(true);
            linLayout.setEnabled(true);
        }

        linLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.requestEventCode("a-4008");

                imgSelecter.setSelected(!imgSelecter.isSelected());

                bean.setMedicineChest(imgSelecter.isSelected() ? "1" : "0");

                if("1".equals(bean.getMedicineChest())){
                    mDataSelected.add(bean.getDrugID()+",");
                } else {
                    mDataSelected.remove(bean.getDrugID()+",");
                }

            }
        });

        String name = bean.getName();
        String goodsName = bean.getGoodsName();
        String specifications = bean.getSpecifications();

        if (TextUtils.isEmpty(goodsName)) {
            tvName.setText(StringUtils.getSpannableText(name + " ", specifications));
        } else {
            tvName.setText(StringUtils.getSpannableText(goodsName + " " + name + " ", specifications));
        }

        tvVender.setText(bean.getVender());

        if(TextUtils.isEmpty(bean.getImage())) return;

        Picasso.with(mContext)
                .load(bean.getImage())
                .placeholder(R.mipmap.image_cache_drug)
                .error(R.mipmap.image_cache_drug)
                .into(imgDrug);

    }
}


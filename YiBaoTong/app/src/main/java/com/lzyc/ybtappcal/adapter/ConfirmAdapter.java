package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.top.HispitalListActivity;
import com.lzyc.ybtappcal.bean.DrugConfirmBean;
import com.lzyc.ybtappcal.bean.Event;
import com.lzyc.ybtappcal.util.SharePreferenceUtil;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xujm
 * 时间：2016/2/22
 * 备注：ConfirmAdapter
 */
public class ConfirmAdapter extends CommonAdapter<DrugConfirmBean> {
    private DrugConfirmBean drugConfirmBean;
    private String currentProvince;

    public ConfirmAdapter(Context context, int layoutId, List<DrugConfirmBean> datas) {
        super(context, layoutId, datas);
    }

    public DrugConfirmBean getDrugConfirmBean() {
        return drugConfirmBean;
    }

    public void setDrugConfirmBean(DrugConfirmBean drugConfirmBean) {
        this.drugConfirmBean = drugConfirmBean;
    }

    public void setCurrentProvince(String currentProvince) {
        currentProvince = (String) SharePreferenceUtil.get(mContext, SharePreferenceUtil.PROVICE_TOP_CHOOSE, "北京市");
        this.currentProvince = currentProvince;
    }


    public void setList(List<DrugConfirmBean> list) {
        if (list == null) {
            list = new ArrayList<DrugConfirmBean>();
        }
        if (this.mDatas != null) {
            this.mDatas.clear();
        }
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public void addList(List<DrugConfirmBean> list) {
        if (list == null) {
            list = new ArrayList<DrugConfirmBean>();
        }
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    protected void convert(ViewHolder helper, final DrugConfirmBean item, int position) {
        LinearLayout ll = helper.getView(R.id.ll_item_confirm);
        ImageView iv = helper.getView(R.id.iv_item_confirm);
        TextView tv_drugname = helper.getView(R.id.tv_item_confirm_goodsName);
        TextView tv_name = helper.getView(R.id.tv_item_confirm_name);
        TextView tv_factory = helper.getView(R.id.tv_item_confirm_factory);
        TextView tv_spec = helper.getView(R.id.tv_item_confirm_spec);
        TextView tv_price = helper.getView(R.id.tv_item_confirm_price);
        TextView tv_oprice = helper.getView(R.id.tv_item_confirm_oprice);
        TextView tv_num = helper.getView(R.id.tv_item_confirm_num);
        TextView tv_current_druge = helper.getView(R.id.tv_current_druge);
        ImageView iv_icon_item_confirm_first = helper.getView(R.id.iv_icon_item_confirm_first);

        if (drugConfirmBean.getDrug_id().equals(item.getDrug_id())) {
            //listview 是当前药品的时候
            tv_current_druge.setVisibility(View.VISIBLE);
        } else {
            tv_current_druge.setVisibility(View.INVISIBLE);
        }
        //第一条,显示王冠
        if (item.isFirst()) {
            iv_icon_item_confirm_first.setVisibility(View.VISIBLE);
        } else {
            iv_icon_item_confirm_first.setVisibility(View.GONE);

        }
        tv_name.setText(item.getName());
        String goodsName = item.getGoodsName();
        String name = item.getName();
        if (!TextUtils.isEmpty(goodsName)) {
            tv_drugname.setText(goodsName);
            tv_name.setText(name);
        } else {
            tv_drugname.setText(name);
            tv_name.setText("");
        }

        // tv_drugname.setText(item.getName());
        //被横线化了的价格
        //tv_oprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);


        tv_factory.setText(item.getVender());
        tv_num.setText(item.getHosNum());
        tv_spec.setText(item.getSpecifications()+"");
        //北京省
        if (currentProvince.equals("北京")) {
            tv_oprice.setVisibility(View.VISIBLE);
            tv_price.setText("¥" + item.getNewPrice());
            String newPrice=item.getNewPrice();
            if (TextUtils.isEmpty(newPrice)) {
                tv_price.setVisibility(View.VISIBLE);
                tv_oprice.setVisibility(View.GONE);
                tv_price.setText("暂无报价");
            } else {
                if(Double.parseDouble(newPrice)==0){
                    tv_price.setVisibility(View.VISIBLE);
                    tv_oprice.setVisibility(View.GONE);
                    tv_price.setText("暂无报价");
                }else{
                    tv_price.setText("¥ " + newPrice);
                    tv_oprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    tv_oprice.setText("¥ " + item.getPrice());
                }
            }
        } else {
            tv_oprice.setVisibility(View.GONE);
            //其他省份
            tv_price.setText("¥" + item.getPrice());
        }

        switch (position % 2) {
            case 0:
                ll.setBackgroundResource(R.drawable.selector_item_fdfdfe);
                break;
            case 1:
                ll.setBackgroundResource(R.drawable.selector_item_f9f9fb);
                break;
        }

        //认可度的中排名的点击
        tv_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EventBus.getDefault().post(new Event("g001"));
                Intent intent = new Intent(mContext, HispitalListActivity.class);
                intent.putExtra("drugId", ""+item.getDrug_id());
                intent.putExtra("venderID",""+ item.getVenderID());
                intent.putExtra("drugNameID", ""+item.getDrugNameID());
                intent.putExtra("specificationsID", ""+item.getSpecificationsID());
                mContext.startActivity(intent);
            }
        });
        String images = item.getScanName();
        if (!TextUtils.isEmpty(images)) {
            Picasso.with(mContext)
                    .load(images)
                    .placeholder(R.mipmap.image_cache_drug_list)
                    .error(R.mipmap.image_cache_drug_list)
                    .resize(138, 136)
                    .into(iv);
        } else {
            iv.setImageResource(R.mipmap.icon_sanca_history);
        }
    }
}

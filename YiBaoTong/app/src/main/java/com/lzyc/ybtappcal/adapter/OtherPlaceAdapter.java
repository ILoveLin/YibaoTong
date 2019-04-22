package com.lzyc.ybtappcal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.reimbursement.ReimbursementNoYbDetailsActivity;
import com.lzyc.ybtappcal.activity.reimbursement.ReimbursementYbDetailsActivity;
import com.lzyc.ybtappcal.bean.AreaBean;
import com.lzyc.ybtappcal.bean.DrugBean;
import com.lzyc.ybtappcal.bean.Event;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.listener.OnAddressClickListener;
import com.lzyc.ybtappcal.listener.OnDeletePlanClickListener;
import com.lzyc.ybtappcal.view.YbtListView;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 异地方案
 * 注意：这里的position是指group的下标
 * package_name com.lzyc.ybtappcal.adapter
 * Created by yang on 2016/10/25.
 */
public class OtherPlaceAdapter extends CommonAdapter<AreaBean> {

    private OnAddressClickListener listener;
    private OnDeletePlanClickListener deletePlanClickListener;

    private int num = -1;
    private List<DrugBean> pList = new ArrayList<DrugBean>();

    public OtherPlaceAdapter(Context context, int layoutId, List<AreaBean> datas) {
        super(context, layoutId, datas);
    }

    public List<DrugBean> getpList() {
        return pList;
    }

    public void setpList(List<DrugBean> pList) {
        this.pList = pList;
    }

    public void removeItem(int position, DrugBean drugBean) {
        pList.remove(drugBean);
        if (pList.size() == 0) {
            mDatas.remove(position);
        }
        notifyDataSetChanged();
    }

    public OnAddressClickListener getListener() {
        return listener;
    }

    public void setListener(OnAddressClickListener listener) {
        this.listener = listener;
    }

    public OnDeletePlanClickListener getDeletePlanClickListener() {
        return deletePlanClickListener;
    }

    public void setDeletePlanClickListener(OnDeletePlanClickListener deletePlanClickListener) {
        this.deletePlanClickListener = deletePlanClickListener;
    }

    @Override
    public void convert(final ViewHolder helper, final AreaBean item, final int position) {
        final TextView tv = helper.getView(R.id.tv_item_other);
        final YbtListView smlv = helper.getView(R.id.smlv_item_other);
        String city = item.getCity();
        tv.setText(city);
        //异地点击事件
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == position) {
                    num = -1;
                    notifyDataSetChanged();
                } else {
                    listener.onAddressClick(position);
                    num = position;
                }
            }
        });
        //被点击的展开药品名字
        if (position == num) {
            smlv.setVisibility(View.VISIBLE);
            tv.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_triangel), null, mContext.getResources().getDrawable(R.mipmap.icon_forword_down), null);
        } else {
            smlv.setVisibility(View.GONE);
            tv.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_triangel), null, mContext.getResources().getDrawable(R.mipmap.icon_forword_right0), null);
        }

        //药品侧啦删除
        smlv.setFocusable(false);
        smlv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deletePlanClickListener.onDeletePlanClick(position, pList.get(i));
//                pList.remove(i);
//                //bug 解决
//                if (pList.size() == 0) {
//                    mDatas.remove(item);
//                    notifyDataSetChanged();
//                }
                return true;
            }
        });
        if (city.equals("北京") || city.equals("北京市")) {
            OtherPlaceItemAdapter adapter = new OtherPlaceItemAdapter(mContext, R.layout.item_other_place_item, pList);
            smlv.setAdapter(adapter);
            smlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    EventBus.getDefault().post(new Event("o005"));
                    DrugBean drugBean = pList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Contants.KEY_PAGE_DRUG, Contants.VAL_PAGE_MINEPLAN);
                    bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
                    //参保城市为北京,打开报销详情
                    openActivity(ReimbursementYbDetailsActivity.class, bundle);

                }
            });
        } else {
            OtherPlaceItemAdapter adapter = new OtherPlaceItemAdapter(mContext, R.layout.item_other_place_item, pList);
            smlv.setAdapter(adapter);
            smlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DrugBean drugBean = pList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Contants.KEY_PAGE_DRUG, Contants.VAL_PAGE_MINEPLAN);
                    bundle.putSerializable(Contants.KEY_OBJ_DRUGBEAN, drugBean);
                    //参保城市为异地
                    openActivity(ReimbursementNoYbDetailsActivity.class, bundle);
                }
            });
        }
    }

    /**
     * 打开activity
     *
     * @param ActivityClass
     * @param b
     */
    public void openActivity(Class<? extends Activity> ActivityClass, Bundle b) {
        Intent intent = new Intent(mContext, ActivityClass);
        intent.putExtras(b);
        mContext.startActivity(intent);
    }

}

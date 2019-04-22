package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.AddressInfo;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by yang on 2017/03/15.
 */

public class AddressManagerAdapter extends CommonAdapter<AddressInfo> {
    private OnChildClickListener listener;

    public AddressManagerAdapter(Context context, int layoutId, List<AddressInfo> datas) {
        super(context, layoutId, datas);
    }

    public AddressInfo getSetDefault() {
        AddressInfo addressInfo = mDatas.get(0);
        addressInfo.setDefault("1");
        return addressInfo;
    }

  public List<AddressInfo> getAddressInfo() {
        return mDatas;
    }

    public void setOnChildClickListener(OnChildClickListener listener) {
        this.listener = listener;
    }

    public void setListData(List<AddressInfo> listData) {
        this.mDatas = listData;
        notifyDataSetChanged();
    }

    public void updateItem(int position) {
        String setDefault;
        for (int i = 0; i < mDatas.size(); i++) {
            AddressInfo addressInfo = mDatas.get(i);
            if (i == position) {
                setDefault = "1";
            } else {
                setDefault = "0";
            }
            addressInfo.setDefault(setDefault);
            mDatas.set(i, addressInfo);
        }
        notifyDataSetChanged();
    }


    @Override
    public void convert(ViewHolder helper, final AddressInfo item, final int position) {
        TextView address_manager_account = helper.getView(R.id.address_manager_account);
        TextView address_manager_phone = helper.getView(R.id.address_manager_phone);
        TextView address_manager_info = helper.getView(R.id.address_manager_info);
        TextView address_manager_edit = helper.getView(R.id.address_manager_edit);
        TextView address_manager_clear = helper.getView(R.id.address_manager_clear);
        TextView address_manager_setdefault = helper.getView(R.id.address_manager_setdefault);
        LinearLayout address_manager_choose = helper.getView((R.id.address_manager_choose));
        View lineBottomView=helper.getView(R.id.line_bottom_view);
        if(position==mDatas.size()-1){
            lineBottomView.setVisibility(View.VISIBLE);
        }else{
            lineBottomView.setVisibility(View.GONE);
        }
        final String setDefault = item.getDefault();
        final String id = item.getID();
        if (setDefault.equals("1")) {
            address_manager_setdefault.setText("默认地址");
            address_manager_setdefault.setSelected(true);
        } else {
            address_manager_setdefault.setText("设为默认");
            address_manager_setdefault.setSelected(false);
        }
        address_manager_account.setText(item.getName());
        address_manager_phone.setText(item.getPhone());
        String diqu=item.getAddressRegion();
        diqu=diqu.replaceAll(" ","");
        address_manager_info.setText(diqu+item.getAddressDetail());
        address_manager_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onEditClickListener(item);
                }
            }
        });
        address_manager_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClearClickListener(id);
                }
            }
        });
        address_manager_setdefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSetDefaultListener(position, item);
                }
            }
        });
        address_manager_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSetResultListener(item);
                }
            }
        });

    }

    public interface OnChildClickListener {

        void onEditClickListener(AddressInfo addressInfo);

        void onClearClickListener(String id);

        void onSetDefaultListener(int postition, AddressInfo addressInfo);

        void onSetResultListener(AddressInfo addressInfo);

    }

}

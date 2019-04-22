package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.PinYinStyle;
import com.lzyc.ybtappcal.bean.WenziBean;
import com.lzyc.ybtappcal.view.SwipeLayout;
import java.util.ArrayList;

/**
 * Created by yang on 2017/01/09.
 */
public class WenziAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<WenziBean> list;
    public PinYinStyle sortToken;

    public WenziAdapter(Context context, ArrayList<WenziBean> list) {
        this.context = context;
        this.list = list;
        sortToken = new PinYinStyle();
    }

    @Override
    public int getCount() {
        if(list==null){
            list=new ArrayList<>();
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        WenziBean bean=list.get(position);
        if(bean!=null){
            return bean;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_search_city, null);
            holder.tv_contact_name = (TextView) convertView.findViewById(R.id.tv_contact_name);
            holder.tv_first_alphabet = (TextView) convertView.findViewById(R.id.tv_first_alphabet);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        WenziBean contactBean = list.get(position);
        holder.tv_contact_name.setText(contactBean.getName());
        String currentAlphabet = contactBean.getPinyin().charAt(0) + "";
        if (position > 0) {
            String lastAlphabet = list.get(position - 1).getPinyin().charAt(0) + "";
            if (currentAlphabet.equals(lastAlphabet)) {
                holder.tv_first_alphabet.setVisibility(View.GONE);
            } else {
                holder.tv_first_alphabet.setVisibility(View.VISIBLE);
                holder.tv_first_alphabet.setText(currentAlphabet);
            }
        } else {
            holder.tv_first_alphabet.setVisibility(View.VISIBLE);
            holder.tv_first_alphabet.setText(currentAlphabet);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_contact_name;
        TextView tv_first_alphabet;
    }
}

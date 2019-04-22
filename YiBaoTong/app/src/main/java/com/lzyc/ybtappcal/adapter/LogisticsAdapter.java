package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.LogisticsBean;
import com.lzyc.ybtappcal.util.CalendarFormat;

import java.util.ArrayList;
import java.util.List;

import static com.lzyc.ybtappcal.util.notify.NotifyUtil.context;

/**
 * Created by lxx on 2017/4/21.
 */

public class LogisticsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private List<LogisticsBean.TracesBean> traceList = new ArrayList<>(1);
    private String state;
    private static final int TYPE_TOP = 0;
    private static final int TYPE_SECOND = 1;
    private static final int TYPE_NORMAL= 2;
    private Context mContext;


    public LogisticsAdapter(Context mContext){
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void update(List<LogisticsBean.TracesBean> traceList, String state){
        if(traceList != null && !traceList.isEmpty()){
            this.traceList = traceList;
        }
        this.state = state;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LogisticsViewHolder((inflater.inflate(R.layout.item_logistics, parent, false)));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LogisticsViewHolder itemHolder = (LogisticsViewHolder) holder;

        LogisticsBean.TracesBean bean = traceList.get(position);
        if (position == TYPE_TOP) {

            itemHolder.vTop.setVisibility(View.VISIBLE);
            itemHolder.tvTopLine.setVisibility(View.INVISIBLE);

            itemHolder.tvAcceptStation.setTextSize(13);

            if("4".equals(state)||"3".equals(state)){
                itemHolder.imgDot.setImageResource(R.mipmap.icon_wuliu_dingwei_pre);
                itemHolder.tvAcceptStation.setTextColor(mContext.getResources().getColor(R.color.color_ff8431));
            } else {
                itemHolder.imgDot.setImageResource(R.mipmap.icon_wuliu_dingwei_nor);
                itemHolder.tvAcceptStation.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            }

        } else if(position == TYPE_SECOND){

            itemHolder.vTop.setVisibility(View.GONE);
            itemHolder.tvTopLine.setVisibility(View.VISIBLE);

            itemHolder.tvAcceptStation.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            if("4".equals(state)||"3".equals(state)){
                itemHolder.imgDot.setImageResource(R.mipmap.icon_wuliu_daoda);
            } else {
                itemHolder.imgDot.setImageResource(R.mipmap.icon_wuliu_yunshu);
            }

        } else {
            itemHolder.vTop.setVisibility(View.GONE);
            itemHolder.tvTopLine.setVisibility(View.VISIBLE);
            itemHolder.tvAcceptTime.setTextColor(0xff999999);
            itemHolder.tvAcceptStation.setTextColor(0xff999999);
            itemHolder.imgDot.setImageResource(R.mipmap.icon_wuliu_jindu);
            itemHolder.tvAcceptStation.setTextColor(mContext.getResources().getColor(R.color.color_999999));

        }

        if(position == traceList.size()-1){
            itemHolder.tvBottomLine.setVisibility(View.INVISIBLE);
        } else {
            itemHolder.tvBottomLine.setVisibility(View.VISIBLE);
        }

        itemHolder.bindHolder(bean);
    }

    @Override
    public int getItemCount() {
        return traceList == null ? 0 : traceList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOP;
        }
        return TYPE_NORMAL;
    }

    private class LogisticsViewHolder extends RecyclerView.ViewHolder {

        private TextView tvAcceptTime, tvAcceptDate, tvAcceptStation;
        private TextView tvTopLine, tvBottomLine;
        private ImageView imgDot;
        private View vTop;

        public LogisticsViewHolder(View itemView) {
            super(itemView);
            tvAcceptTime = (TextView) itemView.findViewById(R.id.tvAcceptTime);
            tvAcceptDate = (TextView) itemView.findViewById(R.id.tvAcceptDate);
            tvAcceptStation = (TextView) itemView.findViewById(R.id.tvAcceptStation);
            tvTopLine = (TextView) itemView.findViewById(R.id.tvTopLine);
            tvBottomLine = (TextView) itemView.findViewById(R.id.tvBottomLine);
            imgDot = (ImageView) itemView.findViewById(R.id.imgDot);
            vTop = itemView.findViewById(R.id.v_top);
        }

        public void bindHolder(LogisticsBean.TracesBean bean) {
            tvAcceptStation.setText(bean.getAcceptStation());

            if(TextUtils.isEmpty(bean.getAcceptTime())) {
                tvAcceptTime.setVisibility(View.INVISIBLE);
                tvAcceptDate.setVisibility(View.INVISIBLE);
            } else {
                String[] dateTime = bean.getAcceptTime().split(" ");
                String[] dateM = dateTime[1].split(":");
                tvAcceptTime.setText(dateM[0]+":"+dateM[1]);
                tvAcceptDate.setText(CalendarFormat.formatDateTime(bean.getAcceptTime()));
            }
        }
    }
}

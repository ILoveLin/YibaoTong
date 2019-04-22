package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.HistoryCache;
import com.lzyc.ybtappcal.util.TimeUtil;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * 比药历史记录
 * Created by yang on 2017/03/15.
 */
public class BiyaoHistorysAdapter extends CommonAdapter<HistoryCache> {

    private Context mContext;
    private Drawable drawableLeftSearch;
    private Drawable drawableLeftScan;

    public BiyaoHistorysAdapter(Context context, int layoutId, List<HistoryCache> datas) {
        super(context, layoutId, datas);
        this.mContext=context;
        drawableLeftSearch = mContext.getResources().getDrawable(R.mipmap.icon_history_biyao_search);
        drawableLeftScan = mContext.getResources().getDrawable(R.mipmap.icon_history_biyao_scan);
        drawableLeftSearch.setBounds(0, 0, drawableLeftSearch.getMinimumWidth(), drawableLeftSearch.getMinimumHeight());
        drawableLeftScan.setBounds(0, 0, drawableLeftScan.getMinimumWidth(), drawableLeftScan.getMinimumHeight());
    }

    public void setIndex(int index, HistoryCache hs) {
        this.mDatas.set(index, hs);
//        this.index=index-1;
        notifyDataSetChanged();
    }

    public void setEmptyView(){
        this.mDatas=new ArrayList<HistoryCache>();
        HistoryCache h=new HistoryCache();
        h.setEmptyView(true);
        this.mDatas.add(h);
        notifyDataSetChanged();
    }

    public void setList(List<HistoryCache> list) {
        if (this.mDatas == null) {
            this.mDatas = new ArrayList<HistoryCache>();
        }
        this.mDatas.clear();
        if(list.size()==0){
            setEmptyView();
        }else{
            this.mDatas.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder helper, final HistoryCache item, final int position) {
        TextView item_drugs_history_emptyview=helper.getView(R.id.item_drugs_history_emptyview);
        RelativeLayout item_drugs_history_rel=helper.getView(R.id.item_drugs_history_rel);
        boolean isEmptyView=item.isEmptyView();
        if (isEmptyView) {
            item_drugs_history_emptyview.setVisibility(View.VISIBLE);
            item_drugs_history_rel.setVisibility(View.GONE);
        } else {
            item_drugs_history_emptyview.setVisibility(View.GONE);
            item_drugs_history_rel.setVisibility(View.VISIBLE);
            TextView tvName = helper.getView(R.id.item_drugs_history_name);
            TextView biyaoGoodsName = helper.getView(R.id.item_drugs_history_goodsname);
            TextView tv_time = helper.getView(R.id.item_drugs_history_createtime);
            SimpleDateFormat s = new SimpleDateFormat("MM-dd HH:mm");
            String name = item.getName();
            String goodsName=item.getGoodsName();
            int cacheType=item.getCacheType();
            if(cacheType==HistoryCache.TYPE_CACHE_SEARCH){//搜索
                biyaoGoodsName.setCompoundDrawables(drawableLeftSearch, null, null, null);
            }else{//扫码
                biyaoGoodsName.setCompoundDrawables(drawableLeftScan, null, null, null);
            }
            if (!TextUtils.isEmpty(goodsName)&&goodsName!=null) {
                biyaoGoodsName.setText(goodsName);
                tvName.setText(name);
            }else {
                biyaoGoodsName.setText(name);
                tvName.setText("");
            }
            tv_time.setText("" + TimeUtil.getTime(item.getCreateTime(), s));
            View lineTop=helper.getView(R.id.biyao_history_line_top);
            if(position==0){
                lineTop.setVisibility(View.VISIBLE);
            }else{
                lineTop.setVisibility(View.GONE);
            }
            View viewBottom=helper.getView(R.id.view_bottom);
            if(position==mDatas.size()-1){
                viewBottom.setVisibility(View.VISIBLE);
            }else{
                viewBottom.setVisibility(View.GONE);
            }
        }
    }

}


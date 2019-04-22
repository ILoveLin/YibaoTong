package com.lzyc.ybtappcal.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.GoodsBean;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.widget.popupwindow.lib.DensityUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by yanlc on 17-5-14.
 */

public class OrderMineRecycleAdapter  extends
        RecyclerView.Adapter<OrderMineRecycleAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<GoodsBean> mDatas;
    private OnItemClickLitener mOnItemClickLitener;
    private Context mContext;

    /**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }



    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public OrderMineRecycleAdapter(Context context, List<GoodsBean> datats) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }

        ImageView mImg;
        View viewOut;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_order_mine_horizontal, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImg = (ImageView) view.findViewById(R.id.iv_image_horizontal);
        viewHolder.viewOut = view.findViewById(R.id.view_out);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Picasso.with(mContext)
                .load(mDatas.get(i).getImage())
                .error(R.mipmap.image_empty_order2)
                .placeholder(R.mipmap.image_empty_order2)
                .into(viewHolder.mImg);
        if(i==mDatas.size()-1){
            viewHolder.viewOut.setVisibility(View.VISIBLE);
        }else{
            viewHolder.viewOut.setVisibility(View.GONE);
        }
        if (mOnItemClickLitener != null) {
            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });
        }

    }

}

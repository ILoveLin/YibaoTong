package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.payment.order.OrderDetailActivity;
import com.lzyc.ybtappcal.bean.GoodsBean;
import com.lzyc.ybtappcal.bean.OrderInfo;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.StringUtils;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by yang on 2017/03/15.
 */
public class OrderMineAdapter extends CommonAdapter<OrderInfo> {
    private OnChildClikListener listener;

    public OrderMineAdapter(Context context, int layoutId, List<OrderInfo> datas) {
        super(context, layoutId, datas);
    }


    public void setOnChildClikListener(OnChildClikListener listener) {
        this.listener = listener;
    }

    public void setList(List<OrderInfo> list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public void addList(List<OrderInfo> list) {
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.mDatas.remove(position);
        notifyDataSetChanged();
    }

    public void updateItemState(int position, String state) {
        OrderInfo orderInfo= mDatas.get(position);
        orderInfo.setState(state);
        mDatas.set(position,orderInfo);
        notifyDataSetChanged();
    }

    public void updateItemOrderInfo(int position, OrderInfo orderInfo) {
        mDatas.set(position,orderInfo);
        notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder helper, final OrderInfo item, final int position) {
        ImageView ivLogo=helper.getView(R.id.iv_merchant_logo);
        TextView tvMerchantName=helper.getView(R.id.tv_merchant_name);
//        TextView orderby_mine_id = helper.getView(R.id.order_detail_id);
        TextView tvCancel = helper.getView(R.id.tv_cancel);
        TextView tvZhiFu = helper.getView(R.id.tv_zhifu);
        // 合计
        TextView tvHeJi = helper.getView(R.id.tv_heji);
        TextView tvPayStatus = helper.getView(R.id.tv_pay_status);
        ImageView ivDrugImage = helper.getView(R.id.iv_drug_image);
        TextView tvDrugName = helper.getView(R.id.tv_drugname);
        TextView tvDrugType = helper.getView(R.id.tv_drug_type);
        TextView tvCount = helper.getView(R.id.tv_count);
        TextView orderby_mine_price = helper.getView(R.id.tv_price_present);
        RecyclerView itmeOrderMinelv=helper.getView(R.id.item_order_mine_lv);
        TextView tvPriceOriginal=helper.getView(R.id.tv_price_original);
        LinearLayout mineGoodsLinear=helper.getView(R.id.linear_goods);
        List<GoodsBean> goodsBeanList=item.getGoodsList();
        Picasso.with(mContext).load(item.getMerchantLogo()).error(R.mipmap.empty_logo).placeholder(R.mipmap.empty_logo).into(ivLogo);
        tvMerchantName.setText(item.getMerchantName());
        if(goodsBeanList.size()==1){
            GoodsBean goodsBean=goodsBeanList.get(0);
            itmeOrderMinelv.setVisibility(View.GONE);
            mineGoodsLinear.setVisibility(View.VISIBLE);
            orderby_mine_price.setText(goodsBean.getDeKaiPrice());
            tvCount.setText("x" + goodsBean.getCount());
            String originalPrice=goodsBean.getRetailPrice();//原价
            tvPriceOriginal.setText(originalPrice);
            tvPriceOriginal.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            Picasso.with(mContext).load(goodsBean.getImage()).error(R.mipmap.image_empty_order2).placeholder(R.mipmap.image_empty_order2).into(ivDrugImage);
            String name=goodsBean.getGoodsName();
            if(name!=null){
                name=name+" "+goodsBean.getName();
            }else{
                name=goodsBean.getName();
            }
            String typeDrug = "";
            int type=Integer.parseInt(goodsBean.getType());
            if(type==1){
                typeDrug = "【非处方】";
                name="                 "+name;
            }else{
                typeDrug = "【处方】";
                name="             "+ name;
            }
            tvDrugType.setText(typeDrug);
            tvDrugName.setText(StringUtils.getSpannableText(name," "+goodsBean.getSpecifications(),13));
        } else {
            itmeOrderMinelv.setVisibility(View.VISIBLE);
            mineGoodsLinear.setVisibility(View.GONE);
            OrderMineRecycleAdapter mineRecycleAdapter = new OrderMineRecycleAdapter(mContext,goodsBeanList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            itmeOrderMinelv.setLayoutManager(linearLayoutManager);
            itmeOrderMinelv.setAdapter(mineRecycleAdapter);
            OverScrollDecoratorHelper.setUpOverScroll(itmeOrderMinelv,OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
            mineRecycleAdapter.setOnItemClickLitener(new OrderMineRecycleAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent=new Intent(mContext,OrderDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Contants.KEY_OBJ_ORDERINFO, item);
                    bundle.putInt(Contants.KEY_PAGE_PAYMENT,Contants.VAL_PAGE_PAYMENT_MINE);
                    bundle.putInt(Contants.KEY_POSITION,position);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
//            HorizontalOrderMineAdapter mineAdapter=new HorizontalOrderMineAdapter(mContext,goodsBeanList,R.layout.item_order_mine_horizontal);
//            itmeOrderMinelv.setAdapter(mineAdapter);
//            itmeOrderMinelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Intent intent=new Intent(mContext,OrderDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(Contants.KEY_OBJ_ORDERINFO, item);
//                    bundle.putInt(Contants.KEY_PAGE_PAYMENT,Contants.VAL_PAGE_PAYMENT_MINE);
//                    bundle.putInt(Contants.KEY_POSITION,position);
//                    intent.putExtras(bundle);
//                    mContext.startActivity(intent);
//                }
//            });
        }
        int count=0;
        for (int i = 0; i <goodsBeanList.size(); i++) {
            count=count+Integer.parseInt(goodsBeanList.get(i).getCount());
        }
        tvHeJi.setText("共" + count + "件商品 合计:");
        TextView orderby_mine_payprice = helper.getView(R.id.orderby_mine_payprice);
        double zongjia =Double.parseDouble(item.getTotalPrice())+Double.parseDouble(item.getFreight());
        orderby_mine_payprice.setText(""+ item.getPayPrice());
        TextView orderby_mine_return = helper.getView(R.id.orderby_mine_return);
        String retunMoney=item.getReturnMoney();
//        StringUtils.getReturnMoney()
        orderby_mine_return.setText(retunMoney);
        TextView orderby_mine_yunfei = helper.getView(R.id.orderby_mine_yunfei);
        orderby_mine_yunfei.setText(" (含运费￥" + item.getFreight() + ")");
//        orderby_mine_id.setText(item.getOrderID());
        tvZhiFu.setVisibility(View.VISIBLE);
        final int onLinePay = Integer.parseInt(item.getOnlinePay());
        final int state = Integer.parseInt(item.getState());
        tvZhiFu.setBackgroundResource(R.drawable.shape_order_zhifu);
        switch (state) {
            case 1:
                if (onLinePay == 1) {
                    tvPayStatus.setText("等待付款");
                    tvCancel.setText("取消订单");
                } else {
                    tvPayStatus.setText("等待发货");
                    tvCancel.setText("提醒发货");
                    tvZhiFu.setVisibility(View.GONE);
                }
                break;
            case 2:
                tvPayStatus.setText("等待发货");
                tvZhiFu.setVisibility(View.GONE);
                tvCancel.setText("提醒发货");
                break;
            case 3:
                tvPayStatus.setText("等待收货");
                tvZhiFu.setText("确认收货");
                tvZhiFu.setClickable(false);
                tvCancel.setText("查看物流");
                tvZhiFu.setBackgroundResource(R.drawable.shape_order_shouhuo);
                break;
            case 4:
                tvPayStatus.setText("等待收货");
                tvZhiFu.setText("确认收货");
                tvZhiFu.setClickable(true);
                tvCancel.setText("查看物流");
                tvZhiFu.setBackgroundResource(R.drawable.shape_order_zhifu);
                break;
            case 5:
                tvPayStatus.setText("交易完成");
                tvCancel.setText("删除订单");
                tvZhiFu.setVisibility(View.GONE);
                break;
            case 6:
                tvPayStatus.setText("退款完成");
                tvCancel.setText("删除订单");
                tvZhiFu.setVisibility(View.GONE);
                break;
            default:
                tvPayStatus.setText("交易关闭");
                tvCancel.setText("删除订单");
                tvZhiFu.setVisibility(View.GONE);
                break;
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener == null) {
                    return;
                }
                switch (state) {
                    case 1://取消订单
                        if (onLinePay == 1) {
                            listener.onChildClickOrderCancelListener(position, item.getOrderID());
                        } else {
                            listener.onChildClickOrderPromptShipmentListener();
                        }
                        break;
                    case 2://提醒发货
                        listener.onChildClickOrderPromptShipmentListener();
                        break;
                    case 3://查看物流
                        LogUtil.y("#######orderInfo##########"+new Gson().toJson(item));
                        listener.onChildClickOrderLogisticsInfoListener(item);
                        break;
                    case 4://查看物流
                        LogUtil.y("#######orderInfo##########"+new Gson().toJson(item));
                        listener.onChildClickOrderLogisticsInfoListener(item);
                        break;
                    default:
                        listener.onChildClickOrderDeleteListener(position, item.getOrderID());
                        break;

                }

            }
        });
        tvZhiFu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener == null) {
                    return;
                }
                switch (state){
                    case 3:
                    case 4:
                        listener.onChildClickOrderQueRenShouHuoListener(position,item);
                        break;
                    default:
                        listener.onChildClickOrderPaymentListener(item);
                        break;
                }

            }
        });
    }



    public interface OnChildClikListener {
        /**
         * 取消订单
         */
        void onChildClickOrderCancelListener(int position, String orderId);

        /**
         * 删除订单
         */
        void onChildClickOrderDeleteListener(int position, String orderId);

        /**
         * 立即支付
         */
        void onChildClickOrderPaymentListener(OrderInfo orderInfo);

        /**
         * 提醒发货
         */
        void onChildClickOrderPromptShipmentListener();

        /**
         * 物流信息
         */
        void onChildClickOrderLogisticsInfoListener(OrderInfo orderInfo);

        /**
         * 确认收货
         */
        void onChildClickOrderQueRenShouHuoListener(int position, OrderInfo orderInfo);

    }

}

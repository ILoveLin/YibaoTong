package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.AddressInfo;
import com.lzyc.ybtappcal.bean.GoodsBean;
import com.lzyc.ybtappcal.bean.ShopingCart;
import com.lzyc.ybtappcal.util.StringUtils;
import com.lzyc.ybtappcal.util.Util;
import com.squareup.picasso.Picasso;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 检查订单
 * Created by lxx on 2017/4/26.
 */

public class OrderCheckAdapter extends SectionedRecyclerViewAdapter<OrderCheckAdapter.HeaderViewHolder,
        OrderCheckAdapter.ItemViewHolder,
        OrderCheckAdapter.FooterViewHolder> {

    Context mContext;
    LayoutInflater mInflater;
    AddressInfo addressInfoData;

    List<ShopingCart> mData = new ArrayList<>();

    public OrderCheckAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void updata(List<ShopingCart> data) {
        mData.clear();
        if(null != data && !data.isEmpty()){
            this.mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void updataAddress(AddressInfo addressInfoData) {
        this.addressInfoData = addressInfoData;
        notifyDataSetChanged();
    }

    public List<ShopingCart> getData(){
        return mData;
    }
    @Override
    protected int getSectionCount() {
        return mData.size() + 1;
    }

    private boolean isAddressItem(int section) {
        return section == 0;
    }

    @Override
    protected int getItemCountForSection(int section) {
        if (isAddressItem(section)) {
            return 0;
        }
        section -= 1;
        return mData.get(section).getGoodsList().size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return !isAddressItem(section);
    }

    @Override
    protected boolean isSectionHeaderViewType(int viewType) {
        return Math.abs(viewType) == Math.abs(TYPE_SECTION_HEADER);
    }

    @Override
    protected int getSectionHeaderViewType(int section) {
        return isAddressItem(section) ? Math.abs(TYPE_SECTION_HEADER) : TYPE_SECTION_HEADER;
    }

    @Override
    protected int getSectionItemViewType(int section, int position) {
        return Math.abs(TYPE_ITEM);
    }

    @Override
    protected OrderCheckAdapter.HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Math.abs(TYPE_SECTION_HEADER)) {
            //地址
            return new OrderCheckAdapter.AddressViewHolder(mInflater.inflate(R.layout.item_check_order_header_address, parent, false));
        } else {
            //头布局
            return new OrderCheckAdapter.HeaderPharmacyViewHolder(mInflater.inflate(R.layout.item_check_order_header, parent, false));
        }
    }

    @Override
    protected OrderCheckAdapter.FooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        //底布局
        return new OrderCheckAdapter.FooterViewHolder(mInflater.inflate(R.layout.item_order_check_footer, parent, false));
    }

    @Override
    protected OrderCheckAdapter.ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        //子布局
        return new OrderCheckAdapter.ItemViewHolder(mInflater.inflate(R.layout.item_check_order, parent, false));
    }

    @Override
    protected void onBindSectionHeaderViewHolder(OrderCheckAdapter.HeaderViewHolder holder, int section) {

        if (holder instanceof OrderCheckAdapter.AddressViewHolder) {
            final OrderCheckAdapter.AddressViewHolder vh = (OrderCheckAdapter.AddressViewHolder) holder;
            if (null != addressInfoData) {
                vh.relCheckAddress.setVisibility(View.GONE);
                vh.linAddress.setVisibility(View.VISIBLE);

                vh.tvReceivingName.setText(addressInfoData.getName());
                vh.tvReceivingCall.setText(addressInfoData.getPhone());
                vh.tvReceivingAddress.setText(addressInfoData.getAddressRegion() + addressInfoData.getAddressDetail());
            } else {
                vh.linAddress.setVisibility(View.GONE);
                vh.relCheckAddress.setVisibility(View.VISIBLE);
            }
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onCheckOrderListener != null) {
                        onCheckOrderListener.onAddress();
                    }
                }
            });
        }

        if (holder instanceof OrderCheckAdapter.HeaderPharmacyViewHolder) {
            OrderCheckAdapter.HeaderPharmacyViewHolder vh = (OrderCheckAdapter.HeaderPharmacyViewHolder) holder;

            ShopingCart sCart = mData.get(section-1);
            Picasso.with(mContext)
                    .load(sCart.getLogo())
                    .placeholder(R.mipmap.image_empty_order)
                    .error(R.mipmap.image_empty_order)
                    .into(vh.imgPharmacy);
            vh.tvPharmacy.setText(sCart.getName());

            vh.tvPayType.setText("0".equals(sCart.getGoodsList().get(0).getOnlinePay()) ? "货到付款" : "在线支付");//0 货到付款  1 在线支付
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(OrderCheckAdapter.FooterViewHolder holder, int section) {
        section -= 1;
        ShopingCart sCart = mData.get(section);

        if(section == mData.size()-1){
            holder.vBottom.setVisibility(View.VISIBLE);
        } else {
            holder.vBottom.setVisibility(View.GONE);
        }

        double freight = 0;
        double freeShip = 0;
        String freightPrice = "";
        double totalPrice = 0;

        try{

            for(GoodsBean goodsBean : sCart.getGoodsList()){
                totalPrice += (Double.parseDouble(goodsBean.getDeKaiPrice())* goodsBean.getGoodsCount());
            }

            freight = Double.parseDouble(sCart.getFreight().toString());
            freeShip = Double.parseDouble(sCart.getFreeShipp().toString());

        } catch (Exception e){}

        //运费为0  或  运费大于等于商品总价
        if(0 >= freight || freeShip <= totalPrice){
            freightPrice = "包邮";
            holder.tvFreightPriceSign.setVisibility(View.GONE);
        } else {
            freightPrice = Util.getFloatDotStr(String.valueOf(freight));
            holder.tvFreightPriceSign.setVisibility(View.VISIBLE);
        }

        holder.tvDrugPrice.setText(Util.getFloatDotStr(String.valueOf(totalPrice)));
        holder.tvFreightPrice.setText(freightPrice);
    }

    @Override
    protected void onBindItemViewHolder(OrderCheckAdapter.ItemViewHolder holder, int section, int position) {

        section -= 1;

        GoodsBean bean = mData.get(section).getGoodsList().get(position);
        if(holder instanceof ItemViewHolder) {
            final ItemViewHolder vh = holder;

            String typeDrug = "";
            String textName="";
            if ("1".equals(bean.getType())) {

                typeDrug = "【非处方】";
                textName="                 "+ bean.getGoodsName() + " " + bean.getName();

            } else {

                typeDrug = "【处方】";
                textName="             "+ bean.getGoodsName() + " " + bean.getName();
            }

            vh.tvType.setText(typeDrug);
            vh.tvName.setText(StringUtils.getSpannableText(textName, " " + bean.getSpecifications()));
            vh.tvPriceNew.setText(bean.getDeKaiPrice());
            vh.tvPriceOld.setText(bean.getRetailPrice());
            vh.tvPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            Picasso.with(mContext)
                    .load(bean.getImage())
                    .placeholder(R.mipmap.image_empty)
                    .error(R.mipmap.image_empty)
                    .into(holder.imgDrug);

            vh.tvNum.setText("x"+bean.getGoodsCount());

        }
    }

    class AddressViewHolder extends OrderCheckAdapter.HeaderViewHolder {
        @BindView(R.id.rel_check_address)
        RelativeLayout relCheckAddress;
        @BindView(R.id.tv_receiving_name)
        TextView tvReceivingName;
        @BindView(R.id.tv_receiving_call)
        TextView tvReceivingCall;
        @BindView(R.id.tv_receiving_address)
        TextView tvReceivingAddress;
        @BindView(R.id.lin_address)
        LinearLayout linAddress;

        public AddressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    class HeaderPharmacyViewHolder extends OrderCheckAdapter.HeaderViewHolder {
        @BindView(R.id.img_pharmacy)
        ImageView imgPharmacy;
        @BindView(R.id.tv_pharmacy)
        TextView tvPharmacy;
        @BindView(R.id.tv_pay_type)
        TextView tvPayType;

        public HeaderPharmacyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_drug)
        ImageView imgDrug;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price_new)
        TextView tvPriceNew;
        @BindView(R.id.tv_price_old)
        TextView tvPriceOld;
        @BindView(R.id.tv_num)
        TextView tvNum;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_drug_price)
        TextView tvDrugPrice;
        @BindView(R.id.tv_freight_price)
        TextView tvFreightPrice;
        @BindView(R.id.tv_freight_price_sign)
        TextView tvFreightPriceSign;
        @BindView(R.id.v_bottom)
        View vBottom;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private CheckOrderListener onCheckOrderListener;

    public void setCheckOrderListener(CheckOrderListener onCheckOrderListener) {
        this.onCheckOrderListener = onCheckOrderListener;
    }

    public interface CheckOrderListener {
        void onAddress();
    }
}

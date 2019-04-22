package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.GoodsListBean;
import com.lzyc.ybtappcal.widget.verticalpager.CirclePageIndicator;
import com.lzyc.ybtappcal.widget.verticalpager.Proxy;
import com.lzyc.ybtappcal.widget.verticalpager.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxx on 2017/5/12.
 */

class HomeAttributeAdapter extends RecyclerView.Adapter implements HomeAttrDiscounterAdapter.HomeDiscounterListener{

    String TAG = getClass().getSimpleName();

    Context mContext;
    LayoutInflater mInflater;
    List<GoodsListBean> mData = new ArrayList<>();
    List<GoodsListBean> mDataTmp1 = new ArrayList<>();
    List<GoodsListBean> mDataTmp2 = new ArrayList<>();
    List<GoodsListBean> mDataTmp3 = new ArrayList<>();


    int column;

    public HomeAttributeAdapter(Context context) {

        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void updata(List<GoodsListBean> data) {

        mData.clear();

        if(null != data && !data.isEmpty()){
            mData.addAll(data);
            for (int i = 0; i < mData.size(); i++) {
                if (0 == i % 3) {
                    mDataTmp1.add(mData.get(i));
                }
                if (1 == i % 3) {
                    mDataTmp2.add(mData.get(i));
                }
                if (2 == i % 3) {
                    mDataTmp3.add(mData.get(i));
                }
            }
        }

        if(50 < data.size()){
//            notifyItemInsertChange(data.size()-11, 10);
//            notifyItemRangeInserted(data.size()-11, 10);
//            notifyItemRangeChanged(data.size()-11, 10);
            notifyItemChanged(data.size()-11, "123");
//            LogUtil.d("lxx", "<50--->"+data.size());
            return;
        }
//        LogUtil.d("lxx", "<76--->"+data.size());
//        notifyItemRangeChanged(0, data.size());
        notifyDataSetChanged();

    }

    public void updataColumn(int col) {
        this.column = col;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try{
            if (3 == column) {
                return new VerPagerViewHolder(mInflater.inflate(R.layout.item_home_ver_pager, null));
            }
            return new AttributeViewHolder(mInflater.inflate(R.layout.item_home_drugs,null));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof VerPagerViewHolder) {

            VerPagerViewHolder holder = (VerPagerViewHolder) viewHolder;

            holder.onProxy1(mDataTmp1);
            holder.onProxy2(mDataTmp2);
            holder.onProxy3(mDataTmp3);

        } else if (viewHolder instanceof AttributeViewHolder) {

            AttributeViewHolder holder = (AttributeViewHolder) viewHolder;

            final GoodsListBean bean = mData.get(position);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onAttributeListener) {
                        onAttributeListener.onAttributeListener(bean.getDKID());
                    }
                }
            });

            String goodsName = "";
            String name = "";

            if (TextUtils.isEmpty(bean.getGoodsName())) {
                goodsName = bean.getName();
            } else {
                goodsName = bean.getGoodsName();
                name = bean.getName();
            }

            holder.tvDrugPrice.setText(bean.getDeKaiPrice());
            holder.tvReturnMoney.setText(bean.getReturnMoney());
            holder.tvGoodsName.setText(goodsName);
            holder.tvName.setText(name);

            if (TextUtils.isEmpty(bean.getImage())) return;


//            Picasso.with(mContext)
//                    .load(bean.getImage())
//                    .placeholder(R.mipmap.image_empty_no_bg)
//                    .error(R.mipmap.image_empty_no_bg)
//                    .config(Bitmap.Config.RGB_565)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
//                    .tag("list")
//                    .into(holder.imgDrug);

            holder.imgDrug.setImageURI(Uri.parse(bean.getImage()));

//            ImageUtil.showThumb(mContext, Uri.parse(bean.getImage()), holder.imgDrug, RecyclerView.LayoutParams.MATCH_PARENT, 27);
        }

    }

    @Override
    public int getItemCount() {
        return column == 3 ? 1 : mData.size();
    }

    @Override
    public void onItenClick(String idStr) {
        if (null != onAttributeListener) {
            onAttributeListener.onAttributeListener(idStr);
        }
    }

    class VerPagerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vp_1)
        VerticalViewPager verticalViewPager01;
        @BindView(R.id.vp_2)
        VerticalViewPager verticalViewPager02;
        @BindView(R.id.vp_3)
        VerticalViewPager verticalViewPager03;
        @BindView(R.id.indicator_lunbo1)
        CirclePageIndicator circlePageIndicator01;
        @BindView(R.id.indicator_lunbo2)
        CirclePageIndicator circlePageIndicator02;
        @BindView(R.id.indicator_lunbo3)
        CirclePageIndicator circlePageIndicator03;

        Proxy<GoodsListBean> proxy1,proxy2,proxy3;
        public HomeAttrDiscounterAdapter adapter1,adapter2,adapter3;
        public VerPagerViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            //Context context, ViewPager viewPager, HomeAttrDiscounterAdapter pagerAdapter,CirclePageIndicator indicator
//            verticalViewPager01.setCurrentItem(0,true);
//            verticalViewPager02.setCurrentItem(0,true);
//            verticalViewPager03.setCurrentItem(0,true);

            adapter1 = new HomeAttrDiscounterAdapter(mContext);
            adapter1.setOnHomeDiscounterListener(HomeAttributeAdapter.this);
            proxy1 = new Proxy<>(mContext,verticalViewPager01,adapter1,circlePageIndicator01);

            adapter2 = new HomeAttrDiscounterAdapter(mContext);
            adapter2.setOnHomeDiscounterListener(HomeAttributeAdapter.this);
            proxy2 = new Proxy<>(mContext,verticalViewPager02,adapter2,circlePageIndicator02);

            adapter3 = new HomeAttrDiscounterAdapter(mContext);
            adapter3.setOnHomeDiscounterListener(HomeAttributeAdapter.this);
            proxy3 = new Proxy<>(mContext,verticalViewPager03,adapter3,circlePageIndicator03);
        }

        public void onProxy1(List<GoodsListBean> data){
            onProxy(data,proxy1,adapter1,verticalViewPager01,circlePageIndicator01);
        }

        public void onProxy2(List<GoodsListBean> data){
            onProxy(data,proxy2,adapter2,verticalViewPager02,circlePageIndicator02);
        }

        public void onProxy3(List<GoodsListBean> data){
            onProxy(data,proxy3,adapter3,verticalViewPager03,circlePageIndicator03);
        }

        private void onProxy(List<GoodsListBean> data,Proxy proxy,HomeAttrDiscounterAdapter adapter,VerticalViewPager pager,CirclePageIndicator indicator){
            adapter.updata(data);
            proxy.onBindView(data.size());
        }
    }

    class AttributeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_home_drug)
        SimpleDraweeView imgDrug;
        @BindView(R.id.tv_goods_name)
        TextView tvGoodsName;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.item_home_drug_price)
        TextView tvDrugPrice;
        @BindView(R.id.tv_return_money)
        TextView tvReturnMoney;

        public AttributeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    private AttributeListener onAttributeListener;

    public void setOnAttributeListener(AttributeListener onAttributeListener) {
        this.onAttributeListener = onAttributeListener;
    }

    public interface AttributeListener {

        void onAttributeListener(String idStr);
    }

}

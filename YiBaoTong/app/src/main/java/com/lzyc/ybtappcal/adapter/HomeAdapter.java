package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.activity.MainActivity;
import com.lzyc.ybtappcal.activity.home.HotActivity;
import com.lzyc.ybtappcal.activity.home.TypeActivity;
import com.lzyc.ybtappcal.activity.payment.order.OrderbyActivity;
import com.lzyc.ybtappcal.bean.AttributeBean;
import com.lzyc.ybtappcal.bean.BannerBean;
import com.lzyc.ybtappcal.bean.CategoryBean;
import com.lzyc.ybtappcal.bean.GoodsHomePageList2;
import com.lzyc.ybtappcal.constant.Contants;
import com.lzyc.ybtappcal.constant.HttpConstant;
import com.lzyc.ybtappcal.util.LogUtil;
import com.lzyc.ybtappcal.util.ScreenUtils;
import com.lzyc.ybtappcal.util.TBanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lxx on 2017/3/27.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ItemViewHolder>{

    static final int TYPE_BANNER = 10;
    static final int TYPE_CATE = 11;
    static final int TYPE_HOT = 12;

    Context mContext;

    LayoutInflater mInflater;

    List<CategoryBean> mCategoryData = new ArrayList<>();

    List<GoodsHomePageList2> mData = new ArrayList<>();

    List<BannerBean.ImagesBean> mBannersData = new ArrayList<>();

    List<String> mBannerStrings = new ArrayList<>();

    Map<Integer,Boolean> cacheMap = new HashMap<>();
    String mSearchTitle;
    List<ContentViewHolder> mContentViewHolders = new ArrayList<>();
    CateViewHolder mCateViewHolder;

    MainActivity activity;

    public void updataActivity(MainActivity activity){
        this.activity = activity;
    }

    public HomeAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void updateBannerList(List<BannerBean.ImagesBean> data) {
        mBannersData.clear();
        mBannerStrings.clear();
        if (null != data && !data.isEmpty()) {
            mBannersData.addAll(data);
            for (BannerBean.ImagesBean bean : data) {
                mBannerStrings.add(bean.getImg_url());
            }
        }
    }

    public void updataCategory(List<CategoryBean> data) {
        mCategoryData.clear();
        if (null != data && !data.isEmpty()) {
            mCategoryData.addAll(data);
        }
    }

    public void updataSearch(String txt) {
        this.mSearchTitle = txt;
    }

    public void updateData(List<GoodsHomePageList2> data) {
        mData.clear();

        if (null != data && !data.isEmpty()) {
            mData.addAll(data);
        }
        notifyDataSetChangeds();
    }

    public void notifyDataSetChangeds(){
        notifyDataSetChanged();
        cacheMap.clear();
    }

    public void notifyDataSetChangedChild(){
        if(mContentViewHolders.size() > 0){
            mContentViewHolders.get(mContentViewHolders.size()-1).mAttributeAdapter.notifyDataSetChanged();
        }
    }

    public void notifyCateDataSetChanged(){
        if(mCateViewHolder != null){
            mCateViewHolder.mCategoryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        int count = 2;
        if(mData.isEmpty()){
            return count;
        }
        try{
            return mData.get(0).getAttribute().size() + count;
        } catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) {
            return new BannerViewHolder(mInflater.inflate(R.layout.item_home_banner, parent, false));
        }
        if (viewType == TYPE_CATE) {
            Log.e("yanlc","new cate view");
            mCateViewHolder = new CateViewHolder(mInflater.inflate(R.layout.item_home_recycler, parent, false));
            return mCateViewHolder;
        }
        ContentViewHolder holder = new ContentViewHolder(mInflater.inflate(R.layout.item_home_item, parent, false));
        mContentViewHolders.add(holder);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_BANNER;
        }
        if(position == 1){
            return TYPE_CATE;
        }
//        if(position == 2){
//            return TYPE_HOT;
//        }
        return position;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        if (holder instanceof BannerViewHolder) {
            final BannerViewHolder vh = (BannerViewHolder) holder;

            vh.update(mBannerStrings);

            final int index = position;
            vh.tbanner.setOnImgRollChanged(new TBanner.ImgRollChanged() {
                @Override
                public void onChanged(int p) {
                    if (null != onHomeListener) {
                        onHomeListener.OnBannerClickListener(mBannersData.get(index).getWeb_url());
                    }
                }
            });
        }

        if (holder instanceof CateViewHolder) {

            CateViewHolder vh = (CateViewHolder) holder;

            final String[] events = {"k005", "k006", " k007"};

            try{
                vh.tvCategory.setText(mCategoryData.get(mCategoryData.size()-1).getName());
                vh.imgCategory.setImageURI(Uri.parse(mCategoryData.get(mCategoryData.size()-1).getIcon()));
//
//                Picasso.with(mContext)
//                        .load(mCategoryData.get(mCategoryData.size()-1).getIcon())
//                        .placeholder(R.mipmap.image_empty_category)
//                        .error(R.mipmap.image_empty_category)
//                        .into(vh.imgCategory);
//                ImageUtil.showThumb(mContext, Uri.parse(mCategoryData.get(mCategoryData.size()-1).getIcon()), vh.imgCategory, 130, 100);
            } catch (Exception e){
                e.printStackTrace();
            }

            vh.mCategoryAdapter.updata(mCategoryData);

            vh.mCategoryAdapter.setOnCategoryListener(new HomeCategoryAdapter.CategoryListener() {

                @Override
                public void onHotClickListener(String title, String type, String idStr, int postion) {

                    activity.requestEventCode(events[postion]);

                    Intent intent = new Intent(mContext, HotActivity.class);
                    intent.putExtra(Contants.KEY_HOME_TITLE, title);
                    intent.putExtra(Contants.KEY_HOME_TYPE, type);
                    intent.putExtra(Contants.KEY_HOME_ID, idStr);
                    mContext.startActivity(intent);
                }
            });
        }

        position -= 2;
        if (holder instanceof ContentViewHolder) {

            ContentViewHolder vh = (ContentViewHolder) holder;
            try {

                final AttributeBean bean = mData.get(0).getAttribute().get(position);

                vh.updateColumn(bean.getColumn());

                vh.mAttributeAdapter.updata(bean.getGoodsList());
                vh.mAttributeAdapter.updataColumn(bean.getColumn());

                vh.linHot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != onHomeListener) {
                            if (0 == bean.getShowMore()) {
                                return;
                            }
                            onHomeListener.onHotClickListener(bean.getName(), "attribute", bean.getID());
                        }
                    }
                });

                if (!TextUtils.isEmpty(bean.getIcon())) {
                    vh.imgHot.setImageURI(Uri.parse(bean.getIcon()));
//                    ImageUtil.showThumb(mContext, Uri.parse(bean.getIcon()), vh.imgHot, 100, RecyclerView.LayoutParams.MATCH_PARENT);
//                    Picasso.with(mContext)
//                            .load(bean.getIcon())
//                            .config(Bitmap.Config.RGB_565)
//                            .placeholder(R.mipmap.image_empty_home_hot)
//                            .error(R.mipmap.image_empty_home_hot)
//
//                            .into(vh.imgHot);
                }

                if (0 == bean.getShowMore()) {
                    vh.tvShowMore.setVisibility(View.GONE);
                } else {
                    vh.tvShowMore.setVisibility(View.VISIBLE);
                }

            }catch (Exception e){
                e.printStackTrace();
                LogUtil.d("lxx", e.toString());
                System.gc();
            }
        }


    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class BannerViewHolder extends ItemViewHolder {

        @BindView(R.id.tbanner)
        public TBanner tbanner;

        List<String> mData = new ArrayList<>();
        public BannerViewHolder(View itemView) {
            super(itemView);
        }

        public void update(List<String> data){
            if(!mData.isEmpty()){
                return;
            }

            mData.addAll(data);
            tbanner.update(data);
        }

    }

    class CateViewHolder extends ItemViewHolder {

        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;
        @BindView(R.id.lin_right)
        LinearLayout linRight;
        @BindView(R.id.img_category)
        SimpleDraweeView imgCategory;
        @BindView(R.id.tv_category)
        TextView tvCategory;

        HomeCategoryAdapter mCategoryAdapter = new HomeCategoryAdapter(mContext);

        public CateViewHolder(View itemView) {
            super(itemView);

            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            recyclerview.setLayoutManager(layoutManager);
            recyclerview.setAdapter(mCategoryAdapter);

            linRight.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth() / 4, ViewGroup.LayoutParams.WRAP_CONTENT));

            linRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.requestEventCode("k008");
                    Intent intent = new Intent(mContext, TypeActivity.class);
                    intent.putExtra("search", mSearchTitle);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    class ContentViewHolder extends ItemViewHolder {

        @BindView(R.id.img_hot)
        SimpleDraweeView imgHot;
        @BindView(R.id.tv_show_more)
        TextView tvShowMore;
        @BindView(R.id.lin_hot)
        LinearLayout linHot;
        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;

        HomeAttributeAdapter mAttributeAdapter;

        GridLayoutManager mLayoutManager;

        public ContentViewHolder(View itemView) {
            super(itemView);

            recyclerview.setLayoutManager(mLayoutManager = new GridLayoutManager(mContext, 2){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });

            recyclerview.setAdapter(mAttributeAdapter = new HomeAttributeAdapter(mContext));

            linHot.setVisibility(View.VISIBLE);

            mAttributeAdapter.setOnAttributeListener(new HomeAttributeAdapter.AttributeListener() {
                @Override
                public void onAttributeListener(String idStr) {
                    activity.requestEventCode("k010");
                    Intent intent = new Intent(mContext, OrderbyActivity.class);
                    intent.putExtra(HttpConstant.SHOP_GOODS_PARAM_DKID, idStr);
                    mContext.startActivity(intent);
                }
            });
        }

        public void updateColumn(final int column){

            mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int spanSize = (int) Math.ceil(column == 3 ? 2 : 1);
                    return spanSize;
                }
            });
        }
    }

    private HomeListener onHomeListener;

    public void setOnHomeListener(HomeListener onHomeListener) {
        this.onHomeListener = onHomeListener;
    }

    public interface HomeListener {

        void OnBannerClickListener(String url);

        void onHotClickListener(String title, String type, String idStr);

    }
}

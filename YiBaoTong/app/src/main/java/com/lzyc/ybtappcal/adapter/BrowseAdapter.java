package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.BrowseBean;
import com.lzyc.ybtappcal.util.DensityUtils;
import com.lzyc.ybtappcal.util.TimeUtil;
import com.lzyc.ybtappcal.util.ToastUtil;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2017/06/02.
 */
public class BrowseAdapter extends CommonAdapter<BrowseBean> {
    private int tabValue = 0;
    private OnItemBrowseClickListener listener;
    private boolean isDeleteGoods = false;
    private boolean isDeleteDrug = false;
    private boolean isSelectedDrugAll = false;
    private boolean isSelectedGoodsAll = false;

    public BrowseAdapter(Context context, int layoutId, List<BrowseBean> datas) {
        super(context, layoutId, datas);
    }

    public void setStatusGoods(boolean isDeleteGoods) {
        this.isDeleteGoods = isDeleteGoods;
        notifyDataSetChanged();
    }

    public void setTabValue(int tabValue) {
        this.tabValue = tabValue;
    }

    public void removeAll() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public List<BrowseBean> getListSelected(){
        List<BrowseBean> list=new ArrayList<>();
        for (int i = 0; i <mDatas.size() ; i++) {
            BrowseBean item=mDatas.get(i);
            if(item.isSelectedGoods()){
                list.add(item);
            }
            if(item.isSelectedDrug()){
                list.add(item);
            }
        }
        return list;
    }

    public void removeSome() {
        for (int i = 0; i < mDatas.size(); i++) {
            BrowseBean item = mDatas.get(i);
            if (item.isSelectedGoods()) {
                mDatas.remove(i);
            }
            if (item.isSelectedDrug()) {
                mDatas.remove(i);
            }
        }
        notifyDataSetChanged();
    }

    public void remveItem(int position){
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    public void setSelecteAllGoods(boolean isSelected) {
        this.isSelectedGoodsAll = isSelected;
        for (int i = 0; i < mDatas.size(); i++) {
            BrowseBean item = mDatas.get(i);
            item.setSelectedGoods(isSelected);
            mDatas.set(i, item);
        }
        notifyDataSetChanged();
    }

    public void setSelecteAllDrug(boolean isSelected) {
        isSelectedDrugAll = isSelected;
        for (int i = 0; i < mDatas.size(); i++) {
            BrowseBean item = mDatas.get(i);
            item.setSelectedDrug(isSelected);
            mDatas.set(i, item);
        }
        notifyDataSetChanged();
    }

    public void setStatusDrug(boolean isDeleteDrug) {
        this.isDeleteDrug = isDeleteDrug;
        notifyDataSetChanged();
    }

    public void setItemSelected(int position, BrowseBean item) {
        mDatas.set(position, item);
        notifyDataSetChanged();
    }

    public void setOnItemBrowseClickListener(OnItemBrowseClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<BrowseBean> list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public void addList(List<BrowseBean> list) {
        for (int i = 0; i < list.size(); i++) {
            BrowseBean item = list.get(i);
            item.setSelectedDrug(isSelectedDrugAll);
            item.setSelectedGoods(isSelectedGoodsAll);
            list.set(i, item);
        }
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public String getLastTime() {
        String time = mDatas.get(mDatas.size() - 1).getLastTime();
        return time;
    }

    public int getSelectedDrugCount() {
        int count = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            BrowseBean item = mDatas.get(i);
            if (item.isSelectedDrug()) {
                count++;
            }
        }
        return count;
    }

    public int getSelectedGoodsCount() {
        int count = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            BrowseBean item = mDatas.get(i);
            if (item.isSelectedGoods()) {
                count++;
            }
        }
        return count;
    }


    @Override
    protected void convert(ViewHolder helper, final BrowseBean item, final int position) {
        LinearLayout browseItem = helper.getView(R.id.item_browse_linear);
        LinearLayout linearGoods = helper.getView(R.id.linear_goods);
        LinearLayout linearDrug = helper.getView(R.id.linear_drug);
        View bottomLine = helper.getView(R.id.bottom_line);
        View bottomLineBottom = helper.getView(R.id.browse_group_line_bottom);
        View zhanweiLine = helper.getView(R.id.browse_group_zhanwei_line);
        View bottomLineBottom2 = helper.getView(R.id.browse_group_bottom_line);
        TextView tvGoodsDrugname = helper.getView(R.id.item_goods_drugname);
        TextView browseGroupDesc = helper.getView(R.id.browse_group_desc);
        ImageView browseGoodsImage = helper.getView(R.id.item_goods_image);
        ImageView browseDrugImage = helper.getView(R.id.item_drugs_search_photo);
        TextView tvDrugName = helper.getView(R.id.item_drugs_search_drugname);
        TextView tvVender = helper.getView(R.id.item_home_manufacturer);
        TextView tvDrugVender = helper.getView(R.id.item_drugs_search_address);
        TextView tvGoodsPrice = helper.getView(R.id.item_home_drug_price);
        TextView tvGoodsReturnMoney = helper.getView(R.id.item_home_return_price);
        ImageView imageViewGoods = helper.getView(R.id.item_imageview_selected_goods);
        ImageView imageViewDrug = helper.getView(R.id.item_imageview_selected_drug);
        LinearLayout broweLinearPrice = helper.getView(R.id.browe_linear_price);
        TextView broweWuxiao = helper.getView(R.id.browe_wuxiao);
        View lineBottom = helper.getView(R.id.v_line_bottom);
        String goodsName = item.getGoodsName();
        String name = item.getName();
        String image = item.getImage();
        String vender = item.getVender();
        String price = item.getDeKaiPrice();
        String priceReturn = item.getReturnMoney();
        if (tabValue == 0) {
            linearGoods.setVisibility(View.VISIBLE);
            linearDrug.setVisibility(View.GONE);
            if (TextUtils.isEmpty(goodsName)) {
                tvGoodsDrugname.setText(getSpannableText(name + " ", item.getSpecifications()));
            } else {
                tvGoodsDrugname.setText(getSpannableText(goodsName + " " + name + " ", item.getSpecifications()));
            }
            Picasso.with(mContext).load(image).error(R.mipmap.image_empty_order).placeholder(R.mipmap.image_empty_order).into(browseGoodsImage);
            tvVender.setText(vender);
            tvGoodsPrice.setText(price);
            tvGoodsReturnMoney.setText(priceReturn);
            if (isDeleteGoods) {
                imageViewGoods.setVisibility(View.VISIBLE);
            } else {
                imageViewGoods.setVisibility(View.GONE);
            }
            imageViewGoods.setSelected(item.isSelectedGoods());
            imageViewGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        if (item.isSelectedGoods()) {
                            item.setSelectedGoods(false);
                        } else {
                            item.setSelectedGoods(true);
                        }
                        listener.onItemSelectedGoods(position, item);
                    }
                }
            });
            if (item.getTheShelves().equals("1")) {
                broweLinearPrice.setVisibility(View.VISIBLE);
                broweWuxiao.setVisibility(View.GONE);
                tvGoodsDrugname.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
            } else {
                broweWuxiao.setVisibility(View.VISIBLE);
                broweWuxiao.setText("" + item.getMsg());
                broweLinearPrice.setVisibility(View.GONE);
                tvGoodsDrugname.setTextColor(ContextCompat.getColor(mContext, R.color.color_b1b1b1));
            }
            browseItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getTheShelves().equals("1")) {
                        if (listener != null) {
                            listener.onItemBrowseGoodsClick(position, item);
                        }
                    } else {
                        ToastUtil.showToastCenter(mContext, "本商品无效");
                    }
                }
            });
        } else {
            linearGoods.setVisibility(View.GONE);
            linearDrug.setVisibility(View.VISIBLE);
            tvDrugName.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
            if (TextUtils.isEmpty(goodsName)) {
                tvDrugName.setText(getSpannableText(name + " ", item.getSpecifications()));
            } else {
                tvDrugName.setText(getSpannableText(goodsName + " " + name + " ", item.getSpecifications()));
            }
            Picasso.with(mContext).load(image).error(R.mipmap.image_empty_order).placeholder(R.mipmap.image_empty_order).into(browseDrugImage);
            tvDrugVender.setText(vender);
            if (isDeleteDrug) {
                imageViewDrug.setVisibility(View.VISIBLE);
            } else {
                imageViewDrug.setVisibility(View.GONE);
            }
            imageViewDrug.setSelected(item.isSelectedDrug());
            imageViewDrug.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        if (item.isSelectedDrug()) {
                            item.setSelectedDrug(false);
                        } else {
                            item.setSelectedDrug(true);
                        }
                        listener.onItemSelectedDrug(position, item);
                    }
                }
            });
            browseItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemBrowseDrugClick(position, item);
                    }
                }
            });
        }
        String time = item.getLastTime();
        try {
            boolean isToday = TimeUtil.IsToday(time);
            boolean isYesterday = TimeUtil.IsYesterday(time);
            if (isToday) {
                browseGroupDesc.setText("今天");
            } else if (isYesterday) {
                browseGroupDesc.setText("昨天");
            } else {
                browseGroupDesc.setText(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            browseGroupDesc.setText(time);
        }

        if (item.isEquals()) {
            browseGroupDesc.setVisibility(View.GONE);
            bottomLineBottom.setVisibility(View.GONE);
            bottomLineBottom2.setVisibility(View.GONE);
            zhanweiLine.setVisibility(View.GONE);
        } else {
            browseGroupDesc.setVisibility(View.VISIBLE);
            bottomLineBottom.setVisibility(View.VISIBLE);
            if(0 == position){
                bottomLineBottom2.setVisibility(View.GONE);
                zhanweiLine.setVisibility(View.GONE);
            } else {
                bottomLineBottom2.setVisibility(View.VISIBLE);
                zhanweiLine.setVisibility(View.VISIBLE);
            }

        }
        if (position == mDatas.size() - 1) {
            bottomLine.setVisibility(View.VISIBLE);
            lineBottom.setVisibility(View.VISIBLE);
        } else {
            bottomLine.setVisibility(View.GONE);
            lineBottom.setVisibility(View.GONE);
        }

        browseItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onItemLongBrowseClick(tabValue, position, item);
                }
                return true;
            }
        });
    }

    /**
     * 设置内部字体颜色
     *
     * @param text
     * @return
     */
    private SpannableStringBuilder getSpannableText(String text, String speci) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(speci);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(DensityUtils.sp2px(13));
        spannableStringBuilder2.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), 0, speci.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder2.setSpan(sizeSpan, 0, speci.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.append(spannableStringBuilder2);
        return spannableStringBuilder;
    }


    /**
     * item 事件回调接口
     */
    public interface OnItemBrowseClickListener {
        void onItemSelectedGoods(int position, BrowseBean browseBean);

        void onItemSelectedDrug(int position, BrowseBean browseBean);

        void onItemBrowseGoodsClick(int position, BrowseBean browseBean);

        void onItemBrowseDrugClick(int position, BrowseBean browseBean);

        void onItemLongBrowseClick(int tabvalue, int position, BrowseBean browseBean);
    }

}

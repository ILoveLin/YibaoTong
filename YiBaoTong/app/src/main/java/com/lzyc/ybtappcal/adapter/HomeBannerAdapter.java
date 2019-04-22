/*
 * AUTHOR：Yolanda
 * 
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.lzyc.ybtappcal.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.GoodsHomePageList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeBannerAdapter extends PagerAdapter {

    private Context mContext;

    private List<GoodsHomePageList.ImagesBean> resIds;

    public HomeBannerAdapter(Context context) {
        this.mContext = context;
    }

    public void update(List<GoodsHomePageList.ImagesBean> resIds) {
        if (this.resIds != null)
            this.resIds.clear();
        if (resIds != null)
            this.resIds = resIds;
    }

    @Override
    public int getCount() {
        return resIds == null ? 0 : resIds.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_home_banner_img, container, false);

        ImageView img = (ImageView) v.findViewById(R.id.banner_img);

        Picasso.with(mContext)
                .load(resIds.get(position).getImg_url())
                .placeholder(R.mipmap.empty_biyao_banner)
                .error(R.mipmap.empty_biyao_banner)
                .into(img);
        container.addView(img);
        return img;
    }
}

package com.lzyc.ybtappcal.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.bean.HelpBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * @author yang
 */
public class HelpAdapter extends CommonAdapter<HelpBean> {
    private int currentPosition=-1;

    public void updateItem(int position, HelpBean item) {
        this.currentPosition = position;
        for (int i = 0; i < mDatas.size(); i++) {
            HelpBean item2 = mDatas.get(i);
            if(i==position){
                mDatas.set(i, item);
            }else{
                item2.setOpen(false);
                mDatas.set(i, item2);
            }
        }
        HelpAdapter.this.notifyDataSetChanged();
    }

    public HelpAdapter(Context context, int layoutId, List<HelpBean> datas) {
        super(context, layoutId, datas);
    }

    public void setList(List<HelpBean> list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder helper, final HelpBean item, final int position) {
        final LinearLayout linearTitle = helper.getView(R.id.linear_title_name);
        final TextView tvContent = helper.getView(R.id.tv_content);
        final LinearLayout linearContent = helper.getView(R.id.linear_content);
        final TextView tvName = helper.getView(R.id.tv_name);
        final ImageView ivClose = helper.getView(R.id.iv_close);
        final ImageView ivOpen = helper.getView(R.id.iv_open);
        final RelativeLayout relAll = helper.getView(R.id.rel_all);
        tvContent.setText("" + item.getAnswer());
        tvName.setText("" + item.getTitle());
        if (item.isOpen()) {
            linearContent.setVisibility(View.VISIBLE);
            if (currentPosition == position) {
                animationOpenAndClose(linearTitle, item.getHeight(), linearContent, ivClose, ivOpen, true);
            }else{
                animationOpenAndCloseDefalt(ivClose, ivOpen, true);
            }
        } else {
            if (currentPosition == position) {
                animationOpenAndClose(linearTitle, item.getHeight(), linearContent, ivClose, ivOpen, false);
            }else{
                linearContent.setVisibility(View.GONE);
                animationOpenAndCloseDefalt(ivClose, ivOpen, false);
            }
        }
        linearTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(View.GONE==linearContent.getVisibility()){
                    linearContent.setVisibility(View.INVISIBLE);
                }
                relAll.post(new Runnable() {
                    @Override
                    public void run() {
                        if(linearContent.getHeight()>0){
                            item.setHeight(linearContent.getHeight());
                        }
                        if (item.isOpen()) {
                            item.setOpen(false);
                        } else {
                            item.setOpen(true);
                        }
                        updateItem(position,item);
                    }
                });
            }
        });
    }

    /**
     * 执行展开关闭的动画
     *
     * @param linearTitle
     * @param contentHeight
     * @param linearContent
     * @param ivClose
     * @param ivOpen
     * @param isOpen
     */
    private void animationOpenAndClose(final LinearLayout linearTitle, int contentHeight, final LinearLayout linearContent, ImageView ivClose, ImageView ivOpen, final boolean isOpen) {
        ObjectAnimator alphaAnimationClose, alphaAnimationOpen, rotationAnimationClose, rotationAnimationOpen;
        ObjectAnimator translationContent, alphaAnimationContent;
        ValueAnimator valueAnimator;
        if (isOpen) {
            alphaAnimationClose = ObjectAnimator.ofFloat(ivClose, "alpha", 1f, 0f);
            alphaAnimationOpen = ObjectAnimator.ofFloat(ivOpen, "alpha", 0, 1f);
            rotationAnimationClose = ObjectAnimator.ofFloat(ivClose, "rotation", 0f, 180f);
            rotationAnimationOpen = ObjectAnimator.ofFloat(ivOpen, "rotation", 0f, 180f);
            translationContent = ObjectAnimator.ofFloat(linearContent, "translationX", -linearContent.getWidth(), 0);
            alphaAnimationContent = ObjectAnimator.ofFloat(linearContent, "alpha", 0f, 1f);
            valueAnimator = ValueAnimator.ofInt(contentHeight, contentHeight);
        } else {
            alphaAnimationClose = ObjectAnimator.ofFloat(ivClose, "alpha", 0f, 1f);
            alphaAnimationOpen = ObjectAnimator.ofFloat(ivOpen, "alpha", 1, 0f);
            rotationAnimationClose = ObjectAnimator.ofFloat(ivClose, "rotation", 180f, 360f);
            rotationAnimationOpen = ObjectAnimator.ofFloat(ivOpen, "rotation", 180f, 360f);
            translationContent = ObjectAnimator.ofFloat(linearContent, "translationX", 0, 0);
            alphaAnimationContent = ObjectAnimator.ofFloat(linearContent, "alpha", 1f, 0f);
            valueAnimator = ValueAnimator.ofInt(contentHeight, 0);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer animatedValue = (Integer) animation.getAnimatedValue();
                LayoutParams layoutParams = (LayoutParams) linearContent.getLayoutParams();
                layoutParams.height = animatedValue;
                linearContent.setLayoutParams(layoutParams);
            }
        });
        AnimatorSet set = new AnimatorSet();
        if (isOpen) {
            set.play(alphaAnimationClose).with(alphaAnimationOpen).with(alphaAnimationContent).with(rotationAnimationClose).with(rotationAnimationOpen).with(translationContent).with(valueAnimator);
        } else {
            set.play(alphaAnimationClose).with(alphaAnimationOpen).with(rotationAnimationClose).with(rotationAnimationOpen).with(translationContent).with(alphaAnimationContent).with(valueAnimator);
        }
        set.setDuration(200);
        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(!isOpen){
                    linearContent.setVisibility(View.GONE);
                }
            }
        });
        set.start();
    }


    /**
     * bu执行展开关闭的动画
     * @param ivClose
     * @param ivOpen
     * @param isOpen
     */
    private void animationOpenAndCloseDefalt(ImageView ivClose, ImageView ivOpen, final boolean isOpen) {
        ObjectAnimator alphaAnimationClose, alphaAnimationOpen, rotationAnimationClose, rotationAnimationOpen;
        if (isOpen) {
            alphaAnimationClose = ObjectAnimator.ofFloat(ivClose, "alpha", 0f, 0f);
            alphaAnimationOpen = ObjectAnimator.ofFloat(ivOpen, "alpha", 1f, 1f);
            rotationAnimationClose = ObjectAnimator.ofFloat(ivClose, "rotation", 180f, 180f);
            rotationAnimationOpen = ObjectAnimator.ofFloat(ivOpen, "rotation", 180f, 180f);
        } else {
            alphaAnimationClose = ObjectAnimator.ofFloat(ivClose, "alpha", 1f, 1f);
            alphaAnimationOpen = ObjectAnimator.ofFloat(ivOpen, "alpha", 0, 0f);
            rotationAnimationClose = ObjectAnimator.ofFloat(ivClose, "rotation", 360f, 360f);
            rotationAnimationOpen = ObjectAnimator.ofFloat(ivOpen, "rotation", 360f, 360f);
        }
        AnimatorSet set = new AnimatorSet();
        if (isOpen) {
            set.play(alphaAnimationClose).with(alphaAnimationOpen).with(rotationAnimationClose).with(rotationAnimationOpen);
        } else {
            set.play(alphaAnimationClose).with(alphaAnimationOpen).with(rotationAnimationClose).with(rotationAnimationOpen);
        }
        set.setDuration(200);
        set.start();
    }

}
